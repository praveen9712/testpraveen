
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.codes.CodeSubSystemManager;
import com.qhrtech.emr.accuro.api.codes.CodeSystemManager;
import com.qhrtech.emr.accuro.model.codes.CodeSubTableName;
import com.qhrtech.emr.accuro.model.codes.CodeSubValue;
import com.qhrtech.emr.accuro.model.codes.CodeSystem;
import com.qhrtech.emr.accuro.model.codes.CodeTableName;
import com.qhrtech.emr.accuro.model.codes.CodeValue;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.OfficialLanguageDto;
import com.qhrtech.emr.restapi.models.dto.SpokenLanguageDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class LanguageEndpointTest extends AbstractEndpointTest<LanguageEndpoint> {


  private CodeSubSystemManager codeSubSystemManager;

  private CodeSystemManager codeSystemManager;

  public LanguageEndpointTest() {
    super(new LanguageEndpoint(), LanguageEndpoint.class);
    codeSubSystemManager = mock(CodeSubSystemManager.class);
    codeSystemManager = mock(CodeSystemManager.class);
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
    servicesMap.put(CodeSubSystemManager.class, codeSubSystemManager);
    servicesMap.put(CodeSystemManager.class, codeSystemManager);
    return servicesMap;
  }

  @Test
  public void testGetOfficialLangs() throws ProtossException {
    List<CodeSubValue> protossResults =
        getFixtures(CodeSubValue.class, ArrayList::new, 2);
    List<OfficialLanguageDto> expected =
        mapDto(protossResults, OfficialLanguageDto.class, ArrayList::new);
    // mock dependencies
    when(codeSubSystemManager.getBySubTableId(CodeSubTableName.OFFICIAL_LANAGUAGES.getSubTableId()))
        .thenReturn(protossResults);

    List<OfficialLanguageDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/enumerations/languages/official")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(OfficialLanguageDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(codeSubSystemManager)
        .getBySubTableId(CodeSubTableName.OFFICIAL_LANAGUAGES.getSubTableId());
  }

  @Test
  public void testGetSpokenLangs() throws ProtossException {
    List<CodeValue> protossResults =
        getFixtures(CodeValue.class, ArrayList::new, 2);
    CodeSystem codeSystem = new CodeSystem();

    codeSystem.setBuiltInValues(protossResults);
    codeSystem.setCustomValues(Collections.emptyList());

    List<SpokenLanguageDto> expected =
        mapDto(protossResults, SpokenLanguageDto.class, ArrayList::new);
    // mock dependencies
    when(codeSystemManager.getCodeSystem(CodeTableName.LANGUAGES_ISO.getTableId()))
        .thenReturn(codeSystem);

    List<SpokenLanguageDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/enumerations/languages/spoken")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(SpokenLanguageDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(codeSystemManager).getCodeSystem(CodeTableName.LANGUAGES_ISO.getTableId());
  }

}
