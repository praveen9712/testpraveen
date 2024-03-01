
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.letters.GeneratedLetterManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.letters.GeneratedLetter;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.GeneratedLetterDto;
import com.qhrtech.emr.restapi.models.dto.ReferralStatus;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.joda.time.LocalDate;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>GeneratedLetterEndpoint</code> collection is designed to expose the generated letter
 * and the related public endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/generated-letters")
@Facet("provider-portal")
@Tag(name = "GeneratedLetter Endpoints",
    description = "Exposes generated letter endpoints ")
public class GeneratedLetterEndpoint extends AbstractEndpoint {

  public static final String PDF = "pdf";
  public static final String TIFF = "tiff";

  /**
   * Retrieves all generated letters without file content for all the provided filters.
   * <p>
   * The results will be in ascending order by Id.
   * </p>
   *
   * @param userId The user id and is the required field.
   * @param patientId The patient id and is the required field.
   * @param startDate If this is provided without the endDate, this method will return all generated
   *        letters from the specified startDate. If endDate is also provided then results within
   *        the date range will be returned. Example: <br>
   *        {@code 2020-01-21}
   * @param endDate If this field is provided, the results will be filtered by the date range
   *        startDate - endDate. Example: <br>
   *        {@code 2020-02-22}
   * @param appointmentId If this field is provided, the results will be filtered by the appointment
   *        they are a part of.
   * @return A {@link List} of {@link GeneratedLetterDto}.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Bad request parameters
   * @deprecated Use version 2 to retrieve the same data.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'LETTER_READ', 'LETTER_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_CLINICAL_NOTES', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access "
          + "to generated letters with a provider the user has this permission for.")
  @Operation(
      summary = "Retrieves generated letters. (Deprecated: Refer to the version v2).",
      description = "Retrieves all generated letters without file content for all the provided "
          + "filters. The results will be in ascending order by id.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "User id and patient id must be provided"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = GeneratedLetterDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "userId",
              description = "The user id",
              in = ParameterIn.PATH,
              required = true,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "patientId",
              description = "The patient id",
              in = ParameterIn.PATH,
              required = true,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "startDate",
              description = "The start date of the searching period. "
                  + "If this is provided without an endDate, this method will return "
                  + "all generated letters from the specified startDate.\n\n If endDate is also "
                  + "provided then results within the date range will be returned. \n\n",
              in = ParameterIn.QUERY,
              example = "2020-02-21"),
          @Parameter(
              name = "endDate",
              description = "The end date of the searching period. "
                  + "If this field is provided, "
                  + "the results will be filtered by the date range. </br> "
                  + "startDate - endDate. <br> "
                  + "Example: 2020-02-22<br>",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "appointmentId",
              description = "The appointment id",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer"))})
  @Deprecated
  public List<GeneratedLetterDto> getGeneratedLetters(
      @Parameter(hidden = true) @QueryParam("userId") Integer userId,
      @Parameter(hidden = true) @QueryParam("patientId") Integer patientId,
      @Parameter(hidden = true) @QueryParam("startDate") Calendar startDate,
      @Parameter(hidden = true) @QueryParam("endDate") Calendar endDate,
      @Parameter(hidden = true) @QueryParam("appointmentId") Integer appointmentId)
      throws ProtossException {

    if (userId == null || patientId == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "User id and patient id must be provided.");
    }

    LocalDate localStartDate = startDate == null ? null : LocalDate.fromCalendarFields(startDate);
    LocalDate localEndDate = endDate == null ? null : LocalDate.fromCalendarFields(endDate);

    // The audit log user is the target id of all generated letters for E-referral.

    GeneratedLetterManager manager = getImpl(GeneratedLetterManager.class);
    return mapDto(
        manager.getAllWithNoContent(userId, patientId, localStartDate, localEndDate,
            appointmentId),
        GeneratedLetterDto.class, ArrayList::new);
  }

  /**
   * Retrieves the generated letter with the given id. It includes the meta data and contents. It
   * produces a response of type "multipart/form-data"
   *
   * @param id The user id and is the required field.
   * @return The Multipart response which has two parts :
   *         <ol>
   *         <li>First part contains json string of {@link GeneratedLetterDto} with media type
   *         application/json.</li>
   *         <li>Second part contains binary data with Media type application/pdf. If the file
   *         extension is other than pdf then Media type would be application/octet-stream</li>
   *         </ol>
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 Resource doesn't exist
   */
  @GET
  @Path("/{id}")
  @Produces("multipart/form-data")
  @PreAuthorize("#oauth2.hasAnyScope( 'LETTER_READ', 'LETTER_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_CLINICAL_NOTES', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access if the user has"
          + " this permission for the provider of the generated letter.")
  @Operation(
      summary = "Retrieves the generated letter by Id",
      description = "Retrieves the generated letter in two parts.\n\n"
          + "The letter metadata which is json string of GeneratedLetterDto object and "
          + "the actual binary data of the generated letter.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Resource doesn't exist"),
          @ApiResponse(
              responseCode = "200",
              description = "Success. Multipart response in two parts: Json string in first part "
                  + "and binary data in second. The example on this doc is raw data in return.",
              content = @Content(
                  mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                  schema = @Schema(type = "object",
                      example = "--uuid:359c1fc9-82d1-48c7-85b2-b12475a98f0f\n"
                          + "Content-Type: application/json\n"
                          + "Content-ID: <root.message@cxf.apache.org>\n"
                          + "GeneratedLetter of json format\n"
                          + "--uuid:359c1fc9-82d1-48c7-85b2-b12475a98f0f\n"
                          + "Content-Type: application/pdf\n"
                          + "Content-ID: <1>\n"
                          + "binary data of content\n"
                          + "--uuid:359c1fc9-82d1-48c7-85b2-b12475a98f0f\n")))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "id",
              description = "The id of the generated letter",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  public Response getGeneratedLetterById(
      @Parameter(hidden = true) @PathParam("id") int id) throws ProtossException {

    GeneratedLetterManager manager = getImpl(GeneratedLetterManager.class);
    GeneratedLetter letter = manager.get(id);

    if (letter == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    GeneratedLetterDto letterDto = mapDto(letter, GeneratedLetterDto.class);

    String mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

    /*
     * due to recent changes in 74.0.0 Protoss Release for V2 endpoint, if dao returns extension as
     * tiff we make letter content and extension as null to avoid breaking changes
     */
    if (letterDto.getExtension() != null
        && letterDto.getExtension().equalsIgnoreCase(PDF)) {
      mediaType = MediaType.APPLICATION_PDF_VALUE;
    } else if (letterDto.getExtension() != null
        && letterDto.getExtension().equalsIgnoreCase(TIFF)) {
      letterDto.setLetterContent(null);
      letterDto.setExtension(null);
    }
    Map<String, Object> map = new LinkedHashMap<>();
    map.put(MediaType.APPLICATION_JSON_VALUE, letterDto);
    if (letterDto.getLetterContent() != null) {
      map.put(mediaType, letterDto.getLetterContent());
    }

    return Response.ok().entity(map).build();
  }

  /**
   * Updates the status of the generated letter.
   *
   * @param id Id of the generated letter which has to be updated.
   * @param status {@link ReferralStatus} object
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Bad request parameters
   */

  @PUT
  @Path("/{id}/status")
  @PreAuthorize("#oauth2.hasAnyScope('LETTER_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_CLINICAL_NOTES', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for the provider of the letter.")
  @Operation(
      summary = "Updates the status of the generated letter",
      description = "Updates the status of a generated letter. "
          + "Referral status can be any of the Status mentioned in ReferralStatus enum.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Status must be provided. The provided generated id letter doesn't "
                  + "match with the specified resource, or no generated letter found with "
                  + "the id provided."),
          @ApiResponse(
              responseCode = "200",
              description = "Success")
      })
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(name = "id",
              description = "The id of the generated letter",
              in = ParameterIn.PATH,
              required = true,
              schema = @Schema(type = "integer"))
      })
  @RequestBody(
      description = "Referral status of the generated letter to updated",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(type = "string",
              allowableValues = {"NO_FILE", "FILE_READY", "FILE_RETRIEVED"})))
  public Response updateLetterStatus(
      @Parameter(hidden = true) @PathParam("id") Integer id,
      String status) throws ProtossException {

    GeneratedLetterDto generatedLetterDto = new GeneratedLetterDto();
    generatedLetterDto.setId(id);
    generatedLetterDto.setStatus(ReferralStatus.fromString(status));

    GeneratedLetter generatedLetter = mapDto(generatedLetterDto, GeneratedLetter.class);
    GeneratedLetterManager manager = getImpl(GeneratedLetterManager.class);
    // no need verify by ID as it is already checked in the protoss method.
    manager.updateStatus(generatedLetter, getUser());

    return Response.ok().build();
  }
}
