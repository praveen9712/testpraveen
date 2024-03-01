
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.medeo.MedeoProviderLinkManager;
import com.qhrtech.emr.accuro.model.medeo.MedeoProviderLink;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.MedeoProviderLinkDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class MedeoPractitionerLinksEndpointTest
    extends AbstractEndpointTest<MedeoPractitionerLinksEndpoint> {


  private final MedeoProviderLinkManager medeoProviderLinkManager;

  public MedeoPractitionerLinksEndpointTest() {
    super(new MedeoPractitionerLinksEndpoint(), MedeoPractitionerLinksEndpoint.class);
    medeoProviderLinkManager = mock(MedeoProviderLinkManager.class);

  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(MedeoProviderLinkManager.class, medeoProviderLinkManager);
  }

  @Test
  public void testGetPractitionerLinks() throws Exception {
    List<MedeoProviderLink> protossResults =
        getFixtures(MedeoProviderLink.class, ArrayList::new, 4);

    Integer providerId = protossResults.get(0).getAccuroProviderId();

    doReturn(protossResults)
        .when(medeoProviderLinkManager)
        .getPractitionersByProviderId(providerId);

    List<MedeoProviderLinkDto> expected =
        mapDto(protossResults, MedeoProviderLinkDto.class, ArrayList::new);

    List<MedeoProviderLinkDto> actual = toCollection(
        given()
            .queryParam("accuroProviderId", providerId)
            .when().get(getBaseUrl() + "/v1/medeo-practitioner-links")
            .then().assertThat().statusCode(200)
            .extract()
            .as(MedeoProviderLinkDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(medeoProviderLinkManager).getPractitionersByProviderId(providerId);
  }


  @Test
  public void testDeletePractitionerLinks() throws Exception {

    MedeoProviderLink medeoProviderLink = getFixture(MedeoProviderLink.class);

    Integer providerId = medeoProviderLink.getAccuroProviderId();
    Long orgId = medeoProviderLink.getMedeoOrganizationId();

    doReturn(medeoProviderLink)
        .when(medeoProviderLinkManager)
        .getPractitioner(providerId, orgId);

    doReturn(true)
        .when(medeoProviderLinkManager)
        .deleteProviderLink(providerId, orgId);

    given()
        .queryParam("accuroProviderId", providerId)
        .queryParam("orgId", orgId)
        .when().delete(getBaseUrl() + "/v1/medeo-practitioner-links")
        .then().assertThat().statusCode(204);

    verify(medeoProviderLinkManager).deleteProviderLink(providerId, orgId);
  }

  @Test
  public void testDeletePractitionerLinksNotFound() throws Exception {

    MedeoProviderLink medeoProviderLink = getFixture(MedeoProviderLink.class);

    Integer providerId = medeoProviderLink.getAccuroProviderId();
    Long orgId = medeoProviderLink.getMedeoOrganizationId();

    doReturn(true)
        .when(medeoProviderLinkManager)
        .deleteProviderLink(providerId, orgId);

    given()
        .queryParam("accuroProviderId", providerId)
        .queryParam("orgId", orgId)
        .when().delete(getBaseUrl() + "/v1/medeo-practitioner-links")
        .then().assertThat().statusCode(404);

    verify(medeoProviderLinkManager, never()).deleteProviderLink(providerId, orgId);
  }

}
