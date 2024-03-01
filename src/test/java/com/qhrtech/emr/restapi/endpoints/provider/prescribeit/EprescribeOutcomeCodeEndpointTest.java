
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.messaging.eprescription.EprescribeOutcomeCodeManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.messaging.eprescription.EprescribeOutcomeCode;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeOutcomeCodeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeOutcomeCodeEndpointTest
    extends AbstractEndpointTest<EprescribeOutcomeCodeEndpoint> {

  private final String endpointUrl = "/v1/provider-portal/erx-outcome-codes";
  EprescribeOutcomeCodeManager manager;

  public EprescribeOutcomeCodeEndpointTest() {
    super(new EprescribeOutcomeCodeEndpoint(), EprescribeOutcomeCodeEndpoint.class);
    manager = mock(EprescribeOutcomeCodeManager.class);
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
    servicesMap.put(EprescribeOutcomeCodeManager.class, manager);
    return servicesMap;
  }

  @Test
  public void testGetAllErxOutcomeCode()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {

    List<EprescribeOutcomeCode> expected =
        getFixtures(EprescribeOutcomeCode.class, ArrayList::new, 2);

    // mock dependencies
    when(manager.getAllErxOutcomeCodes()).thenReturn(expected);

    // test
    List<EprescribeOutcomeCode> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + endpointUrl)
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(EprescribeOutcomeCode[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(manager).getAllErxOutcomeCodes();
  }

  @Test
  public void testCreateErxOutcomeCode()
      throws UnsupportedSchemaVersionException,
      DatabaseInteractionException {
    EprescribeOutcomeCodeDto erxOutcomeCodeDto = getFixture(EprescribeOutcomeCodeDto.class);
    EprescribeOutcomeCode erxOutcomeCode = mapDto(erxOutcomeCodeDto, EprescribeOutcomeCode.class);
    int expected = erxOutcomeCode.getId();

    doReturn(expected).when(manager).createErxOutcomeCode(erxOutcomeCode);

    int actual = given()
        .body(erxOutcomeCodeDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    verify(manager).createErxOutcomeCode(erxOutcomeCode);
    assertEquals(expected, actual);
  }

  @Test
  public void testCreateErxOutcomeCodeWithoutBody() {
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void testDeleteErxOutcomeCode()
      throws UnsupportedSchemaVersionException,
      DatabaseInteractionException, NoDataFoundException {

    int id = TestUtilities.nextInt();
    given()
        .pathParam("id", id)
        .when()
        .delete(getBaseUrl() + endpointUrl + "/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).deleteErxOutcomeCode(id);
  }

}
