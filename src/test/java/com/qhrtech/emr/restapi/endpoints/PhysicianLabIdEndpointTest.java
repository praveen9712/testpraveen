
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import com.qhrtech.emr.accuro.api.provider.PhysicianLabIdsManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.provider.PhysicianLab;
import com.qhrtech.emr.accuro.model.security.AuthorizedClient;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.PhysicianLabIdDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class PhysicianLabIdEndpointTest extends AbstractEndpointTest<PhysicianLabIdEndpoint> {

  private PhysicianLabIdsManager manager;


  public PhysicianLabIdEndpointTest() {
    super(new PhysicianLabIdEndpoint(), PhysicianLabIdEndpoint.class);
    manager = mock(PhysicianLabIdsManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(getFixture(AuditLogUser.class));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> managerMap = new HashMap<>();
    managerMap.put(PhysicianLabIdsManager.class, manager);
    return managerMap;
  }

  @Test
  public void testGet()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    List<AuthorizedClient> protossResult = getFixtures(AuthorizedClient.class, ArrayList::new, 10);

    Integer physicianId = TestUtilities.nextInt();
    doReturn(protossResult).when(manager)
        .getPhysicianLabIdsByPhysicianId(physicianId);

    // test
    List<PhysicianLabIdDto> actual = toCollection(
        given().pathParams("physicianId", physicianId)
            .when()
            .get(getBaseUrl() + "/v1/providers/{physicianId}/lab-ids")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PhysicianLabIdDto[].class),
        ArrayList::new);

    // assertions
    verify(manager).getPhysicianLabIdsByPhysicianId(physicianId);
  }

  @Test
  public void testGetWithParam()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {

    PhysicianLab physicianLab = getFixture(PhysicianLab.class);
    PhysicianLabIdDto expected = mapDto(physicianLab, PhysicianLabIdDto.class);
    given()
        .pathParams("physicianId", RandomUtils.nextInt())
        .body(expected)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/providers/{physicianId}/lab-ids")
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void testCreate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {
    PhysicianLab physicianLab = getFixture(PhysicianLab.class);
    PhysicianLabIdDto expected = mapDto(physicianLab, PhysicianLabIdDto.class);
    doNothing().when(manager).createPhysicianLabIds(physicianLab);
    given()
        .pathParams("physicianId", physicianLab.getPhysicianId())
        .body(expected)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/providers/{physicianId}/lab-ids")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode());

    verify(manager).createPhysicianLabIds(physicianLab);
  }

  @Test
  public void testCreateNoBody() {
    Integer physicianId = TestUtilities.nextInt();

    given().pathParams("physicianId", physicianId)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/providers/{physicianId}/lab-ids")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
    verifyNoInteractions(manager);
  }

  @Test
  public void testDelete() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {
    int id = TestUtilities.nextInt();
    doNothing().when(manager).deletePhysicianLabIds(id);
    given()
        .pathParams("physicianId", id)
        .when()
        .delete(getBaseUrl() + "/v1/providers/{physicianId}/lab-ids")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).deletePhysicianLabIds(id);
  }

  @Test
  public void testDeleteNoException()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {
    int id = TestUtilities.nextInt();
    doThrow(NoDataFoundException.class).when(manager).deletePhysicianLabIds(id);

    given()
        .pathParams("physicianId", id)
        .when()
        .delete(getBaseUrl() + "/v1/providers/{physicianId}/lab-ids")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(manager).deletePhysicianLabIds(id);
  }
}
