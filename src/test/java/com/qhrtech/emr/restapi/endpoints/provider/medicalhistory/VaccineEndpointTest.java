
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medicalhistory.VaccineManager;
import com.qhrtech.emr.accuro.model.medicalhistory.Vaccine;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.VaccineDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class VaccineEndpointTest extends AbstractEndpointTest<VaccineEndpoint> {

  private final VaccineManager vaccineManager;

  public VaccineEndpointTest() {
    super(new VaccineEndpoint(), VaccineEndpoint.class);
    vaccineManager = mock(VaccineManager.class);
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
    servicesMap.put(VaccineManager.class, vaccineManager);
    return servicesMap;
  }

  @Test
  public void testGetVaccines() throws Exception {
    // setup random data
    List<Vaccine> protossResults =
        getFixtures(Vaccine.class, ArrayList::new, 3);

    // mock dependencies
    when(vaccineManager.getActiveVaccines()).thenReturn(protossResults);

    List<VaccineDto> expected =
        mapDto(protossResults, VaccineDto.class, ArrayList::new);

    // test
    List<VaccineDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/vaccines/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(VaccineDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(vaccineManager).getActiveVaccines();
  }

  @Test
  public void testGetVaccineById() throws Exception {
    // setup random data
    Vaccine protossResult = getFixture(Vaccine.class);

    int vaccineId = protossResult.getVaccineId();

    // mock dependencies
    when(vaccineManager.getVaccineById(vaccineId)).thenReturn(protossResult);

    VaccineDto expected = mapDto(protossResult, VaccineDto.class);

    // test
    VaccineDto actual =
        given()
            .pathParam("vaccineId", vaccineId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/vaccines/{vaccineId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(VaccineDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(vaccineManager).getVaccineById(vaccineId);
  }

  @Test
  public void testGetVaccineByIdNotFound() throws Exception {

    int vaccineId = TestUtilities.nextId();

    // mock dependencies
    when(vaccineManager.getVaccineById(vaccineId)).thenReturn(null);

    // test
    given()
        .pathParam("vaccineId", vaccineId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/vaccines/{vaccineId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(vaccineManager).getVaccineById(vaccineId);
  }

}
