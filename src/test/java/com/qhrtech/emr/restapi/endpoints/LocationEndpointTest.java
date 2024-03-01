
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.demographics.LocationManager;
import com.qhrtech.emr.accuro.model.demographics.Location;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.LocationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class LocationEndpointTest extends AbstractEndpointTest<LocationEndpoint> {
  private LocationManager locationManager;

  public LocationEndpointTest() {
    super(new LocationEndpoint(), LocationEndpoint.class);
    locationManager = mock(LocationManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(LocationManager.class, locationManager);
  }

  @Test
  public void testGetDetails() throws ProtossException {
    List<Location> protossResults =
        getFixtures(Location.class, ArrayList::new, 2);
    List<LocationDto> expected = mapDto(protossResults, LocationDto.class, ArrayList::new);
    // mock dependencies
    when(locationManager.getLocations()).thenReturn(protossResults);

    List<LocationDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/locations")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LocationDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(locationManager).getLocations();
  }


}
