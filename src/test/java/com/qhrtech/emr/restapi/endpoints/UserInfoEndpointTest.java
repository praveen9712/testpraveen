
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import com.qhrtech.emr.accuro.model.patient.Demographics;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.model.provider.Provider;
import com.qhrtech.emr.accuro.model.security.AccuroProvince;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AuthGrantTypeDetail;
import com.qhrtech.emr.restapi.models.dto.PatientCredentialDetail;
import com.qhrtech.emr.restapi.models.dto.UserInfoAddressDto;
import com.qhrtech.emr.restapi.models.dto.UserInfoDto;
import com.qhrtech.emr.restapi.models.dto.security.OfficeRoleDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response.Status;
import org.junit.Assert;
import org.junit.Test;

public class UserInfoEndpointTest extends AbstractEndpointTest<UserInfoEndpoint> {

  private LocationManager locationManager;
  private AccuroUserManager accuroUserManager;
  private PatientManager patientManager;
  private ProviderManager providerManager;
  private InsurerManager insurerManager;
  private UserAuthenticationManager userAuthenticationManager;
  private RoleManager roleManager;
  private AuditLogUser user;
  private Set<String> scopes;
  private String grantType;
  private String tenant;
  private String oauthId;
  private ApiSecurityContext context = new ApiSecurityContext();
  private SystemInformationManager systemInformationManager;

  public UserInfoEndpointTest() {
    super(new UserInfoEndpoint(), UserInfoEndpoint.class);
    locationManager = mock(LocationManager.class);
    accuroUserManager = mock(AccuroUserManager.class);
    patientManager = mock(PatientManager.class);
    providerManager = mock(ProviderManager.class);
    insurerManager = mock(InsurerManager.class);
    userAuthenticationManager = mock(UserAuthenticationManager.class);
    roleManager = mock(RoleManager.class);
    systemInformationManager = mock(SystemInformationManager.class);

    user = getFixture(AuditLogUser.class);
    scopes = getFixtures(String.class, HashSet::new, 5);
    grantType = TestUtilities.nextString(10);
    tenant = TestUtilities.nextString(5);
    oauthId = TestUtilities.nextString(100);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    context.setScopes(scopes);
    context.setGrantType(grantType);
    context.setUser(user);
    context.setTenantId(tenant);
    context.setOauthClientId(oauthId);
    context.setAccuroApiContext(getFixture(AccuroApiContext.class));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> serviceMap = new HashMap<>();
    serviceMap.put(LocationManager.class, locationManager);
    serviceMap.put(AccuroUserManager.class, accuroUserManager);
    serviceMap.put(PatientManager.class, patientManager);
    serviceMap.put(ProviderManager.class, providerManager);
    serviceMap.put(InsurerManager.class, insurerManager);
    serviceMap.put(UserAuthenticationManager.class, userAuthenticationManager);
    serviceMap.put(RoleManager.class, roleManager);
    serviceMap.put(SystemInformationManager.class, systemInformationManager);
    return serviceMap;
  }



  @Test
  public void testGetUserInfo() throws Exception {
    UserInfoDto expected = getFixture(UserInfoDto.class);
    expected.setPatientCredentialDetail(null);
    expected.setOauthClientId(null);
    expected.setScopes(scopes);
    expected.setTenant(tenant);
    expected.setGrantType(grantType);

    AccuroUser accuroUser = getFixture(AccuroUser.class);
    int userId = context.getUser().getUserId();
    accuroUser.setUserId(userId);
    AuthGrantTypeDetail authGrantTypeDetail = getAuthGrantTypeDetail(accuroUser);
    expected.setAuthGrantTypeDetail(authGrantTypeDetail);
    expected.setProvince(context.getAccuroApiContext().getAccuroProvince().name());
    when(accuroUserManager.getAccuroUser(userId)).thenReturn(accuroUser);

    UserInfoDto actual = given()
        .when()
        .get(getBaseUrl() + "/v1/whoami")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(UserInfoDto.class);

    Assert.assertEquals(expected, actual);
    verify(accuroUserManager).getAccuroUser(context.getUser().getUserId());
    verify(locationManager).getLocationsById(any());
  }

  @Test
  public void testGetUserInfoWithInvalidUserId() throws Exception {
    given()
        .when()
        .get(getBaseUrl() + "/v1/whoami")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(accuroUserManager).getAccuroUser(context.getUser().getUserId());
    verify(locationManager, never()).getLocationsById(any());
  }

  @Test
  public void testGetUserInfoPatient() throws Exception {

    UserInfoDto expected = getFixture(UserInfoDto.class);
    expected.setAuthGrantTypeDetail(null);
    expected.setOauthClientId(null);
    expected.setScopes(scopes);
    expected.setTenant(tenant);
    expected.setGrantType(grantType);

    Patient patient = getFixture(Patient.class);
    int patientId = patient.getPatientId();
    context.setPatientId(patientId);
    PatientCredentialDetail patientCredentialDetail = getPatientCredentialDetail(patient);
    expected.setPatientCredentialDetail(patientCredentialDetail);
    expected.setProvince(context.getAccuroApiContext().getAccuroProvince().name());
    when(patientManager.getPatientById(patientId)).thenReturn(patient);
    AccuroProvince province = context.getAccuroApiContext().getAccuroProvince();
    when(systemInformationManager.getProvince()).thenReturn(province);

    UserInfoDto actual = given()
        .when()
        .get(getBaseUrl() + "/v1/whoami")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(UserInfoDto.class);

    Assert.assertEquals(expected, actual);
    verify(patientManager).getPatientById(patient.getPatientId());
    verify(insurerManager).getInsurerById(patient.getInsurerId());
    verify(providerManager).getProvidersById(any());
  }

