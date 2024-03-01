
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.security.AccuroUserManager;
import com.qhrtech.emr.accuro.model.demographics.Phone;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.AccuroUserValidationException;
import com.qhrtech.emr.accuro.model.patient.ContactType;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AccuroUserDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.util.PhoneNumberFormatterUtils;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class AccuroUserEndpointTest extends AbstractEndpointTest<AccuroUserEndpoint> {

  private AccuroUserManager manager;
  private AuditLogUser user;

  public AccuroUserEndpointTest() {
    super(new AccuroUserEndpoint(), AccuroUserEndpoint.class);
    manager = mock(AccuroUserManager.class);
    user = getFixture(AuditLogUser.class);
  }


  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(user);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> managerMap = new HashMap<>();
    managerMap.put(AccuroUserManager.class, manager);
    return managerMap;
  }

  @Test
  public void testCreate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, AccuroUserValidationException {
    AccuroUser accuroUser = getFixture(AccuroUser.class);
    Phone phoneDto = new Phone();
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    phoneDto.setContactType(ContactType.CellPhone);
    accuroUser.setCellPhone(phoneDto);
    Phone phoneDto1 = new Phone();
    phoneDto1.setNumber(RandomStringUtils.random(10, false, true));
    phoneDto1.setContactType(ContactType.HomePhone);
    accuroUser.setHomePhone(phoneDto1);
    Phone phoneDto2 = new Phone();
    phoneDto2.setNumber(RandomStringUtils.random(10, false, true));
    phoneDto2.setContactType(ContactType.WorkPhone);
    accuroUser.setWorkPhone(phoneDto2);
    int id = accuroUser.getUserId();
    accuroUser.setUserId(0);
    AccuroUserDto expected = mapDto(accuroUser, AccuroUserDto.class);
    formatPhoneNumber(expected);

    when(manager.createAccuroUser(mapDto(expected, AccuroUser.class), user)).thenReturn(id);
    int actual = given()
        .body(expected)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/users")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertEquals(id, actual);
    verify(manager).createAccuroUser(mapDto(expected, AccuroUser.class), user);
  }

  @Test
  public void testCreateNoBody() {
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/users")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
    verifyNoInteractions(manager);
  }

  @Test
  public void testCreateInvalidBody() throws TimeZoneNotFoundException,
      UnsupportedSchemaVersionException,
      DatabaseInteractionException, AccuroUserValidationException {
    AccuroUser accuroUser = getFixture(AccuroUser.class);
    accuroUser.setUsername("  ");
    Phone phoneDto = new Phone();
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    phoneDto.setContactType(ContactType.CellPhone);
    accuroUser.setCellPhone(phoneDto);
    accuroUser.setHomePhone(null);
    accuroUser.setWorkPhone(null);
    accuroUser.setUserId(0);
    AccuroUserDto newUser = mapDto(accuroUser, AccuroUserDto.class);
    formatPhoneNumber(newUser);

    when(manager.createAccuroUser(mapDto(newUser, AccuroUser.class), user))
        .thenThrow(new AccuroUserValidationException(""));
    given()
        .body(newUser)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/users")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
    verify(manager).createAccuroUser(mapDto(newUser, AccuroUser.class), user);

  }

  @Test
  public void testUpdate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, AccuroUserValidationException {
    AccuroUser accuroUser = getFixture(AccuroUser.class);
    Phone phoneDto = new Phone();
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    phoneDto.setContactType(ContactType.CellPhone);
    accuroUser.setCellPhone(phoneDto);
    accuroUser.setHomePhone(null);
    accuroUser.setWorkPhone(null);
    AccuroUserDto expected = mapDto(accuroUser, AccuroUserDto.class);
    formatPhoneNumber(expected);
    given()
        .body(expected)
        .pathParam("userId", expected.getUserId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/users/{userId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).updateAccuroUser(mapDto(expected, AccuroUser.class), user);
  }

  @Test
  public void testUpdateInvalidId() {
    AccuroUser accuroUser = getFixture(AccuroUser.class);
    Phone phoneDto = new Phone();
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    phoneDto.setContactType(ContactType.CellPhone);
    accuroUser.setCellPhone(phoneDto);
    accuroUser.setHomePhone(null);
    accuroUser.setWorkPhone(null);
    AccuroUserDto expected = mapDto(accuroUser, AccuroUserDto.class);
    given()
        .body(expected)
        .pathParam("userId", RandomUtils.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/users/{userId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

  }

  @Test
  public void testUpdateNoId() {
    AccuroUser accuroUser = getFixture(AccuroUser.class);
    AccuroUserDto expected = mapDto(accuroUser, AccuroUserDto.class);
    given()
        .body(expected)
        .pathParam("userId", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/users/{userId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);
  }

  @Test
  public void testUpdateNoBody() {
    given()
        .when()
        .contentType(ContentType.JSON)
        .pathParam("userId", TestUtilities.nextInt())
        .put(getBaseUrl() + "/v1/users/{userId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
    verifyNoInteractions(manager);
  }

  @Test
  public void testUpdateInvalidBody() throws UnsupportedSchemaVersionException,
      DatabaseInteractionException,
      TimeZoneNotFoundException,
      AccuroUserValidationException {
    AccuroUser accuroUser = getFixture(AccuroUser.class);
    accuroUser.setUsername("  ");
    Phone phoneDto = new Phone();
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    phoneDto.setContactType(ContactType.CellPhone);
    accuroUser.setCellPhone(phoneDto);
    accuroUser.setHomePhone(null);
    accuroUser.setWorkPhone(null);
    AccuroUserDto expected = mapDto(accuroUser, AccuroUserDto.class);
    formatPhoneNumber(expected);

    AccuroUser updateUser = mapDto(expected, AccuroUser.class);
    doThrow(new AccuroUserValidationException("")).when(manager)
        .updateAccuroUser(updateUser, user);

    given()
        .body(expected)
        .when()
        .contentType(ContentType.JSON)
        .pathParam("userId", expected.getUserId())
        .put(getBaseUrl() + "/v1/users/{userId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
  }

  private void formatPhoneNumber(AccuroUserDto accuroUserDto) {

    if (accuroUserDto.getCellPhone() != null) {
      if (StringUtils.isNotBlank(accuroUserDto.getCellPhone().getNumber())) {
        accuroUserDto.getCellPhone().setNumber(
            PhoneNumberFormatterUtils.formatPhoneNumber(accuroUserDto.getCellPhone().getNumber()));
      }
    }
    if (accuroUserDto.getHomePhone() != null) {
      if (StringUtils.isNotBlank(accuroUserDto.getHomePhone().getNumber())) {
        accuroUserDto.getHomePhone().setNumber(
            PhoneNumberFormatterUtils.formatPhoneNumber(accuroUserDto.getHomePhone().getNumber()));
      }
    }
    if (accuroUserDto.getWorkPhone() != null) {
      if (StringUtils.isNotBlank(accuroUserDto.getWorkPhone().getNumber())) {
        accuroUserDto.getWorkPhone().setNumber(
            PhoneNumberFormatterUtils.formatPhoneNumber(accuroUserDto.getWorkPhone().getNumber()));
      }
    }
  }


}
