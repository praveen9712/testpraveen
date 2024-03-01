
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryRegularManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryTextManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryTrackingManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryUrlManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryRegular;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryText;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryTracking;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryUrl;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AbstractPatientHistoryItemDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.HistoryTypeDto.HistoryType;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryRegularDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTextDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTrackingDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryUrlDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.LogicalOperation;
import com.qhrtech.emr.restapi.models.swagger.RolePermission;
import com.qhrtech.emr.restapi.models.swagger.RolePermissions;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * These endpoints expose patient history bands. These are categorized into 4 different types:
 * Regular, Free Text, URL and Tracking {@link HistoryType}'s as an each distinctive history feature
 * type. There can be multiple bands for each type. For example, type REGULAR has
 * FamilyHistory/Lifestyle/SurgicalHistory. Each patient can have multiple records for each type
 * which are being referred as Patient history item(s).
 *
 * <p>
 * The patient history item(s) is/are represented by its corresponding Dto. Please refer each of
 * them below <br>
 * {@link PatientHistoryRegularDto} <br>
 * {@link PatientHistoryTextDto} <br>
 * {@link PatientHistoryUrlDto} <br>
 * {@link PatientHistoryTrackingDto} <br>
 * </p>
 *
 * @RequestHeader Authorization Provider bearer token.
 * @HTTP 200 Request Successful.
 * @HTTP 401 Unauthorized Request.
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientHistory Endpoints", description = "Expose patient history endpoints")
public class PatientHistoryEndpoint extends AbstractEndpoint {

