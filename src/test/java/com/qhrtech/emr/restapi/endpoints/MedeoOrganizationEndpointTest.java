
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.medeo.MedeoOrganizationManager;
import com.qhrtech.emr.accuro.model.medeo.MedeoOrganization;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.MedeoOrganizationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class MedeoOrganizationEndpointTest
    extends AbstractEndpointTest<MedeoOrganizationEndpoint> {


  private final MedeoOrganizationManager medeoOrganizationManager;

  public MedeoOrganizationEndpointTest() {
    super(new MedeoOrganizationEndpoint(), MedeoOrganizationEndpoint.class);
    medeoOrganizationManager = mock(MedeoOrganizationManager.class);

  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(MedeoOrganizationManager.class, medeoOrganizationManager);
  }

  @Test
  public void testGetMedeoOrg() throws Exception {
    List<MedeoOrganization> protossResults =
        getFixtures(MedeoOrganization.class, ArrayList::new, 4);
    doReturn(protossResults)
        .when(medeoOrganizationManager)
        .getMedeoOrganization(null, null);

    List<MedeoOrganizationDto> expected =
        mapDto(protossResults, MedeoOrganizationDto.class, ArrayList::new);

    List<MedeoOrganizationDto> actual = toCollection(
        given()
            .when().get(getBaseUrl() + "/v1/medeo-organizations")
            .then().assertThat().statusCode(200)
            .extract()
            .as(MedeoOrganizationDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(medeoOrganizationManager).getMedeoOrganization(null, null);
  }


  @Test
  public void testGetMedeoOrgByMedeoId() throws Exception {
    List<MedeoOrganization> protossResults =
        getFixtures(MedeoOrganization.class, ArrayList::new, 4);
    Long medeoId = protossResults.get(0).getMedeoId();
    doReturn(Collections.singletonList(protossResults.get(0)))
        .when(medeoOrganizationManager)
        .getMedeoOrganization(medeoId, null);

    List<MedeoOrganizationDto> expected =
        mapDto(protossResults, MedeoOrganizationDto.class, ArrayList::new);

    List<MedeoOrganizationDto> actual = toCollection(
        given()
            .queryParam("medeoId", medeoId)
            .when().get(getBaseUrl() + "/v1/medeo-organizations")
            .then().assertThat().statusCode(200)
            .extract()
            .as(MedeoOrganizationDto[].class),
        ArrayList::new);

    assertEquals(Collections.singletonList(expected.get(0)), actual);
    verify(medeoOrganizationManager).getMedeoOrganization(medeoId, null);
  }
}
