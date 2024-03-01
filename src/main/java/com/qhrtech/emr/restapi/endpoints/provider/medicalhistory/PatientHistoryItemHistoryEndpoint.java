
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryRegularHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryTextHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryTrackingManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryUrlHistoryManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryRegularHistory;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryTextHistory;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryTracking;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryUrlHistory;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AbstractPatientHistoryItemHistoryDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.HistoryTypeDto.HistoryType;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryRegularHistoryDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTextHistoryDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTrackingHistoryDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryUrlHistoryDto;
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
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * These endpoints expose patient history item history records.
 *
 * <p>
 * There are 4 types of patient history item as follows:
 * <ul>
 * <li>REGULAR</li>
 * <li>TRACKING</li>
 * <li>URL</li>
 * <li>FREE_TEXT</li>
 * </ul>
 * A specific type is required to retrieve the patient history item history records through these
 * endpoints.
 * </p>
 *
 * <p>
 * Also, Regular type has some built-in types as follow:
 * <ul>
 * <li>Family History</li>
 * <li>Life Style</li>
 * <li>Surgical History</li>
 * </ul>
 * So if you like to retrieve those band's histories should use the {@code historyType} query param
 * as {@code Regular}
 * </p>
 *
 * <p>
 * The result entities are varying depending on the provided history types. The results have a base
 * entity as
 * {@link com.qhrtech.emr.restapi.models.dto.medicalhistory.AbstractPatientHistoryItemHistoryDto}.
 * The different entities are as follows:
 * <ul>
 * <li>REGULAR -
 * {@link com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryRegularHistoryDto}</li>
 * <li>TRACKING -
 * {@link com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTrackingHistoryDto}</li>
 * <li>URL -
 * {@link com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryUrlHistoryDto}</li>
 * <li>FREE_TEXT -
 * {@link com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTextHistoryDto}</li>
 * </ul>
 * </p>
 *
 * @RequestHeader Authorization Provider bearer token.
 * @HTTP 200 Request Successful.
 * @HTTP 401 Unauthorized Request.
 * @see com.qhrtech.emr.restapi.models.dto.medicalhistory.AbstractPatientHistoryItemHistoryDto
 * @see com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryRegularHistoryDto
 * @see com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTrackingHistoryDto
 * @see com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryUrlHistoryDto
 * @see com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTextHistoryDto
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientHistoryItemHistory Endpoints",
    description = "Exposes patient history item history endpoints")
public class PatientHistoryItemHistoryEndpoint extends AbstractEndpoint {