  /**
   * Gets all patient history items filtering by the history type specified. If no history type
   * specified then all types of existing patient history items will be returned.
   *
   * <p>
   * History type should be one of REGULAR, FREE_TEXT, URL and TRACKING.
   *
   * @param patientId Patient id
   * @param historyType Optionally filter by {@link HistoryType}.
   * @return A collection of Patient history Items.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/history-items")
  @PreAuthorize("#oauth2.hasAnyScope('EMR_MEDS_READ', 'EMR_MEDS_WRITE') "
      + "and #accuro.hasAccess('EMR_MEDICAL_HISTORY', 'READ_ONLY') "
      + "and #accuro.hasAccess('PATIENT_DIAGNOSTICS', 'READ_ONLY')")
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "EMR_MEDICAL_HISTORY", accessLevel = "READ_ONLY")})
  @Operation(
      summary = "Retrieves patient history items",
      description = "These endpoints expose patient history bands. These are categorized into "
          + "4 different types:\n"
          + " Regular, Free Text, URL and Tracking {@link HistoryType}'s as an each distinctive"
          + " history feature\n"
          + " type. There can be multiple bands for each type. For example, type REGULAR has\n"
          + " FamilyHistory/Lifestyle/SurgicalHistory. Each patient can have multiple records "
          + "for each type\n"
          + " which are being referred as Patient history item(s).\n"
          + "\n"
          + " <p>\n"
          + " The patient history item(s) is/are represented by its corresponding Dto. "
          + "Please refer each of\n"
          + " them below <br>\n"
          + " PatientHistoryRegularDto <br>\n"
          + " PatientHistoryTextDto <br>\n"
          + " PatientHistoryUrlDto <br>\n"
          + " PatientHistoryTrackingDto <br>\n"
          + " </p> This Endpoint Retrieves all the patient history items filtering by the"
          + " history type "
          + "specified. If no history type specified then all types of existing patient history "
          + "items will be returned.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          type = "object",
                          oneOf = {
                              PatientHistoryRegularDto.class,
                              PatientHistoryTextDto.class,
                              PatientHistoryTrackingDto.class,
                              PatientHistoryUrlDto.class
                          }))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "Patient id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "historyType",
              description = "History type",
              schema = @Schema(implementation = HistoryType.class),
              in = ParameterIn.QUERY)})
  public List<AbstractPatientHistoryItemDto> getPatientHistoryItems(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @QueryParam("historyType") HistoryType historyType)
      throws ProtossException {

    List<AbstractPatientHistoryItemDto> results = new ArrayList<>();

    if (historyType == null) {
      results.addAll(getHistoryRegular(patientId));
      results.addAll(getHistoryText(patientId));
      results.addAll(getHistoryTracking(patientId));
      results.addAll(getHistoryUrl(patientId));
    } else {
      switch (historyType) {
        case REGULAR: {
          results.addAll(getHistoryRegular(patientId));
          break;
        }
        case FREE_TEXT: {
          results.addAll(getHistoryText(patientId));
          break;
        }
        case TRACKING: {
          results.addAll(getHistoryTracking(patientId));
          break;
        }
        case URL: {
          results.addAll(getHistoryUrl(patientId));
          break;
        }
        default:
          break;
      }
    }

    return results;
  }

  /**
   * Get single patient history Item for the patient by history type and its id.
   * <p>
   * History type is mandatory and should be one of REGULAR, FREE_TEXT, URL and TRACKING.
   *
   * @param patientId Patient id
   * @param historyItemId Patient history item unique identity.
   * @param historyType History type
   * @return A patient history Item.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Invalid history type specified.
   */
  @GET
  @Path("/{patientId}/history-items/{historyItemId}")
  @PreAuthorize("#oauth2.hasAnyScope('EMR_MEDS_READ', 'EMR_MEDS_WRITE') "
      + "and #accuro.hasAccess('EMR_MEDICAL_HISTORY', 'READ_ONLY') "
      + "and #accuro.hasAccess('PATIENT_DIAGNOSTICS', 'READ_ONLY')")
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "EMR_MEDICAL_HISTORY", accessLevel = "READ_ONLY")})
  @Operation(
      summary = "Retrieves patient history item by id",
      description = "These endpoints expose patient history bands. These are categorized into"
          + " 4 different types:\n"
          + " Regular, Free Text, URL and Tracking HistoryType's as an each distinctive"
          + " history feature\n"
          + " type. There can be multiple bands for each type. For example, type REGULAR"
          + " has\n"
          + " FamilyHistory/Lifestyle/SurgicalHistory. Each patient can have multiple "
          + "records for each type\n"
          + " which are being referred as Patient history item(s).\n"
          + "\n"
          + " <p>\n"
          + " The patient history item(s) is/are represented by its corresponding Dto."
          + " Please refer each of\n"
          + " them below <br>\n"
          + " PatientHistoryRegularDto <br>\n"
          + " PatientHistoryTextDto <br>\n"
          + " PatientHistoryUrlDto <br>\n"
          + " PatientHistoryTrackingDto <br>\n"
          + "This Endpoint Retrieves a patient history item by the given id. "
          + "The history type is mandatory ",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          type = "object",
                          oneOf = {
                              PatientHistoryRegularDto.class,
                              PatientHistoryTextDto.class,
                              PatientHistoryTrackingDto.class,
                              PatientHistoryUrlDto.class
                          })))),
          @ApiResponse(
              responseCode = "400",
              description = "No history type provided"),
          @ApiResponse(
              responseCode = "404",
              description = "Not found")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "Patient id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "historyItemId",
              description = "History item id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "historyType",
              description = "History type",
              schema = @Schema(implementation = HistoryType.class),
              required = true,
              in = ParameterIn.QUERY)})
  public AbstractPatientHistoryItemDto getPatientHistoryItemById(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @PathParam("historyItemId") int historyItemId,
      @Parameter(hidden = true) @QueryParam("historyType") HistoryType historyType)
      throws ProtossException {

    if (historyType == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "History type must be supplied.");
    }

    AbstractPatientHistoryItemDto item = null;
    switch (historyType) {
      case REGULAR: {
        item = getHistoryRegularById(historyItemId);
        break;
      }
      case FREE_TEXT: {
        item = getHistoryTextById(historyItemId);
        break;
      }
      case TRACKING: {
        item = getHistoryTrackingById(historyItemId);
        break;
      }
      case URL: {
        item = getHistoryUrlById(historyItemId);
        break;
      }
      default:
        break;
    }

    if (item == null || item.getPatientId() != patientId) {
      throw Error.webApplicationException(Response.Status.NOT_FOUND,
          "History item not found.");
    }

    return item;
  }

  private List<PatientHistoryRegularDto> getHistoryRegular(int patientId)
      throws ProtossException {
    PatientHistoryRegularManager managerRegular = getImpl(PatientHistoryRegularManager.class);
    List<PatientHistoryRegular> items = managerRegular.getForPatient(patientId);
    return mapDto(items, PatientHistoryRegularDto.class, ArrayList::new);
  }

  private List<PatientHistoryTextDto> getHistoryText(int patientId) throws ProtossException {
    PatientHistoryTextManager managerText = getImpl(PatientHistoryTextManager.class);
    List<PatientHistoryText> items = managerText.getForPatient(patientId);
    return mapDto(items, PatientHistoryTextDto.class, ArrayList::new);
  }

  private List<PatientHistoryTrackingDto> getHistoryTracking(int patientId)
      throws ProtossException {
    PatientHistoryTrackingManager managerTracking = getImpl(PatientHistoryTrackingManager.class);
    List<PatientHistoryTracking> items = managerTracking.getForPatient(patientId);
    return mapDto(items, PatientHistoryTrackingDto.class, ArrayList::new);
  }

  private List<PatientHistoryUrlDto> getHistoryUrl(int patientId) throws ProtossException {
    PatientHistoryUrlManager managerUrl = getImpl(PatientHistoryUrlManager.class);
    List<PatientHistoryUrl> items = managerUrl.getForPatient(patientId);
    return mapDto(items, PatientHistoryUrlDto.class, ArrayList::new);
  }

  private PatientHistoryRegularDto getHistoryRegularById(int historyItemId)
      throws ProtossException {
    PatientHistoryRegularManager managerRegular = getImpl(PatientHistoryRegularManager.class);
    PatientHistoryRegular item = managerRegular.getById(historyItemId);
    if (item == null) {
      return null;
    }
    return mapDto(item, PatientHistoryRegularDto.class);
  }

  private PatientHistoryTextDto getHistoryTextById(int historyItemId)
      throws ProtossException {
    PatientHistoryTextManager managerText = getImpl(PatientHistoryTextManager.class);
    PatientHistoryText item = managerText.getById(historyItemId);
    if (item == null) {
      return null;
    }
    return mapDto(item, PatientHistoryTextDto.class);
  }

  private PatientHistoryUrlDto getHistoryUrlById(int historyItemId)
      throws ProtossException {
    PatientHistoryUrlManager managerUrl = getImpl(PatientHistoryUrlManager.class);
    PatientHistoryUrl item = managerUrl.getById(historyItemId);
    if (item == null) {
      return null;
    }
    return mapDto(item, PatientHistoryUrlDto.class);
  }

  private PatientHistoryTrackingDto getHistoryTrackingById(int historyItemId)
      throws ProtossException {
    PatientHistoryTrackingManager managerTracking = getImpl(PatientHistoryTrackingManager.class);
    PatientHistoryTracking item = managerTracking.getById(historyItemId);
    if (item == null) {
      return null;
    }
    return mapDto(item, PatientHistoryTrackingDto.class);
  }

}
