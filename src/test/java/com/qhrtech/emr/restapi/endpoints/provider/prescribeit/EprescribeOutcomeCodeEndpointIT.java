
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import com.qhrtech.emr.accuro.api.messaging.eprescription.DefaultEprescribeOutcomeCodeManager;
import com.qhrtech.emr.accuro.api.messaging.eprescription.EprescribeOutcomeCodeManager;
import com.qhrtech.emr.accuro.db.messaging.eprescription.EprescribeOutcomeCodeFixture;
import com.qhrtech.emr.accuro.db.messaging.eprescription.EprescribeOutcomeTypeDao;
import com.qhrtech.emr.accuro.db.messaging.eprescription.EprescribeOutcomeTypeFixture;
import com.qhrtech.emr.accuro.db.versioning.VersionedDaoFactory;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.messaging.eprescription.EprescribeOutcomeCode;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeOutcomeCodeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeOutcomeCodeEndpointIT
    extends AbstractEndpointIntegrationTest<EprescribeOutcomeCodeEndpoint> {

  private final String subUrl = "/v1/provider-portal/erx-outcome-codes";
  private EprescribeOutcomeCodeManager manager;
  private EprescribeOutcomeTypeDao outcomeCodeTypeDao;

  public EprescribeOutcomeCodeEndpointIT()
      throws IOException, SQLException, UnsupportedSchemaVersionException {
    super(new EprescribeOutcomeCodeEndpoint(), EprescribeOutcomeCodeEndpoint.class);
    manager = new DefaultEprescribeOutcomeCodeManager(ds);
    outcomeCodeTypeDao =
        new VersionedDaoFactory(getConnection()).getDao(EprescribeOutcomeTypeDao.class);
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
    servicesMap.put(EprescribeOutcomeCodeManager.class, manager);
    return servicesMap;
  }

  @Test
  public void testGetAllErxOutcomeCode()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      NoDataFoundException {

    EprescribeOutcomeCodeFixture erxOutcomeCodeFixture = new EprescribeOutcomeCodeFixture();
    erxOutcomeCodeFixture.setUp(getConnection());

    List<EprescribeOutcomeCodeDto> expected =
        mapDto(manager.getAllErxOutcomeCodes(), EprescribeOutcomeCodeDto.class, ArrayList::new);

    List<EprescribeOutcomeCodeDto> actual =
        toCollection(when().get(getBaseUrl() + subUrl)
            .then()
            .assertThat().statusCode(200)
            .extract().as(EprescribeOutcomeCodeDto[].class), ArrayList::new);

    // clean up db
    manager.deleteErxOutcomeCode(erxOutcomeCodeFixture.get().getId());
    outcomeCodeTypeDao.delete(erxOutcomeCodeFixture.get().getOutcomeTypeId());

    assertEquals(expected, actual);
  }

  @Test
  public void testCreateErxOutcomeCode()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      SQLException {
    EprescribeOutcomeTypeFixture erxOutcomeTypeFixture = new EprescribeOutcomeTypeFixture();
    erxOutcomeTypeFixture.setUp(getConnection());

    int outcomeTypeId = erxOutcomeTypeFixture.get().getId();

    EprescribeOutcomeCodeDto erxOutcomeCodeDto = getFixture(EprescribeOutcomeCodeDto.class);
    erxOutcomeCodeDto.setOutcomeTypeId(outcomeTypeId);

    int id = given()
        .body(erxOutcomeCodeDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + subUrl)
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    EprescribeOutcomeCode expected = mapDto(erxOutcomeCodeDto, EprescribeOutcomeCode.class);
    expected.setId(id);

    EprescribeOutcomeCode actual = manager.getAllErxOutcomeCodes().stream()
        .filter(customer -> customer.getId() == id).findAny().orElse(
            new EprescribeOutcomeCode());

    // clean up db
    manager.deleteErxOutcomeCode(id);
    outcomeCodeTypeDao.delete(outcomeTypeId);

    expected.setCreatedAt(actual.getCreatedAt());
    assertEquals(expected, actual);
  }

  @Test
  public void testDeleteErxOutcomeCode()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException {

    EprescribeOutcomeCodeFixture erxOutcomeCodeFixture = new EprescribeOutcomeCodeFixture();
    erxOutcomeCodeFixture.setUp(getConnection());

    int id = erxOutcomeCodeFixture.get().getId();

    EprescribeOutcomeCode existing = manager.getAllErxOutcomeCodes().stream()
        .filter(customer -> customer.getId() == id).findAny().orElse(null);

    // Check exisitng
    assertNotNull(existing);

    given()
        .pathParam("id", id)
        .when()
        .delete(getBaseUrl() + subUrl + "/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    // clean up db
    outcomeCodeTypeDao.delete(erxOutcomeCodeFixture.get().getOutcomeTypeId());

    EprescribeOutcomeCode after = manager.getAllErxOutcomeCodes().stream()
        .filter(customer -> customer.getId() == id).findAny().orElse(null);

    // Check not exisitng
    assertNull(after);
  }

}
