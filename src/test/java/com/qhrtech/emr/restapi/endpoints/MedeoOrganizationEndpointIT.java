
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import com.qhrtech.emr.accuro.api.medeo.DefaultMedeoOrganizationManager;
import com.qhrtech.emr.accuro.api.medeo.MedeoOrganizationManager;
import com.qhrtech.emr.accuro.db.medeo.MedeoOrganizationFixture;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.MedeoOrganizationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class MedeoOrganizationEndpointIT
    extends AbstractEndpointIntegrationTest<MedeoOrganizationEndpoint> {

  private MedeoOrganizationManager manager;


  public MedeoOrganizationEndpointIT() throws IOException {
    super(new MedeoOrganizationEndpoint(), MedeoOrganizationEndpoint.class);
    manager = new DefaultMedeoOrganizationManager(getDs());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> managerMap = new HashMap<>();
    managerMap.put(MedeoOrganizationManager.class, manager);
    return managerMap;
  }

  @Test
  public void getMedeoOrganizations() {
    List<MedeoOrganizationDto> expected =
        toCollection(when().get(getBaseUrl() + "/v1/medeo-organizations")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(MedeoOrganizationDto[].class), ArrayList::new);

    try (Connection conn = getConnection()) {
      MedeoOrganizationFixture fixture = new MedeoOrganizationFixture();
      fixture.setUp(conn);
      expected.add(mapDto(fixture.get(), MedeoOrganizationDto.class));
    } catch (Exception e) {
      fail("Fail to create new org");
    }

    List<MedeoOrganizationDto> actual =
        toCollection(when().get(getBaseUrl() + "/v1/medeo-organizations")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(MedeoOrganizationDto[].class), ArrayList::new);

    assertEquals(expected, actual);
  }


}

