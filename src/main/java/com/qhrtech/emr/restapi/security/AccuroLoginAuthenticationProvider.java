/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.accuro.api.patient.DefaultPatientManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.api.security.AccuroUserManager;
import com.qhrtech.emr.accuro.api.security.DefaultAccuroUserManager;
import com.qhrtech.emr.accuro.api.security.DefaultUserAuthenticationManager;
import com.qhrtech.emr.accuro.api.security.UserAuthenticationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.security.LoginException;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.restapi.external.RegistryTokenManager;
import com.qhrtech.emr.restapi.security.datasource.DataSourceService;
import com.qhrtech.emr.restapi.security.exceptions.FilterException;
import com.qhrtech.emr.restapi.security.exceptions.InvalidTenantException;
import com.qhrtech.emr.restapi.util.JwtUtil;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Blake Dickie
 */
public class AccuroLoginAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  private static final Logger log = LoggerFactory.getLogger(RegistryTokenManager.class);

  @Autowired
  private JwtConsumer idpJwtConsumer;

  @Autowired
  private DataSourceService dsService;

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    // Normally this is where the password would be checked, but we already do that in the
    // retrieveUser step.
  }

  @Override
  protected UserDetails retrieveUser(String username,
      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    if (authentication.getCredentials() == null) {
      // No password provided.
      throw new BadCredentialsException("No password provided");
    }
    try {
      // If we have received web authentication details it means a provider
      // is attempting to login by using the OAuth web form
      if (authentication.getDetails() instanceof AccuroWebAuthenticationDetails) {

        // We have enhanced the web form and details to additionally contain
        // a tenant id specified by the user
        AccuroWebAuthenticationDetails details =
            (AccuroWebAuthenticationDetails) authentication.getDetails();
        String tenantId = details.getTenantId();

        // Handle Accuro user login
        String password = authentication.getCredentials().toString();
        return getAccuroUserDetails(tenantId, username, password, null);
      }

      // If we have received a map this likely indicates that authentication
      // is being performed programatically via password_grant for providers
      // or the portal endpoint for patients
      if (authentication.getDetails() instanceof Map) {
        Map<String, String> details = (Map<String, String>) authentication.getDetails();

        // Check if the authentication details contains a jwt, if so we
        // should treat this as a patient login
        if (details.containsKey("jwt")) {
          // Handle patient login from the PortalEndpoint
          return getPatientUserDetails(details);
        }

        // Handle a provider login using the password grant workflow
        if (authentication.getCredentials() == null
            || !(authentication.getCredentials() instanceof String)) {
          throw new AuthenticationServiceException("Invalid authentication details.");
        }

        // Extract required values from the authentication and details
        String password = (String) authentication.getCredentials();
        String tenantId = details.get("tenant");
        String office = details.get("office");

        if (office == null || office.isEmpty()) {
          throw new InsufficientAuthenticationException(
              "Office must be supplied when authenticating a provider using password grant.");
        }
        int officeId;
        try {
          officeId = Integer.parseInt(office);
        } catch (NumberFormatException ex) {
          throw new InsufficientAuthenticationException(
              "Office must be supplied as an integer when "
                  + "authenticating a provider using password grant.",
              ex);
        }

        return getAccuroUserDetails(tenantId, username, password, officeId);
      }
    } catch (ProtossException e) {
      throw new AuthenticationServiceException("Unable to retrieve Patient user details.", e);
    } catch (javax.ws.rs.NotFoundException e) {
      throw new AuthenticationServiceException("Accuro clinic not found.", e);
    } catch (javax.ws.rs.ProcessingException | InvalidTenantException
        | IllegalArgumentException e) {
      log.error("Unable to authenticate user.", e);
      throw new AuthenticationServiceException("Unable to authenticate user", e);
      // we have to catch all other possible exceptions so as to avoid spitting out
      // the entire stack trace. Check any super method and default method, it always
      // throwing default exception as AuthenticationException.
    } catch (Exception e) {
      log.error("Invalid authentication details..", e);
      throw new AuthenticationServiceException("Invalid authentication details.", e);
    }
    throw new AuthenticationServiceException("Invalid authentication details.");
  }

  private AccuroUserDetails getAccuroUserDetails(
      String tenantId,
      String username,
      String password,
      Integer officeId) throws ProtossException {

    // Get the DataSource for the correct Accuro database to verify against
    final DataSource ds = dsService.getDataSource(tenantId);
    if (ds == null) {
      throw new AuthenticationServiceException("Invalid tenant id.");
    }

    UserAuthenticationManager userAuthenticationInterface =
        new DefaultUserAuthenticationManager(ds);
    int userId;
    try {
      userId = userAuthenticationInterface.getUserId(username, password.toCharArray());
    } catch (LoginException ex) {
      switch (ex.getType()) {
        case INVALID:
          throw new BadCredentialsException(ex.getMessage(), ex);
        case LOCKED:
          throw new LockedException(ex.getMessage(), ex);
        case DISABLED:
          throw new DisabledException(ex.getMessage(), ex);
        case ERROR:
        default:
          throw new AuthenticationServiceException(ex.getMessage(), ex);
      }
    }

    // Note: Retrieving AccuroUser and Offices for the user can be done in parallel
    // Retrieve AccuroUser
    AccuroUserManager userManager = new DefaultAccuroUserManager(ds);
    AccuroUser user = userManager.getAccuroUser(userId);

    // Retrieve Offices for user
    Set<Integer> officeIds = userAuthenticationInterface.getOfficeIds(userId);

    // If user does not belong to any offices for this tenant they should
    // not be able to authenticate
    if (officeIds.isEmpty()) {
      throw new InsufficientAuthenticationException("User does not belong to any Office.");
    }

    // If office has been specified such as during password grant verify the
    // user is actually in that office
    if (officeId != null && !officeIds.contains(officeId)) {
      throw new InsufficientAuthenticationException(
          "User does not belong to the requested Office.");
    }

    return new AccuroUserDetails.Builder()
        .setUserId(user.getUserId())
        .setUsername(user.getUsername())
        .setTenantId(tenantId)
        .build();
  }

  public void validateOktaUser(String tenantId, int userId)
      throws DatabaseInteractionException, LoginException {
    DataSource ds = dsService.getDataSource(tenantId);
    if (ds == null) {
      throw new AuthenticationServiceException("Invalid tenant id.");
    }
    UserAuthenticationManager userAuthenticationInterface =
        new DefaultUserAuthenticationManager(ds);
    userAuthenticationInterface.validateUserByUserId(userId);
    // If user does not belong to any offices for this tenant they should
    // not be able to authenticate
    if (userAuthenticationInterface.getOfficeIds(userId).isEmpty()) {
      throw new FilterException(HttpStatus.UNAUTHORIZED.value(),
          "User does not belong to any Office.");
    }
  }

  private PatientUserDetails getPatientUserDetails(
      Map<String, String> details) throws ProtossException {

    // Verify that grant type is a password grant
    if (!"password".equals(details.get("grant_type"))) {
      throw new AuthenticationServiceException(
          "Attempted to do patient login without password grant type.");
    }

    // Extract the idp id from the Medeo JWT token
    Long idpUserId;
    try {
      String token = (String) details.get("jwt");
      idpUserId = JwtUtil.getIdpUserId(idpJwtConsumer, token);
    } catch (InvalidJwtException ex) {
      throw new AuthenticationServiceException(ex.getMessage(), ex);
    }
    if (idpUserId == null) {
      throw new UsernameNotFoundException("Unable to find user by resource id.");
    }

    String tenantId = details.get("tenant");
    DataSource ds = dsService.getDataSource(tenantId);
    PatientManager patientManager =
        new DefaultPatientManager(ds, null, null);
    Patient patient = patientManager.getPatientByIdpUserId(idpUserId);
    return new PatientUserDetails.Builder()
        .setPatientId(patient.getPatientId())
        .setUsername(patient.getDemographics().getFirstName().charAt(0)
            + patient.getDemographics().getLastName())
        .setTenantId(tenantId)
        .build();
  }

}
