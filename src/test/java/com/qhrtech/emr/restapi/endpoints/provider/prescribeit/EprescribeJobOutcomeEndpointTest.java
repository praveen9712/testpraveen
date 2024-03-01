
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.messaging.eprescription.EprescribeOutcomeManager;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.messaging.eprescription.EprescribeOutcome;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobOutcomeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeJobOutcomeEndpointTest
    extends AbstractEndpointTest<EprescribeJobOutcomeEndpoint> {

  private final String subUrl = "/v1/provider-portal/erx-job-outcomes";
  private EprescribeOutcomeManager outcomeManager;

  public EprescribeJobOutcomeEndpointTest() {
    super(new EprescribeJobOutcomeEndpoint(), EprescribeJobOutcomeEndpoint.class);
    outcomeManager = mock(EprescribeOutcomeManager.class);
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
    servicesMap.put(EprescribeOutcomeManager.class, outcomeManager);
    return servicesMap;
  }

  @Test
  public void testCreate() throws SupportingResourceNotFoundException, TimeZoneNotFoundException,
      UnsupportedSchemaVersionException, DatabaseInteractionException {
    EprescribeJobOutcomeDto outcomeDto = getFixture(EprescribeJobOutcomeDto.class);
    outcomeDto.setCreatedAt(TestUtilities.nextLocalDateTime(false));
    outcomeDto.setTimestamp(TestUtilities.nextLocalDateTime(false));
    EprescribeOutcome outcome = mapDto(outcomeDto, EprescribeOutcome.class);
    int expected = outcomeDto.getId();

    doReturn(expected).when(outcomeManager).createErxOutcome(outcome);

    int actual = given()
        .body(outcomeDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + subUrl)
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertEquals(expected, actual);
    verify(outcomeManager).createErxOutcome(outcome);
  }

  @Test
  public void testCreateNull()
      throws SupportingResourceNotFoundException, TimeZoneNotFoundException,
      UnsupportedSchemaVersionException, DatabaseInteractionException {
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + subUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(outcomeManager, never()).createErxOutcome(any());
  }

  @Test
  public void testGetById() throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    EprescribeOutcome outcome = getFixture(EprescribeOutcome.class);
    EprescribeJobOutcomeDto expected = mapDto(outcome, EprescribeJobOutcomeDto.class);
    int id = expected.getId();

    doReturn(outcome).when(outcomeManager).getErxOutcome(id);

    EprescribeJobOutcomeDto actual = given()
        .pathParam("outcomeId", id)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + subUrl + "/{outcomeId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobOutcomeDto.class);

    assertEquals(expected, actual);
    verify(outcomeManager).getErxOutcome(id);
  }
}
