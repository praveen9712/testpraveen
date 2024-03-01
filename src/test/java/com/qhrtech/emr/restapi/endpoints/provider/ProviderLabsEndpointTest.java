
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.labs.LabManager;
import com.qhrtech.emr.accuro.model.labs.LabGroup;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.LabGroupDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class ProviderLabsEndpointTest extends AbstractEndpointTest<ProviderLabsEndpoint> {
  private final LabManager labManager;

  public ProviderLabsEndpointTest() {
    super(new ProviderLabsEndpoint(), ProviderLabsEndpoint.class);
    labManager = mock(LabManager.class);
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
    servicesMap.put(LabManager.class, labManager);
    return servicesMap;
  }

  @Test
  public void testGetActiveLabGroups() throws Exception {
    // setup random data
    Set<LabGroup> protossResults =
        getFixtures(LabGroup.class, HashSet::new, 1);
    int providerId = RandomUtils.nextInt();
    // mock dependencies
    when(labManager.getActiveLabsForProvider(providerId)).thenReturn(protossResults);
    Set<LabGroupDto> expected = mapDto(protossResults, LabGroupDto.class, HashSet::new);

    // test
    Set<LabGroupDto> actual = toCollection(
        given()
            .pathParam("providerId", providerId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/provider/{providerId}/lab-groups")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabGroupDto[].class),
        HashSet::new);
    // assertions
    assertEquals(expected, actual);
    verify(labManager).getActiveLabsForProvider(providerId);
  }

  @Test
  public void testGetActiveLabGroupsNegativeProviderId() throws Exception {
    // setup random data
    int providerId = RandomUtils.nextInt() * -1;
    // mock dependencies
    given()
        .pathParam("providerId", providerId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/provider/{providerId}/lab-groups")
        .then()
        .assertThat()
        .statusCode(400);
    verify(labManager, never()).getActiveLabsForProvider(providerId);
  }

}
