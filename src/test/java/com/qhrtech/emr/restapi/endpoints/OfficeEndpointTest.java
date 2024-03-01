
package com.qhrtech.emr.restapi.endpoints;

import static com.optimedsoftware.webservice.RemoteMapType.HashMap;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medeo.MedeoOrganizationManager;
import com.qhrtech.emr.accuro.api.office.OfficeManager;
import com.qhrtech.emr.accuro.model.demographics.Office;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.medeo.MedeoOrganization;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.MedeoOrganizationDto;
import com.qhrtech.emr.restapi.models.dto.OfficeDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ContactDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class OfficeEndpointTest extends AbstractEndpointTest<OfficeEndpoint> {
  private OfficeManager officeManager;

  private MedeoOrganizationManager medeoOrganizationManager;

  public OfficeEndpointTest() {
    super(new OfficeEndpoint(), OfficeEndpoint.class);
    officeManager = mock(OfficeManager.class);
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
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(OfficeManager.class, officeManager);
    servicesMap.put(MedeoOrganizationManager.class, medeoOrganizationManager);
    return servicesMap;
  }

  @Test
  public void testGetOfficeList() throws ProtossException {
    Set<Office> protossResults =
        getFixtures(Office.class, HashSet::new, 2);
    Set<OfficeDto> expected = mapDto(protossResults, OfficeDto.class, HashSet::new);
    // mock dependencies
    when(officeManager.getAllOffices()).thenReturn(protossResults);

    Set<OfficeDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/offices")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(OfficeDto[].class),
        HashSet::new);
    assertEquals(expected, actual);
    verify(officeManager).getAllOffices();
  }

  @Test
  public void testGetMedeoOrganizationForOffice() throws ProtossException {
    MedeoOrganization organization = getFixture(MedeoOrganization.class);
    int officeId = organization.getAccuroOfficeId();

    // mock dependencies
    when(medeoOrganizationManager.getMedeoOrganizationByOffice(officeId)).thenReturn(organization);

    MedeoOrganizationDto expected =
        mapDto(organization, MedeoOrganizationDto.class);

    MedeoOrganizationDto actual =
        given()
            .pathParams("officeId", officeId)
            .when()
            .get(getBaseUrl() + "/v1/offices/{officeId}/medeo-organizations")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(MedeoOrganizationDto.class);
    assertEquals(expected, actual);
    verify(medeoOrganizationManager).getMedeoOrganizationByOffice(officeId);
  }
}
