/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.apicontext;

import com.qhrtech.emr.accuro.api.security.AccuroApiContextManager;
import com.qhrtech.emr.accuro.api.security.AuthorizedClientManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.LoginException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.security.AuthorizedClient;
import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;
import com.qhrtech.emr.restapi.security.AccuroLoginAuthenticationProvider;
import com.qhrtech.emr.restapi.security.exceptions.FilterException;
import com.qhrtech.emr.restapi.services.AccuroApiService;
import com.qhrtech.emr.restapi.util.AccapiScopes;
import com.qhrtech.emr.restapi.util.AccuroApiPartialContextEndpoints;
import java.util.Set;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author james.michaud
 */
@Service
public class DefaultAccuroApiContextService implements AccuroApiContextService {

  private static final String IDENTITY_SYSTEM_SQUID = "SQUID";

  private final AccuroApiService api;

  private final AccuroLoginAuthenticationProvider accuroLoginAuthenticationProvider;


  public DefaultAccuroApiContextService(
      @Autowired AccuroApiService api,
      @Autowired AccuroLoginAuthenticationProvider accuroLoginAuthenticationProvider) {
    this.api = api;
    this.accuroLoginAuthenticationProvider = accuroLoginAuthenticationProvider;
  }

  private AccuroApiContext loadSquidBasedContext(String userIdentifier, String tenantId,
      boolean reducedPermissionRequired, String clientId, Set<String> scopes) {

    AccuroApiContextManager manager =
        api.getImpl(AccuroApiContextManager.class, tenantId);

    AccuroApiContext accuroApiContext;
    try {
      accuroApiContext =
          manager.getAccuroApiContext(userIdentifier, IDENTITY_SYSTEM_SQUID,
              reducedPermissionRequired);

    } catch (ProtossException ex) {
      throw new FilterException(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), ex);
    }

