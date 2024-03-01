
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.eprescribe.ExternalPatientManager;
import com.qhrtech.emr.accuro.model.eprescribe.ExternalPatient;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ExternalPatientDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.joda.time.LocalDate;
import org.junit.Test;

public class ExternalPatientEndpointTest extends AbstractEndpointTest<ExternalPatientEndpoint> {

  ExternalPatientManager manager;

  public ExternalPatientEndpointTest() {

    super(new ExternalPatientEndpoint(), ExternalPatientEndpoint.class);
    manager = mock(ExternalPatientManager.class);

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
    servicesMap.put(ExternalPatientManager.class, manager);
    return servicesMap;
  }

  @Test
  public void testGetById()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {

    ExternalPatientDto expected = getFixture(ExternalPatientDto.class);

    int id = expected.getId();
    ExternalPatient protossModel = mapDto(expected, ExternalPatient.class);
    when(manager.getExternalPatientById(id)).thenReturn(protossModel);

    ExternalPatientDto actual = given()
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/external-patients/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalPatientDto.class);

    assertEquals(expected, actual);
    verify(manager).getExternalPatientById(id);

  }


  @Test
  public void testGetByIdentifiers()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {

    ExternalPatientDto expected = getFixture(ExternalPatientDto.class);

    ExternalPatient protossModel = mapDto(expected, ExternalPatient.class);
    when(manager.getByIdentifiers(expected.getExternalSystemIdentifier(),
        expected.getExternalPatientIdentifier())).thenReturn(protossModel);

    List<ExternalPatientDto> actual = toCollection(given()
        .queryParam("extSystemId", expected.getExternalSystemIdentifier())
        .queryParam("extPatientId", expected.getExternalPatientIdentifier())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/external-patients/")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(ExternalPatientDto[].class),
        ArrayList::new);

    assertEquals(expected, actual.get(0));
    verify(manager).getByIdentifiers(expected.getExternalSystemIdentifier(),
        expected.getExternalPatientIdentifier());

  }

  @Test
  public void testCreate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {
    ExternalPatientDto externalPatientDto = getFixture(ExternalPatientDto.class);

    LocalDate birthDate = LocalDate.now();
    externalPatientDto.setBirthDate(birthDate);

    ExternalPatient protossModel = mapDto(externalPatientDto, ExternalPatient.class);

    when(manager.createExternalPatient(protossModel)).thenReturn(protossModel.getId());

    Integer actual = given()
        .body(externalPatientDto)
        .contentType(ContentType.JSON)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/external-patients")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertTrue(externalPatientDto.getId() == actual);
    verify(manager).createExternalPatient(protossModel);

  }


}
