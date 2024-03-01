
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultExternalPatientManager;
import com.qhrtech.emr.accuro.api.eprescribe.ExternalPatientManager;
import com.qhrtech.emr.accuro.db.eprescribe.ExternalPatientFixture;
import com.qhrtech.emr.accuro.model.eprescribe.ExternalPatient;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ExternalPatientDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.util.RandomUtils;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class ExternalPatientEndpointIT extends
    AbstractEndpointIntegrationTest<ExternalPatientEndpoint> {

  private ExternalPatientManager managerWithDs;


  public ExternalPatientEndpointIT() throws IOException {
    super(new ExternalPatientEndpoint(), ExternalPatientEndpoint.class);
    managerWithDs = new DefaultExternalPatientManager(ds,
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
    return Collections.singletonMap(ExternalPatientManager.class, managerWithDs);
  }

  @Test
  public void testGetById()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {

    ExternalPatientDto expected = setUpAndGet();

    ExternalPatientDto actual = given()
        .pathParam("id", expected.getId())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/external-patient/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalPatientDto.class);

    assertEquals(expected, actual);
    managerWithDs.deleteExternalPatient(expected.getId());

  }

  @Test
  public void testGetExternalPatients()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {

    ExternalPatientDto expected = setUpAndGet();

    List<ExternalPatientDto> actual = toCollection(given()
        .queryParam("extSystemId", expected.getExternalSystemIdentifier())
        .queryParam("extPatientId", expected.getExternalPatientIdentifier())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/external-patient/")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(ExternalPatientDto[].class),
        ArrayList::new);

    assertEquals(expected, actual.get(0));
    managerWithDs.deleteExternalPatient(expected.getId());
  }

  @Test
  public void testGetExternalPatientsEmpty() {

    List<ExternalPatientDto> actual = toCollection(given()
        .queryParam("extSystemId", RandomUtils.getString(10))
        .queryParam("extPatientId", RandomUtils.getString(10))
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/external-patient/")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(ExternalPatientDto[].class),
        ArrayList::new);

    assertTrue(actual.isEmpty());
  }

  @Test
  public void testCreate()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      TimeZoneNotFoundException, SQLException {

    ExternalPatientDto expected = setUpAndGet();

    expected.setExternalSystemIdentifier(TestUtilities.nextString(225));
    expected.setExternalPatientIdentifier(TestUtilities.nextString(225));

    Integer id = given()
        .body(expected)
        .contentType(ContentType.JSON)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/external-patient/")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    ExternalPatient externalPatient =
        managerWithDs.getExternalPatientById(id);

    ExternalPatientDto actual = mapDto(externalPatient, ExternalPatientDto.class);
    expected.setId(id);
    assertEquals(expected, actual);
    managerWithDs.deleteExternalPatient(id);
  }


  private ExternalPatientDto setUpAndGet() {
    ExternalPatient externalPatient = new ExternalPatient();

    try (Connection conn = getConnection()) {
      ExternalPatientFixture fixture = new ExternalPatientFixture();
      fixture.setUp(conn);
      externalPatient = fixture.get();
    } catch (Exception ex) {
      fail("Fail to create new External Patient");
    }
    return mapDto(externalPatient, ExternalPatientDto.class);
  }

}