    if (null == accuroApiContext || null == accuroApiContext.getAccuroUser()) {
      throw new FilterException(HttpStatus.UNAUTHORIZED.value(),
          "There is no valid user associated with your request");
    }
    checkExpiration(accuroApiContext.getIdentityValidFrom(),
        accuroApiContext.getIdentityValidTo());
    validateAccuroUser(accuroApiContext.getAccuroUser().getUserId(), tenantId);
    checkQhrFirstPartyOrAuthorizedClient(tenantId, clientId, scopes);
    return accuroApiContext;
  }

  private AccuroApiContext loadNoUserContext(String tenantId,
      boolean reducedPermissionRequired) {

    return loadUserIdBasedContext(null, tenantId, reducedPermissionRequired);
  }

  private AccuroApiContext loadNoUserOktaContext(String tenantId,
      boolean reducedPermissionRequired, String clientId, Set<String> scopes) {
    AccuroApiContext accuroApiContext =
        loadUserIdBasedContext(null, tenantId, reducedPermissionRequired);
    checkQhrFirstPartyOrAuthorizedClient(tenantId, clientId, scopes);
    return accuroApiContext;
  }

  private AccuroApiContext loadUserIdBasedContext(Integer userId, String tenantId,
      boolean reducedPermissionRequired) {

    AccuroApiContextManager manager = api.getImpl(AccuroApiContextManager.class, tenantId);

    AccuroApiContext accuroApiContext;
    try {
      accuroApiContext =
          manager.getAccuroApiContext(userId, reducedPermissionRequired);
    } catch (ProtossException ex) {
      throw new FilterException(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), ex);
    }

    return accuroApiContext;
  }

  private AccuroApiContext loadUserIdBasedOktaContext(Integer userId, String tenantId,
      boolean reducedPermissionRequired, String clientId, Set<String> scopes) {

    AccuroApiContext accuroApiContext =
        loadUserIdBasedContext(userId, tenantId, reducedPermissionRequired);
    checkQhrFirstPartyOrAuthorizedClient(tenantId, clientId, scopes);
    return accuroApiContext;
  }

  @Override
  public AccuroApiContext getAccuroApiUserContext(AccuroApiTokenAdapter accuroApiToken) {

    boolean partialPermission =
        basicPermissionAcceptable(accuroApiToken.getTokenRequestDetails());

    AccuroApiContext accuroApiContext;
    switch (accuroApiToken.getTokenType()) {
      case LEGACY_CLIENT_CREDENTIALS:
        accuroApiContext =
            loadNoUserContext(accuroApiToken.getTenantId(), partialPermission);
        break;
      case OKTA_CLIENT_CREDENTIALS:
        accuroApiContext =
            loadNoUserOktaContext(accuroApiToken.getTenantId(), partialPermission,
                accuroApiToken.getClientId(),
                accuroApiToken.getScopes());
        break;
      case OKTA_PROVIDER:
        accuroApiContext = loadSquidBasedContext(accuroApiToken.getUserIdentifier(),
            accuroApiToken.getTenantId(), partialPermission, accuroApiToken.getClientId(),
            accuroApiToken.getScopes());
        break;
      case OKTA_THIRD_PARTY:
        accuroApiContext =
            loadUserIdBasedOktaContext(Integer.valueOf(accuroApiToken.getUserIdentifier()),
                accuroApiToken.getTenantId(),
                partialPermission, accuroApiToken.getClientId(),
                accuroApiToken.getScopes());
        break;
      case LEGACY_PROVIDER:
        accuroApiContext =
            loadUserIdBasedContext(Integer.valueOf(accuroApiToken.getUserIdentifier()),
                accuroApiToken.getTenantId(),
                partialPermission);
        break;
      default:
        throw new FilterException(HttpStatus.BAD_REQUEST.value(),
            "No valid authentication found for this request. " + accuroApiToken.getTokenType());
    }

    if (null == accuroApiContext) {
      String errorMsg = String.format("Unauthorized %s user: %s", accuroApiToken.getTokenType(),
          accuroApiToken.getUserIdentifier());
      throw new FilterException(HttpStatus.UNAUTHORIZED.value(), errorMsg);
    }

    return accuroApiContext;
  }

  private void validateAccuroUser(Integer accuroUserId, String tenantId) {
    try {
      accuroLoginAuthenticationProvider.validateOktaUser(tenantId, accuroUserId);

    } catch (LoginException ex) {
      throw new FilterException(HttpStatus.UNAUTHORIZED.value(),
          ex.getMessage(), ex);
    } catch (DatabaseInteractionException ex) {
      throw new FilterException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
          "Error occurred while authenticating user.", ex);
    }

  }

  private void checkExpiration(LocalDateTime validFrom, LocalDateTime validTo) {

    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

    // We want to compare the dates with UTC, as architect confirmed those fields are expected to be
    // in UTC.
    DateTime dateTime = new DateTime(DateTimeZone.UTC);
    LocalDateTime currentDate = LocalDateTime.parse(dateTime.toString(formatter), formatter);

    if (validFrom != null && !validFrom.isBefore(currentDate)) {
      throw new FilterException(HttpStatus.UNAUTHORIZED.value(),
          "Unauthorized user: SQUID is not yet valid as of today");
    }

    if (validTo != null && !validTo.isAfter(currentDate)) {

      throw new FilterException(HttpStatus.UNAUTHORIZED.value(),
          "Unauthorized user: SQUID is expired");
    }
  }

  /**
   * This method returns true when partial 'read only' permissions are acceptable. False Indicates
   * that a full set of permissions will be required to fulfill the request.
   *
   * @return true if the Accuro context type can be partial. For eg. READ requests false - Full
   *         Accuro context needs to be loaded. For eg. WRITE requests
   */
  private boolean basicPermissionAcceptable(TokenRequestDetails tokenRequestDetails) {
    // check in mapper if the request is having exception of partial permission context
    Boolean isPartialContext =
        AccuroApiPartialContextEndpoints.lookUpByUrlAndRequestType(
            tokenRequestDetails.getRequestUri(), tokenRequestDetails.getHttpMethod().toString());
    if (isPartialContext != null) {
      return isPartialContext;
    } else if (tokenRequestDetails.getHttpMethod().matches("GET")) {
      return true;
    }
    return false;
  }

  private void checkQhrFirstPartyOrAuthorizedClient(String tenantId, String clientId,
      Set<String> scopes) {
    if (!scopes.contains(AccapiScopes.QHR_FIRST_PARTY.getApiScope()) && !scopes.contains(
        AccapiScopes.QHR_AUTHORIZED_CLIENT.getApiScope())) {
      validateUserByClientId(clientId, tenantId);
    }
  }

  private void validateUserByClientId(String clientId, String tenantId) {
    AuthorizedClientManager authorizedClientManager =
        api.getImpl(AuthorizedClientManager.class, tenantId, true);

    try {
      Envelope<AuthorizedClient> authorizedClientEnvelope =
          authorizedClientManager.search(clientId, null, null, null, null, null);
      if (authorizedClientEnvelope == null || authorizedClientEnvelope.getContents().size() < 1) {
        throw new FilterException(HttpStatus.UNAUTHORIZED.value(),
            "Provided clientId is not authorized to access the resources.");
      }
    } catch (DatabaseInteractionException ex) {
      throw new FilterException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
          "Error occurred while authenticating user.", ex);
    } catch (UnsupportedSchemaVersionException ex) {
      throw new FilterException(HttpStatus.BAD_REQUEST.value(),
          "Schema version is not supported for this type of Authentication.", ex);
    }

  }

}
