
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobTypeManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobTypeManager;
import com.qhrtech.emr.accuro.api.messaging.eprescription.DefaultEprescribeOutcomeManager;
import com.qhrtech.emr.accuro.api.messaging.eprescription.EprescribeOutcomeManager;
import com.qhrtech.emr.accuro.db.eprescribe.EprescribeJobFixture;
import com.qhrtech.emr.accuro.db.messaging.eprescription.EprescribeOutcomeCodeDao;
import com.qhrtech.emr.accuro.db.messaging.eprescription.EprescribeOutcomeCodeFixture;
import com.qhrtech.emr.accuro.db.versioning.VersionedDaoFactory;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.messaging.eprescription.EprescribeOutcome;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobOutcomeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeJobOutcomeEndpointIT
    extends AbstractEndpointIntegrationTest<EprescribeJobOutcomeEndpoint> {

  private final String subUrl = "/v1/provider-portal/erx-job-outcomes";
  private EprescribeOutcomeManager outcomeManager;
  private EprescribeJobManager jobManager;
  private EprescribeJobTypeManager jobTypeManager;
  private EprescribeOutcomeCodeDao outcomeCodeDao;

  public EprescribeJobOutcomeEndpointIT()
      throws IOException, SQLException, UnsupportedSchemaVersionException {
    super(new EprescribeJobOutcomeEndpoint(), EprescribeJobOutcomeEndpoint.class);
    outcomeManager = new DefaultEprescribeOutcomeManager(ds, null, defaultUser());
    jobManager = new DefaultEprescribeJobManager(ds);
    jobTypeManager = new DefaultEprescribeJobTypeManager(ds);
    outcomeCodeDao =
        new VersionedDaoFactory(getConnection()).getDao(EprescribeOutcomeCodeDao.class);
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
    servicesMap.put(EprescribeOutcomeManager.class, outcomeManager);
    servicesMap.put(EprescribeJobManager.class, jobManager);
    servicesMap.put(EprescribeJobTypeManager.class, jobTypeManager);
    return servicesMap;
  }

  @Test
  public void testCreate() throws BusinessLogicException, TimeZoneNotFoundException,
      UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      SQLException {
    EprescribeJobFixture eprescribeJobFixture = new EprescribeJobFixture();
    eprescribeJobFixture.setUp(getConnection());

    EprescribeOutcomeCodeFixture outcomeCodeFixture = new EprescribeOutcomeCodeFixture();
    outcomeCodeFixture.setUp(getConnection());

    EprescribeJobOutcomeDto outcomeDto = getFixture(EprescribeJobOutcomeDto.class);
    outcomeDto.setPreviousMessageHeaderId(eprescribeJobFixture.get().getMessageHeaderId());
    outcomeDto.setOutcomeCodeId(outcomeCodeFixture.get().getId());

    int id = given()
        .body(outcomeDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + subUrl)
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    EprescribeOutcome actual = outcomeManager.getErxOutcome(id);
    EprescribeOutcome expected = mapDto(outcomeDto, EprescribeOutcome.class);
    expected.setId(id);
    expected.setCreatedAt(actual.getCreatedAt());
    assertEquals(expected, actual);

    // clean up db
    outcomeManager.deleteErxOutcome(id);
    jobManager.deleteEprescribeJob(eprescribeJobFixture.get().getePrescribeJobId());
    jobTypeManager
        .deleteEprescribeJobType(eprescribeJobFixture.get().getJobType().getePrescribeJobTypeId());
    outcomeCodeDao.deleteErxOutcomeCode(outcomeCodeFixture.get().getId());
  }

  @Test
  public void testGetById()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      NoDataFoundException, TimeZoneNotFoundException, BusinessLogicException {
    EprescribeJobFixture eprescribeJobFixture = new EprescribeJobFixture();
    eprescribeJobFixture.setUp(getConnection());

    EprescribeOutcomeCodeFixture outcomeCodeFixture = new EprescribeOutcomeCodeFixture();
    outcomeCodeFixture.setUp(getConnection());

    EprescribeJobOutcomeDto outcomeDto = getFixture(EprescribeJobOutcomeDto.class);
    outcomeDto.setPreviousMessageHeaderId(eprescribeJobFixture.get().getMessageHeaderId());
    outcomeDto.setOutcomeCodeId(outcomeCodeFixture.get().getId());
    int id = outcomeManager.createErxOutcome(mapDto(outcomeDto, EprescribeOutcome.class));
    EprescribeOutcome outcome = outcomeManager.getErxOutcome(id);
    EprescribeJobOutcomeDto expected = mapDto(outcome, EprescribeJobOutcomeDto.class);

    EprescribeJobOutcomeDto actual = given()
        .pathParam("outcomeId", id)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + subUrl + "/{outcomeId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobOutcomeDto.class);

    assertEquals(expected, actual);

    // clean up db
    outcomeManager.deleteErxOutcome(id);
    jobManager.deleteEprescribeJob(eprescribeJobFixture.get().getePrescribeJobId());
    jobTypeManager
        .deleteEprescribeJobType(eprescribeJobFixture.get().getJobType().getePrescribeJobTypeId());
    outcomeCodeDao.deleteErxOutcomeCode(outcomeCodeFixture.get().getId());
  }
}
