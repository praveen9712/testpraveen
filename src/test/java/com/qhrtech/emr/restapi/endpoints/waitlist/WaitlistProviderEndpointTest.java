
package com.qhrtech.emr.restapi.endpoints.waitlist;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.waitlist.WaitlistProviderManager;
import com.qhrtech.emr.accuro.model.waitlist.WaitlistProvider;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.waitlist.WaitlistProviderDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class WaitlistProviderEndpointTest extends AbstractEndpointTest<WaitlistProviderEndpoint> {

  private WaitlistProviderManager waitlistProviderManager;

  public WaitlistProviderEndpointTest() {
    super(new WaitlistProviderEndpoint(), WaitlistProviderEndpoint.class);
    waitlistProviderManager = mock(WaitlistProviderManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(WaitlistProviderManager.class, waitlistProviderManager);
  }

  @Test
  public void testGetAll() throws Exception {
    Set<WaitlistProvider> protossResult = getFixtures(WaitlistProvider.class, HashSet::new, 5);

    when(waitlistProviderManager.getAll()).thenReturn(protossResult);

    Set<WaitlistProviderDto> expected =
        mapDto(protossResult, WaitlistProviderDto.class, HashSet::new);

    Set<WaitlistProviderDto> actual = toCollection(
        given().when()
            .get(getBaseUrl() + "/v1/provider-portal/waitlist-providers")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(WaitlistProviderDto[].class),
        HashSet::new);

    assertEquals(expected, actual);
    verify(waitlistProviderManager).getAll();

  }

  @Test
  public void testGetById() throws Exception {
    WaitlistProvider protossResult = getFixture(WaitlistProvider.class);
    int id = protossResult.getId();
    when(waitlistProviderManager.getById(id)).thenReturn(protossResult);

    WaitlistProviderDto expected = mapDto(protossResult, WaitlistProviderDto.class);

    WaitlistProviderDto actual = given()
        .pathParam("providerId", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/waitlist-providers/{providerId}")
        .then()
        .assertThat().statusCode(200)
        .extract().as(WaitlistProviderDto.class);

    assertEquals(expected, actual);
    verify(waitlistProviderManager).getById(id);
  }

  @Test
  public void testGetByIdNotFound() throws Exception {
    int id = RandomUtils.nextInt();
    when(waitlistProviderManager.getById(id)).thenReturn(null);

    // test
    given()
        .pathParam("providerId", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/waitlist-providers/{providerId}")
        .then()
        .assertThat().statusCode(404);

    verify(waitlistProviderManager).getById(id);
  }
}
