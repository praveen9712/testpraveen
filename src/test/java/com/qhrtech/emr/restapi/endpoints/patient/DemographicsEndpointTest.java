
package com.qhrtech.emr.restapi.endpoints.patient;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.model.demographics.Address;
import com.qhrtech.emr.accuro.model.demographics.Phone;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.patient.Demographics;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.accuro.utils.time.TimeFormatUtil;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.DemographicsDto;
import com.qhrtech.emr.restapi.models.dto.PhoneDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

public class DemographicsEndpointTest extends AbstractEndpointTest<DemographicsEndpoint> {

  // see com.qhrtech.emr.restapi.config.serialization.CalendarSerializer for the source of this
  // format
  private static final String EXPECTED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ";
  private PatientManager patientManager;
  private ApiSecurityContext context;
  private AuditLogUser user;

  public DemographicsEndpointTest() {
    super(new DemographicsEndpoint(), DemographicsEndpoint.class);
    patientManager = mock(PatientManager.class);
    context = new ApiSecurityContext();
    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
    context.setTenantId(TestUtilities.nextString(5));
    context.setPatientId(TestUtilities.nextId());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(PatientManager.class, patientManager);
  }

  @Test
  public void getPatient() throws ProtossException {
    Demographics expected = getFixture(Demographics.class);

    when(patientManager.getPatientDemographics(context.getPatientId())).thenReturn(expected);

    when().get(getBaseUrl() + "/v1/patient-portal/demographics")
        .then()
        .assertThat().statusCode(200)
        .and().body("firstName", is(expected.getFirstName()))
        .and().body("lastName", is(expected.getLastName()))
        .and().body("middleName", is(expected.getMiddleName()))
        .and().body("title", is(expected.getTitle()))
        .and().body("suffix", is(expected.getSuffix()))
        .and()
        .body("birthday",
            is(TimeFormatUtil.formatDate(expected.getBirthday()
                .toDateTimeAtStartOfDay().toCalendar(null),
                EXPECTED_DATE_FORMAT)))
        .and().body("genderId", is(expected.getGenderId()))
        .and().body("nextKinName", is(expected.getNextKinName()))
        .and().body("officialLanguageCode", is(expected.getOfficialLanguageCode()))
        .and().body("spokenLanguageCode", is(expected.getSpokenLanguageCode()))
        .and().body("relationshipStatusId", is(expected.getRelationshipStatusId()))
        .and().body("preferredContactType", is(expected.getPreferredContactType().name()))
        .and().body("healthCard.phn", is(expected.getHealthCard().getPhn()))
        .and().body("healthCard.locationId", is(expected.getHealthCard().getLocationId()))
        .and()
        .body("healthCard.expiry",
            is(TimeFormatUtil.formatDate(
                expected.getHealthCard().getExpiry().toDateTimeAtStartOfDay().toCalendar(null),
                EXPECTED_DATE_FORMAT)))
        .and().body("email.emailId", is(expected.getEmail().getEmailId()))
        .and().body("email.type", is(expected.getEmail().getType()))
        .and().body("email.address", is(expected.getEmail().getAddress()))
        .and().body("email.order", is(expected.getEmail().getOrder()))
        .and()
        .body("phones.phoneId",
            hasItems(expected.getPhones().stream().map(Phone::getPhoneId).toArray()))
        .and()
        .body("phones.number",
            hasItems(expected.getPhones().stream().map(Phone::getNumber).toArray()))
        .and()
        .body("phones.ext", hasItems(expected.getPhones().stream().map(Phone::getExt).toArray()))
        .and()
        .body("phones.equipType",
            hasItems(expected.getPhones().stream().map(Phone::getEquipType).toArray()))
        .and()
        .body("phones.usage",
            hasItems(expected.getPhones().stream().map(Phone::getUsage).toArray()))
        .and()
        .body("phones.notes",
            hasItems(expected.getPhones().stream().map(Phone::getNotes).toArray()))
        .and()
        .body("phones.order",
            hasItems(expected.getPhones().stream().map(Phone::getOrder).toArray()))
        .and()
        .body("phones.contactType",
            hasItems(expected.getPhones().stream().map(p -> p.getContactType().name()).toArray()))
        .and()
        .body("addresses.street",
            hasItems(expected.getAddresses().stream().map(Address::getStreet).toArray()))
        .and()
        .body("addresses.city",
            hasItems(expected.getAddresses().stream().map(Address::getCity).toArray()))
        .and()
        .body("addresses.postalZip",
            hasItems(expected.getAddresses().stream().map(Address::getPostalZip).toArray()))
        .and()
        .body("addresses.locationId",
            hasItems(expected.getAddresses().stream().map(Address::getLocationId).toArray()))
        .and()
        .body("addresses.note",
            hasItems(expected.getAddresses().stream().map(Address::getNote).toArray()))
        .and()
        .body("addresses.type",
            hasItems(expected.getAddresses().stream().map(Address::getType).toArray()))
        .and()
        .body("addresses.start",
            hasItems(expected.getAddresses().stream()
                .map(a -> TimeFormatUtil.formatDate(a.getStart(), EXPECTED_DATE_FORMAT))
                .toArray()))
        .and().body("addresses.end", hasItems(
            expected.getAddresses().stream()
                .map(a -> TimeFormatUtil.formatDate(a.getEnd(), EXPECTED_DATE_FORMAT)).toArray()));

    verify(patientManager).getPatientDemographics(context.getPatientId());

  }

