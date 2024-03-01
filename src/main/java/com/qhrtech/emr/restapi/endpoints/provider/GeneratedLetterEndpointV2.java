
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.letters.GeneratedLetterManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.letters.GeneratedLetter;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.GeneratedLetterDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.GET;
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
@Path("/v2/provider-portal/generated-letters")
@Facet("provider-portal")
@Tag(name = "GeneratedLetter Endpoints",
    description = "Exposes generated letter endpoints")
public class GeneratedLetterEndpointV2 extends AbstractEndpoint {

  public static final String PDF = "pdf";
  public static final String TIFF = "tiff";
  public static final String IMAGE_TIFF_VALUE = "image/tiff";
  private static final int DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;

  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'LETTER_READ', 'LETTER_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_CLINICAL_NOTES', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access "
          + "to generated letters with a provider the user has this permission for.")
  @Operation(
      summary = "Retrieves generated letters",
      description = "Retrieves all generated letters without file content for all the provided "
          + "filters. The results will be paginated in ascending order by id. ",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "User id and patient id must be provided"),
          @ApiResponse(
              responseCode = "400",
              description = "If the createdStartDate is greater than createdEndDate"),
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
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "patientId",
              description = "The patient id",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "createdStartDate",
              description = "The start date of the searching period for the generated letter date. "
                  + "If this is provided without an createdEndDate, this endpoint will return "
                  + "all generated letters created on and after the specified startDate.\n\n "
                  + "If createdEndDate is also "
                  + "provided then results within the date range will be returned. \n\n",
              in = ParameterIn.QUERY,
              example = "2020-02-21"),
          @Parameter(
              name = "createdEndDate",
              description = "The end date of the searching period for the generated letter date."
                  + " If this field is provided "
                  + "without createdStartDate, this endpoint will return all generated letters "
                  + "created on  and before the specified createdEndDate."
                  + "If this field is provided along with createdStartDate, "
                  + "then results within the date range will be returned \n "
                  + "startDate - endDate. <br> "
                  + "Example: 2020-02-22<br>",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "appointmentId",
              description = "The appointment id",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer"))})
  public EnvelopeDto<GeneratedLetterDto> getGeneratedLetters(
      @Parameter(hidden = true) @QueryParam("userId") Integer userId,
      @Parameter(hidden = true) @QueryParam("patientId") Integer patientId,
      @Parameter(hidden = true) @QueryParam("createdStartDate") String createdStartDate,
      @Parameter(hidden = true) @QueryParam("createdEndDate") String createdEndDate,
      @Parameter(hidden = true) @QueryParam("appointmentId") Integer appointmentId,
      @Parameter(hidden = true) @QueryParam("startingId") Integer startingId,
      @Parameter(hidden = true) @QueryParam("pageSize") Integer pageSize)
      throws ProtossException {

    LocalDate startDate =
        createdStartDate == null ? null : dateParser(createdStartDate, "createdStartDate");
    LocalDate endDate =
        createdEndDate == null ? null : dateParser(createdEndDate, "createdEndDate");

    if (endDate != null && startDate != null && startDate.isAfter(endDate)) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "createdStartDate cannot be greater than createdEndDate.");
    }
    if (pageSize == null || pageSize <= 0) {
      pageSize = DEFAULT_PAGE_SIZE;
    }

    if (pageSize > MAX_PAGE_SIZE) {
      pageSize = MAX_PAGE_SIZE;
    }

    // The audit log user is the target id of all generated letters for E-referral.
    GeneratedLetterManager manager = getImpl(GeneratedLetterManager.class);
    Envelope<GeneratedLetter> generatedLetters =
        manager.getAllWithNoContent(userId, patientId, startDate, endDate,
            appointmentId, pageSize, startingId);

    EnvelopeDto<GeneratedLetterDto> envelope = new EnvelopeDto<>();

    envelope.setContents(
        mapDto(generatedLetters.getContents(), GeneratedLetterDto.class, ArrayList::new));

    envelope.setCount(generatedLetters.getCount());
    envelope.setTotal(generatedLetters.getTotal());
    envelope.setLastId(generatedLetters.getLastId());
    return envelope;

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
   *         <li>Second part contains binary data with Media type application/pdf if the file
   *         extension is pdf or it will be image/tiff it the file extension is tiff.</li>
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
      summary = "Retrieves the generated letter by id",
      description = "Get generated letter and content by id, the multipart response includes "
          + "two sections:"
          + " The letter metadata is provided as a JSON string in one part of response."
          + " It represents the properties of the generated letter and is structured according"
          + " to the `GeneratedLetterDto` object.The actual binary content of the letter is "
          + "included in the other part of response. The format of the content depends on the file"
          + " extension of the generated letter: If the file extension is pdf, the content will"
          + " be in the application/pdf format. If the file extension is tiff, the content will "
          + "be in the image/tiff format or else it will be application/octet-stream.  "
          + "Please note that the letter's content is not included within the DTO but is provided "
          + "separately in the multipart response. You can access the content in the appropriate "
          + "section of the response based on the file extension.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Resource doesn't exist"),
          @ApiResponse(
              responseCode = "200",
              description = "Success. Multipart response in two parts: Json string in the first "
                  + "part and binary data in the second part. The example is showing raw data of "
                  + "the response.",
              content = @Content(
                  mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                  schema = @Schema(type = "object",
                      example = "--uuid:359c1fc9-82d1-48c7-85b2-b12475a98f0f\n"
                          + "Content-Type: application/json\n"
                          + "Content-ID: <root.message@cxf.apache.org>\n"
                          + "GeneratedLetter of json format\n"
                          + "--uuid:359c1fc9-82d1-48c7-85b2-b12475a98f0f\n"
                          + "Content-Type: application/pdf\n"
                          + "Content-ID: <1>\n\n"
                          + "{\"letterContent\":null,\"id\":223109,\"letterId\":1015,"
                          + "\"referralOrderId\":0,\"letterVersion\":5,\"physicianId\":28471,"
                          + "\"officeId\":18232,\"userId\":1,\"targetId\":0,\"appointmentId\":-1,"
                          + "\"patientId\":2474,\"cc\":0,....,\"extension\":\"pdf\"}\n"
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
    String mediaType;

    if (letterDto.getExtension() != null
        && letterDto.getExtension().equalsIgnoreCase(PDF)) {
      mediaType = MediaType.APPLICATION_PDF_VALUE;
    } else if (letterDto.getExtension() != null
        && letterDto.getExtension().equalsIgnoreCase(TIFF)) {
      mediaType = IMAGE_TIFF_VALUE;
    } else {
      mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
    byte[] letterContent = letterDto.getLetterContent();
    letterDto.setLetterContent(null);

    Map<String, Object> map = new LinkedHashMap<>();
    map.put(MediaType.APPLICATION_JSON_VALUE, letterDto);
    if (letterContent != null) {
      map.put(mediaType, letterContent);
    }

    return Response.ok().entity(map).build();
  }


}