  /**
   * Retrieve a single patient history item history record.
   *
   * @param patientId The unique id of the patient associated to the history item record.
   * @param historyItemId The unique id of the patient history item record.
   * @param historyId The unique id of the patient history item history record.
   * @param historyType The history type(mandatory)
   * @return The matching history item history record if found.
   * @throws DataAccessException If there was a database error.
   * @HTTP 400 If history type is invalid or not provided
   * @HTTP 404 If patient history item history doesn't exist, or history item id or patient id does
   *       not match the supplied id
   */
  @GET
  @Path("/{patientId}/history-items/{historyItemId}/histories/{historyId}")
  @PreAuthorize("#oauth2.hasAnyScope('EMR_MEDS_READ', 'EMR_MEDS_WRITE') "
      + "and #accuro.hasAccess('PATIENT_DIAGNOSTICS', 'READ_ONLY')"
      + "and #accuro.hasAccess( 'EMR_MEDICAL_HISTORY', 'READ_ONLY' )")
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "PATIENT_DIAGNOSTICS", accessLevel = "READ_ONLY")})
  @Operation(
      summary = "Retrieves history item record for the patient",
      description = " <p>\n"
          + " There are 4 types of patient history item as follows:\n"
          + " <ul>\n"
          + " <li>REGULAR</li>\n"
          + " <li>TRACKING</li>\n"
          + " <li>URL</li>\n"
          + " <li>FREE_TEXT</li>\n"
          + " </ul>\n"
          + " A specific type is required to retrieve the patient history item history records"
          + " through these\n"
          + " endpoints.\n"
          + " </p>\n"
          + " \n"
          + " <p>\n"
          + " Also, Regular type has some built-in types as follow:\n"
          + " <ul>\n"
          + " <li>Family History</li>\n"
          + " <li>Life Style</li>\n"
          + " <li>Surgical History</li>\n"
          + " </ul>\n"
          + " So if you like to retrieve those band's histories should use the historyType"
          + " query param\n"
          + " as Regular\n"
          + " </p>\n"
          + " \n"
          + " <p>\n"
          + " The result entities are varying depending on the provided history types."
          + " The results have a base\n"
          + " entity as\n"
          + " AbstractPatientHistoryItemHistoryDto.\n"
          + " The different entities are as follows:\n"
          + " <ul>\n"
          + " <li>REGULAR -\n"
          + " PatientHistoryRegularHistoryDto</li>\n"
          + " <li>TRACKING -\n"
          + " PatientHistoryTrackingHistoryDto</li>\n"
          + " <li>URL -\n"
          + " PatientHistoryUrlHistoryDto</li>\n"
          + " <li>FREE_TEXT -\n"
          + " PatientHistoryTextHistoryDto</li>\n"
          + " </ul>\n"
          + " </p>Retrieves a single record of the patient's history item.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          type = "object",
                          oneOf = {
                              PatientHistoryRegularHistoryDto.class,
                              PatientHistoryTextHistoryDto.class,
                              PatientHistoryTrackingHistoryDto.class,
                              PatientHistoryUrlHistoryDto.class
                          })))),
          @ApiResponse(
              responseCode = "400",
              description = "The history type is not provided"),
          @ApiResponse(
              responseCode = "404",
              description = "The history type is invalid or history item is not found")
      })
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
              name = "historyId",
              description = "History item record id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "historyType",
              description = "History type",
              schema = @Schema(implementation = HistoryType.class),
              required = true,
              in = ParameterIn.QUERY)})
  public AbstractPatientHistoryItemHistoryDto getByHistoryId(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @PathParam("historyItemId") int historyItemId,
      @Parameter(hidden = true) @PathParam("historyId") int historyId,
      @Parameter(hidden = true) @QueryParam("historyType") HistoryType historyType)
      throws ProtossException {

    if (historyType == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "History type must be supplied.");
    }

    AbstractPatientHistoryItemHistoryDto history = null;
    switch (historyType) {
      case REGULAR:
        history = getRegularHistoryById(historyItemId, historyId);
        break;
      case TRACKING:
        history = getTrackingHistoryById(historyItemId, historyId);
        break;
      case URL:
        history = getUrlHistoryById(historyItemId, historyId);
        break;
      case FREE_TEXT:
        history = getTextHistoryById(historyItemId, historyId);
        break;
      default:
        break;
    }

    if (history == null || patientId != history.getPatientId()) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    }

    return history;
  }

  private AbstractPatientHistoryItemHistoryDto getRegularHistoryById(int historyItemId,
      int historyId) throws ProtossException {
    PatientHistoryRegularHistoryManager manager =
        getImpl(PatientHistoryRegularHistoryManager.class);
    PatientHistoryRegularHistory history = manager.getById(historyId);
    if ((history == null) || (historyItemId != history.getPatientHistoryRegularId())) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    return mapDto(history, PatientHistoryRegularHistoryDto.class);
  }

  /**
   * Retrieve all of patient history item history records.
   *
   * <p>
   * Records will be in descending order by patient history item history id.
   * </p>
   *
   * @param patientId The unique id of the patient who owns the history item record.
   * @param historyItemId The unique id of the patient history item record.
   * @param historyType The history type(mandatory)
   * @return All history item history records for a patient.
   * @throws DataAccessException If there was a database error.
   * @HTTP 400 If history type is invalid or not provided
   */
  @GET
  @Path("/{patientId}/history-items/{historyItemId}/histories/")
  @PreAuthorize("#oauth2.hasAnyScope('EMR_MEDS_READ', 'EMR_MEDS_WRITE') "
      + "and #accuro.hasAccess('PATIENT_DIAGNOSTICS', 'READ_ONLY')"
      + "and #accuro.hasAccess( 'EMR_MEDICAL_HISTORY', 'READ_ONLY' )")
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "PATIENT_DIAGNOSTICS", accessLevel = "READ_ONLY")})
  @Operation(
      summary = "Retrieves records of patient's history item",
      description = "Retrieves the records for the patient history item. "
          + "Sorting of the records will depend upon HistoryType: \n\n"
          + "`REGULAR`: descending order of the record modified date.\n\n"
          + "`TRACKING`: descending order of the tracking Id. \n\n"
          + "`URL`: descending order of the patient history Id.\n\n"
          + "`FREE_TEXT`: descending order of the patient history Id. \n\n",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          type = "object",
                          oneOf = {
                              PatientHistoryRegularHistoryDto.class,
                              PatientHistoryTextHistoryDto.class,
                              PatientHistoryTrackingHistoryDto.class,
                              PatientHistoryUrlHistoryDto.class
                          })))),
          @ApiResponse(
              responseCode = "400",
              description = "No history type is provided")
      })
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
  public List<AbstractPatientHistoryItemHistoryDto> getForPatientHistoryItem(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @PathParam("historyItemId") int historyItemId,
      @Parameter(hidden = true) @QueryParam("historyType") HistoryType historyType)
      throws ProtossException {

    if (historyType == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "History type must be supplied.");
    }

    List<AbstractPatientHistoryItemHistoryDto> histories = new ArrayList<>();
    switch (historyType) {
      case REGULAR:
        histories.addAll(getForPatientHistoryRegular(patientId, historyItemId));
        break;
      case TRACKING:
        histories.addAll(getForPatientHistoryTracking(patientId, historyItemId));
        break;
      case URL:
        histories.addAll(getForPatientHistoryUrl(patientId, historyItemId));
        break;
      case FREE_TEXT:
        histories.addAll(getForPatientHistoryText(patientId, historyItemId));
        break;
      default:
        break;
    }

    return histories;
  }

  private AbstractPatientHistoryItemHistoryDto getTrackingHistoryById(int historyItemId,
      int historyId) throws ProtossException {
    PatientHistoryTrackingManager manager = getImpl(PatientHistoryTrackingManager.class);
    PatientHistoryTracking history = manager.getHistoryById(historyId);
    if (history == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }

    Integer trackingHistoryItemId = history.getHistoryTrackingItem() == null ? null
        : history.getHistoryTrackingItem().getId();
    if (historyItemId != trackingHistoryItemId) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    return mapDto(history, PatientHistoryTrackingHistoryDto.class);
  }

  private AbstractPatientHistoryItemHistoryDto getUrlHistoryById(int historyItemId,
      int historyId) throws ProtossException {
    PatientHistoryUrlHistoryManager manager = getImpl(PatientHistoryUrlHistoryManager.class);
    PatientHistoryUrlHistory history = manager.getById(historyId);
    if ((history == null) || (historyItemId != history.getPatientHistoryUrlId())) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    return mapDto(history, PatientHistoryUrlHistoryDto.class);
  }

  private AbstractPatientHistoryItemHistoryDto getTextHistoryById(int historyItemId,
      int historyId) throws ProtossException {
    PatientHistoryTextHistoryManager manager = getImpl(PatientHistoryTextHistoryManager.class);
    PatientHistoryTextHistory history = manager.getById(historyId);
    if ((history == null) || (historyItemId != history.getPatientHistoryTextId())) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    return mapDto(history, PatientHistoryTextHistoryDto.class);
  }

  private List<AbstractPatientHistoryItemHistoryDto> getForPatientHistoryRegular(int patientId,
      int historyItemId) throws ProtossException {
    PatientHistoryRegularHistoryManager manager =
        getImpl(PatientHistoryRegularHistoryManager.class);
    List<PatientHistoryRegularHistory> histories = manager.getByHistoryItemId(historyItemId);
    List<AbstractPatientHistoryItemHistoryDto> historyDtos = new ArrayList<>();
    histories.forEach(h -> {
      if (patientId == h.getPatientId()) {
        AbstractPatientHistoryItemHistoryDto historyDto =
            mapDto(h, PatientHistoryRegularHistoryDto.class);
        historyDtos.add(historyDto);
      }
    });
    return historyDtos;
  }

  private List<AbstractPatientHistoryItemHistoryDto> getForPatientHistoryTracking(int patientId,
      int historyItemId) throws ProtossException {
    PatientHistoryTrackingManager manager = getImpl(PatientHistoryTrackingManager.class);
    List<PatientHistoryTracking> histories =
        manager.getHistoryByPatientIdAndTrackingItemId(patientId, historyItemId);
    List<AbstractPatientHistoryItemHistoryDto> historyDtos = new ArrayList<>();
    histories.forEach(h -> {
      AbstractPatientHistoryItemHistoryDto historyDto =
          mapDto(h, PatientHistoryTrackingHistoryDto.class);
      historyDtos.add(historyDto);
    });
    return historyDtos;
  }

  private List<AbstractPatientHistoryItemHistoryDto> getForPatientHistoryUrl(int patientId,
      int historyItemId) throws ProtossException {
    PatientHistoryUrlHistoryManager manager = getImpl(PatientHistoryUrlHistoryManager.class);
    List<PatientHistoryUrlHistory> histories =
        manager.getForPatientHistoryUrl(historyItemId);
    List<AbstractPatientHistoryItemHistoryDto> historyDtos = new ArrayList<>();
    histories.forEach(h -> {
      if (patientId == h.getPatientId()) {
        AbstractPatientHistoryItemHistoryDto historyDto =
            mapDto(h, PatientHistoryUrlHistoryDto.class);
        historyDtos.add(historyDto);
      }
    });
    return historyDtos;
  }

  private List<AbstractPatientHistoryItemHistoryDto> getForPatientHistoryText(int patientId,
      int historyItemId) throws ProtossException {
    PatientHistoryTextHistoryManager manager = getImpl(PatientHistoryTextHistoryManager.class);
    List<PatientHistoryTextHistory> histories =
        manager.getForPatientHistoryText(historyItemId);
    List<AbstractPatientHistoryItemHistoryDto> historyDtos = new ArrayList<>();
    histories.forEach(h -> {
      if (patientId == h.getPatientId()) {
        AbstractPatientHistoryItemHistoryDto historyDto =
            mapDto(h, PatientHistoryTextHistoryDto.class);
        historyDtos.add(historyDto);
      }
    });
    return historyDtos;
  }
}
