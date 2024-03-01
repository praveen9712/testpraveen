
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import com.qhrtech.emr.accuro.api.provider.DefaultPhysicianLabIdsManager;
import com.qhrtech.emr.accuro.api.provider.PhysicianLabIdsManager;
import com.qhrtech.emr.accuro.db.physicianlabids.PhysicianLabIdsFixture;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.provider.PhysicianLab;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.PhysicianLabIdDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class PhysicianLabIdEndpointIT extends
    AbstractEndpointIntegrationTest<PhysicianLabIdEndpoint> {

  private PhysicianLabIdsManager manager;

  public PhysicianLabIdEndpointIT() throws IOException {
    super(new PhysicianLabIdEndpoint(), PhysicianLabIdEndpoint.class);
    manager = new DefaultPhysicianLabIdsManager(getDs(), null, defaultUser());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(defaultUser());
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> managerMap = new HashMap<>();
    managerMap.put(PhysicianLabIdsManager.class, manager);
    return managerMap;
  }

  @Test
  public void testGetPhysicianLabs()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, SQLException {

    PhysicianLabIdsFixture fixture = new PhysicianLabIdsFixture();
    fixture.setUp(getConnection());
    PhysicianLabIdDto physicianLabDto = mapDto(fixture.get(), PhysicianLabIdDto.class);
    int id = physicianLabDto.getPhysicianId();
    List<PhysicianLabIdDto> actual = toCollection(
        given().pathParams("physicianId", id)
            .when()
            .get(getBaseUrl() + "/v1/providers/{physicianId}/lab-ids")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PhysicianLabIdDto[].class),
        ArrayList::new);

    assertTrue(actual.size() > 0);
  }

  @Test
  public void testCreate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, SQLException {

    PhysicianLabIdsFixture fixture = new PhysicianLabIdsFixture();
    fixture.setUp(getConnection());
    PhysicianLabIdDto physicianLabDto = mapDto(fixture.get(), PhysicianLabIdDto.class);
    int id = physicianLabDto.getPhysicianId();
    given()
        .pathParam("physicianId", id)
        .body(physicianLabDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/providers/{physicianId}/lab-ids")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode());
  }


  @Test
  public void testDelete() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException, SQLException {
    PhysicianLabIdsFixture fixture = new PhysicianLabIdsFixture();
    fixture.setUp(getConnection());
    PhysicianLabIdDto physicianLabDto = mapDto(fixture.get(), PhysicianLabIdDto.class);
    int id = physicianLabDto.getPhysicianId();
    given()
        .pathParam("physicianId", id)
        .when()
        .delete(getBaseUrl() + "/v1/providers/{physicianId}/lab-ids")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    List<PhysicianLab> physicianLabList =
        manager.getPhysicianLabIdsByPhysicianId(physicianLabDto.getPhysicianId());
    assertTrue(physicianLabList.size() == 0);
  }
}
