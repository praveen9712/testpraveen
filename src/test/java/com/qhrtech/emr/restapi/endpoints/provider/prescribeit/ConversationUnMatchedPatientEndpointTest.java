
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.synapse.ConversationUnmatchedPatientManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.synapse.ConversationUnmatchedPatient;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationUnmatchedPatientDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class ConversationUnMatchedPatientEndpointTest
    extends AbstractEndpointTest<ConversationUnMatchedPatientEndpoint> {

  private final ConversationUnmatchedPatientManager manager;
  private final String endpointUrl = "/v1/provider-portal/unmatched-patients/";

  public ConversationUnMatchedPatientEndpointTest() {
    super(new ConversationUnMatchedPatientEndpoint(), ConversationUnMatchedPatientEndpoint.class);
    manager = mock(ConversationUnmatchedPatientManager.class);
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
    servicesMap.put(ConversationUnmatchedPatientManager.class, manager);
    return servicesMap;
  }

  @Test
  public void testGetById() throws ProtossException {
    ConversationUnmatchedPatient protossResult = getFixture(ConversationUnmatchedPatient.class);
    int id = protossResult.getId();
    when(manager.getUnmatchedPatientById(id)).thenReturn(protossResult);

    ConversationUnmatchedPatientDto expected =
        mapDto(protossResult, ConversationUnmatchedPatientDto.class);

    ConversationUnmatchedPatientDto actual = given()
        .when()
        .pathParam("patientId", id)
        .get(getBaseUrl() + endpointUrl + "{patientId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(ConversationUnmatchedPatientDto.class);

    assertEquals(expected, actual);
    verify(manager).getUnmatchedPatientById(id);

  }

  @Test
  public void testGetByIdNotFound()
      throws ProtossException {
    ConversationUnmatchedPatient protossResult = getFixture(ConversationUnmatchedPatient.class);
    int id = protossResult.getId();
    when(manager.getUnmatchedPatientById(id)).thenReturn(null);

    given()
        .when()
        .pathParam("patientId", id)
        .get(getBaseUrl() + endpointUrl + "{patientId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(manager).getUnmatchedPatientById(id);

  }


  @Test
  public void testCreate() throws ProtossException {
    ConversationUnmatchedPatientDto patientDto = getFixture(ConversationUnmatchedPatientDto.class);

    ConversationUnmatchedPatient protossModel =
        mapDto(patientDto, ConversationUnmatchedPatient.class);
    when(manager.createUnmatchedPatient(protossModel)).thenReturn(protossModel.getId());

    Integer createdId = given()
        .body(patientDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertTrue(patientDto.getId() == createdId);
    verify(manager).createUnmatchedPatient(protossModel);

  }
}
