
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeCancelResponseManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeCancelResponseManager;
import com.qhrtech.emr.accuro.db.eprescribe.EprescribeCancelRequestFixture;
import com.qhrtech.emr.accuro.db.eprescribe.EprescribeCancelResponseFixture;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeCancelRequest;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeCancelResponse;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeCancelResponseDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeCancelResponseEndpointIT extends
    AbstractEndpointIntegrationTest<EprescribeCancelResponseEndpoint> {

  private EprescribeCancelResponseManager erxCancelResponseManager;

  public EprescribeCancelResponseEndpointIT() throws IOException {
    super(new EprescribeCancelResponseEndpoint(), EprescribeCancelResponseEndpoint.class);
    erxCancelResponseManager = new DefaultEprescribeCancelResponseManager(ds, null, defaultUser());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(EprescribeCancelResponseManager.class,
        erxCancelResponseManager);
  }

  @Test
  public void testGetByRequestLocalId()
      throws Exception {

    EprescribeCancelResponse erxCancelResponse = new EprescribeCancelResponse();

    try (Connection conn = getConnection()) {
      EprescribeCancelResponseFixture fixture = new EprescribeCancelResponseFixture();
      fixture.setUp(conn);
      erxCancelResponse = fixture.get();
    } catch (Exception e) {
      fail("Fail to create new erx cancel response");
    }

    EprescribeCancelResponseDto actual = given()
        .pathParam("localId", erxCancelResponse.getEprescribeCancelRequestId())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}/responses")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(EprescribeCancelResponseDto.class);

    EprescribeCancelResponseDto expected =
        mapDto(erxCancelResponse, EprescribeCancelResponseDto.class);
    expected.setCreatedDate(actual.getCreatedDate());

    assertEquals(expected, actual);
    erxCancelResponseManager.deleteEprescribeCancelResponse(expected.getId());
  }

  @Test
  public void testCreate() throws Exception {
    EprescribeCancelResponse erxCancelResponse = getFixture(EprescribeCancelResponse.class);

    try (Connection conn = getConnection()) {
      EprescribeCancelRequestFixture cancelRequestFixture = new EprescribeCancelRequestFixture();
      cancelRequestFixture.setUp(conn);
      cancelRequestFixture.create(conn);
      EprescribeCancelRequest cancelRequest = cancelRequestFixture.get();
      erxCancelResponse.setEprescribeCancelRequestId(cancelRequest.getLocalId());
      erxCancelResponse.setPrescriptionMedicationId(cancelRequest.getPrescriptionMedicationId());
      erxCancelResponse.setPatientId(cancelRequest.getPatientId());

    } catch (Exception e) {
      fail("Fail to create new erx cancel response");
    }
    EprescribeCancelResponseDto expected =
        mapDto(erxCancelResponse,
            EprescribeCancelResponseDto.class);

    int id = given()
        .pathParam("localId", erxCancelResponse.getEprescribeCancelRequestId())
        .body(expected)
        .contentType(ContentType.JSON)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}/responses")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    EprescribeCancelResponse cancelResponse =
        erxCancelResponseManager.getEprescribeCancelResponseById(id);

    EprescribeCancelResponseDto actual =
        mapDto(cancelResponse, EprescribeCancelResponseDto.class);
    expected.setCreatedDate(actual.getCreatedDate());
    expected.setLastUpdated(actual.getLastUpdated());
    expected.setId(id);

    assertEquals(expected, actual);
    erxCancelResponseManager.deleteEprescribeCancelResponse(actual.getId());
  }

}
