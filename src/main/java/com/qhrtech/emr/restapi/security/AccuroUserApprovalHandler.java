
package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.accuro.api.logging.AuditLogManager;
import com.qhrtech.emr.accuro.api.logging.DefaultAuditLogManager;
import com.qhrtech.emr.accuro.api.office.DefaultOfficeManager;
import com.qhrtech.emr.accuro.api.office.OfficeManager;
import com.qhrtech.emr.accuro.api.security.DefaultUserAuthenticationManager;
import com.qhrtech.emr.accuro.api.security.UserAuthenticationManager;
import com.qhrtech.emr.accuro.model.demographics.Office;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.logging.AuditLog;
import com.qhrtech.emr.accuro.model.logging.LogActivityType;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.security.datasource.DataSourceService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;

/**
 * @author Blake Dickie
 */
public class AccuroUserApprovalHandler implements UserApprovalHandler {

  private final DataSourceService dsService;

  public AccuroUserApprovalHandler(DataSourceService dsService) {
    this.dsService = dsService;
  }

  @Override
  public boolean isApproved(
      AuthorizationRequest authorizationRequest,
      Authentication userAuthentication) {
    return authorizationRequest.isApproved();
  }

  @Override
  public AuthorizationRequest checkForPreApproval(
      AuthorizationRequest authorizationRequest,
      Authentication userAuthentication) {
    // No support for pre-approvals.
    return authorizationRequest;
  }

  /**
   * This is called after the user has approved or denied the request in the approval page. The
   * method should update the approved state of the request if approved.
   *
   * @param authorizationRequest A request for authorization by an OAuth 2 Client
   * @param userAuthentication Token for a user authentication request
   *
   * @return A request for authorization by an OAuth 2 Client
   */
  @Override
  public AuthorizationRequest updateAfterApproval(
      AuthorizationRequest authorizationRequest,
      Authentication userAuthentication) {

    // Authorization approvals are expected to be for Providers only so we
    // can consider any other request rejected
    if (!(userAuthentication.getPrincipal() instanceof AccuroUserDetails)) {
      authorizationRequest.setApproved(false);
      return authorizationRequest;
    }

    Map<String, String> approvalParameters = authorizationRequest.getApprovalParameters();
    String flag = approvalParameters.get("user_oauth_approval");

    boolean approved = flag != null && flag.toLowerCase().equals("true");
    authorizationRequest.setApproved(approved);

    AccuroUserDetails details = (AccuroUserDetails) userAuthentication.getPrincipal();
    String oauthId = authorizationRequest.getClientId();

    // Get the DataSource used for audit logging
    DataSource ds = dsService.getDataSource(details.getTenantId());

    AuditLogManager auditLogApi = new DefaultAuditLogManager(ds);
    AuditLog.Builder builder = new AuditLog.Builder();
    builder.setUserId(details.getUserId());
    builder.setServiceName("Accuro Rest API");
    builder.setServiceId("oauth:" + oauthId);

    if (!approved) {
      builder.setType(LogActivityType.OAUTH_ATTEMPT);
      builder.setMessage("OAuth access has been attempted.");
      AuditLog auditLog = builder.build();
      try {
        auditLogApi.createAuditLog(auditLog);
        return authorizationRequest;
      } catch (ProtossException e) {
        throw Error.returnInternalServerErrorResult(e.getMessage());
      }
    }

    builder.setType(LogActivityType.OAUTH_GRANT);
    builder.setMessage("OAuth access has been granted.");
    AuditLog auditLog = builder.build();
    try {
      auditLogApi.createAuditLog(auditLog);
    } catch (ProtossException e) {
      throw Error.returnInternalServerErrorResult(e.getMessage());
    }

    authorizationRequest.getExtensions().put("oauth_client_id", oauthId);

    String office = approvalParameters.get("office");
    try {
      int officeId = Integer.parseInt(office);
      authorizationRequest.getExtensions().put("office", officeId);
    } catch (NumberFormatException ex) {
      // Office id is expected from the approval page, something has gone
      // wrong and we should not approve the authentication
      authorizationRequest.setApproved(false);
      return authorizationRequest;
    }

    return authorizationRequest;
  }

  /**
   * The values in the returned map, will be placed in the HttpRequest object as attributes to be
   * accessible from the controller for displaying the approval screen.
   *
   * @param authorizationRequest A request for authorization by an OAuth 2 Client
   * @param userAuthentication Token for a user authentication request
   *
   * @return A map of user approval request objects
   */
  @Override
  public Map<String, Object> getUserApprovalRequest(
      AuthorizationRequest authorizationRequest,
      Authentication userAuthentication) {

    Map<String, Object> model = new HashMap<>();
    // In case of a redirect we might want the request parameters to be included
    model.putAll(authorizationRequest.getRequestParameters());
    Object principal = userAuthentication.getPrincipal();

    // If the user is an Accuro provider we want to make the list of offices
    // available to choose from
    if (principal instanceof AccuroUserDetails) {
      try {
        AccuroUserDetails details = (AccuroUserDetails) principal;

        DataSource ds = dsService.getDataSource(details.getTenantId());

        UserAuthenticationManager userAuthenticationInterface =
            new DefaultUserAuthenticationManager(ds);

        OfficeManager officeInterface = new DefaultOfficeManager(ds);

        Set<Integer> officeIds = userAuthenticationInterface.getOfficeIds(details.getUserId());
        Set<Office> offices = officeInterface.getOfficesById(officeIds);
        model.put("offices", offices);
      } catch (ProtossException e) {
        throw Error.returnInternalServerErrorResult(
            "Error getting user approval request.", e);
      }
    }
    return model;
  }

}
