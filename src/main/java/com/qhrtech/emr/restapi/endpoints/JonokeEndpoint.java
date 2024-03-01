/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.model.patient.PersonalHealthCard;
import com.qhrtech.emr.accuro.utils.Transformer;
import com.qhrtech.emr.accuro.utils.transformers.JonokeHash;
import com.qhrtech.emr.restapi.external.IdpTokenManager;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.endpoints.JonokeLink;
import com.qhrtech.emr.restapi.models.endpoints.JonokeLogin;
import com.qhrtech.emr.restapi.models.endpoints.JonokePatientInvite;
import com.qhrtech.emr.restapi.models.endpoints.PortalToken;
import com.qhrtech.emr.restapi.services.MedeoService;
import com.qhrtech.emr.restapi.util.JwtUtil;
import com.webcohesion.enunciate.metadata.Facet;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * This functionality has been deprecated.
 *
 * @author kevin.kendall
 * @deprecated No alternative, all Jonoke work flows are no longer in use.
 */
@Deprecated
@Component
@Path("/v1/jonoke")
@PreAuthorize("#oauth2.isClient()")
@Facet("internal")
public class JonokeEndpoint extends AbstractEndpoint {

  // Defined errors
  private static final String NOT_FOUND = "not found";
  private static final String NO_ACCOUNT = "no idp account";
  private static final String HAS_ACCOUNT = "has idp account";
  private final Transformer<char[], String> jenokeHash = new JonokeHash();
  @Autowired
  private MedeoService medeoService;
  @Autowired
  private IdpTokenManager idpTokenManager;
  @Autowired
  private PortalEndpoint portalEndpoint;
  @Autowired
  private JwtConsumer idpJwtConsumer;

  @PUT
  @Path("/login")
  @PreAuthorize("#oauth2.hasScope( 'PORTAL_GENERATE_TOKEN' )")
  public PortalToken getLogin(
      JonokeLogin login) {

    String username = login.getUsername();
    String password = login.getPassword();

    if (username == null || username.isEmpty()) {
      throw Error.returnBadRequestResult("username can't be empty");
    } else if (password == null || password.isEmpty()) {
      throw Error.returnBadRequestResult("password can't be empty");
    }

    // Attempt to login to IDP with name/password
    ClientAccessToken token = idpTokenManager.getPasswordToken(username, password);

    // If IDP login success, generate Accuro access token and return
    if (token != null) {
      try {
        PortalToken portalToken = portalEndpoint.getToken(null, null, token.getTokenKey());
        return portalToken;
      } catch (Exception ex) {
        throw Error
            .returnInternalServerErrorResult("Error generating Accuro token for IDP token.", ex);
      }
    } else {
      throw Error.returnUnauthorizedResult(NO_ACCOUNT);
    }

  }

  @PUT
  @Path("/sign_up")
  @PreAuthorize("#oauth2.hasScope( 'PORTAL_GENERATE_TOKEN' ) "
      + "and #oauth2.hasScope( 'PORTAL_LINK_PATIENT' )")
  public PortalToken signUpExistingPatient(
      JonokeLink link) throws ProtossException {

    final String username = link.getUsername();
    final String jonokePassword = link.getJonokePassword();
    final String phoneNumber = link.getPhoneNumber();
    final String email = link.getEmail();
    final String password = link.getPassword();

    if (username == null || username.isEmpty()) {
      throw Error.returnBadRequestResult("jonoke_login can't be empty");
    } else if (jonokePassword == null || jonokePassword.isEmpty()) {
      throw Error.returnBadRequestResult("jonoke_password can't be empty");
    } else if (email == null || email.isEmpty()) {
      throw Error.returnBadRequestResult("email can't be empty");
    } else if (password == null || password.isEmpty()) {
      throw Error.returnBadRequestResult("password can't be empty");
    }

    // Convert password into a Jonoke hash
    String hash = jenokeHash.transform(jonokePassword.toCharArray());

    // Lookup patient for name/hash
    PatientManager patientManager = getImpl(PatientManager.class);
    Patient patient = patientManager.getPatientByJonokeLogin(username, hash);

    if (patient == null) {
      throw Error.returnUnauthorizedResult(NOT_FOUND);
    }

    // Attempt to login to IDP with name/password
    ClientAccessToken token = idpTokenManager.getPasswordToken(email, password);

    if (token != null) {
      throw Error.returnUnauthorizedResult(HAS_ACCOUNT);
    }

    String tokenKey = signUpPatient(patient, phoneNumber, email, password);

    // If IDP login success, generate Accuro access token and return
    try {
      PortalToken portalToken = portalEndpoint.getToken(null, null, tokenKey);
      return portalToken;
    } catch (HttpRequestMethodNotSupportedException ex) {
      throw Error.returnInternalServerErrorResult("Error logging into Medeo IDP.", ex);
    }
  }

