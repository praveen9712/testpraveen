
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeCancelRequestManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeCancelRequest;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ErxCancelRequestsDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class ErxCancelRequestEndpointTest extends AbstractEndpointTest<ErxCancelRequestEndpoint> {


  EprescribeCancelRequestManager manager;

  public ErxCancelRequestEndpointTest() {
    super(new ErxCancelRequestEndpoint(), ErxCancelRequestEndpoint.class);
    manager = mock(EprescribeCancelRequestManager.class);
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
    servicesMap.put(EprescribeCancelRequestManager.class, manager);
    return servicesMap;
  }

  @Test
  public void testGetByLocalId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {

    ErxCancelRequestsDto expected = getFixture(ErxCancelRequestsDto.class);

    int localId = expected.getLocalId();
    EprescribeCancelRequest protossModel = mapDto(expected, EprescribeCancelRequest.class);
    when(manager.getEprescribeCancelRequestById(localId)).thenReturn(protossModel);

    ErxCancelRequestsDto actual = given()
        .pathParam("localId", localId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ErxCancelRequestsDto.class);

    assertEquals(expected, actual);
    verify(manager).getEprescribeCancelRequestById(localId);

  }

  @Test
  public void testGetByExternalId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    ErxCancelRequestsDto expected = getFixture(ErxCancelRequestsDto.class);

    UUID externalId = expected.getExternalId();
    EprescribeCancelRequest protossModel = mapDto(expected, EprescribeCancelRequest.class);
    when(manager.getEprescribeCancelRequestByExternalId(externalId)).thenReturn(protossModel);

    ErxCancelRequestsDto actual = given()
        .queryParam("externalId", externalId.toString())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ErxCancelRequestsDto.class);

    assertEquals(expected, actual);
    verify(manager).getEprescribeCancelRequestByExternalId(externalId);
  }

  @Test
  public void testGetByExternalIdNoExternalId() {

    given()
        .queryParam("externalId", "")
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
    verifyNoInteractions(manager);
  }

  @Test
  public void testGetByPrescriptionId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    ErxCancelRequestsDto expected = getFixture(ErxCancelRequestsDto.class);

    Integer prescriptionId = expected.getPrescriptionMedicationId();
    EprescribeCancelRequest erxCancelRequest = mapDto(expected, EprescribeCancelRequest.class);
    when(manager.getEprescribeCancelRequestByRxId(prescriptionId)).thenReturn(erxCancelRequest);

    ErxCancelRequestsDto actual = given()
        .queryParam("prescriptionId", prescriptionId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ErxCancelRequestsDto.class);

    assertEquals(expected, actual);
    verify(manager).getEprescribeCancelRequestByRxId(prescriptionId);
  }

  @Test
  public void testGetByPrescriptionIdAndExternalId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    ErxCancelRequestsDto expected = getFixture(ErxCancelRequestsDto.class);

    Integer prescriptionId = expected.getPrescriptionMedicationId();
    UUID externalId = expected.getExternalId();
    EprescribeCancelRequest erxCancelRequest = mapDto(expected, EprescribeCancelRequest.class);
    when(manager.getEprescribeCancelRequestByExternalId(externalId)).thenReturn(erxCancelRequest);

    ErxCancelRequestsDto actual = given()
        .queryParam("externalId", externalId.toString())
        .queryParam("prescriptionId", prescriptionId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ErxCancelRequestsDto.class);

    assertEquals(expected, actual);
    verify(manager).getEprescribeCancelRequestByExternalId(externalId);
    verify(manager, never()).getEprescribeCancelRequestByRxId(prescriptionId);
  }

  @Test
  public void testCreate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {
    ErxCancelRequestsDto cancelRequestsDto = getFixture(ErxCancelRequestsDto.class);

    EprescribeCancelRequest protossModel = mapDto(cancelRequestsDto, EprescribeCancelRequest.class);

    when(manager.createEprescribeCancelRequest(protossModel)).thenReturn(protossModel.getLocalId());

    Integer actual = given()
        .body(cancelRequestsDto)
        .contentType(ContentType.JSON)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertTrue(cancelRequestsDto.getLocalId() == actual);
    verify(manager).createEprescribeCancelRequest(protossModel);

  }

  @Test
  public void testCreateWithoutBody() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);
  }

  @Test
  public void testUpdate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {
    ErxCancelRequestsDto cancelRequestsDto = getFixture(ErxCancelRequestsDto.class);

    EprescribeCancelRequest protossModel = mapDto(cancelRequestsDto, EprescribeCancelRequest.class);


    given()
        .pathParam("localId", protossModel.getLocalId())
        .body(cancelRequestsDto)
        .contentType(ContentType.JSON)
        .when()
        .put(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).updateEprescribeCancelRequest(protossModel);

  }

  @Test
  public void testUpdateIdNotPath() {
    ErxCancelRequestsDto cancelRequestsDto = getFixture(ErxCancelRequestsDto.class);

    given()
        .pathParam("localId", TestUtilities.nextInt())
        .body(cancelRequestsDto)
        .contentType(ContentType.JSON)
        .when()
        .put(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);

  }

  @Test
  public void testUpdateWithoutBody() {
    given()
        .pathParam("localId", TestUtilities.nextInt())
        .contentType(ContentType.JSON)
        .when()
        .put(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);
  }

  @Test
  public void testDelete() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {
    int localId = TestUtilities.nextInt();
    given()
        .pathParam("localId", localId)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).deleteEprescribeCancelRequest(localId);

  }


}
