
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medications.LimitedUseCodeManager;
import com.qhrtech.emr.accuro.api.sysinfo.SystemInformationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.medications.LimitedUseCode;
import com.qhrtech.emr.accuro.model.security.AccuroProvince;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class LimitedUseCodeEndpointTest extends AbstractEndpointTest<LimitedUseCodeEndpoint> {

  private final LimitedUseCodeManager limitedUseCodeManager;
  private final SystemInformationManager systemInfoManager;

  public LimitedUseCodeEndpointTest() {
    super(new LimitedUseCodeEndpoint(), LimitedUseCodeEndpoint.class);
    limitedUseCodeManager = mock(LimitedUseCodeManager.class);
    systemInfoManager = mock(SystemInformationManager.class);
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
    servicesMap.put(LimitedUseCodeManager.class, limitedUseCodeManager);
    servicesMap.put(SystemInformationManager.class, systemInfoManager);
    return servicesMap;
  }

  @Test
  public void testGetLmitedUseCodeByDrugId() throws ProtossException {

    int drugId = TestUtilities.nextId();
    LimitedUseCode first = getFixture(LimitedUseCode.class);
    LimitedUseCode second = getFixture(LimitedUseCode.class);
    List<LimitedUseCode> expected = Arrays.asList(first, second);

    when(limitedUseCodeManager.getLimitedUseCodeByDrugId(anyInt())).thenReturn(expected);
    when(systemInfoManager.getProvince()).thenReturn(AccuroProvince.ON);

    given().queryParam("din", drugId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/medications/limited-use-codes")
        .then()
        .assertThat().statusCode(200)
        .and().body("code", hasItems(first.getCode(), second.getCode()))
        .and().body("description", hasItems(first.getDescription(), second.getDescription()));

    verify(limitedUseCodeManager).getLimitedUseCodeByDrugId(drugId);
  }

  @Test
  public void testGetLmitedUseCodeByDrugIdNotOntario() throws ProtossException {

    when(systemInfoManager.getProvince()).thenReturn(AccuroProvince.BC);

    given().queryParam("din", "123456").when()
        .get(getBaseUrl() + "/v1/provider-portal/medications/limited-use-codes")
        .then()
        .assertThat().statusCode(400);
  }

  @Test
  public void testGetLmitedUseCodeByDrugIdInvalidDin() throws ProtossException {

    when(systemInfoManager.getProvince()).thenReturn(AccuroProvince.ON);

    given().queryParam("din", "xxxxxxxxxx").when()
        .get(getBaseUrl() + "/v1/provider-portal/medications/limited-use-codes")
        .then()
        .assertThat().statusCode(400);
  }

}
