
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.labs.LabManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.labs.LabResult;
import com.qhrtech.emr.accuro.model.labs.LabTest;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.LabResultDto;
import com.qhrtech.emr.restapi.models.dto.LabTestDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>LabTemplateEndpoint</code> collection is designed to expose the Lab Template DTO and
 * related public endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component("providerLabTemplateEndPoint")
@Path("/v1/provider-portal/labs")
@Facet("provider-portal")
@Tag(name = "LabTemplate Endpoints - Provider",
    description = "Exposes lab template endpoints(provider)")
public class LabTemplateEndpoint extends AbstractEndpoint {

  /**
   * Retrieves the a set of lab tests.
   *
   * @return A set of Lab Test DTOs
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/tests")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves a set of all lab tests",
      description = "Retrieves a set of lab tests.",
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
   * Retrieves the specified lab test by id.
   *
   * @param testId Lab Test ID
   * @return A Lab Test DTO
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 Lab Test is not found
   */
  @GET
  @Path("/tests/{testId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the specified lab test by id",
      description = "Retrieves the specified lab test.",
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
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "testId",
              description = "The lab test id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when the user has this permission for at least one provider.")
  public LabTestDto getLabTest(
      @Parameter(hidden = true) @PathParam("testId") int testId)
      throws ProtossException {

    LabManager labManager = getImpl(LabManager.class);
    return mapDto(
        labManager
            .getLabTestsById(Collections.singleton(testId))
            .stream()
            .findFirst()
            .orElseThrow(
                () -> Error.webApplicationException(Status.NOT_FOUND, "Lab Test Not Found")),
        LabTestDto.class);
  }

  /**
   * Retrieves all lab results for a specified lab test. It will return an empty list if the
   * specified test has no lab results, or if the test does not exist.
   *
   * @param testId Lab Test ID
   * @return A set of Lab Test DTOs
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/tests/{testId}/results")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves lab results for lab test",
      description = "Retrieves all lab results for a specified lab test. "
          + "It will return an empty list if the specified test has no lab results, "
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
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "testId",
              description = "The lab test id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when the user has this permission for at least one provider.")
  public Set<LabResultDto> getLabResultsForTest(
      @Parameter(hidden = true) @PathParam("testId") int testId)
      throws ProtossException {

    LabManager labManager = getImpl(LabManager.class);
    return mapDto(labManager.getResultsForTest(testId), LabResultDto.class, HashSet::new);
  }

  /**
   * Retrieves a set of all lab results.
   *
   * @return A set of Lab Result DTOs
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/results")
  @Operation(
      summary = "Retrieves a set of all lab results",
      description = "Retrieves a set of all lab results.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = LabResultDto.class))))})
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when the user has this permission for at least one provider.")
  public Set<LabResultDto> getLabResults() throws ProtossException {
    LabManager labManager = getImpl(LabManager.class);
    return mapDto(labManager.getLabResults(), LabResultDto.class, HashSet::new);
  }

  /**
   * Returns the specified Lab Result.
   *
   * @param resultId Lab Result ID
   * @return A Lab Result DTO
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the specified Lab Result does not exist.
   */
  @GET
  @Path("/results/{resultId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the lab result",
      description = "Retrieves the lab result.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Lab result doesn't exists for the given id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = LabResultDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "resultId",
              description = "The lab result id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when the user has this permission for at least one provider.")
  public LabResultDto getLabResult(
      @Parameter(hidden = true) @PathParam("resultId") int resultId)
      throws ProtossException {

    LabManager labManager = getImpl(LabManager.class);
    return mapDto(
        labManager
            .getLabResultsById(Collections.singleton(resultId))
            .stream()
            .findFirst()
            .orElseThrow(
                () -> Error.webApplicationException(Status.NOT_FOUND, "Lab Result Not Found")),
        LabResultDto.class);
  }

