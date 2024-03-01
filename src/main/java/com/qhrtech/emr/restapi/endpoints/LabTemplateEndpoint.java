
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.labs.LabManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.models.dto.LabResultDto;
import com.qhrtech.emr.restapi.models.dto.LabTestDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import org.springframework.stereotype.Component;

/**
 * This <code>LabTemplateEndpoint</code> collection is designed to expose the Lab Template objects
 * and related public endpoints. Requires client, provider, or patient level authorization.
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @see com.qhrtech.emr.restapi.models.dto.LabResultDto
 * @see com.qhrtech.emr.restapi.models.dto.LabTestDto
 */
@Component("publicLabTemplateEndpoint")
@Path("/v1/labs")
@Tag(name = "LabTemplate Endpoints - Public",
    description = "Exposes lab template endpoints(public)")
public class LabTemplateEndpoint extends AbstractEndpoint {

  /**
   * Retrieves a set of all LabTestDtos
   *
   * @return A set of all LabTestDtos
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/tests")
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves lab tests",
      description = "Retrieves a set of all lab tests.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = LabTestDto.class))))})
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when the user has this permission for at least one provider.")
  public Set<LabTestDto> getLabTests() throws ProtossException {

    LabManager labManager = getImpl(LabManager.class);
    return mapDto(labManager.getLabTests(), LabTestDto.class, HashSet::new);
  }

  /**
   * Return the specified LabTestDto.
   *
   * @param testId Lab test ID
   * @return A single LabTestDto
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 Lab Test not found
   */
  @GET
  @Path("/tests/{testId}")

  @Operation(
      summary = "Retrieves Lab Test",
      description = "Retrieves the specified lab test by the given test id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = LabTestDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Lab test doesn't exist with the given id")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client, patient, or provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "testId",
              description = "The lab test id",
              in = ParameterIn.PATH)})
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when the user has this permission for at least one provider.")
  public LabTestDto getLabTest(
      @Parameter(hidden = true) @PathParam("testId") int testId) throws ProtossException {

    LabManager labManager = getImpl(LabManager.class);
    return mapDto(labManager
        .getLabTestsById(Collections.singleton(testId))
        .stream()
        .findFirst()
        .orElseThrow(
            () -> Error.webApplicationException(Status.NOT_FOUND, "Lab Test Not Found")),
        LabTestDto.class);
  }

  /**
   * Retrieves all Lab Result for a specified Lab Test. Will return an empty list if the specified
   * test has no Results, or if the test does not exist.
   *
   * @param testId Lab test ID
   * @return a Collection of database layer objects to a Collection of data transfer objects
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/tests/{testId}/results")
  @Operation(
      summary = "Retrieves lab results for the lab test",
      description = "Retrieves all lab results for the specified lab test. "
          + "It will return an empty list if the specified test has no results, "
          + "or if the test does not exist.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = LabResultDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client, patient, or provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "testId",
              description = "The lab test id",
              in = ParameterIn.PATH)})
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when the user has this permission for at least one provider.")
  public Set<LabResultDto> getLabResultsForTest(
      @Parameter(hidden = true) @PathParam("testId") int testId) throws ProtossException {

    LabManager labManager = getImpl(LabManager.class);
    return mapDto(labManager.getResultsForTest(testId), LabResultDto.class, HashSet::new);
  }

  /**
   * Retrieves a list of all Lab Results.
   *
   * @return a Collection of database layer objects to a Collection of data transfer objects
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/results")
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves lab results",
      description = "Retrieves a list of all lab results.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = LabResultDto.class))))})
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when the user has this permission for at least one provider.")
  public Set<LabResultDto> getLabResults() throws ProtossException {

    LabManager labManager = getImpl(LabManager.class);
    return mapDto(labManager.getLabResults(), LabResultDto.class, HashSet::new);
  }

  /**
   * Retrieves the specified Lab Result.
   *
   * @param resultId Result ID
   * @return a Collection of database layer objects to a Collection of data transfer objects
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 Lad Result not found
   */
  @GET
  @Path("/results/{resultId}")
  @Operation(
      summary = "Retrieves lab result",
      description = "Retrieves the specified lab result.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = LabResultDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Lab result doesn't exist")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client, patient, or provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "resultId",
              description = "The lab result id",
              in = ParameterIn.PATH)})
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when the user has this permission for at least one provider.")
  public LabResultDto getLabResult(
      @Parameter(hidden = true) @PathParam("resultId") int resultId) throws ProtossException {

    LabManager labManager = getImpl(LabManager.class);
    return mapDto(labManager
        .getLabResultsById(Collections.singleton(resultId))
        .stream()
        .findFirst()
        .orElseThrow(
            () -> Error.webApplicationException(Status.NOT_FOUND, "Lab Result Not Found")),
        LabResultDto.class);
  }

  /**
   * Retrieves a list of all Lab Sources.
   *
   * @return A map of all lab sources.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/sources")
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves lab sources",
      description = "Retrieves a map of all lab sources.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success. Returns a map source id to source name",
              content = @Content(
                  mediaType = MediaType.APPLICATION_JSON,
                  schema = @Schema(example = "{\n"
                      + "    \"1\": \"Excelleris\",\n"
                      + "    \"2\": \"Medinet\",\n"
                      + "    \"3\": \"Capital Health\"\n"
                      + "}")))})
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when the user has this permission for at least one provider.")
  public Map<Integer, String> getLabSources() throws ProtossException {

    LabManager labManager = getImpl(LabManager.class);
    return labManager.getLabSources();
  }
}
