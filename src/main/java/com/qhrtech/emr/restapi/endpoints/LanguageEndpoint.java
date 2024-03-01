
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.codes.CodeSubSystemManager;
import com.qhrtech.emr.accuro.api.codes.CodeSystemManager;
import com.qhrtech.emr.accuro.model.codes.CodeSubTableName;
import com.qhrtech.emr.accuro.model.codes.CodeSubValue;
import com.qhrtech.emr.accuro.model.codes.CodeSystem;
import com.qhrtech.emr.accuro.model.codes.CodeTableName;
import com.qhrtech.emr.accuro.model.codes.CodeValue;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.models.dto.OfficialLanguageDto;
import com.qhrtech.emr.restapi.models.dto.SpokenLanguageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.stereotype.Component;

/**
 * This <code>LanguageEndpoint</code> collection is designed to expose the languages. Requires
 * client, provider, or patient level authorization.
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 If authorized
 * @HTTP 401 If unauthorized
 */
@Component
@Path("/v1/enumerations/languages")
@Tag(name = "language Endpoints",
    description = "Exposes language endpoints.")
public class LanguageEndpoint extends AbstractEndpoint {

  /**
   * Retrieve a list of all available official languages.
   *
   * @return A <code>List<></code> of OfficialLanguageDto's.
   * @throws DataAccessException If there has been a database error.
   */
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant.",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves official languages",
      description = "Gets a set of all available official languages. "
          + "The results are ordered by code and description values.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = OfficialLanguageDto.class))))
      })
  @GET
  @Path("/official")
  public List<OfficialLanguageDto> getOfficialLanguages() throws ProtossException {
    CodeSubSystemManager manager = getImpl(CodeSubSystemManager.class);
    List<CodeSubValue> protossResults =
        manager.getBySubTableId(CodeSubTableName.OFFICIAL_LANAGUAGES.getSubTableId());

    return mapDto(protossResults, OfficialLanguageDto.class, ArrayList::new);
  }


  /**
   * Retrieve a list of all available spoken languages.
   *
   * @return A <code>List<></code> of SpokenLanguageDto's.
   * @throws DataAccessException If there has been a database error.
   */
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant.",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves spoken languages",
      description = "Gets a set of all available spoken languages."
          + " The results are ordered by code order.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = SpokenLanguageDto.class))))
      })
  @GET
  @Path("/spoken")
  public List<SpokenLanguageDto> getSpokenLanguages() throws ProtossException {
    CodeSystemManager manager = getImpl(CodeSystemManager.class);
    CodeSystem codeSystem = manager.getCodeSystem(CodeTableName.LANGUAGES_ISO.getTableId());

    List<CodeValue> protossResults = new ArrayList<>();

    protossResults.addAll(codeSystem.getBuiltInValues());
    protossResults.addAll(codeSystem.getCustomValues());

    return mapDto(protossResults, SpokenLanguageDto.class, ArrayList::new);
  }

}
