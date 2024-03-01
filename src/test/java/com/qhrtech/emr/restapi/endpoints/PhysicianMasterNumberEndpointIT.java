
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import com.qhrtech.emr.accuro.api.physician.DefaultMasterNumberManager;
import com.qhrtech.emr.accuro.api.physician.MasterNumberManager;
import com.qhrtech.emr.accuro.api.sysinfo.DefaultSystemInformationManager;
import com.qhrtech.emr.accuro.api.sysinfo.SystemInformationManager;
import com.qhrtech.emr.accuro.model.security.AccuroProvince;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class PhysicianMasterNumberEndpointIT extends
    AbstractEndpointIntegrationTest<PhysicianMasterNumbersEndpoint> {

  private MasterNumberManager masterNumberManager;
  private SystemInformationManager systemInformationManager;

  public PhysicianMasterNumberEndpointIT() throws IOException {
    super(new PhysicianMasterNumbersEndpoint(), PhysicianMasterNumbersEndpoint.class);
    masterNumberManager = new DefaultMasterNumberManager(ds);
    systemInformationManager = new DefaultSystemInformationManager(ds);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {

    Map<Class, Object> managersMap = new HashMap<>();
    managersMap.put(MasterNumberManager.class, masterNumberManager);
    managersMap.put(SystemInformationManager.class, systemInformationManager);
    return managersMap;

  }

  @Test
  public void getPhysicianMasterNumbers() throws Exception {

    AccuroProvince province = systemInformationManager.getProvince();

    int statusCode = 400;
    if (AccuroProvince.ON.equals(province)) {
      statusCode = 200;
    }
    given()
        .when()
        .get(getBaseUrl() + "/v1/enumerations/physician-master-numbers")
        .then()
        .assertThat().statusCode(statusCode);
  }

}

