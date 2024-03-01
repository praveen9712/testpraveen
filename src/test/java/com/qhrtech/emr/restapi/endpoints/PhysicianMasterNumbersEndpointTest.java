
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.physician.MasterNumberManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.physician.MasterNumber;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.MasterNumberDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class PhysicianMasterNumbersEndpointTest
    extends AbstractEndpointTest<PhysicianMasterNumbersEndpoint> {

  private MasterNumberManager masterNumberManager;

  public PhysicianMasterNumbersEndpointTest() {
    super(new PhysicianMasterNumbersEndpoint(), PhysicianMasterNumbersEndpoint.class);
    masterNumberManager = mock(MasterNumberManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(MasterNumberManager.class, masterNumberManager);
  }

  @Test
  public void getPhysicianMasterNumbers() throws ProtossException {

    List<MasterNumber> masterNumbers = new ArrayList<>();
    MasterNumber masterNumber = getFixture(MasterNumber.class);
    masterNumbers.add(masterNumber);

    ArrayList<MasterNumberDto> expected =
        mapDto(masterNumbers, MasterNumberDto.class, ArrayList::new);

    when(masterNumberManager.getAssignedMasterNumbers())
        .thenReturn(masterNumbers);

    ArrayList<MasterNumberDto> actual = toCollection(given()
        .when()
        .get(getBaseUrl() + "/v1/enumerations/physician-master-numbers")
        .then()
        .assertThat().statusCode(200)
        .extract()
        .as(MasterNumberDto[].class),
        ArrayList::new);


    verify(masterNumberManager).getAssignedMasterNumbers();
    Assert.assertEquals(expected, actual);

  }


}

