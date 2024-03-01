
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.demographics.InsurerManager;
import com.qhrtech.emr.accuro.api.demographics.LocationManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.api.provider.ProviderManager;
import com.qhrtech.emr.accuro.api.security.AccuroUserManager;
import com.qhrtech.emr.accuro.api.security.UserAuthenticationManager;
import com.qhrtech.emr.accuro.api.security.roles.RoleManager;
import com.qhrtech.emr.accuro.api.sysinfo.SystemInformationManager;
import com.qhrtech.emr.accuro.model.demographics.Address;
import com.qhrtech.emr.accuro.model.demographics.Insurer;
import com.qhrtech.emr.accuro.model.demographics.Location;
import com.qhrtech.emr.accuro.model.demographics.Phone;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.patient.Demographics;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.model.provider.Provider;
import com.qhrtech.emr.accuro.model.security.AccuroProvince;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.models.dto.AuthGrantTypeDetail;
import com.qhrtech.emr.restapi.models.dto.PatientCredentialDetail;
import com.qhrtech.emr.restapi.models.dto.UserInfoAddressDto;
import com.qhrtech.emr.restapi.models.dto.UserInfoDto;
import com.qhrtech.emr.restapi.models.dto.security.OfficeRoleDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response.Status;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/whoami")
@Tag(name = "Who Am I Endpoint",
    description = "Exposes whoami endpoint")
public class UserInfoEndpoint extends AbstractEndpoint {

  private static final String NOT_FOUND_MSG = "User not found";

