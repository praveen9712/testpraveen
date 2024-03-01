
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.person.RelationshipStatusManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.person.RelationshipStatus;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.RelationshipStatusDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class RelationshipStatusEndpointTest
    extends AbstractEndpointTest<RelationshipStatusEndpoint> {


  private RelationshipStatusManager manager;

  public RelationshipStatusEndpointTest() {
    super(new RelationshipStatusEndpoint(), RelationshipStatusEndpoint.class);
    manager = mock(RelationshipStatusManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(RelationshipStatusManager.class, manager);
  }

  @Test
  public void testGetRelationshipStatus() throws ProtossException {
    List<RelationshipStatus> protossResults =
        getFixtures(RelationshipStatus.class, ArrayList::new, 2);
    List<RelationshipStatusDto> expected =
        mapDto(protossResults, RelationshipStatusDto.class, ArrayList::new);
    // mock dependencies
    when(manager.getAll()).thenReturn(protossResults);

    List<RelationshipStatusDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/enumerations/relationship-statuses")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(RelationshipStatusDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(manager).getAll();
  }


}
