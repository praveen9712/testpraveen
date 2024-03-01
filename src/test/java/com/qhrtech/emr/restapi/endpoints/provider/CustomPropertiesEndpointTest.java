
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.customfield.CustomFieldManager;
import com.qhrtech.emr.accuro.model.customfield.CustomField;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.CustomFieldDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class CustomPropertiesEndpointTest extends AbstractEndpointTest<CustomPropertiesEndpoint> {

  private CustomFieldManager customFieldManager;
  ApiSecurityContext context;

  public CustomPropertiesEndpointTest() {
    super(new CustomPropertiesEndpoint(), CustomPropertiesEndpoint.class);
    customFieldManager = mock(CustomFieldManager.class);
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    AuditLogUser user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> map = new HashMap<>();
    map.put(CustomFieldManager.class, customFieldManager);
    return map;
  }

  @Test
  public void testGetCustomProperties() throws ProtossException {

    List<CustomField> customFields = getFixtures(CustomField.class, ArrayList::new, 2);

    // mock dependencies
    when(customFieldManager.getCustomFields(null, true, getSecurityContext()
        .getUser())).thenReturn(customFields);

    ArrayList<CustomFieldDto> expected =
        mapDto(customFields, CustomFieldDto.class, ArrayList::new);

    List<CustomFieldDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/custom-properties")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(CustomFieldDto[].class),
        ArrayList::new);
    assertEquals(expected, actual);
    verify(customFieldManager).getCustomFields(null, true, getSecurityContext()
        .getUser());
  }

  @Test
  public void testGetCustomPropertiesEmptySet() throws ProtossException {

    // mock dependencies
    when(customFieldManager.getCustomFields(null, true, getSecurityContext()
        .getUser())).thenReturn(new ArrayList<>());

    ArrayList<CustomFieldDto> expected =
        mapDto(new ArrayList<>(), CustomFieldDto.class, ArrayList::new);

    List<CustomFieldDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/custom-properties")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(CustomFieldDto[].class),
        ArrayList::new);
    assertEquals(expected, actual);
    verify(customFieldManager).getCustomFields(null, true, getSecurityContext()
        .getUser());
  }
}

