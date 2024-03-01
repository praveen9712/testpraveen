
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeCancelRequestManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeCancelRequestManager;
import com.qhrtech.emr.accuro.db.eprescribe.EprescribeCancelRequestFixture;
import com.qhrtech.emr.accuro.db.prescription.PrescriptionMedicationFixture;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeCancelRequest;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ErxCancelRequestsDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class ErxCancelRequestEndpointIT extends
    AbstractEndpointIntegrationTest<ErxCancelRequestEndpoint> {

  private EprescribeCancelRequestManager managerWithDs;

  public ErxCancelRequestEndpointIT() throws IOException {
    super(new ErxCancelRequestEndpoint(), ErxCancelRequestEndpoint.class);
    managerWithDs = new DefaultEprescribeCancelRequestManager(ds,
        null, defaultUser());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(EprescribeCancelRequestManager.class, managerWithDs);
  }

  @Test
  public void testGetByLocalId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {

    EprescribeCancelRequest request = setUpAndGet();

    ErxCancelRequestsDto actual = given()
        .pathParam("localId", request.getLocalId())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ErxCancelRequestsDto.class);

    ErxCancelRequestsDto expected = mapDto(request, ErxCancelRequestsDto.class);

    assertEquals(expected, actual);
    managerWithDs.deleteEprescribeCancelRequest(request.getLocalId());

  }

  @Test
  public void testGetByLocalIdNotFound() {

    given()
        .pathParam("localId", TestUtilities.nextInt())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void testGetByExternalId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {

    EprescribeCancelRequest request = setUpAndGet();

    UUID externalId = request.getExternalId();
    ErxCancelRequestsDto actual = given()
        .queryParam("externalId", externalId.toString())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ErxCancelRequestsDto.class);

    ErxCancelRequestsDto expected = mapDto(request, ErxCancelRequestsDto.class);

    assertEquals(expected, actual);
    managerWithDs.deleteEprescribeCancelRequest(request.getLocalId());

  }

  @Test
  public void testGetByExternalIdNotFound() {

    UUID externalId = UUID.randomUUID();
    given()
        .queryParam("externalId", externalId.toString())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void testGetByPrescriptionId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {

    EprescribeCancelRequest request = setUpAndGet();

    Integer prescriptionId = request.getPrescriptionMedicationId();
    ErxCancelRequestsDto actual = given()
        .queryParam("prescriptionId", prescriptionId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ErxCancelRequestsDto.class);

    ErxCancelRequestsDto expected = mapDto(request, ErxCancelRequestsDto.class);

    assertEquals(expected, actual);
    managerWithDs.deleteEprescribeCancelRequest(request.getLocalId());
  }

  @Test
  public void testCreate()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      TimeZoneNotFoundException, SQLException {
    EprescribeCancelRequest request = setUpAndGet();
    request.setExternalId(UUID.randomUUID());
    request.setPrescriptionMedicationId(getPrescriptionId());
    ErxCancelRequestsDto expected = mapDto(request, ErxCancelRequestsDto.class);

    Integer id = given()
        .body(expected)
        .contentType(ContentType.JSON)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    EprescribeCancelRequest eprescribeCancelRequestById =
        managerWithDs.getEprescribeCancelRequestById(id);
    expected.setLocalId(id);
    // set created and updated date time as set by protoss
    expected.setCreated(eprescribeCancelRequestById.getCreated());
    expected.setLastUpdated(eprescribeCancelRequestById.getLastUpdated());

    ErxCancelRequestsDto actual = mapDto(eprescribeCancelRequestById, ErxCancelRequestsDto.class);

    assertEquals(expected, actual);
    managerWithDs.deleteEprescribeCancelRequest(request.getLocalId());
  }

  @Test
  public void testUpdate()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      TimeZoneNotFoundException {
    EprescribeCancelRequest request = setUpAndGet();
    ErxCancelRequestsDto expected = mapDto(request, ErxCancelRequestsDto.class);

    expected.setDeliveryMethod(TestUtilities.nextString(10));
    given()
        .pathParam("localId", request.getLocalId())
        .body(expected)
        .contentType(ContentType.JSON)
        .when()
        .put(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    EprescribeCancelRequest eprescribeCancelRequestById =
        managerWithDs.getEprescribeCancelRequestById(request.getLocalId());

    expected.setLastUpdated(eprescribeCancelRequestById.getLastUpdated());
    ErxCancelRequestsDto actual = mapDto(eprescribeCancelRequestById, ErxCancelRequestsDto.class);
    assertEquals(expected, actual);
    managerWithDs.deleteEprescribeCancelRequest(request.getLocalId());
  }

  @Test
  public void testDelete()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    EprescribeCancelRequest request = setUpAndGet();

    given()
        .pathParam("localId", request.getLocalId())
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    try {
      managerWithDs.getEprescribeCancelRequestById(request.getLocalId());
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testDeleteNotFound() {

    given()
        .pathParam("localId", TestUtilities.nextInt())
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/erx-cancel-requests/{localId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  private Integer getPrescriptionId() throws SQLException, UnsupportedSchemaVersionException {

    try (Connection conn = getConnection()) {
      PrescriptionMedicationFixture medicationFixture = new PrescriptionMedicationFixture();
      medicationFixture.setUp(conn);
      return medicationFixture.get().getPrescriptionId();
    }
  }


  private EprescribeCancelRequest setUpAndGet() {
    EprescribeCancelRequest request = new EprescribeCancelRequest();

    try (Connection conn = getConnection()) {
      EprescribeCancelRequestFixture fixture = new EprescribeCancelRequestFixture();
      fixture.setUp(conn);
      fixture.create(conn);
      request = fixture.get();
    } catch (Exception ex) {
      fail("Fail to create new Eprescribe cancel request");
    }
    return request;
  }

}
