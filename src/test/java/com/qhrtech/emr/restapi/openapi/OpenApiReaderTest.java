
package com.qhrtech.emr.restapi.openapi;

import com.qhrtech.emr.restapi.endpoints.provider.PatientEndpointV2;
import com.qhrtech.emr.restapi.endpoints.provider.ScheduleEndpoint;
import com.qhrtech.emr.restapi.endpoints.provider.WaitRoomEntryEndpoint;
import io.swagger.v3.oas.models.OpenAPI;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OpenApiReaderTest {


  OpenApiReader reader;

  @Before
  public void setUp() {
    reader = new OpenApiReader();
  }


  @Test
  public void testForSCopesPAtterns() {
    List<String> scopes1 =
        reader.findScopes("#oauth2.hasAnyScope( 'user/provider.AccuroSettings.read' )");
    Assert.assertTrue(scopes1.contains("user/provider.AccuroSettings.read"));

    List<String> scopes2 =
        reader.findScopes("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) ");
    Assert.assertTrue(scopes2.contains("SCHEDULING_READ"));
    Assert.assertTrue(scopes2.contains("SCHEDULING_WRITE"));

    List<String> scopes3 = reader.findScopes("#oauth2.hasAnyScope( 'SCHEDULING_WRITE' ) ");
    Assert.assertFalse(scopes3.contains("SCHEDULING_READ"));
    Assert.assertTrue(scopes3.contains("SCHEDULING_WRITE"));

    List<String> scopes4 = reader.findScopes("#oauth2.hasAnyScope('SCHEDULING_WRITE')");
    Assert.assertFalse(scopes4.contains("SCHEDULING_READ"));

    List<String> scopes5 =
        reader.findScopes("#oauth2.hasAnyScope('SCHEDULING_READ','SCHEDULING_WRITE')");
    Assert.assertTrue(scopes5.contains("SCHEDULING_READ"));
    Assert.assertTrue(scopes5.contains("SCHEDULING_WRITE"));


  }

  @Test
  public void testForRolesWithMethodAnnotations() {
    Method[] declaredMethods = WaitRoomEntryEndpoint.class.getDeclaredMethods();

    for (Method method : declaredMethods) {
      if (method.getName().equals("getWaitRoomEntryById")) {
        String roles =
            reader
                .findAccess("and #accuro.hasAccess('PATIENT_DEMOGRAPHICS', 'READ_WRITE')", method);
        Assert
            .assertTrue(roles
                .contains("SCHEDULING with access level: READ_ONLY OR "
                    + "TRAFFIC_MANAGER with access level: READ_ONLY"));
      }

    }


  }

  @Test
  public void testForRoles() {
    Method[] declaredMethods = PatientEndpointV2.class.getDeclaredMethods();

    for (Method method : declaredMethods) {
      if (method.getName().equals("createPatient")) {
        String roles =
            reader
                .findAccess(" #accuro.hasAccess('PATIENT_DEMOGRAPHICS', 'READ_WRITE')"
                    + "", method);
        Assert
            .assertTrue(roles
                .contains("PATIENT_DEMOGRAPHICS with access level: READ_WRITE"));
      }

    }


  }


  @Test
  public void testForClass() {

    OpenAPI api = reader.read(Collections.singleton(ScheduleEndpoint.class), new HashMap<>());
    Assert.assertNotNull(api.getTags());
    Assert.assertEquals(api.getTags().size(), 1);

  }


}
