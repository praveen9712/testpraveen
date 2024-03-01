
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobTaskManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobTask;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobTaskDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeJobTaskEndpointTest extends AbstractEndpointTest<EprescribeJobTaskEndpoint> {

  EprescribeJobTaskManager manager;

  public EprescribeJobTaskEndpointTest() {
    super(new EprescribeJobTaskEndpoint(), EprescribeJobTaskEndpoint.class);
    manager = mock(EprescribeJobTaskManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(EprescribeJobTaskManager.class, manager);
    return servicesMap;
  }

  @Test
  public void testGetById()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    EprescribeJobTaskDto expected = getFixture(EprescribeJobTaskDto.class);
    int jobId = expected.getErxJobId();
    int id = expected.getErxJobTaskId();

    EprescribeJobTask protossModel = mapDto(expected, EprescribeJobTask.class);

    when(manager.getEprescribeJobTaskById(id)).thenReturn(protossModel);

    EprescribeJobTaskDto actual = given()
        .pathParam("jobId", jobId)
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(EprescribeJobTaskDto.class);

    assertEquals(expected, actual);
    verify(manager).getEprescribeJobTaskById(id);
  }


  @Test
  public void testGetByIdNotFound()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    EprescribeJobTaskDto expected = getFixture(EprescribeJobTaskDto.class);
    int jobId = TestUtilities.nextInt();
    int id = expected.getErxJobTaskId();

    EprescribeJobTask protossModel = mapDto(expected, EprescribeJobTask.class);

    when(manager.getEprescribeJobTaskById(id)).thenReturn(protossModel);

    given()
        .pathParam("jobId", jobId)
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(manager).getEprescribeJobTaskById(id);
  }

  @Test
  public void testGetByJobId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    List<EprescribeJobTaskDto> expected =
        getFixtures(EprescribeJobTaskDto.class, ArrayList::new, 10);
    int jobId = TestUtilities.nextInt();
    List<EprescribeJobTask> protossResult =
        mapDto(expected, EprescribeJobTask.class, ArrayList::new);
    when(manager.getAllJobTasksByJobId(jobId)).thenReturn(protossResult);

    List<EprescribeJobTaskDto> actual = toCollection(
        given()
            .pathParam("jobId", jobId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(EprescribeJobTaskDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(manager).getAllJobTasksByJobId(jobId);
  }

  @Test
  public void testCreate() throws ResourceConflictException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, SupportingResourceNotFoundException {
    EprescribeJobTaskDto dto = getFixture(EprescribeJobTaskDto.class);
    int jobId = dto.getErxJobId();
    EprescribeJobTask protossModel = mapDto(dto, EprescribeJobTask.class);

    when(manager.createEprescribeJobTask(protossModel)).thenReturn(dto.getErxJobTaskId());

    Integer id = given()
        .pathParam("jobId", jobId)
        .body(dto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode()).extract().as(Integer.class);

    assertTrue(dto.getErxJobTaskId() == id);
    verify(manager).createEprescribeJobTask(protossModel);

  }

  @Test
  public void testCreateNullBody() {
    given()
        .pathParam("jobId", TestUtilities.nextId())
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);

  }

  @Test
  public void testCreatePathBodyIdNotMatch() {
    EprescribeJobTaskDto dto = getFixture(EprescribeJobTaskDto.class);
    given()
        .pathParam("jobId", TestUtilities.nextInt())
        .body(dto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);

  }

  @Test
  public void testUpdate() throws ResourceConflictException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, SupportingResourceNotFoundException, NoDataFoundException {
    EprescribeJobTaskDto dto = getFixture(EprescribeJobTaskDto.class);
    int jobId = dto.getErxJobId();
    given()
        .pathParam("jobId", jobId)
        .pathParam("id", dto.getErxJobTaskId())
        .body(dto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).updateEprescribeJobTask(mapDto(dto, EprescribeJobTask.class));

  }

  @Test
  public void testUpdateNullBody() {
    given()
        .pathParam("jobId", TestUtilities.nextId())
        .pathParam("id", TestUtilities.nextId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);

  }

  @Test
  public void testUpdatePathBodyJobIdNotMatch() {
    EprescribeJobTaskDto dto = getFixture(EprescribeJobTaskDto.class);
    given()
        .pathParam("jobId", TestUtilities.nextInt())
        .pathParam("id", dto.getErxJobTaskId())
        .body(dto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);

  }

  @Test
  public void testUpdatePathBodyIdNotMatch() {
    EprescribeJobTaskDto dto = getFixture(EprescribeJobTaskDto.class);
    given()
        .pathParam("jobId", dto.getErxJobId())
        .pathParam("id", TestUtilities.nextInt())
        .body(dto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);

  }

  @Test
  public void testDelete()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {

    EprescribeJobTask protossResult = getFixture(EprescribeJobTask.class);
    int id = protossResult.getePrescribeJobTaskId();
    int jobId = protossResult.getePrescribeJobId();

    when(manager.getEprescribeJobTaskById(id)).thenReturn(protossResult);
    given()
        .pathParam("jobId", jobId)
        .pathParam("id", id)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).deleteEprescribeJobTask(id);
    verify(manager).getEprescribeJobTaskById(id);
  }

  @Test
  public void testDeleteNotFound()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {

    EprescribeJobTask protossResult = getFixture(EprescribeJobTask.class);
    int id = protossResult.getePrescribeJobTaskId();
    int jobId = TestUtilities.nextInt();

    when(manager.getEprescribeJobTaskById(id)).thenReturn(protossResult);
    given()
        .pathParam("jobId", jobId)
        .pathParam("id", id)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/erx-jobs/{jobId}/tasks/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(manager).getEprescribeJobTaskById(id);
  }

}
