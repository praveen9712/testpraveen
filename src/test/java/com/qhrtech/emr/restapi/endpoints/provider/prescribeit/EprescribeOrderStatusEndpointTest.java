
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeOrderStatusManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeOrderStatus;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeOrderStatusSystem;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientRolesException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeOrderStatusDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeOrderStatusEndpointTest
    extends AbstractEndpointTest<EprescribeOrderStatusEndpoint> {

  private final String endpointUrl = "/v1/provider-portal/eprescribe-order-statuses";

  private EprescribeOrderStatusManager manager;


  public EprescribeOrderStatusEndpointTest() {
    super(new EprescribeOrderStatusEndpoint(), EprescribeOrderStatusEndpoint.class);
    manager = mock(EprescribeOrderStatusManager.class);
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
    servicesMap.put(EprescribeOrderStatusManager.class, manager);
    return servicesMap;
  }

  @Test
  public void create()
      throws ForbiddenException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      SupportingResourceNotFoundException {
    EprescribeOrderStatusDto orderStatusDto = getFixture(EprescribeOrderStatusDto.class);
    EprescribeOrderStatus protossModel = mapDto(orderStatusDto, EprescribeOrderStatus.class);
    int id = protossModel.getId();

    when(manager.createEprescribeOrderStatus(protossModel)).thenReturn(id);

    // Test
    int actual = given()
        .body(orderStatusDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);
    verify(manager).createEprescribeOrderStatus(protossModel);
    assertEquals(id, actual);
  }

  @Test
  public void createStatusGreaterThanMax() {
    EprescribeOrderStatusDto orderStatusDto = getFixture(EprescribeOrderStatusDto.class);
    orderStatusDto.setStatus(TestUtilities.nextString(256));

    // Test
    given()
        .body(orderStatusDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
    verifyNoInteractions(manager);
  }

  @Test
  public void createNoBody() {
    // Test
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);

  }

  @Test
  public void testGetEprescribeStatusOrder()
      throws InsufficientRolesException, UnsupportedSchemaVersionException,
      DatabaseInteractionException {
    List<EprescribeOrderStatusDto> orderStatusDto =
        getFixtures(EprescribeOrderStatusDto.class, ArrayList::new, 5);
    List<EprescribeOrderStatus> protossResult =
        mapDto(orderStatusDto, EprescribeOrderStatus.class, ArrayList::new);
    int prescriptionId = TestUtilities.nextInt();
    EprescribeOrderStatusSystem system = TestUtilities.nextValue(EprescribeOrderStatusSystem.class);
    when(manager.getEprescribeOrderStatusHistory(prescriptionId, system)).thenReturn(protossResult);

    List<EprescribeOrderStatusDto> actual = toCollection(given()
        .queryParam("rxId", prescriptionId)
        .queryParam("system", system)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeOrderStatusDto[].class), ArrayList::new);

    verify(manager).getEprescribeOrderStatusHistory(prescriptionId, system);
    assertEquals(orderStatusDto, actual);

  }

  @Test
  public void testGetEprescribeStatusOrderNoRxId() {

    EprescribeOrderStatusSystem system = TestUtilities.nextValue(EprescribeOrderStatusSystem.class);

    given()
        .queryParam("system", system)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);

  }

  @Test
  public void testGetEprescribeStatusOrderNullSystem()
      throws InsufficientRolesException, UnsupportedSchemaVersionException,
      DatabaseInteractionException {
    List<EprescribeOrderStatusDto> orderStatusDto =
        getFixtures(EprescribeOrderStatusDto.class, ArrayList::new, 5);
    List<EprescribeOrderStatus> protossResult =
        mapDto(orderStatusDto, EprescribeOrderStatus.class, ArrayList::new);
    int prescriptionId = TestUtilities.nextInt();

    when(manager.getEprescribeOrderStatusHistory(prescriptionId, null)).thenReturn(protossResult);

    List<EprescribeOrderStatusDto> actual = toCollection(given()
        .queryParam("rxId", prescriptionId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeOrderStatusDto[].class), ArrayList::new);

    verify(manager).getEprescribeOrderStatusHistory(prescriptionId, null);
    assertEquals(orderStatusDto, actual);

  }


}
