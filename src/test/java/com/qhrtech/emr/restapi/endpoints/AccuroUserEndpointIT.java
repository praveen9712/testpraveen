
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import com.qhrtech.emr.accuro.api.security.AccuroUserManager;
import com.qhrtech.emr.accuro.api.security.DefaultAccuroUserManager;
import com.qhrtech.emr.accuro.db.provider.ProviderFixture;
import com.qhrtech.emr.accuro.db.security.AccuroUserFixture;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AccuroUserDto;
import com.qhrtech.emr.restapi.models.dto.ContactType;
import com.qhrtech.emr.restapi.models.dto.PhoneDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

public class AccuroUserEndpointIT extends AbstractEndpointIntegrationTest<AccuroUserEndpoint> {

  private AccuroUserManager manager;
  private AuditLogUser user;


  public AccuroUserEndpointIT() throws IOException {
    super(new AccuroUserEndpoint(), AccuroUserEndpoint.class);
    manager = new DefaultAccuroUserManager(getDs());
    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
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
      TimeZoneNotFoundException {

    AccuroUserDto accuroUser = new AccuroUserDto();
    PhoneDto phoneDto = new PhoneDto();
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    phoneDto.setContactType(ContactType.CellPhone);
    accuroUser.setCellPhone(phoneDto);
    accuroUser.setHomePhone(null);
    accuroUser.setWorkPhone(null);
    accuroUser.setActive(true);
    accuroUser.setUsername(TestUtilities.nextString(20));
    accuroUser.setFirstName("");
    accuroUser.setLastName("");
    accuroUser.setActiveDirectoryUser("");
    int actual = given()
        .body(accuroUser)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/users")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    AccuroUser accuroUser1 = manager.getAccuroUser(actual);

    AccuroUserDto getById = mapDto(accuroUser1, AccuroUserDto.class);
    accuroUser.setAddress(getById.getAddress());
    accuroUser.setUserId(actual);

    assertEquals(getById, accuroUser);

  }

  @Test
  public void testUpdate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException, SQLException {
    AccuroUserFixture fixture = new AccuroUserFixture();
    ProviderFixture providerFixture = new ProviderFixture();
    providerFixture.setUp(getConnection());
    fixture.setProviderId(providerFixture.get().getProviderId());
    fixture.setUp(getConnection());
    AccuroUserDto updatedDto = mapDto(fixture.get(), AccuroUserDto.class);
    PhoneDto phoneDto = new PhoneDto();
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    phoneDto.setContactType(ContactType.CellPhone);
    updatedDto.setCellPhone(phoneDto);
    updatedDto.setHomePhone(null);
    updatedDto.setWorkPhone(null);
    updatedDto.setFirstName(TestUtilities.nextString(25));

    given()
        .body(updatedDto)
        .pathParam("userId", updatedDto.getUserId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/users/{userId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());
    AccuroUser accuroUser1 = manager.getAccuroUser(updatedDto.getUserId());
    AccuroUserDto getById = mapDto(accuroUser1, AccuroUserDto.class);
    assertEquals(updatedDto, getById);
  }
}