  /**
   * Retrieves a list of all lab sources.
   *
   * @return A list of lab sources
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/sources")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves lab sources",
      description = "Retrieves a list of all lab sources.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success. Returns a map source id to source name.",
              content = @Content(mediaType = MediaType.APPLICATION_JSON,
                  schema = @Schema(example = "{ 1 : \"Excelleris\"} ")))})
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when the user has this permission for at least one provider.")
  public Map<Integer, String> getLabSources() throws ProtossException {
    LabManager labManager = getImpl(LabManager.class);
    return labManager.getLabSources();
  }

  /**
   * Saves a new Lab Test and returns the id of the created entity.
   *
   * @param labTest Lab Test DTO
   * @return Lab Test DTO
   * @throws DataAccessException If there has been a database error.
   */
  @POST
  @Path("/tests")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Saves lab test",
      description = "Saves a new lab test and returns the id of the created entity.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success. Returns the Lab test Id.",
              content = @Content(
                  schema = @Schema(type = "integer", format = "int32", example = "1")))})
  @RequestBody(
      description = "Lab test to create",
      content = @Content(schema = @Schema(implementation = LabTestDto.class)))
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
      description = "Allows access when the user has this permission for at least one provider.")
  public int createLabTest(
      @RequestBody LabTestDto labTest) throws ProtossException {
    LabManager labManager = getImpl(LabManager.class);
    return labManager.createLabTest(mapDto(labTest, LabTest.class));
  }

  /**
   * Updates all fields on the passed Lab Test.
   *
   * @param testId Lab Test ID
   * @param test Lab Test DTO
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 ID of the passed resource does not match the specified path
   */
  @PUT
  @Path("/tests/{testId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates lab test",
      description = "Updates all fields on the passed lab test.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Lab Test resource id does not match with the id in the body"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")
      })
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "testId",
              description = "The lab test id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @RequestBody(
      description = "Lab test to update",
      content = @Content(schema = @Schema(implementation = LabTestDto.class)))
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
      description = "Allows access when the user has this permission for at least one provider.")
  public void updateLabTest(
      @Parameter(hidden = true) @PathParam("testId") int testId,
      @RequestBody LabTestDto test) throws ProtossException {

    if (test.getTestId() != testId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided lab test does not match the specified resource.");
    }
    LabManager labManager = getImpl(LabManager.class);
    labManager.updateLabTest(mapDto(test, LabTest.class));
  }

  /**
   * Associates the passed Lab Results with the specified resource.
   *
   * @param testId Lab Test ID
   * @param resultIds Lab Result ID
   * @throws DataAccessException If there has been a database error.
   */
  @PUT
  @Path("/tests/{testId}/results")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates to add all lab results on the lab test",
      description = "Updates to add all lab results on the passed lab test.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "testId",
              description = "The lab test id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @RequestBody(
      description = "List of lab result ids to add to test",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = Integer.class))))
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
      description = "Allows access when the user has this permission for at least one provider.")
  public void addResultsToTest(
      @Parameter(hidden = true) @PathParam("testId") int testId,
      Set<Integer> resultIds)
      throws ProtossException {

    LabManager labManager = getImpl(LabManager.class);
    labManager.addResultsToTest(testId, resultIds);
  }

  /**
   * Deletes the passed Lab Results with the specified resource.
   *
   * @param testId Lab Test ID
   * @param resultIds Lab Result ID
   * @throws DataAccessException If there has been a database error.
   */
  @DELETE
  @Path("/tests/{testId}/results")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Deletes lab result from the lab test",
      description = "Deletes the passed lab results from the specific lab test.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content")
      })
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "testId",
              description = "The lab test id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @RequestBody(
      description = "List of lab result ids to delete from test",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = Integer.class))))
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
      description = "Allows access when the user has this permission for at least one provider.")
  public void deleteLabResultsForTest(
      @Parameter(hidden = true) @PathParam("testId") int testId,
      Set<Integer> resultIds) throws ProtossException {

    LabManager labManager = getImpl(LabManager.class);
    labManager.removeResultsFromTest(testId, resultIds);
  }

  /**
   * Creates a new Lab Result resource.
   *
   * @param labResult Lab Result DTO
   * @return ID of new Lab Result
   * @throws DataAccessException If there has been a database error.
   */
  @POST
  @Path("/results")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Saves lab result",
      description = "Saves a new lab result resource.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success. Returns Lab result Id.",
              content = @Content(
                  schema = @Schema(type = "integer", description = "lab result id",
                      example = "1")))})
  @RequestBody(
      description = "Lab result to create",
      content = @Content(schema = @Schema(implementation = LabResultDto.class)))
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
      description = "Allows access when the user has this permission for at least one provider.")
  public int createLabResult(LabResultDto labResult) throws ProtossException {
    LabManager labManager = getImpl(LabManager.class);
    return labManager.createLabResult(mapDto(labResult, LabResult.class));
  }

  /**
   * Updates all fields on an existing Lab Result resource.
   *
   * @param resultId Lab Result ID
   * @param labResult Lab Result FDTO
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the provider Lab Result id does not match the resource path.
   */
  @PUT
  @Path("/results/{resultId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates lab result",
      description = "Updates all fields on an existing lab result resource.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Lab result id in the resource does not match with the id in the "
                  + "body"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")
      })
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "resultId",
              description = "The lab result id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @RequestBody(
      description = "Lab result to update",
      content = @Content(schema = @Schema(implementation = LabResultDto.class)))
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
      description = "Allows access when the user has this permission for at least one provider.")
  public void updateLabResult(
      @Parameter(hidden = true) @PathParam("resultId") int resultId,
      LabResultDto labResult)
      throws ProtossException {
    if (resultId != labResult.getResultId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided lab result does not match the specified resource.");
    }
    LabManager labManager = getImpl(LabManager.class);
    labManager.updateLabResult(mapDto(labResult, LabResult.class));
  }
}
