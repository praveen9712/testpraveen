
package com.qhrtech.emr.restapi.endpoints.provider;

import static com.qhrtech.emr.restapi.config.serialization.AccuroObjectMapperFactory.newJsonObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.qhrtech.emr.accuro.api.customfield.CustomFieldManager;
import com.qhrtech.emr.accuro.api.customfield.PatientCustomFieldManager;
import com.qhrtech.emr.accuro.api.logging.LogManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.api.patient.PatientProfilePictureManager;
import com.qhrtech.emr.accuro.model.customfield.CustomField;
import com.qhrtech.emr.accuro.model.customfield.PatientCustomField;
import com.qhrtech.emr.accuro.model.demographics.Email;
import com.qhrtech.emr.accuro.model.demographics.Flag;
import com.qhrtech.emr.accuro.model.demographics.Phone;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.logging.PatientActivity;
import com.qhrtech.emr.accuro.model.patient.Alias;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.model.patient.PatientProfilePicture;
import com.qhrtech.emr.accuro.model.patient.PatientProviderEnrollmentTerminationReason;
import com.qhrtech.emr.accuro.permissions.FeatureType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.AddressDto;
import com.qhrtech.emr.restapi.models.dto.AliasDto;
import com.qhrtech.emr.restapi.models.dto.EmailDto;
import com.qhrtech.emr.restapi.models.dto.PatientActivityDto;
import com.qhrtech.emr.restapi.models.dto.PatientDto;
import com.qhrtech.emr.restapi.models.dto.PatientFlagDto;
import com.qhrtech.emr.restapi.models.dto.PhoneDto;
import com.qhrtech.emr.restapi.models.dto.ProviderEnrollmentTerminationReason;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.FeaturePermission;
import com.qhrtech.emr.restapi.models.swagger.FeaturePermissions;
import com.qhrtech.emr.restapi.models.swagger.LogicalOperation;
import com.qhrtech.emr.restapi.util.ImageResizer;
import com.qhrtech.emr.restapi.util.PATCH;
import com.qhrtech.emr.restapi.util.PhoneNumberFormatterUtils;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>PatientEndpoint</code> collection is designed to expose the Patient DTO and related
 * public endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "Patient Endpoints", description = "Exposes patient endpoints")
public class PatientEndpoint extends AbstractEndpoint {

  private static final int MAX_DEFAULT_WIDTH = 100;
  private static final int MAX_DEFAULT_HEIGHT = 100;