  /**
   * Retrieves information about the user.
   *
   * @return The UserInfo model object
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Operation(
      summary = "Retrieves user Information",
      description = "Retrieves information about the service user.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Not Found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = UserInfoDto.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Client, patient, password level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public UserInfoDto getUserInfo() throws ProtossException {

    UserInfoDto userInfo = new UserInfoDto();
    // Fill details with the OAuth related information
    // Set the common fields => TENANT, SCOPES, GRANT TYPE
    userInfo.setTenant(getTenantId());
    userInfo.setScopes(getScopes());
    userInfo.setGrantType(getGrantType());

    if (getUser() != null) {
      if (getPatientId() == null) {
        // ACCURO USER
        AccuroUserManager accuroUserManager = getImpl(AccuroUserManager.class);
        AccuroUser accuroUser = accuroUserManager.getAccuroUser(getUser().getUserId());
        if (accuroUser == null) {
          throw Error.webApplicationException(Status.NOT_FOUND, NOT_FOUND_MSG);
        }
        AuthGrantTypeDetail authGrantTypeDetail = getAuthGrantDetail(accuroUser);
        userInfo.setAuthGrantTypeDetail(authGrantTypeDetail);
        userInfo.setProvince(getAccuroApiContext().getAccuroProvince().name());

      } else {
        // PATIENT
        PatientManager patientManager = getImpl(PatientManager.class);
        Patient patient = patientManager.getPatientById(getPatientId());
        if (patient == null) {
          throw Error.webApplicationException(Status.NOT_FOUND, NOT_FOUND_MSG);
        }
        PatientCredentialDetail patientCredentialDetail = getPatientCredentialDetail(patient);
        userInfo.setPatientCredentialDetail(patientCredentialDetail);
        userInfo.setProvince(getProvince().name());
      }

    } else {
      // CLIENT CREDENTIAL
      userInfo.setOauthClientId(getOAuthId());
      userInfo.setProvince(getAccuroApiContext().getAccuroProvince().name());
    }

    return userInfo;
  }

  private AccuroProvince getProvince()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    SystemInformationManager systemInformationManager = getImpl(SystemInformationManager.class);
    return systemInformationManager.getProvince();
  }

  private AuthGrantTypeDetail getAuthGrantDetail(AccuroUser accuroUser)
      throws ProtossException {

    AuthGrantTypeDetail authGrantTypeDetail = new AuthGrantTypeDetail();

    int userId = accuroUser.getUserId();
    authGrantTypeDetail.setUserId(userId);
    authGrantTypeDetail.setId(accuroUser.getUsername());
    String name = accuroUser.getFirstName() + " " + accuroUser.getLastName();
    authGrantTypeDetail.setName(name);
    authGrantTypeDetail.setProviderId(accuroUser.getProviderId());

    // PHONES
    List<Phone> phones = new ArrayList<>();
    phones.add(accuroUser.getCellPhone());
    phones.add(accuroUser.getHomePhone());
    phones.add(accuroUser.getWorkPhone());
    phones.remove(null);

    authGrantTypeDetail.setPhones(phones);

    if (accuroUser.getEmail() != null) {
      authGrantTypeDetail.setEmail(accuroUser.getEmail().getAddress());
    }

    // ADDRESS
    if (accuroUser.getAddress() != null) {
      List<Address> userAddress = Collections.singletonList(accuroUser.getAddress());
      List<UserInfoAddressDto> userInfoAddress = getUserInfoAddresses(userAddress);
      authGrantTypeDetail.setAddresses(userInfoAddress);
    }

    // OFFICE ROLES
    UserAuthenticationManager userAuthenticationManager = getImpl(UserAuthenticationManager.class);
    Set<Integer> officeIds = userAuthenticationManager.getOfficeIds(userId);
    RoleManager roleManager = getImpl(RoleManager.class);
    Set<OfficeRoleDto> officeRoles = new HashSet<>();
    for (int officeId : officeIds) {
      OfficeRoleDto officeRole = new OfficeRoleDto();
      Set<Integer> roleIds = roleManager.getRoles(new AuditLogUser(userId, officeId, "", "", ""));
      officeRole.setOfficeId(officeId);
      officeRole.setRoleIds(roleIds);
      officeRoles.add(officeRole);
    }
    authGrantTypeDetail.setOfficeRoles(officeRoles);
    return authGrantTypeDetail;
  }

  private List<UserInfoAddressDto> getUserInfoAddresses(List<Address> addresses)
      throws ProtossException {
    List<UserInfoAddressDto> userInfoAddresses = new ArrayList<>();

    for (Address address : addresses) {
      UserInfoAddressDto userInfoAddress = new UserInfoAddressDto();
      userInfoAddress.setAddress(address.getStreet());
      userInfoAddress.setCity(address.getCity());
      userInfoAddress.setPostalCode(address.getPostalZip());

      // Getting province and country by locationId
      if (address.getLocationId() != null) {
        Location location = getLocationById(address.getLocationId());
        if (location != null) {
          userInfoAddress.setProvince(location.getProvince());
          userInfoAddress.setCountry(location.getCountry());
        }
      }

      userInfoAddresses.add(userInfoAddress);
    }

    return userInfoAddresses;
  }

  private Location getLocationById(int locationId)
      throws ProtossException {
    LocationManager locationManager = getImpl(LocationManager.class);
    List<Location> locations =
        locationManager.getLocationsById(Collections.singletonList(locationId));
    return locations.stream().findFirst().orElse(null);
  }

  private PatientCredentialDetail getPatientCredentialDetail(Patient patient)
      throws ProtossException {

    PatientCredentialDetail patientCredentialDetail = new PatientCredentialDetail();

    if (patient.getDemographics() != null) {
      Demographics demographics = patient.getDemographics();

      // name
      String name = demographics.getFirstName() + " " + demographics.getLastName();
      patientCredentialDetail.setName(name);

      // phone
      patientCredentialDetail.setPhones(demographics.getPhones());

      // email
      if (demographics.getEmail() != null) {
        patientCredentialDetail.setEmail(demographics.getEmail().getAddress());
      }

      // address
      List<UserInfoAddressDto> userInfoAddresses =
          getUserInfoAddresses(demographics.getAddresses());
      patientCredentialDetail.setAddresses(userInfoAddresses);
    }

    patientCredentialDetail.setOccupation(patient.getOccupation());

    Integer insurerId = patient.getInsurerId();
    // Insurer Name
    InsurerManager insurerManager = getImpl(InsurerManager.class);
    Insurer insurer = insurerManager.getInsurerById(insurerId);
    if (insurer != null) {
      patientCredentialDetail.setInsurer(insurer.getName());
    }

    Set<Integer> providerIds = new HashSet<>();
    providerIds.add(patient.getReferringProviderId());
    providerIds.add(patient.getEnrolledProviderId());
    providerIds.add(patient.getFamilyProviderId());
    providerIds.add(patient.getOfficeProviderId());
    providerIds.remove(null);

    Set<Provider> providers = getProviders(providerIds);

    for (Provider provider : providers) {

      int providerId = provider.getProviderId();
      String fullName = provider.getFirstName() + " " + provider.getLastName();

      if (patient.getReferringProviderId() != null
          && providerId == patient.getReferringProviderId()) {
        patientCredentialDetail.setReferringProvider(fullName);
      }

      if (patient.getEnrolledProviderId() != null
          && providerId == patient.getEnrolledProviderId()) {
        patientCredentialDetail.setEnrolledProvider(fullName);
      }

      if (patient.getFamilyProviderId() != null && providerId == patient.getFamilyProviderId()) {
        patientCredentialDetail.setFamilyProvider(fullName);
      }

      if (patient.getOfficeProviderId() != null && providerId == patient.getOfficeProviderId()) {
        patientCredentialDetail.setOfficeProvider(fullName);
      }
    }

    return patientCredentialDetail;
  }

  private Set<Provider> getProviders(Set<Integer> providerIds)
      throws ProtossException {
    ProviderManager providerManager = getImpl(ProviderManager.class);
    return providerManager.getProvidersById(providerIds);
  }
}
