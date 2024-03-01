
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.office.OfficeManager;
import com.qhrtech.emr.accuro.api.security.UserPermissionManager;
import com.qhrtech.emr.accuro.model.demographics.Office;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class CopyUserPermissionsEndpointTest extends
    AbstractEndpointTest<CopyUserPermissionsEndpoint> {

  private final UserPermissionManager userPermissionManager;
  private final OfficeManager officeManager;
  private final AuditLogUser user;
  private ApiSecurityContext context;
  private static final String endpointUrl = "/v1/users/{userId}/copy-user-permissions";

  public CopyUserPermissionsEndpointTest() {
    super(new CopyUserPermissionsEndpoint(), CopyUserPermissionsEndpoint.class);
    userPermissionManager = mock(UserPermissionManager.class);
    officeManager = mock(OfficeManager.class);
    user = getFixture(AuditLogUser.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(user);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(UserPermissionManager.class, userPermissionManager);
    servicesMap.put(OfficeManager.class, officeManager);
    return servicesMap;
  }

  @Test
  public void testCopyUserPermissions() throws Exception {
    Set<Integer> officeIds = new HashSet<>();

    int sourceUser = TestUtilities.nextId();
    int userId = -1;

    doThrow(IllegalArgumentException.class).when(
        userPermissionManager).copyUserPermissions(sourceUser, userId,
            user, officeIds);
    given()
        .pathParam("userId", userId)
        .queryParam("sourceUser", sourceUser)
        .when()
        .put(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(400);

    verify(userPermissionManager).copyUserPermissions(sourceUser, userId, user, officeIds);

  }

  @Test
  public void testCopyUserPermissionsSuccess() throws Exception {

    int sourceUser = TestUtilities.nextId();
    int userId = TestUtilities.nextId();
    Set<Integer> officeIds = new HashSet<>();

    doNothing().when(userPermissionManager).copyUserPermissions(sourceUser, userId, user,
        officeIds);

    given()
        .pathParam("userId", userId)
        .queryParam("sourceUser", sourceUser)
        .when()
        .put(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(userPermissionManager).copyUserPermissions(sourceUser, userId, user, officeIds);

  }

  @Test
  public void testCopyUserPermissionsWithInvalidSourceId() throws Exception {

    String sourceUser = "121212121212121212";
    int userId = TestUtilities.nextId();
    Set<Integer> officeIds = new HashSet<>();


    given()
        .pathParam("userId", userId)
        .queryParam("sourceUser", sourceUser)
        .when()
        .put(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(400);



  }

  @Test
  public void testCopyUserPermissionsSuccessWithOfficeIds() throws Exception {

    int officeId1 = TestUtilities.nextId();
    int officeId2 = TestUtilities.nextId();
    Set<Integer> officeIds = new HashSet<>();
    officeIds.add(officeId1);
    officeIds.add(officeId2);

    Set<Office> offices = new HashSet<>();
    Office office1 = new Office();
    office1.setOfficeId(officeId1);
    Office office2 = new Office();
    office2.setOfficeId(officeId2);
    offices.add(office1);
    offices.add(office2);

    doReturn(offices).when(officeManager).getAllOffices();

    int sourceUser = TestUtilities.nextId();
    int userId = TestUtilities.nextId();
    doNothing().when(userPermissionManager).copyUserPermissions(sourceUser, userId, user,
        officeIds);

    given()
        .pathParam("userId", userId)
        .queryParam("sourceUser", sourceUser)
        .queryParam("includedOfficeId", officeId1)
        .queryParam("includedOfficeId", officeId2)
        .when()
        .put(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(userPermissionManager).copyUserPermissions(sourceUser, userId, user, officeIds);

  }

  @Test
  public void testCopyUserPermissionsBadOfficeId() throws Exception {

    int sourceUser = TestUtilities.nextId();
    int userId = TestUtilities.nextId();
    int officeId1 = TestUtilities.nextId();
    String officeId2 = TestUtilities.nextString(10);

    given()
        .pathParam("userId", userId)
        .queryParam("sourceUser", sourceUser)
        .queryParam("includedOfficeId", officeId1)
        .queryParam("includedOfficeId", officeId2)
        .when()
        .put(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
  }

  @Test
  public void testCopyUserPermissionsInvalidOfficeId() throws Exception {

    int officeId1 = TestUtilities.nextId();
    int officeId2 = TestUtilities.nextId();
    Set<Integer> officeIds = new HashSet<>();
    officeIds.add(officeId1);
    officeIds.add(officeId2);

    Set<Office> offices = new HashSet<>();
    Office office1 = new Office();
    office1.setOfficeId(officeId1);

    doReturn(offices).when(officeManager).getAllOffices();

    int sourceUser = TestUtilities.nextId();
    int userId = TestUtilities.nextId();
    doNothing().when(userPermissionManager).copyUserPermissions(sourceUser, userId, user,
        officeIds);

    given()
        .pathParam("userId", userId)
        .queryParam("sourceUser", sourceUser)
        .queryParam("includedOfficeId", officeId1)
        .queryParam("includedOfficeId", officeId2)
        .when()
        .put(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    // Not executed
    verify(userPermissionManager, never()).copyUserPermissions(sourceUser, userId, user, officeIds);

  }

  @Test
  public void testCopyUserPermissionsNoUser() throws Exception {
    int sourceUser = TestUtilities.nextId();
    Integer userId = TestUtilities.nextId();
    Set<Integer> officeIds = new HashSet<>();
    context.setUser(null);

    doThrow(IllegalArgumentException.class).when(
        userPermissionManager).copyUserPermissions(sourceUser, userId,
            null, officeIds);
    given()
        .pathParam("userId", userId)
        .queryParam("sourceUser", sourceUser)
        .when()
        .put(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(400);

    verify(userPermissionManager)
        .copyUserPermissions(sourceUser, userId, null, officeIds);

  }
}