  /**
   * Return the patient associated with this patient ID
   *
   * @param patientId Patient ID
   * @return Patient associated with this patient ID
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the patient records does not exist in the database.
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
                  schema = @Schema(ref = "PatientDto")))})
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
      description = "Creates a new patient.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Patient is missing or "
                  + "location id on secondary address is not provided"),
          @ApiResponse(
              responseCode = "403",
              description = "If no sufficient roles or features"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
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

    List<AddressDto> addresses = patient.getDemographics().getAddresses();
    if (addresses != null && addresses.size() > 2) {
      addresses = addresses.subList(2, addresses.size());
      if (addresses.stream().anyMatch(a -> Objects.isNull(a.getLocationId()))) {
        throw Error.webApplicationException(Status.BAD_REQUEST,
            "Location id on non primary or secondary address must be supplied.");
      }
    }
    if (null != patient.getDemographics().getNextKinPhone() && StringUtils.isNotBlank(
        patient.getDemographics()
            .getNextKinPhone().getNumber())) {
      PhoneNumberFormatterUtils.validatePhoneNumber(
          patient.getDemographics().getNextKinPhone().getNumber());
      patient.getDemographics()
          .getNextKinPhone().setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(
              patient.getDemographics().getNextKinPhone().getNumber()));
    }
    formatPhoneNumber(patient);

    // Create the patient and get back the generated database id
    PatientManager patientManager = getImpl(PatientManager.class);
    int patientId =
        patientManager.createPatientBypassValidation(mapDto(patient, Patient.class), getUser());

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
   *       the <code>enrolledProviderId</code> field under {@link PatientDto} object.
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

    List<AddressDto> addresses = patient.getDemographics().getAddresses();
    if (addresses != null && addresses.size() > 2) {
      addresses = addresses.subList(2, addresses.size());
      if (addresses.stream().anyMatch(a -> Objects.isNull(a.getLocationId()))) {
        throw Error.webApplicationException(Status.BAD_REQUEST,
            "Location id on non primary or secondary address must be supplied.");
      }
    }
    patient.setUuid(null);
    formatPhoneNumber(patient);
    if (null != patient.getDemographics().getNextKinPhone() && StringUtils.isNotBlank(
        patient.getDemographics()
            .getNextKinPhone().getNumber())) {
      PhoneNumberFormatterUtils.validatePhoneNumber(
          patient.getDemographics().getNextKinPhone().getNumber());
      patient.getDemographics()
          .getNextKinPhone().setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(
              patient.getDemographics().getNextKinPhone().getNumber()));
    }

    ProviderEnrollmentTerminationReason enrolledProvideTerminationReason =
        patient.getEnrolledProvideTerminationReason();
    PatientManager patientManager = getImpl(PatientManager.class);
    patientManager.updatePatientBypassValidation(mapDto(patient, Patient.class), getUser(),
        enrolledProvideTerminationReason != null
            ? PatientProviderEnrollmentTerminationReason
                .lookupByCode(enrolledProvideTerminationReason.getCode())
            : null);
    return true;
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
   *       <code>enrolledProviderId</code> field under {@link PatientDto} object.
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
      @RequestBody(description = "Updated patient fields",
          content = @Content(
              schema = @Schema(
                  example = "{ \"CustomProperty\": \"CustomValue\" }",
                  type = "object"))) JsonMergePatch patch)
      throws ProtossException, JsonPatchException, JsonProcessingException {

    PatientManager patientManagerRead = getImpl(PatientManager.class, true);
    Patient patientById = patientManagerRead.getPatientById(patientId);
    if (patientById == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid patient ID.");
    }

    PatientDto patientDto = mapDto(patientById, PatientDto.class);

    final ObjectMapper objectMapper = newJsonObjectMapper();
    JsonNode patched = patch.apply(objectMapper.convertValue(patientDto, JsonNode.class));
    PatientDto patchedPatientDto = objectMapper.treeToValue(patched, PatientDto.class);
    PatientDto patientDto1 = objectMapper.convertValue(patch, PatientDto.class);

    if (patchedPatientDto.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient id in url does not match the id provided in the body.");
    }

    List<AddressDto> addresses = patchedPatientDto.getDemographics().getAddresses();
    if (addresses != null && addresses.size() > 2) {
      addresses = addresses.subList(2, addresses.size());
      if (addresses.stream().anyMatch(a -> Objects.isNull(a.getLocationId()))) {
        throw Error.webApplicationException(Status.BAD_REQUEST,
            "Location id on non primary or secondary address must be supplied.");
      }
    }

    if (null != patientDto1.getDemographics()) {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<PhoneDto>> violations = CollectionHelper.newHashSet();
      if (null != patientDto1.getDemographics().getPhones()) {
        List<PhoneDto> phoneDtoList = patientDto1.getDemographics().getPhones();
        for (PhoneDto phoneDto : phoneDtoList) {
          violations.addAll(validator.validate(phoneDto));
        }
        if (!violations.isEmpty()) {
          throw new ConstraintViolationException("Request Validation Exception",
              violations);
        }
      }
      if (null != patientDto1.getDemographics().getNextKinPhone()
          && null != patientDto1.getDemographics().getNextKinPhone().getNumber()) {
        PhoneNumberFormatterUtils.validatePhoneNumber(
            patientDto1.getDemographics().getNextKinPhone().getNumber());
      }
    }
    formatPhoneNumber(patchedPatientDto);
    if (null != patchedPatientDto.getDemographics().getNextKinPhone()
        && StringUtils.isNotBlank(
            patchedPatientDto.getDemographics().getNextKinPhone().getNumber())) {
      patchedPatientDto.getDemographics()
          .getNextKinPhone().setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(
              patchedPatientDto.getDemographics().getNextKinPhone().getNumber()));
    }
    patchedPatientDto.setUuid(null);
    ProviderEnrollmentTerminationReason enrolledProvideTerminationReason =
        patchedPatientDto.getEnrolledProvideTerminationReason();
    PatientManager patientManager = getImpl(PatientManager.class);
    patientManager.updatePatientBypassValidation(mapDto(patchedPatientDto, Patient.class),
        getUser(),
        enrolledProvideTerminationReason != null
            ? PatientProviderEnrollmentTerminationReason
                .lookupByCode(enrolledProvideTerminationReason.getCode())
            : null);
    return Response.noContent().build();
  }

  /**
   * Retrieve a list the patients email address information.
   *
   * @param patientId Patient ID
   * @return List of Patient's emails
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/emails")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient's emails",
      description = "Retrieves a list of the patient's emails.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(ref = "EmailDto"))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<EmailDto> getPatientEmails(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    return mapDto(patientManager.getPatientEmails(patientId), EmailDto.class, ArrayList::new);
  }

  /**
   * Update Patient's Email.
   *
   * @param patientId Patient ID
   * @param patientEmail Email object
   * @return <code>true</code> if the patient's email was updated successfully
   * @throws DataAccessException If there has been a database error.
   */
  @PUT
  @Path("/{patientId}/emails")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates patient's email",
      description = "Updates the patient's email.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "string",
                      example = "true",
                      description = "Returns true if update is successful")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Boolean updatePatientEmail(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @RequestBody(description = "Updated email") EmailDto patientEmail)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (patientEmail == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient email information is missing.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    Email email = mapDto(patientEmail, Email.class);
    patientManager.updatePatientEmail(patientId, email);
    return true;
  }

  /**
   * Create Patient's Email.
   *
   * @param patientId Patient ID
   * @param patientEmail Email.
   * @return New email ID
   * @throws DataAccessException If there has been a database error.
   */
  @POST
  @Path("/{patientId}/emails")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Create patient's email",
      description = "Creates the patient's email.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns email id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Integer createPatientEmail(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @RequestBody(description = "New patient's email") EmailDto patientEmail)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (patientEmail == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient email information is missing.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    Email email = mapDto(patientEmail, Email.class);
    return patientManager.createPatientEmail(patientId, email);
  }

  /**
   * Delete Patient's Email.
   *
   * @param patientId Patient Id
   * @param emailId Email Id
   * @return <code>true</code> if the patient's email was deleted successfully
   * @throws DataAccessException If there has been a database error.
   */
  @DELETE
  @Path("/{patientId}/emails")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Deletes patient's email",
      description = "Deletes the patient's email.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean",
                      example = "true",
                      description = "Returns true if delete is successful")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Boolean deletePatientEmail(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Email id to be deleted",
          required = true) @QueryParam("emailId") Integer emailId)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    patientManager.deletePatientEmail(emailId);
    return true;
  }

  /**
   * Retrieve list of patients phone.
   *
   * @param patientId Patient ID
   * @return List of Patient's Phones
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/phones")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the additional patient's phone",
      description = "Retrieves a list of the additional patient's phone. "
          + "It doesn't include default home, work or cell phone numbers.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(ref = "PhoneDto"))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<PhoneDto> getPatientPhones(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    return mapDto(patientManager.getPatientPhones(patientId), PhoneDto.class, ArrayList::new);
  }

  /**
   * Update a patients phone.
   *
   * @param patientId Patient ID
   * @param patientPhone Phone
   * @return <code>true</code> if the patient's phone was updated successfully
   * @throws DataAccessException If there has been a database error.
   */
  @PUT
  @Path("/{patientId}/phones")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates the additional patient's phone",
      description = "Updates the additional patient's phone. "
          + "It doesn't update default home, work or cell phone numbers.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Boolean updatePatientPhone(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @RequestBody(description = "Updated patient phone") PhoneDto patientPhone)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (patientPhone == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Missing phone information.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    if (StringUtils.isNotBlank(patientPhone.getNumber())) {
      PhoneNumberFormatterUtils.validatePhoneNumber(patientPhone.getNumber());
      patientPhone.setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(patientPhone.getNumber()));
    }
    Phone phone = mapDto(patientPhone, Phone.class);
    patientManager.updatePatientPhone(patientId, phone);
    return true;
  }

  /**
   * Create Patient's Phone.
   *
   * @param patientId Patient ID
   * @param patientPhone Phone
   * @return Phone's ID if successful
   * @throws DataAccessException If there has been a database error.
   */
  @POST
  @Path("/{patientId}/phones")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Creates the additional patient's phone",
      description = "Creates the additional patient's phone. "
          + "It doesn't create default home, work or cell phone numbers.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns phone id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Integer createPatientPhone(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @RequestBody(description = "New patient's phone") PhoneDto patientPhone)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (patientPhone == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Missing phone information.");
    }
    if (StringUtils.isNotBlank(patientPhone.getNumber())) {
      PhoneNumberFormatterUtils.validatePhoneNumber(patientPhone.getNumber());
      patientPhone.setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(patientPhone.getNumber()));
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    Phone phone = mapDto(patientPhone, Phone.class);
    return patientManager.createPatientPhone(patientId, phone);
  }

  /**
   * Delete Patient's Phone.
   *
   * @param patientId Patient ID
   * @param phoneId Phone ID
   * @return <code>true</code> if the patient's phone was deleted successfully
   * @throws DataAccessException If there has been a database error.
   */
  @DELETE
  @Path("/{patientId}/phones")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Deletes the additional patient's phone",
      description = "Deletes the additional patient's phone. "
          + "It doesn't delete default home, work or cell phone numbers.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean",
                      example = "true",
                      description = "Returns true if delete is successful")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Boolean deletePatientPhone(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Phone id", required = true) @QueryParam("phoneId") Integer phoneId)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    patientManager.deletePatientPhone(phoneId);
    return true;
  }

  /**
   * Retrieve a list of patients aliases.
   *
   * @param patientId Patient ID
   * @return List of Patient's Aliases
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/aliases")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient aliases",
      description = "Retrieves a list of the patient aliases.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = AliasDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<AliasDto> getPatientAliases(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    return mapDto(patientManager.getPatientAliases(patientId), AliasDto.class, ArrayList::new);
  }

  /**
   * Update a patient alias.
   *
   * @param patientId Patient ID
   * @param patientAlias Alias
   * @return <code>true</code> if the patient's alias was updated successfully
   * @throws DataAccessException If there has been a database error.
   */
  @PUT
  @Path("/{patientId}/aliases")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates patient alias",
      description = "Updates the patient alias.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
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
  public Boolean updatePatientAlias(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @RequestBody(description = "Patients alias") @Valid AliasDto patientAlias)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (patientAlias == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Missing alias information.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    Alias alias = mapDto(patientAlias, Alias.class);
    patientManager.updatePatientAlias(alias);
    return true;
  }

  /**
   * Add a new patient alias.
   *
   * @param patientId Patient ID
   * @param patientAlias Alias
   * @return Alias's ID if successful
   * @throws DataAccessException If there has been a database error.
   */
  @POST
  @Path("/{patientId}/aliases")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Adds new patient alias",
      description = "Adds a new patient's alias.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns patient alias id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Integer createPatientAlias(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @RequestBody(description = "New patient alias") @Valid AliasDto patientAlias)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (patientAlias == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Missing alias information.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    Alias alias = mapDto(patientAlias, Alias.class);
    return patientManager.createPatientAlias(patientId, alias);
  }

  /**
   * Delete a Patient's Aliases.
   *
   * @param patientId Patient ID
   * @param aliasId Alias ID
   * @return <code>true</code> if the patient's alias was delete successfully
   * @throws DataAccessException If there has been a database error.
   */
  @DELETE
  @Path("/{patientId}/aliases")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Deletes patient's alias",
      description = "Deletes a patient's alias.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean",
                      example = "true",
                      description = "Returns true if delete is successful")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Boolean deletePatientAlias(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Alias id", required = true) @QueryParam("aliasId") Integer aliasId)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    patientManager.deletePatientAlias(aliasId);
    return true;
  }

  /**
   * Retrieve a list of insurer ID's which are the private insurers for a specific patient.
   *
   * @param patientId Patient ID
   * @return List of Patient's Private Insurer IDs
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/insurers")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the patient's private insurer ids",
      description = "Retrieves a list of private insurer ids"
          + " for the specific patient.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success, returns a list of insurer ids",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      description = "Returns a list of insurer ids",
                      example = "[1, 2]")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<Integer> getPatientPrivateInsurerIds(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    return patientManager.getPatientPrivateInsurerIDs(patientId);
  }

  /**
   * Update a patients private insurers.
   *
   * @param patientId Patient ID
   * @param privateInsurerIDs List of insurer ID's
   * @return <code>true</code> if the patient's insurer information was updated successfully
   * @throws DataAccessException If there has been a database error.
   */
  @PUT
  @Path("/{patientId}/insurers")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates patient's private insurers",
      description = "Updates the patient's private insurers.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
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
  public Boolean updatePatientPrivateInsurerIds(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @RequestBody(description = "Array of insurer ids") Set<Integer> privateInsurerIDs)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (privateInsurerIDs == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Missing insurer id's.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    patientManager.updatePatientPrivateInsurerIDs(patientId, privateInsurerIDs);
    return true;
  }

  /**
   * Retrieve Patient's User Flags.
   *
   * @param patientId Patient ID
   * @return Map of User ID to Flag
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/user-flags")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient's user flags",
      description = "Retrieves the patient's user flags.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      description = "Returns a Map of user id (Integer) and patient flag",
                      example = "{ 1: { \"message\": \"string\","
                          + "  \"lastUpdated\": \"2019-04-18T20:05:51.529Z\" } }",
                      type = "object")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Map<Integer, PatientFlagDto> getPatientUserFlags(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    Map<Integer, Flag> flagMap = patientManager.getPatientUserFlags(patientId);
    return mapDto(flagMap, PatientFlagDto.class, HashMap::new);
  }

  /**
   * Update Patient's User Flags.
   *
   * @param patientId Patient ID
   * @param userFlags Map of Accuro user ID to flag
   * @return <code>true</code> if the patient's user flags were updated successfully
   * @throws DataAccessException If there has been a database error.
   */
  @PUT
  @Path("/{patientId}/user-flags")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates patient's user flags",
      description = "Updates the patient's user flags.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
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
  @RequestBody(
      description = "The map of user id (integer) and patient flag",
      content = @Content(
          schema = @Schema(
              description = "The map of user id (integer) and patient flag",
              example = "{ 1: { \"message\": \"string\","
                  + " \"lastUpdated\": \"2019-04-18T20:05:51.529Z\" } }",
              type = "object")))
  public Boolean updatePatientUserFlags(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      Map<Integer, PatientFlagDto> userFlags)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (userFlags == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Missing user flags.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    patientManager.updatePatientUserFlags(patientId, mapDto(userFlags, Flag.class, HashMap::new));
    return true;
  }

  /**
   * Retrieve Patient's Role Flags.
   *
   * @param patientId Patient ID
   * @return Map of Role ID to Flag
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/role-flags")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient's role flags",
      description = "Retrieves the patient's role flags.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      description = "Returns a map of role id to flag",
                      example = "{ \"1\": { \"flagUser\": 1,"
                          + " \"message\": \"string\","
                          + " \"lastUpdated\": \"2019-04-18T20:05:51.529Z\" } }",
                      type = "object")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Map<Integer, PatientFlagDto> getPatientRoleFlags(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    Map<Integer, Flag> flagMap = patientManager.getPatientRoleFlags(patientId);
    return mapDto(flagMap, PatientFlagDto.class, HashMap::new);
  }

  /**
   * Update Patient's Role Flags.
   *
   * @param patientId Patient ID
   * @param roleFlags Map of role ID to flag
   * @return <code>true</code> if the patient's role flags were updated successfully
   * @throws DataAccessException If there has been a database error.
   */
  @PUT
  @Path("/{patientId}/role-flags")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates patient's role flags",
      description = "Updates patient's role flags.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "400",
              description = "Missing role flags"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean",
                      example = "true",
                      description = "Returns true if update is successful")))})
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
              schema = @Schema(type = "integer"))})
  @RequestBody(
      description = "A map of role id to flag",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(
              type = "string",
              example = "{ \"1\": { \"flagUser\": 1,"
                  + " \"message\": \"string\","
                  + " \"lastUpdated\": \"2019-04-18T20:05:51.529Z\" } }")))
  public Boolean updatePatientRoleFlags(
      @Parameter(hidden = true) @PathParam("patientId") Integer patientId,
      Map<Integer, PatientFlagDto> roleFlags)
      throws ProtossException {

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (roleFlags == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Missing role flags.");
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    patientManager.updatePatientRoleFlags(patientId, mapDto(roleFlags, Flag.class, HashMap::new));
    return true;
  }

  /**
   * Retrieve Patient's Custom Properties.
   *
   * @param patientId Patient ID
   * @return Map of Property Name to Value
   * @throws DataAccessException If there has been a database error.
   * @HTTP 200 Success
   * @HTTP 400 Invalid patient Id or requires specific user
   * @HTTP 403 Forbidden
   */
  @GET
  @Path("/{patientId}/custom-properties")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient's custom properties",
      description = "Retrieves the patient's custom properties.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "400",
              description = "Custom properties are user specific - requires a specific user"),
          @ApiResponse(
              responseCode = "403",
              description = "Insufficient permissions for the given office"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      description = "Returns a map of property name to value."
                          + "Both key values are strings.",
                      example = "{ \"CustomProperty\": \"CustomValue\" }",
                      type = "object")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Parameter(
      name = "officeId",
      description = "Office ID to filter results",
      in = ParameterIn.QUERY)
  public Map<String, String> getPatientCustomProperties(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Office id", hidden = true) @QueryParam("officeId") Integer officeId)
      throws ProtossException {

    validateUser();

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    }

    PatientCustomFieldManager patientCustomFieldManager =
        getImpl(PatientCustomFieldManager.class);
    List<PatientCustomField> customFields =
        patientCustomFieldManager.getPatientCustomFields(patientId, officeId, getUser());
    return mapCustomFields(customFields);
  }

  /**
   * Update Patient's Custom Properties.
   *
   * @param patientId Patient ID
   * @param customProperties Map property name to value
   * @return <code>true</code> if the patient's custom properties were updated successfully
   * @throws DataAccessException If there has been a database error.
   * @HTTP 200 Success
   * @HTTP 400 Invalid patient Id or requires specific user
   * @HTTP 403 Forbidden
   */
  @PUT
  @Path("/{patientId}/custom-properties")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates patient's custom properties",
      description = "Updates the patient's custom properties.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Custom properties are user specific - requires a specific user"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or missing custom properties"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean",
                      example = "true",
                      description = "Returns true if update is successful")))})
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
              schema = @Schema(type = "integer"))})
  @RequestBody(
      description = "A map of custom property name to value",
      content = @Content(
          schema = @Schema(
              example = "{ \"CustomProperty\": \"CustomValue\" }",
              type = "object")))
  public Boolean updatePatientCustomProperties(
      @Parameter(hidden = true) @PathParam("patientId") Integer patientId,
      Map<String, String> customProperties)
      throws ProtossException {

    validateUser();

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (customProperties == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Missing custom properties.");
    }
    CustomFieldManager customFieldManager = getImpl(CustomFieldManager.class);
    // get accessible custom fields to validate if user can update
    // that custom fields for patient or not.
    List<CustomField> customFields = customFieldManager.getCustomFields(null, true, getUser());
    Set<String> accessibleCustomFields = customFields
        .stream()
        .map(x -> x.getName())
        .collect(Collectors.toSet());

    Set<String> customFieldsToBeModified = customProperties.keySet();
    if (!accessibleCustomFields.containsAll(customFieldsToBeModified)) {
      customFieldsToBeModified.removeAll(accessibleCustomFields);

      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Invalid Custom field(s) or insufficient permissions to the following custom fields"
              + customFieldsToBeModified.toString() + ".");
    }

    // get all the patient custom fields irrespective of permissions
    PatientManager patientManager = getImpl(PatientManager.class);
    validatePatientId(patientManager, patientId);
    Map<String, String> allPatientCustomProperties =
        patientManager.getPatientCustomProperties(patientId);

    // map which will hold final merged values
    Map<String, String> finalCustomProperties = new HashMap<>();

    // first add all the inaccessible custom fields to the final map
    for (Entry<String, String> entry : allPatientCustomProperties.entrySet()) {
      if (!accessibleCustomFields.contains(entry.getKey())) {
        finalCustomProperties.put(entry.getKey(), entry.getValue());
      }
    }
    // add all the accessible and updated custom fields to the final map
    finalCustomProperties.putAll(customProperties);
    patientManager.updatePatientCustomProperties(patientId, finalCustomProperties);
    return true;
  }

  /**
   * Merges updated patient's custom properties map to the existing custom properties map. In order
   * to remove custom property, set its value to null in the request.
   *
   * @param patientId Patient ID
   * @param updatedCustomProperties Map of Custom Properties key-value pairs which needs to merged
   *        into the existing Map.
   * @return <code>true</code> if the patient's custom properties were updated successfully
   * @throws DataAccessException If there has been a database error.
   * @HTTP 200 Success
   * @HTTP 400 Invalid patient Id or requires specific user
   * @HTTP 403 Forbidden
   */
  @PATCH
  @Path("/{patientId}/custom-properties")
  @Consumes("application/merge-patch+json")
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates patient's custom properties",
      description = "Merges updated patient's custom properties map to the "
          + "existing custom properties map. To remove a custom property, set its"
          + " value to null in the request.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Custom properties are user specific - requires a specific user"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or missing custom properties"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean",
                      example = "true",
                      description = "Returns true if update is successful")))})
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
              schema = @Schema(type = "integer"))})
  @RequestBody(
      description = "A map of custom property names to values",
      content = @Content(
          schema = @Schema(
              example = "{ \"CustomProperty\": \"CustomValue\" }",
              type = "object")))
  public Boolean patchPatientCustomProperties(
      @Parameter(hidden = true) @PathParam("patientId") Integer patientId,
      Map<String, String> updatedCustomProperties)
      throws ProtossException {

    validateUser();

    if (patientId == null || patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (updatedCustomProperties == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Missing custom properties.");
    }

    validateAccessibleCustomFields(updatedCustomProperties);
    PatientManager patientManagerRead = getImpl(PatientManager.class, true);
    validatePatientId(patientManagerRead, patientId);
    Map<String, String> patientCustomProperties =
        patientManagerRead.getPatientCustomProperties(patientId);
    // map which will hold final merged values
    Map<String, String> finalCustomProperties = new HashMap<>();

    for (Entry<String, String> entry : updatedCustomProperties.entrySet()) {
      if (entry.getValue() == null) {
        patientCustomProperties.remove(entry.getKey());
      } else {
        finalCustomProperties.put(entry.getKey(), entry.getValue());
      }
    }

    patientCustomProperties.forEach(
        (key, value) -> finalCustomProperties.merge(key, value, (v1, v2) -> v1));

    PatientManager patientManager = getImpl(PatientManager.class);
    patientManager.updatePatientCustomProperties(patientId, finalCustomProperties);
    return true;
  }

  private void validatePatientId(PatientManager manager, int patientId) throws ProtossException {
    Patient patientById = manager.getPatientById(patientId);
    if (patientById == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Patient not found with Id: " + patientId);
    }
  }

  private void validateAccessibleCustomFields(Map<String, String> customProperties)
      throws ProtossException {
    CustomFieldManager customFieldManager = getImpl(CustomFieldManager.class);
    List<CustomField> customFields = customFieldManager.getCustomFields(null, true, getUser());
    Set<String> accessibleCustomFields = customFields
        .stream()
        .map(x -> x.getName())
        .collect(Collectors.toSet());

    Set<String> customFieldsToBeModified = customProperties.keySet();
    if (!accessibleCustomFields.containsAll(customFieldsToBeModified)) {
      customFieldsToBeModified.removeAll(accessibleCustomFields);

      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Invalid Custom field(s) or insufficient permissions to the following custom fields"
              + customFieldsToBeModified.toString() + ".");
    }
  }

  /**
   * Get activity logs for accessing a record.
   *
   * @param patientId Patient accessing the record
   * @param recordId Record being checked
   * @return List of activities
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/activity/{recordId}")
  @PreAuthorize("#accuro.isSysAdmin()")
  @Facet("internal")
  @Hidden
  public List<PatientActivityDto> getActivity(
      @PathParam("patientId") Integer patientId,
      @PathParam("recordId") String recordId)
      throws ProtossException {

    if (StringUtils.isBlank(recordId)) {
      return null;
    }
    LogManager logInterface = getImpl(LogManager.class);
    return mapDto(logInterface.getPatientActivity(recordId),
        PatientActivityDto.class,
        ArrayList::new);
  }

  /**
   * Retrieve a map containing a list of log activities for each record id.
   *
   * @param patientId Patient Id
   * @param recordIds List of record id's
   * @return A map of patient activities
   */
  @POST
  @Path("/{patientId}/activity/search")
  @PreAuthorize("#accuro.isSysAdmin()")
  @Facet("internal")
  @Hidden
  public Map<String, List<PatientActivityDto>> getActivities(
      @PathParam("patientId") Integer patientId,
      List<String> recordIds)
      throws ProtossException {

    if (recordIds == null || recordIds.isEmpty()) {
      return null;
    }
    LogManager logInterface = getImpl(LogManager.class);
    Map<String, List<PatientActivity>> activityMap = logInterface.getPatientActivity(recordIds);
    return mapDto(activityMap, PatientActivityDto.class, ArrayList::new, HashMap::new);
  }

  /**
   * Search for patients by personal health number/care card number.
   *
   * @param phn Start of personal health number being searched. This is a mandatory field.
   * @return List of patients matching the search. An empty list if the phn is not provided.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/search")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Searches patients by PHN",
      description = "Searches for patients by the given personal health number/care "
          + "card number.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(ref = "PatientDto"))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<PatientDto> getPatientByPhn(
      @Parameter(description = "The full/partial PHN to search",
          required = true) @QueryParam("phn") String phn)
      throws ProtossException {

    if (StringUtils.isBlank(phn)) {
      return Collections.emptyList();
    }
    PatientManager patientManager = getImpl(PatientManager.class);
    return mapDto(patientManager.getPatientsByPHN(phn), PatientDto.class, ArrayList::new);
  }

  /**
   * Retrieves byte data of the patient profile picture.
   *
   * @param patientId Unique patient id.
   * @return byte data of patient profile picture
   * @HTTP 400 if there are field or business validation errors.
   **/
  @GET
  @Path("/{patientId}/profile-picture")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient's profile picture",
      description = "Retrieves patient's profile picture",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success. Returns file with media type as : "
                  + "application/octet-stream and png format.",
              content = @Content(mediaType = "application/octet-stream",
                  schema = @Schema(
                      type = "string", format = "binary"))),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(responseCode = "404",
              description = "If profile picture is not found.")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              hidden = true,
              description = "Patient id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  public Response getProfilePicture(
      @PathParam("patientId") int patientId) throws DataAccessException {

    PatientProfilePictureManager pictureManager = getImpl(PatientProfilePictureManager.class);
    PatientProfilePicture profilePicture =
        pictureManager.getProfilePictureByPatientId(patientId);
    if (profilePicture == null || profilePicture.getPatientId() != patientId
        || profilePicture.getProfileImage() == null
        || profilePicture.getProfileImage().length < 1) {

      throw Error.webApplicationException(Status.NOT_FOUND,
          "Profile picture not found for the patient: " + patientId);
    }

    return Response.ok(profilePicture.getProfileImage(), MediaType.APPLICATION_OCTET_STREAM)
        .build();
  }

  @POST
  @Path("/{patientId}/profile-picture")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Uploads patient's profile picture",
      description = "Uploads patient's profile picture",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(responseCode = "413",
              description = "If picture uploaded is more than 10MB"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "Integer",
                      example = "12",
                      description = "Returns profile picture id if successful.")))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              hidden = true,
              description = "Patient id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @RequestBody(
      description = "Image to be uploaded as profile picture.",
      content = {
          @Content(mediaType = MediaType.MULTIPART_FORM_DATA,
              schema = @Schema(implementation = PictureMultipartDefinition.class))})
  public Response uploadProfilePicture(
      @PathParam("patientId") int patientId,
      @Multipart(value = "image") @NotNull Attachment image)
      throws IOException, DataAccessException {

    PatientProfilePicture profilePicture = parseAttachment(image, patientId);
    PatientProfilePictureManager profilePictureManager =
        getImpl(PatientProfilePictureManager.class);

    int pictureId = profilePictureManager.uploadProfilePicture(profilePicture, getUser());

    return Response.status(Status.OK).entity(pictureId)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }

  /**
   * Deletes patient profile picture according to the given patient id.
   *
   * @param patientId The patient id
   * @throws DataAccessException If there is a database error.
   * @throws TimeZoneNotFoundException If the time zone preference is not set in the database.
   */
  @DELETE
  @Path("/{patientId}/profile-picture")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Hidden
  @Operation(
      summary = "Deletes patient's profile picture",
      description = "Deletes patient's profile picture",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "404",
              description = "Patient profile picture not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean",
                      example = "true",
                      description = "Returns true if deletion is successful")))})
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
              schema = @Schema(type = "integer"))})
  public Response deleteProfilePicture(@PathParam("patientId") int patientId)
      throws DataAccessException {
    PatientProfilePictureManager pictureManager = getImpl(PatientProfilePictureManager.class);

    PatientProfilePicture profilePicture =
        pictureManager.getProfilePictureByPatientId(patientId);
    if (profilePicture == null || profilePicture.getPatientId() != patientId
        || profilePicture.getProfileImage() == null
        || profilePicture.getProfileImage().length < 1) {

      throw Error.webApplicationException(Status.NOT_FOUND,
          "Profile picture not found for the patient: " + patientId);
    }
    pictureManager.deleteProfilePicture(patientId, getUser());
    return Response.ok().build();

  }

  private void validateUser() {
    if (getUser().getUserId() == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient custom properties are user specific. User Id is required.");
    }
  }

  private PatientProfilePicture parseAttachment(Attachment attachment, int patientId)
      throws IOException {

    PatientProfilePicture patientProfilePicture = new PatientProfilePicture();
    patientProfilePicture.setPatientId(patientId);

    byte[] fileBytes = attachment.getObject(byte[].class);

    InputStream targetStream = new ByteArrayInputStream(fileBytes);

    BufferedImage image = ImageIO.read(targetStream);

    if (image == null) {
      Error.webApplicationException(Status.BAD_REQUEST, "Not a valid image type.");
    }

    BufferedImage formattedImage =
        ImageResizer.resizeImage(MAX_DEFAULT_WIDTH, MAX_DEFAULT_HEIGHT, image, false);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(formattedImage, "png", baos); // write to png lossless format.
    byte[] imageInByte = baos.toByteArray();

    patientProfilePicture.setProfileImage(imageInByte);

    return patientProfilePicture;
  }

  /**
   * Helper method to map a list of {@link PatientCustomField} to a Map object.
   *
   * @param customFields The list of {@link PatientCustomField}s to be mapped.
   * @return Map representing the provided set of {@link PatientCustomField}s
   */
  private Map<String, String> mapCustomFields(List<PatientCustomField> customFields) {
    Map<String, String> customFieldMap = new HashMap<>();
    for (PatientCustomField customField : customFields) {
      customFieldMap.put(customField.getName(), customField.getValue());
    }
    return customFieldMap;
  }

  // this is for swagger and hidden annotation is not working
  private class PictureMultipartDefinition {

    @Schema(description = "File binary", type = "Attachment", format = "binary")
    Attachment profilePicture;

    public Attachment getProfilePicture() {
      return profilePicture;
    }

    public void setProfilePicture(Attachment profilePicture) {
      this.profilePicture = profilePicture;
    }
  }

  private void formatPhoneNumber(PatientDto patientDto) {
    if (patientDto.getDemographics().getPhones() != null) {
      List<PhoneDto> phoneDtoList =
          patientDto.getDemographics().getPhones().stream().map(phoneDto -> {
            if (StringUtils.isNotBlank(phoneDto.getNumber())) {
              phoneDto.setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(phoneDto.getNumber()));
            }
            return phoneDto;
          }).collect(Collectors.toUnmodifiableList());
      patientDto.getDemographics().setPhones(phoneDtoList);
    }
  }

}