  @Test
  public void testGetUserInfoWithInvalidPatientId() throws Exception {
    context.setPatientId(TestUtilities.nextId());

    given()
        .when()
        .get(getBaseUrl() + "/v1/whoami")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(patientManager).getPatientById(context.getPatientId());
    verify(insurerManager, never()).getInsurerById(context.getPatientId());
    verify(providerManager, never()).getProvidersById(any());
  }

  @Test
  public void testGetUserInfoClientCredential() throws Exception {

    UserInfoDto expected = getFixture(UserInfoDto.class);
    expected.setPatientCredentialDetail(null);
    expected.setAuthGrantTypeDetail(null);
    expected.setScopes(scopes);
    expected.setTenant(tenant);
    expected.setGrantType(grantType);
    expected.setOauthClientId(oauthId);
    expected.setProvince(context.getAccuroApiContext().getAccuroProvince().name());

    context.setUser(null);

    UserInfoDto actual = given()
        .when()
        .get(getBaseUrl() + "/v1/whoami")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(UserInfoDto.class);

    Assert.assertEquals(expected, actual);
    verify(accuroUserManager, never()).getAccuroUser(anyInt());
    verify(patientManager, never()).getPatientById(anyInt());
  }

  private AuthGrantTypeDetail getAuthGrantTypeDetail(AccuroUser accuroUser)
      throws Exception {

    AuthGrantTypeDetail authGrantTypeDetail = new AuthGrantTypeDetail();

    authGrantTypeDetail.setUserId(accuroUser.getUserId());
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
    Set<OfficeRoleDto> officeRoles = getFixtures(OfficeRoleDto.class, HashSet::new, 5);
    Set<Integer> officeIds = new HashSet<>();
    for (OfficeRoleDto officeRole : officeRoles) {
      officeIds.add(officeRole.getOfficeId());
      AuditLogUser user =
          new AuditLogUser(accuroUser.getUserId(), officeRole.getOfficeId(), "", "", "");
      when(roleManager.getRoles(user)).thenReturn(officeRole.getRoleIds());
    }
    when(userAuthenticationManager.getOfficeIds(accuroUser.getUserId())).thenReturn(officeIds);
    authGrantTypeDetail.setOfficeRoles(officeRoles);
    return authGrantTypeDetail;
  }

  private List<UserInfoAddressDto> getUserInfoAddresses(List<Address> addresses)
      throws Exception {
    List<UserInfoAddressDto> userInfoAddresses = new ArrayList<>();

    for (Address address : addresses) {
      UserInfoAddressDto userInfoAddress = new UserInfoAddressDto();
      userInfoAddress.setAddress(address.getStreet());
      userInfoAddress.setCity(address.getCity());
      userInfoAddress.setPostalCode(address.getPostalZip());

      // Getting province and country by locationId
      Location location = getLocationById(address.getLocationId());
      if (location != null) {
        userInfoAddress.setProvince(location.getProvince());
        userInfoAddress.setCountry(location.getCountry());
      }

      userInfoAddresses.add(userInfoAddress);
    }

    return userInfoAddresses;
  }

  private Location getLocationById(int locationId)
      throws Exception {
    List<Location> locations = getFixtures(Location.class, ArrayList::new, 1);
    when(locationManager.getLocationsById(Collections.singletonList(locationId)))
        .thenReturn(locations);
    return locations.get(0);
  }

  private PatientCredentialDetail getPatientCredentialDetail(Patient patient)
      throws Exception {

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
      List<UserInfoAddressDto> userInfoAddressDtos =
          getUserInfoAddresses(demographics.getAddresses());
      patientCredentialDetail.setAddresses(userInfoAddressDtos);
    }

    patientCredentialDetail.setOccupation(patient.getOccupation());

    // Insurer Name
    Insurer insurer = getFixture(Insurer.class);
    when(insurerManager.getInsurerById(patient.getInsurerId())).thenReturn(insurer);
    patientCredentialDetail.setInsurer(insurer.getName());

    Set<Integer> providerIds = new HashSet<>();
    Set<Provider> providers = getFixtures(Provider.class, HashSet::new, 4);
    int i = 0;
    for (Provider provider : providers) {
      int providerId = provider.getProviderId();
      String fullName = provider.getFirstName() + " " + provider.getLastName();

      if (i == 0) {
        patient.setReferringProviderId(providerId);
        patientCredentialDetail.setReferringProvider(fullName);
      }
      if (i == 1) {
        patient.setEnrolledProviderId(providerId);
        patientCredentialDetail.setEnrolledProvider(fullName);
      }
      if (i == 2) {
        patient.setFamilyProviderId(providerId);
        patientCredentialDetail.setFamilyProvider(fullName);
      }
      if (i == 3) {
        patient.setOfficeProviderId(providerId);
        patientCredentialDetail.setOfficeProvider(fullName);
      }
      i++;
      providerIds.add(providerId);
    }
    providerIds.remove(null);
    when(providerManager.getProvidersById(providerIds)).thenReturn(providers);

    return patientCredentialDetail;
  }
}
