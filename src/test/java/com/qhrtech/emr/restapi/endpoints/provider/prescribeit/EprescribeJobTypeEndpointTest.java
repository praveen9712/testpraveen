
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobTypeManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobType;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobTypeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeJobTypeEndpointTest extends AbstractEndpointTest<EprescribeJobTypeEndpoint> {

  private EprescribeJobTypeManager manager;

  public EprescribeJobTypeEndpointTest() {
    super(new EprescribeJobTypeEndpoint(), EprescribeJobTypeEndpoint.class);
    manager = mock(EprescribeJobTypeManager.class);
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
    Map<Class, Object> services = new HashMap<>();
    services.put(EprescribeJobTypeManager.class, manager);
    return services;
  }

  @Test
  public void testGetAllJobTypes()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    List<EprescribeJobTypeDto> expected =
        getFixtures(EprescribeJobTypeDto.class, ArrayList::new, 10);
    List<EprescribeJobType> protossResult =
        mapDto(expected, EprescribeJobType.class, ArrayList::new);
    when(manager.getAllJobTypes()).thenReturn(protossResult);

    List<EprescribeJobTypeDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/erx-job-types")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(EprescribeJobTypeDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(manager).getAllJobTypes();
  }

  @Test
  public void testCreate()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      BusinessLogicException {
    EprescribeJobTypeDto dto = getFixture(EprescribeJobTypeDto.class);

    EprescribeJobType protossModel = mapDto(dto, EprescribeJobType.class);

    when(manager.createEprescribeJobType(protossModel)).thenReturn(dto.getEprescribeJobTypeId());

    Integer actual = given()
        .body(dto)
        .when()
        .contentType(JSON)
        .post(getBaseUrl() + "/v1/provider-portal/erx-job-types")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertTrue(dto.getEprescribeJobTypeId() == actual);
    verify(manager).createEprescribeJobType(protossModel);
  }

  @Test
  public void testDelete()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    int id = TestUtilities.nextInt();

    doNothing().when(manager).deleteEprescribeJobType(id);

    given()
        .pathParam("id", id)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/erx-job-types/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).deleteEprescribeJobType(id);

  }
}
