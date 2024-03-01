
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobTaskManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobTask;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobTaskDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeTaskEndpointTest extends AbstractEndpointTest<EprescribeTaskEndpoint> {

  EprescribeJobTaskManager manager;

  public EprescribeTaskEndpointTest() {
    super(new EprescribeTaskEndpoint(), EprescribeTaskEndpoint.class);
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
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    EprescribeJobTaskDto expected = getFixture(EprescribeJobTaskDto.class);
    int id = expected.getErxJobTaskId();

    EprescribeJobTask protossModel = mapDto(expected, EprescribeJobTask.class);

    when(manager.getEprescribeJobTaskById(id)).thenReturn(protossModel);

    EprescribeJobTaskDto actual = given()
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-job-tasks/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(EprescribeJobTaskDto.class);

    assertEquals(expected, actual);
    verify(manager).getEprescribeJobTaskById(id);
  }

  @Test
  public void testGetById_NotFound()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    EprescribeJobTaskDto expected = getFixture(EprescribeJobTaskDto.class);
    int id = expected.getErxJobTaskId();
    when(manager.getEprescribeJobTaskById(id)).thenReturn(null);

    given()
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-job-tasks/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(manager).getEprescribeJobTaskById(id);
  }
}