  @PUT
  @Path("/patient_invite_sign_up")
  @PreAuthorize("#oauth2.hasScope( 'PORTAL_GENERATE_TOKEN' ) "
      + "and #oauth2.hasScope( 'PORTAL_LINK_PATIENT' )")
  public PortalToken signUpNewPatient(
      JonokePatientInvite patientInvite) throws ProtossException {

    PatientManager patientManager = getImpl(PatientManager.class);
    Patient patient = patientManager.getPatientByAccessCode(patientInvite.getAccessCode());
    if (patient == null) {
      throw Error.returnUnauthorizedResult("Could not find patient for this access code.");
    }

    boolean valid =
        patientManager.validatePatientLogin(patient, patientInvite.getPatientDemographics());
    if (!valid) {
      throw Error
          .returnUnauthorizedResult("Demographics are not a close enough match to be verified.");
    }
    String tokenKey = signUpPatient(
        patient,
        patientInvite.getPhoneNumber(),
        patientInvite.getEmail(),
        patientInvite.getPassword());
    patientManager.retireAccessCode(patientInvite.getAccessCode());

    // If IDP login success, generate Accuro access token and return
    try {
      PortalToken portalToken = portalEndpoint.getToken(null, null, tokenKey);
      return portalToken;
    } catch (HttpRequestMethodNotSupportedException ex) {
      throw Error.returnInternalServerErrorResult("Error logging into Medeo IDP.", ex);
    }
  }

  private String signUpPatient(
      Patient patient,
      String phoneNumber,
      String email,
      String password) throws ProtossException {

    PersonalHealthCard phc = patient.getDemographics().getHealthCard();
    String phn = phc == null ? null : phc.getPhn();
    boolean success = medeoService.signUp(
        patient.getDemographics().getFirstName(),
        patient.getDemographics().getLastName(),
        phoneNumber,
        email,
        password,
        patient.getDemographics().getBirthday().toDateTimeAtStartOfDay().toCalendar(null),
        phn);
    if (!success) {
      // Success should never be false unless handling Medeo 422 error fails
      throw Error
          .returnInternalServerErrorResult("Error signing up new IDP account for user.");
    }

    // Attempt to login to IDP with name/password
    ClientAccessToken token = idpTokenManager.getPasswordToken(email, password);

    if (token == null) {
      // Handle not authenticating with new user
      throw Error.returnInternalServerErrorResult("Unable to link IDP account to patient.");
    }

    // Get IDP id from token
    Long idpId;
    try {
      idpId = JwtUtil.getIdpUserId(idpJwtConsumer, token.getTokenKey());
    } catch (InvalidJwtException ex) {
      throw Error
          .returnInternalServerErrorResult("Error accessing newly created IDP account.", ex);
    }

    PatientManager patientManager = getImpl(PatientManager.class);
    // Get IDP resource id from token
    patientManager.setPatientIdpId(patient.getPatientId(), idpId);

    return token.getTokenKey();
  }

