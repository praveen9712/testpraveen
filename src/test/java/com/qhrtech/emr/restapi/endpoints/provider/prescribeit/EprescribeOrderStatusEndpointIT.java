
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeOrderStatusManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeOrderStatusManager;
import com.qhrtech.emr.accuro.db.eprescribe.EprescribeOrderStatusFixture;
import com.qhrtech.emr.accuro.db.prescription.PrescriptionMedicationFixture;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeOrderStatus;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeOrderStatusSystem;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeOrderStatusDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeOrderStatusEndpointIT
    extends AbstractEndpointIntegrationTest<EprescribeOrderStatusEndpoint> {

  private final String endpointUrl = "/v1/provider-portal/eprescribe-order-statuses";

  EprescribeOrderStatusManager manager;

  public EprescribeOrderStatusEndpointIT()
      throws IOException {
    super(new EprescribeOrderStatusEndpoint(), EprescribeOrderStatusEndpoint.class);
    manager = new DefaultEprescribeOrderStatusManager(ds, null, defaultUser());
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
    servicesMap.put(EprescribeOrderStatusManager.class, manager);
    return servicesMap;
  }

  @Test
  public void create()
      throws ForbiddenException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      SupportingResourceNotFoundException, SQLException {

    EprescribeOrderStatus orderStatus = getFixture(EprescribeOrderStatus.class);
    orderStatus.setPrescriptionId(getPrescriptionId());

    EprescribeOrderStatusDto orderStatusDto = mapDto(orderStatus, EprescribeOrderStatusDto.class);

    // Test
    int actual = given()
        .body(orderStatusDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertTrue(actual > 0);

  }

  @Test
  public void testGetOrderStatuses()
      throws SQLException, UnsupportedSchemaVersionException {
    EprescribeOrderStatusFixture fixture = new EprescribeOrderStatusFixture();
    fixture.setUp(getConnection());
    EprescribeOrderStatus orderStatus = fixture.get();
    int rxId = orderStatus.getPrescriptionId();
    EprescribeOrderStatusSystem system = orderStatus.getSystem();

    List<EprescribeOrderStatusDto> actual = toCollection(given()
        .queryParam("rxId", rxId)
        .queryParam("system", system)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeOrderStatusDto[].class), ArrayList::new);
    // set created date as it is not updated by fixture
    orderStatus.setCreatedDate(actual.get(0).getCreatedDate());
    EprescribeOrderStatusDto expected = mapDto(orderStatus, EprescribeOrderStatusDto.class);
    List<EprescribeOrderStatusDto> expectedList = Arrays.asList(expected);
    assertEquals(expectedList, actual);
  }

  private int getPrescriptionId() throws SQLException, UnsupportedSchemaVersionException {
    PrescriptionMedicationFixture medicationFixture = new PrescriptionMedicationFixture();
    medicationFixture.setUp(getConnection());
    PrescriptionMedication medication = medicationFixture.get();
    return medication.getPrescriptionId();
  }
}
