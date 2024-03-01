
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeCancelResponseManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeCancelResponse;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeCancelResponseDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeCancelResponseEndpointTest
    extends AbstractEndpointTest<EprescribeCancelResponseEndpoint> {

  EprescribeCancelResponseManager manager;

  public EprescribeCancelResponseEndpointTest() {
    super(new EprescribeCancelResponseEndpoint(), EprescribeCancelResponseEndpoint.class);
    manager = mock(EprescribeCancelResponseManager.class);
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
    servicesMap.put(EprescribeCancelResponseManager.class, manager);
    return servicesMap;
  }

  @Test
  public void testGetByRequestLocalId()
      throws Exception {
    EprescribeCancelResponseDto expected = getFixture(EprescribeCancelResponseDto.class);
    int localId = expected.getEprescribeCancelRequestId();
    EprescribeCancelResponse protossModel = mapDto(expected, EprescribeCancelResponse.class);
    when(manager.getEprescribeCancelResponseByCancelRequestlId(localId)).thenReturn(protossModel);

    EprescribeCancelResponseDto actual = given()
        .pathParam("localId", localId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}/responses")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(EprescribeCancelResponseDto.class);

    assertEquals(expected, actual);
    verify(manager).getEprescribeCancelResponseByCancelRequestlId(localId);

  }

  @Test
  public void testCreate() throws Exception {
    EprescribeCancelResponseDto cancelResponseDto = getFixture(EprescribeCancelResponseDto.class);

    EprescribeCancelResponse protossModel =
        mapDto(cancelResponseDto, EprescribeCancelResponse.class);

    when(manager.createEprescribeCancelResponse(protossModel)).thenReturn(protossModel.getId());

    int actual = given()
        .pathParam("localId", cancelResponseDto.getEprescribeCancelRequestId())
        .body(cancelResponseDto)
        .contentType(ContentType.JSON)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}/responses")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertTrue(cancelResponseDto.getId() == actual);
    verify(manager).createEprescribeCancelResponse(protossModel);

  }

  @Test
  public void testCreateUnmatchedLocalId() throws Exception {
    EprescribeCancelResponseDto cancelResponseDto = getFixture(EprescribeCancelResponseDto.class);

    given()
        .pathParam("localId", cancelResponseDto.getEprescribeCancelRequestId() + 1)
        .body(cancelResponseDto)
        .contentType(ContentType.JSON)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}/responses")
        .then()
        .assertThat()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);

  }

}