  @PUT
  @Path("/link")
  @PreAuthorize("#oauth2.hasScope( 'PORTAL_LINK_PATIENT' )")
  public String linkExistingPatient(
      JonokeLink link) throws ProtossException {

    String username = link.getUsername();
    String jonokePassword = link.getJonokePassword();
    String email = link.getEmail();
    String password = link.getPassword();

    if (username == null || username.isEmpty()) {
      throw Error.returnBadRequestResult("jonoke_login can't be empty");
    } else if (jonokePassword == null || jonokePassword.isEmpty()) {
      throw Error.returnBadRequestResult("jonoke_password can't be empty");
    } else if (email == null || email.isEmpty()) {
      throw Error.returnBadRequestResult("email can't be empty");
    } else if (password == null || password.isEmpty()) {
      throw Error.returnBadRequestResult("password can't be empty");
    }

    // Convert password into a Jonoke hash
    String hash = jenokeHash.transform(jonokePassword.toCharArray());

    // Lookup patient for name/hash
    PatientManager patientManager = getImpl(PatientManager.class);
    Patient patient = patientManager.getPatientByJonokeLogin(username, hash);
    if (patient == null) {
      throw Error.returnUnauthorizedResult(NOT_FOUND);
    }

    // Attempt to login to IDP with name/password
    ClientAccessToken token = idpTokenManager.getPasswordToken(email, password);

    if (token != null) {
      throw Error.returnUnauthorizedResult(HAS_ACCOUNT);
    }

    linkPatient(patient.getPatientId(), email, password);

    return "IDP account linked to patient.";
  }

  @PUT
  @Path("/patient_invite_link")
  @PreAuthorize("#oauth2.hasScope( 'PORTAL_LINK_PATIENT' )")
  public PortalToken linkNewPatient(
      JonokePatientInvite patientInvite) throws ProtossException {

    PatientManager patientManager = getImpl(PatientManager.class);
    Patient patient = patientManager.getPatientByAccessCode(patientInvite.getAccessCode());
    if (patient == null) {
      throw Error.returnUnauthorizedResult("Could not find patient for this access code.");
    }

    boolean valid =
        patientManager.validatePatientLogin(patient, patientInvite.getPatientDemographics());
    if (!valid) {
      throw Error
          .returnUnauthorizedResult("Demographics are not a close enough match to be verified.");
    }

    String tokenKey = linkPatient(
        patient.getPatientId(),
        patientInvite.getEmail(),
        patientInvite.getPassword());

    patientManager.retireAccessCode(patientInvite.getAccessCode());

    try {
      PortalToken portalToken = portalEndpoint.getToken(null, null, tokenKey);
      return portalToken;
    } catch (HttpRequestMethodNotSupportedException ex) {
      throw Error.returnInternalServerErrorResult("Error logging into Medeo IDP.", ex);
    }
  }

  private String linkPatient(
      int patientId,
      String email,
      String password) throws ProtossException {
    // Attempt to login to IDP with name/password
    ClientAccessToken token = idpTokenManager.getPasswordToken(email, password);

    if (token == null) {
      // Handle not authenticating with new user
      throw Error.returnInternalServerErrorResult("Unable to link IDP account to patient.");
    }

    // Get IDP id from token
    Long idpId;
    try {
      idpId = JwtUtil.getIdpUserId(idpJwtConsumer, token.getTokenKey());
    } catch (InvalidJwtException ex) {
      throw Error
          .returnInternalServerErrorResult("Error accessing newly created IDP account.", ex);
    }

    // Get IDP resource id from token
    PatientManager patientManager = getImpl(PatientManager.class);
    Patient idpPatient = patientManager.getPatientByIdpUserId(idpId);
    if (idpPatient != null) {
      // idp patient already linked in this database
      throw Error
          .returnInternalServerErrorResult("IDP account is already linked to a patient.");
    }

    // Get IDP resource id from token
    patientManager.setPatientIdpId(patientId, idpId);

    return token.getTokenKey();
  }

}