  @Test
  public void getPatientNotFound() throws ProtossException {
    when(patientManager.getPatientDemographics(context.getPatientId())).thenReturn(null);

    when().get(getBaseUrl() + "/v1/patient-portal/demographics")
        .then()
        .assertThat().statusCode(404);
  }

  @Test
  public void updatePatientNotFound() throws Exception {
    when(patientManager.getPatientById(context.getPatientId())).thenReturn(null);

    DemographicsDto dto =
        getDozerMapper().map(getFixture(Demographics.class), DemographicsDto.class);
    dto.setPhones(Collections.singletonList(getFixture(PhoneDto.class)));
    dto.setNextKinPhone(getFixture(PhoneDto.class));

    String json = getObjectMapper().writeValueAsString(dto);
    given().body(json)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/patient-portal/demographics")
        .then().assertThat()
        .statusCode(404);

    verify(patientManager).getPatientById(context.getPatientId());
  }

  @Test
  public void updatePatient() throws Exception {
    Patient patient = getFixture(Patient.class);
    patient.setPatientId(context.getPatientId());
    Phone phone = getFixture(Phone.class);
    phone.setNumber(RandomStringUtils.random(10, false, true));
    patient.getDemographics().setPhones(Collections.singletonList(phone));
    patient.getDemographics().setNextKinPhone(phone);
    when(patientManager.getPatientById(context.getPatientId())).thenReturn(patient);
    Demographics demographics = patient.getDemographics();
    DemographicsDto demographicsDto = getDozerMapper().map(demographics, DemographicsDto.class);
    String json = getObjectMapper().writeValueAsString(demographicsDto);
    given().body(json)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/patient-portal/demographics")
        .then().assertThat()
        .statusCode(204);

    verify(patientManager).getPatientById(context.getPatientId());
    verify(patientManager).updatePatient(patient, user, null);
  }

  @Test
  public void updatePatientWithNullLocationId() throws Exception {
    Patient patient = getFixture(Patient.class);
    patient.setPatientId(context.getPatientId());
    List<Address> addresses =
        getFixtures(Address.class, ArrayList::new, 3);
    addresses.get(2).setLocationId(null); // 3rd and subsequent address locations cannot be null

    patient.getDemographics().setAddresses(addresses);

    when(patientManager.getPatientById(context.getPatientId())).thenReturn(patient);
    DemographicsDto demographicsDto =
        getDozerMapper().map(patient.getDemographics(), DemographicsDto.class);
    String json = getObjectMapper().writeValueAsString(demographicsDto);
    given()
        .body(json)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/patient-portal/demographics")
        .then().assertThat()
        .statusCode(400);
  }
}
