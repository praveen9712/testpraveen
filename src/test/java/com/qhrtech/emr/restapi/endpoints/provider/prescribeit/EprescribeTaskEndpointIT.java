
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobTaskManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobTaskManager;
import com.qhrtech.emr.accuro.db.eprescribe.EprescribeJobTaskFixture;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobTaskDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeTaskEndpointIT
    extends AbstractEndpointIntegrationTest<EprescribeTaskEndpoint> {

  private static final String ENDPOINT_URL = "/v1/provider-portal/erx-job-tasks";
  private final EprescribeJobTaskManager manager;
  private final AuditLogUser user;

  public EprescribeTaskEndpointIT() throws IOException {
    super(new EprescribeTaskEndpoint(), EprescribeTaskEndpoint.class);
    manager = new DefaultEprescribeJobTaskManager(getDs());
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
    managerMap.put(EprescribeJobTaskManager.class, manager);
    return managerMap;
  }

  @Test
  public void testGetById()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      NoDataFoundException {
    EprescribeJobTaskFixture fixture = new EprescribeJobTaskFixture();
    fixture.setUp(getConnection());
    EprescribeJobTaskDto expected = mapDto(fixture.get(), EprescribeJobTaskDto.class);

    EprescribeJobTaskDto actual = given()
        .pathParam("id", expected.getErxJobTaskId())
        .when()
        .get(getBaseUrl() + ENDPOINT_URL + "/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(EprescribeJobTaskDto.class);
    assertEquals(expected, actual);

    manager.deleteEprescribeJobTask(expected.getErxJobTaskId());
  }
}
