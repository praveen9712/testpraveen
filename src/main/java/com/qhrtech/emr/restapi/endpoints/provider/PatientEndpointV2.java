
package com.qhrtech.emr.restapi.endpoints.provider;

import static com.qhrtech.emr.restapi.config.serialization.AccuroObjectMapperFactory.newJsonObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.api.sysinfo.SystemInformationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.model.patient.PatientProviderEnrollmentTerminationReason;
import com.qhrtech.emr.accuro.model.security.AccuroProvince;
import com.qhrtech.emr.accuro.permissions.FeatureType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.ProviderEnrollmentTerminationReason;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.OntarioDetailsDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.PatientDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.FeaturePermission;
import com.qhrtech.emr.restapi.models.swagger.FeaturePermissions;
import com.qhrtech.emr.restapi.models.swagger.LogicalOperation;
import com.qhrtech.emr.restapi.util.PATCH;
import com.qhrtech.emr.restapi.util.PaginationConstant;
import com.qhrtech.emr.restapi.util.PhoneNumberFormatterUtils;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>PatientEndpointV2</code> collection is designed to expose the Patient DTO and related
 * public endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v2/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientV2 Endpoints",
    description = "Exposes the Patient V2 endpoints")
public class PatientEndpointV2 extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();

  /**
   * Get all patients which meet the specified filters. The results will be provided in a paginated
   * form. Set the startingId to the {@code EnvelopeDto.lastId} of the previous page to request the
   * next page. Last id is the {@code patientId} of the last record of the page, and results will be
   * ordered by this field.
   *
   * <p>
   * All of the parameters are optional. All existing patients will be returned if none of the
   * params are provided. If multiple filters are provided, they will be combined with AND operator.
   * </p>
   *
   * @param phn Patient health number. Optional.
   * @param firstName First name, requiring minimum 2 characters. Optional.
   * @param lastName Last name, requiring minimum 2 characters. Optional.
   * @param phone Phone number, requiring minimum 3 characters. Optional.
   * @param fileNumber Patient file number
   * @param patientUuid Patient UUID. The exact value should be provided. Optional.
   * @param pageSize The size of the pages with default value 25 and maximum value 50.
   *        <p>
   *        If the page size is not provided or less than 1, the page size will be set to default
   *        value 25.
   *        </p>
   *        <p>
   *        If page size provided is more than maximum value, the page size will be set to the
   *        default maximum value 50.
   *        </p>
   * @param startingId The starting {@code patientId} (exclusive) of the next page of data.
   *        Typically this is the {@code EnvelopeDto.lastId} from the last page.
   * @return A page of {@link com.qhrtech.emr.restapi.models.dto.PatientDto}'s
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 400 If provided firstName, lastName, or phone is less than minimum characters.
   */
  @GET
  @Path("/search")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves all patients which meet the specified filters",
      description = "The results will be provided in a paginated form. "
          + " To request the next page, specify the startingId which is same as "
          + " **EnvelopeDto.lastId** of the previous page."
          + " Last id is the **patientId** of the last record of the page, "
          + "and results will be ordered by this field i.e **patientId**. "
          + "All searches are begins with search except for the phone, "
          + "i.e matches will only be found at the beginning of the string. "
          + "For the phone, the search for the records which have input parameter "
          + "as the value at any position in them. For the uuid parameter, the search is "
          + "performed for the exact value.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If provided firstName, lastName, or phone "
                  + "is less than minimum characters."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(ref = "EnvelopeDtoPatientV2Dto")))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "phn",
              description = "Patient health number",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "firstName",
              description = "Size must be between 2 and 100",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "lastName",
              description = "Size must be between 2 and 100",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "phone",
              description = "Size must be between 3 and 50",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "fileNumber",
              description = "Patient file number",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "patientUuid",
              description = "Unique Patient UUID.The exact value should be provided.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "pageSize",
              description = "The size of the pages with default value 25 and maximum value 50."
                  + " If the page size is not provided or less than 1, "
                  + " the page size will be set to default value 25."
                  + " If page size provided is more than maximum value, "
                  + " the page size will be set to the default maximum value 50.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "startingId",
              description = "This id is **EnvelopeDto.lastId** of the previous page(request)"
                  + "It is same as the **patientId** of the last records of the previous results.",
              in = ParameterIn.QUERY)
      })

  public EnvelopeDto<PatientDto> getPatients(
      @Parameter(hidden = true) @QueryParam("phn") String phn,
      @Parameter(hidden = true) @QueryParam("firstName") @Size(
          min = 2, max = 100,
          message = "firstName field size must be between 2 and 100") String firstName,
      @Parameter(hidden = true) @QueryParam("lastName") @Size(
          min = 2, max = 100,
          message = "lastName field size must be between 2 and 100") String lastName,
      @Parameter(hidden = true) @QueryParam("phone") @Size(
          min = 3, max = 50, message = "phone field size must be between 3 and 50") String phone,
      @Parameter(hidden = true) @QueryParam("fileNumber") String fileNumber,
      @Parameter(hidden = true) @QueryParam("patientUuid") String patientUuid,
      @Parameter(hidden = true) @QueryParam("pageSize") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "pageSize field can only have numbers") String pageSize,
      @Parameter(hidden = true) @QueryParam("startingId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "startingId field can only have numbers") String startingId)
      throws ProtossException {

    Integer actualPageSize =
        StringUtils.isBlank(pageSize)
            ? DEFAULT_PAGE_SIZE
            : Integer.parseInt(pageSize);

    if (actualPageSize <= 0) {
      actualPageSize = DEFAULT_PAGE_SIZE;
    } else if (actualPageSize > MAX_PAGE_SIZE) {
      actualPageSize = MAX_PAGE_SIZE;
    }
    Integer actualStartingId = StringUtils.isBlank(startingId)
        ? null
        : Integer.parseInt(startingId);

    PatientManager patientManager = getImpl(PatientManager.class);

    Envelope<Patient> patients =
        patientManager.getPatients(phn, firstName, lastName, phone, fileNumber, patientUuid,
            actualStartingId,
            actualPageSize);

    EnvelopeDto<PatientDto> envelopeDto = new EnvelopeDto<>();

    envelopeDto.setContents(mapDto(patients.getContents(),
        PatientDto.class, ArrayList::new));
    envelopeDto.setCount(patients.getCount());
    envelopeDto.setTotal(patients.getTotal());
    envelopeDto.setLastId(patients.getLastId());
    return envelopeDto;
  }

  /**
   * Return the patient associated with this patient ID
   *
   * @param patientId Patient ID
   * @return Patient associated with this patient ID
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the patient records does not exist in the database.
   */
  @GET
  @Path("/{patientId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient",
      description = "Retrieves patient associated with the patient id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Patient not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(ref = "PatientV2Dto")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public PatientDto getPatient(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId)
      throws ProtossException {

    PatientManager patientManager = getImpl(PatientManager.class);
    Patient patient = patientManager.getPatientById(patientId);
    if (patient == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Patient Id Not Found: " + patientId);
    }
    return mapDto(patient, PatientDto.class);
  }


  /**
   * Create a new patient.
   *
   * @param patient JSON representation of the patient fields
   * @param uriInfo URI information
   * @return Response with the patient ID and location of the newly created resource.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 invalid enrolledProviderId
   * @HTTP 400 enroll provider when the module, Patient Enrollment, is off
   * @see ProviderEnrollmentTerminationReason
   */
  @Operation(
      summary = "Creates a new patient",
      description = "Creates a new patient",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "403",
              description = "If no sufficient roles or features"),
          @ApiResponse(
              responseCode = "204",
              description = "Created",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns patient id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Consumes(MediaType.APPLICATION_JSON)
  @POST
  @PreAuthorize("#oauth2.hasScope('PATIENT_DEMOGRAPHICS_WRITE')"
      + "and #accuro.hasAccess('PATIENT_DEMOGRAPHICS', 'READ_WRITE')")
  @FeaturePermissions(
      operation = LogicalOperation.AND,
      featurePermissions = {
          @FeaturePermission(type = FeatureType.PATIENT_OFFICE_PROVIDER,
              description = " - To set an office provider for a patient "),
          @FeaturePermission(type = FeatureType.PATIENT_REF_PHYSICIAN,
              description = " - To set a referring provider for a patient ")})
  public Response createPatient(
      @RequestBody(description = "The new patient") @Valid PatientDto patient,
      @Context UriInfo uriInfo) throws ProtossException {

    if (patient == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient information is missing.");
    }

    validatePatientRequest(patient);
    // disable update and create of UUID from API
    patient.setUuid(null);

    formatPhoneNumber(patient);
    if (null != patient.getDemographics().getNextKinPhone()
        && StringUtils.isNotBlank(patient.getDemographics().getNextKinPhone().getNumber())) {
      PhoneNumberFormatterUtils.validatePhoneNumber(
          patient.getDemographics().getNextKinPhone().getNumber());
      patient.getDemographics()
          .getNextKinPhone().setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(
              patient.getDemographics().getNextKinPhone().getNumber()));
    }

    // Create the patient and get back the generated database id
    PatientManager patientManager = getImpl(PatientManager.class);
    int patientId = patientManager.createPatient(mapDto(patient, Patient.class), getUser());

    // Build a uri representing the location of the new resource
    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
    uriBuilder.path(Integer.toString(patientId));
    return Response.created(uriBuilder.build()).entity(patientId).build();
  }

  /**
   * Update a patient.
   *
   * @param patientId Patient ID
   * @param patient JSON representation of the patient fields
   * @return <code>true</code> if the patient's information was updated successfully
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 patient information missing
   * @HTTP 400 location id missing on the non primary or secondary address
   * @HTTP 400 patient does not exit with the ID provided
   * @HTTP 400 the termination reason is not valid in the current province
   * @HTTP 400 invalid enrolledProviderId
   * @HTTP 400 update enrolled provider when the module, Patient Enrollment, is off
   * @HTTP 400 update enrolled provider in the invalid ways. Please refer to the documentation for
   *       the <code>enrolledProviderId</code> field under
   *       {@link com.qhrtech.emr.restapi.models.dto.PatientDto} object.
   * @see ProviderEnrollmentTerminationReason
   */
  @Operation(
      summary = "Updates patient",
      description = "Updates the given patient.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "403",
              description = "If no sufficient roles or features"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean",
                      example = "true",
                      description = "Returns true if update is successful")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PUT
  @Path("/{patientId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @FeaturePermissions(
      operation = LogicalOperation.AND,
      featurePermissions = {
          @FeaturePermission(type = FeatureType.PATIENT_OFFICE_PROVIDER,
              description = " - To set an office provider for a patient "),
          @FeaturePermission(type = FeatureType.PATIENT_REF_PHYSICIAN,
              description = " - To set a referring provider for a patient ")})
  public Boolean updatePatient(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @RequestBody(description = "Updated patient") @Valid PatientDto patient)
      throws ProtossException {

    if (patient == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient information is missing.");
    } else if (patient.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid patient ID.");
    }

    validatePatientRequest(patient);
    patient.setUuid(null);

    formatPhoneNumber(patient);
    if (null != patient.getDemographics().getNextKinPhone()
        && StringUtils.isNotBlank(patient.getDemographics().getNextKinPhone().getNumber())) {
      PhoneNumberFormatterUtils.validatePhoneNumber(
          patient.getDemographics().getNextKinPhone().getNumber());
      patient.getDemographics()
          .getNextKinPhone().setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(
              patient.getDemographics().getNextKinPhone().getNumber()));
    }

    ProviderEnrollmentTerminationReason enrolledProvideTerminationReason =
        patient.getEnrolledProvideTerminationReason();
    PatientManager patientManager = getImpl(PatientManager.class);
    patientManager.updatePatient(mapDto(patient, Patient.class), getUser(),
        enrolledProvideTerminationReason != null
            ? PatientProviderEnrollmentTerminationReason
                .lookupByCode(enrolledProvideTerminationReason.getCode())
            : null);
    return true;
  }

  private void validatePatientRequest(PatientDto patientDto) throws ProtossException {

    // validate admission & discharge date on Ontario
    SystemInformationManager systemInfoManager = getImpl(SystemInformationManager.class);
    AccuroProvince province = systemInfoManager.getProvince();
    if (AccuroProvince.ON == province) {

      OntarioDetailsDto ontarioDetails = patientDto.getOntarioDetails();
      if (ontarioDetails != null) {
        LocalDate admissionDate = ontarioDetails.getAdmissionDate();
        LocalDate dischargeDate = ontarioDetails.getDischargeDate();
        if (admissionDate != null && dischargeDate != null
            && admissionDate.isAfter(dischargeDate)) {
          throw Error.webApplicationException(Response.Status.BAD_REQUEST,
              "Admission Date should be less than or equal to discharge date.");
        }
      }

    }
  }


  /**
   * Update a patient. This endpoint allows partial update, i.e., update only those fields which
   * need to be updated instead of updating entire Patient object. If there are any invalid fields
   * in the request, they will be ignored and the valid ones will be updated.
   *
   * @param patientId Patient ID
   * @param patch JSON representation of the patient fields which need to be updated or modified. In
   *        other words it is a patient object which includes only those fields which need to be
   *        updated. For example, if we are changing only suffix and email of the object, JSON
   *        representation would be like:
   *        <code>{ "demographics": { "suffix": "suffix", "email": { "address": "test@email.com" }
   * } }</code>
   * @return Response with status 204 if the patient's information was updated successfully
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 patient information missing
   * @HTTP 400 location id missing on the non primary or secondary address
   * @HTTP 400 patient does not exit with the ID provided
   * @HTTP 400 the termination reason is not valid in the current province
   * @HTTP 400 invalid enrolledProviderId
   * @HTTP 400 update enrolled provider when the module Patient Enrollment is off
   * @HTTP 400 update enrolled provider in a invalid way. Please refer to the documentation for the
   *       <code>enrolledProviderId</code> field under
   *       {@link com.qhrtech.emr.restapi.models.dto.PatientDto} object.
   * @see ProviderEnrollmentTerminationReason
   */
  @Operation(
      summary = "Updates patient",
      description = "Updates the given patient. This endpoint allows partial update, i.e., update "
          + "only those fields which need to be updated instead of updating entire Patient "
          + "object. If there are any invalid fields in the request, they will be ignored and "
          + "the valid ones will be updated.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "403",
              description = "If no sufficient roles or features"),
          @ApiResponse(
              responseCode = "204",
              description = "Request successful")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PATCH
  @Path("/{patientId}")
  @Consumes("application/merge-patch+json")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @FeaturePermissions(
      operation = LogicalOperation.AND,
      featurePermissions = {
          @FeaturePermission(type = FeatureType.PATIENT_OFFICE_PROVIDER,
              description = " - To set an office provider for a patient "),
          @FeaturePermission(type = FeatureType.PATIENT_REF_PHYSICIAN,
              description = " - To set a referring provider for a patient ")})
  public Response patchPatient(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @RequestBody(description = "Updated patient fields") JsonMergePatch patch)
      throws ProtossException, JsonPatchException, JsonProcessingException {

    if (patch == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Body is missing for patch");
    }

    PatientManager patientManagerRead = getImpl(PatientManager.class, true);
    Patient patientById = patientManagerRead.getPatientById(patientId);
    if (patientById == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid patient ID.");
    }

    final ObjectMapper objectMapper = newJsonObjectMapper();
    PatientDto patientDto1 = objectMapper.convertValue(patch, PatientDto.class);
    Map<String, Object> map = objectMapper.convertValue(patch, Map.class);
    // We need to set to existing value if passed as it is required field.
    if (!map.containsKey("patientStatusId")) {
      patientDto1.setPatientStatusId(patientById.getPatientStatusId());
    }
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    Validator validator = factory.getValidator();
    Set<ConstraintViolation<PatientDto>> violations = validator.validate(patientDto1);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException("Request Validation Exception", violations);
    }
    if (null != patientDto1.getDemographics().getNextKinPhone()
        && null != patientDto1.getDemographics().getNextKinPhone().getNumber()) {
      PhoneNumberFormatterUtils.validatePhoneNumber(
          patientDto1.getDemographics().getNextKinPhone().getNumber());
    }

    PatientDto patientDto = mapDto(patientById, PatientDto.class);

    JsonNode patched = patch.apply(objectMapper.convertValue(patientDto, JsonNode.class));
    PatientDto patchedPatientDto = objectMapper.treeToValue(patched, PatientDto.class);

    validatePatientRequest(patchedPatientDto);
    formatPhoneNumber(patchedPatientDto);

    if (null != patchedPatientDto.getDemographics().getNextKinPhone()
        && StringUtils.isNotBlank(
            patchedPatientDto.getDemographics().getNextKinPhone().getNumber())) {
      patchedPatientDto.getDemographics()
          .getNextKinPhone().setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(
              patchedPatientDto.getDemographics().getNextKinPhone().getNumber()));
    }

    if (patchedPatientDto.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient id in url does not match the id provided in the body.");
    }
    patchedPatientDto.setUuid(null);
    ProviderEnrollmentTerminationReason enrolledProvideTerminationReason =
        patchedPatientDto.getEnrolledProvideTerminationReason();
    PatientManager patientManager = getImpl(PatientManager.class);
    patientManager.updatePatient(mapDto(patchedPatientDto, Patient.class), getUser(),
        enrolledProvideTerminationReason != null
            ? PatientProviderEnrollmentTerminationReason
                .lookupByCode(enrolledProvideTerminationReason.getCode())
            : null);
    return Response.noContent().build();
  }

  private void formatPhoneNumber(PatientDto patientDto) {
    if (patientDto.getDemographics().getPhones() != null) {
      patientDto.getDemographics()
          .setPhones(patientDto.getDemographics().getPhones().stream().map(phoneDto -> {
            if (StringUtils.isNotBlank(phoneDto.getNumber())) {
              phoneDto.setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(phoneDto.getNumber()));
            }
            return phoneDto;
          }).collect(Collectors.toUnmodifiableList()));
    }
  }
}
