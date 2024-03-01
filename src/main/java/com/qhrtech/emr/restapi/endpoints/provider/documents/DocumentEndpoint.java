
package com.qhrtech.emr.restapi.endpoints.provider.documents;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qhrtech.emr.accdocs.api.docs.AccDocManager;
import com.qhrtech.emr.accdocs.model.docs.AccDoc;
import com.qhrtech.emr.accuro.api.docs.DocumentManager;
import com.qhrtech.emr.accuro.api.locks.ProtectionLockManager;
import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.model.docs.Document;
import com.qhrtech.emr.accuro.model.docs.DocumentReview;
import com.qhrtech.emr.accuro.model.docs.FileType;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientFeatureAccessException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientRolesException;
import com.qhrtech.emr.accuro.model.exceptions.security.MaskException;
import com.qhrtech.emr.accuro.model.locks.ProtectionLock;
import com.qhrtech.emr.accuro.model.locks.ProtectionLockRequest;
import com.qhrtech.emr.accuro.model.locks.ProtectionType;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.DocumentDto;
import com.qhrtech.emr.restapi.models.dto.DocumentReviewDto;
import com.qhrtech.emr.restapi.models.dto.FileTypeDto;
import com.qhrtech.emr.restapi.models.dto.security.ErrorResponse;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.enums.DocumentScope;
import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.qhrtech.emr.restapi.models.validators.DocumentValidator;
import com.qhrtech.emr.restapi.security.exceptions.PreferenceDisabledException;
import com.qhrtech.emr.restapi.services.AcronDetailsService;
import com.qhrtech.emr.restapi.services.ModuleService;
import com.qhrtech.emr.restapi.services.exceptions.MD5Exception;
import com.qhrtech.emr.restapi.services.impl.AzureBlobStorageService;
import com.qhrtech.emr.restapi.util.CustomMD5;
import com.qhrtech.emr.restapi.util.DateFormatter;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>DocumentEndpoint</code> collection is designed to expose {@link DocumentDto} and
 * related public endpoints. Provider level authorization is required.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component("provider-document-endpoint")
@Path("/v1/provider-portal/documents")
@Facet("provider-portal")
@Tag(name = "Document Endpoints - Provider",
    description = "Exposes provider portal document endpoints")
public class DocumentEndpoint extends AbstractEndpoint {

  @Autowired
  private final Logger log = LoggerFactory.getLogger(DocumentEndpoint.class);

  private static final int DEFAULT_LOCK_EXPIRY = (int) TimeUnit.MINUTES.toSeconds(5);

  // Milli seconds
  private static int WAIT_LOCK_INTERVAL = 200;
  private static int WAIT_LOCK_MAX_COUNT = 1500;

  public static final String PDF = ".pdf";

  private static String[] NON_REFERRAL_EXN = {PDF, ".jpg", ".jpeg"};

  @Autowired
  private ModuleService moduleService;

  @Autowired
  private AzureBlobStorageService azureBlobStorageService;

  @Autowired
  private AcronDetailsService discoveryDetailsService;

  /**
   * Creates a document by storing the document meta data and the contents in the database.
   *
   * @param details Multipart request contains file metadata as a JSON String of {@link DocumentDto}
   *        object.
   * @param document Multipart request containing actual Document Files(Binary data).
   *        <p>
   *        For REFERRAL documents, the allowed file extension is PDF and for non-REFERRAL
   *        documents, the allowed file extensions are PDF, JPG or JPEG.
   *        </p>
   *        <p>
   *        Maximum 5 files can be uploaded
   *        </p>
   * @param type Query parameter which determines if the document type is REFERRAL or not. For
   *        referral documents the parameter should be type=REFERRAL and for non-referral documents,
   *        this parameter should be ignored.
   *        <p>
   *        <ul>
   *        <li>sub_folder_id cannot be passed unless the preference 'Document Sub-Types' is enabled
   *        in Accuro.</li>
   *        <li>from_name cannot be passed unless the preference 'Document From Field' is enabled in
   *        Accuro.</li>
   *        </ul>
   *        </p>
   * @return Response with the document id(s) of the document(s) created.
   * @HTTP 400 if there are field or business validation errors.
   * @HTTP 401 if access is denied or preference not set for any field
   * @HTTP 413 if each document uploaded is more than 10MB
   **/
  @POST
  @Consumes("multipart/form-data")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_WRITE' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for the reviewing providers.")
  @Operation(
      summary = "Saves a document",
      description = "Creates a document by storing the document meta data and the contents "
          + "in the database."
          + "The Role DOCUMENTS or EMR_VIRTUAL_CHART with Full"
          + " access level authorize to access this endpoint.",
      responses = {
          @ApiResponse(responseCode = "400",
              description = "If there are field or business validation errors"),
          @ApiResponse(responseCode = "401",
              description = "If access is denied or preference not set for any field"),
          @ApiResponse(responseCode = "413",
              description = "If document uploaded is more than 10MB"),
          @ApiResponse(
              responseCode = "200",
              description = "Success. Returns document id",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(type = "integer",
                          description = "document id", example = "1"))))})
  @Parameters(value = {
      @Parameter(
          in = ParameterIn.HEADER,
          name = "authorization",
          description = "Provider level authorization grant",
          required = true),
      @Parameter(
          in = ParameterIn.QUERY,
          name = "type",
          description = "Query parameter which tells document type is REFERRAL or not.\n"
              + "If 'Document Sub-Types' is enabled at Accuro, sub_folder_id and sub_folder could "
              + "be passed.\n If 'Document From Field' is enabled at Accuro, from_id, "
              + "from_type and from_name could be passed.",
          schema = @Schema(type = "string", example = "REFERRAL"))})
  @RequestBody(
      description = "Required to send document meta data as JSON format "
          + "and document file as binary together",
      content = {
          @Content(mediaType = MediaType.MULTIPART_FORM_DATA,
              schema = @Schema(implementation = DocumentMultipartDefinition.class))})
  public Response createDocument(
      @Multipart(value = "details") String details,
      @Multipart(value = "document") List<Attachment> document,
      @Parameter(hidden = true) @QueryParam("type") String type)
      throws IOException, ConstraintViolationException,
      MD5Exception, ProtossException {

    String lockControlKey = null;
    UUID lockUuid = null;

    try {

      ObjectMapper mapper = new ObjectMapper();
      DocumentDto documentDto = mapper.readValue(details, DocumentDto.class);
      validateRequest(documentDto, type);
      validateAttachments(document, type);

      Set<Integer> documentIds = new HashSet<>();
      AccDocManager accDocManager = getImpl(AccDocManager.class);
      DocumentManager documentManager = getImpl(DocumentManager.class);
      AuditLogUser auditLogUser = getUser();

      lockControlKey = documentDto.getPatientId() + "-" + documentDto.getFolderId();

      // Wait if there's an existing lock util it's done
      int waitCount = 0;
      boolean obtainedLock = false;

      while (!obtainedLock) {
        waitCount++;
        if (waitCount > WAIT_LOCK_MAX_COUNT) {
          return Response
              .status(Status.CONFLICT)
              .build();
        }
        try {
          if (lockExisting(lockControlKey)) {
            Thread.sleep(WAIT_LOCK_INTERVAL);
            continue;
          }
          lockUuid = createProtectionLock(lockControlKey, auditLogUser.getComputerInfo());
          if (lockUuid != null) {
            obtainedLock = true;
          }
        } catch (InterruptedException exp) {
          Thread.currentThread().interrupt();
          log.error("Protection lock error interrupted: " + lockControlKey
              + ", " + exp.getMessage());
        } catch (ResourceConflictException
            | DatabaseInteractionException exp) {
          Thread.sleep(WAIT_LOCK_INTERVAL);
          // Trying not to log too many errors
          if (waitCount % 100 == 0) {
            log.error("Protection lock error: " + lockControlKey + ", " + exp.getMessage());
          }
        }
      }

      boolean isAccBlobMode = moduleService.isAccBlobEnabled();

      for (Attachment attachment : document) {

        documentDto.setFileName(getFileName(attachment));
        // review retrieval should be within loop as review object can be modified by
        // protoss in first loop iteration.

        Set<DocumentReview> reviews = getDocReviewMapping(documentDto);
        byte[] fileBytes = attachment.getObject(byte[].class);

        getEncryptedFileName(documentDto, fileBytes);
        Document doc = getDocumentMapping(documentDto);

        // Azure blob storage
        if (isAccBlobMode) {
          CloudStorageAccount cloudStorageAccount =
              discoveryDetailsService.getCloudStorageAccount(getTenantId());

          BlobUploadStub azureUploader =
              BlobUploadStub.getUploader(azureBlobStorageService, cloudStorageAccount, fileBytes);

          int documentId = documentManager.createDocument(doc, reviews, azureUploader);
          documentIds.add(documentId);
        } else {
          // AccDocs storage
          AccDoc accDoc = getAccDocMapping(documentDto, fileBytes);
          int documentId = documentManager.createDocument(doc,
              reviews, accDoc,
              accDocManager.getAccDocConnection(), auditLogUser);
          documentIds.add(documentId);
        }
      }
      return Response
          .status(Status.OK)
          .entity(documentIds)
          .type(MediaType.APPLICATION_JSON)
          .build();

    } catch (DataAccessException | IllegalArgumentException ex) {
      throw new WebApplicationException(ex, Response
          .status(Status.BAD_REQUEST)
          .entity(new ErrorResponse("Error processing request. Reason: " + ex.getMessage()))
          .type(MediaType.APPLICATION_JSON_TYPE)
          .build());

    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new WebApplicationException(ex, Response
          .status(Status.BAD_REQUEST)
          .entity(new ErrorResponse("Error processing request. Reason: " + ex.getMessage()))
          .type(MediaType.APPLICATION_JSON_TYPE)
          .build());
    } finally {
      if (lockUuid != null) {
        releaseProtectionLock(lockControlKey);
      }
    }
  }

  private String getFileName(Attachment attachment) {
    MultivaluedMap<String, String> headers = attachment.getHeaders();
    List<String> list = headers.get("Content-Disposition");
    // form-data; name="file"; filename="test.txt"
    return (list == null || list.isEmpty()) ? ""
        : list.get(0).replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$",
            "$1");

  }

  private void validateAttachments(List<Attachment> attachments, String type) {

    if (attachments.size() > 5) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Maximum 5 files can be uploaded.");
    }
    List<String> documentType = Arrays.asList(NON_REFERRAL_EXN);

    for (Attachment attachment : attachments) {

      String fileName = getFileName(attachment);
      boolean validFileType = false;

      String logErrorMessage;
      if (fileName.contains(".")) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (DocumentScope.REFERRAL.toString().equalsIgnoreCase(type)) {
          logErrorMessage = "Valid file extension is: PDF.";
          if (PDF.equalsIgnoreCase(fileExtension)) {
            validFileType = true;
          }
        } else {
          logErrorMessage = "Valid file extensions are: PDF, JPEG or JPG.";
          // documentType is always true for now but it can be nullable if someone change the global
          // value later. So we check if the type is null here in case.
          if (documentType != null && documentType.contains(StringUtils.lowerCase(fileExtension))) {
            validFileType = true;
          }
        }
      } else {
        logErrorMessage = "Missing file Extension.";
      }
      if (!validFileType) {
        throw Error.webApplicationException(Status.BAD_REQUEST,
            "Error in file Type: " + fileName + ". " + logErrorMessage);
      }
    }
  }

  private void validateRequest(DocumentDto documentDto, String documentType)
      throws ProtossException {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    AccuroPreferenceManager preferenceManager = getImpl(AccuroPreferenceManager.class);

    if (!(documentDto.getSubtypeId() == null)) {
      if (!preferenceManager.getSystemPreference("ShowDocumentsSubType")
          .equalsIgnoreCase("true")) {
        throw new PreferenceDisabledException("Preference not enabled to view Sub Folder.");
      }
    }

    if (!StringUtils.isBlank(documentDto.getFromName())) {
      if (!preferenceManager.getSystemPreference("ShowDocumentsFrom")
          .equalsIgnoreCase("true")) {
        throw new PreferenceDisabledException("Preference not enabled to view From field.");
      }
    }

    if (documentType != null && documentType.equalsIgnoreCase(DocumentScope.REFERRAL.toString())) {
      DocumentValidator documentValidator = new DocumentValidator();
      documentValidator.setType(documentType);
      documentValidator.setDocumentDto(documentDto);
      Set<ConstraintViolation<DocumentValidator>> violations =
          validator.validate(documentValidator);
      if (!violations.isEmpty()) {
        throw new ConstraintViolationException("Request Validation Exception", violations);
      }
      documentDto.setDateCreated(LocalDate.now().toLocalDateTime(LocalTime.MIDNIGHT));

    } else {
      Set<ConstraintViolation<DocumentDto>> violations = validator.validate(documentDto);
      if (!violations.isEmpty()) {
        throw new ConstraintViolationException("Request Validation Exception", violations);
      }
    }
  }

  private DocumentDto getEncryptedFileName(DocumentDto documentDto, byte[] fileBytes)
      throws MD5Exception {

    CustomMD5 fileCrypt = new CustomMD5("md5");
    fileCrypt.readByteArray(fileBytes);
    String fileName = fileCrypt.toString()
        + documentDto.getFileName().substring(documentDto.getFileName().lastIndexOf("."));
    documentDto.setFileName(fileName);
    return documentDto;
  }

  private Document getDocumentMapping(DocumentDto documentDto) {

    if (documentDto.getPriority() == null) {
      documentDto.setPriority(1);// 1 is corresponding to normal priority.
    }

    LocalDate localDate = LocalDate.now();
    documentDto.setDocumentDate(localDate.toLocalDateTime(LocalTime.MIDNIGHT));
    if (documentDto.getDateCreated() == null) {
      documentDto.setDateCreated(localDate.toLocalDateTime(LocalTime.MIDNIGHT));
    } else {
      documentDto.setDateCreated(DateFormatter.toDay(documentDto.getDateCreated()));

    }

    if (documentDto.getReceivedDate() == null) {
      documentDto.setReceivedDate(localDate.toLocalDateTime(LocalTime.MIDNIGHT));
    } else {
      documentDto.setReceivedDate(DateFormatter.toDay(documentDto.getReceivedDate()));
    }

    Document document = mapDto(documentDto, Document.class);
    return document;
  }

  private Set<DocumentReview> getDocReviewMapping(DocumentDto documentDto) {
    Set<DocumentReview> reviews = new HashSet<>();
    if (documentDto.getReviews() != null) {
      documentDto.getReviews().forEach(x -> {
        if (!(x.getProviderId() == 0 && x.getReviewDate() == null)) {
          x.setReviewDate(DateFormatter.toDay(x.getReviewDate()));
          DocumentReview documentReview = mapDto(x, DocumentReview.class);
          documentReview.setReviewed(!(documentReview.getReviewDate() == null));
          reviews.add(documentReview);
        }
      });
    }
    if (reviews.isEmpty()) {
      DocumentReview documentReview = new DocumentReview();
      documentReview.setReviewed(false);
      documentReview.setProviderId(0);
      reviews.add(documentReview);
    }

    return reviews;
  }

  private AccDoc getAccDocMapping(DocumentDto documentDto, byte[] fileBytes) {
    AccDoc accDoc = new AccDoc();
    accDoc.setContents(fileBytes);
    accDoc.setFileName(documentDto.getFileName());
    accDoc.setUploadedDate(documentDto.getDocumentDate());

    return accDoc;
  }

  /**
   * Retrieves a set of file types (extensions).
   *
   * @return A set of {@link FileTypeDto}. If there are no file types an empty set will be returned.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/file-types")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_READ', 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess( 'DOCUMENTS', 'READ_ONLY' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves file types",
      description = "Gets a set of all existing file types.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = FileTypeDto.class))))})
  public Set<FileTypeDto> getFileTypes() throws ProtossException {
    DocumentManager documentManager = getImpl(DocumentManager.class);
    Set<FileType> fileTypes = documentManager.getFileTypes();
    for (FileType fileType : fileTypes) {
      fileType.setType(fileType.getType().trim());
    }
    return mapDto(fileTypes, FileTypeDto.class, HashSet::new);
  }

  /**
   * Retrieves the document reviews by document ID
   *
   * @param documentId document ID
   * @return A set of {@link DocumentReviewDto}.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{documentId}/reviews")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_READ', 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess( 'DOCUMENTS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access if the user has this permission for any provider.")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves document reviews",
      description = "Gets a set of all document reviews by document id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = DocumentReviewDto.class))))})
  public Set<DocumentReviewDto> getDocumentReviews(
      @Parameter(description = "Document ID") @PathParam("documentId") Integer documentId)
      throws ProtossException {
    DocumentManager documentManager = getImpl(DocumentManager.class);
    Set<DocumentReview> reviews = documentManager.getReviewsForDocument(documentId);

    Set<DocumentReviewDto> reviewsDto = reviews.stream().map(d -> {
      DocumentReviewDto documentReview = mapDto(d, DocumentReviewDto.class);

      return documentReview;
    }).collect(Collectors.toSet());

    return reviewsDto;
  }

  private boolean lockExisting(String lockControlKey) throws DatabaseInteractionException {
    ProtectionLockManager lockManager = getImpl(ProtectionLockManager.class);
    ProtectionLock lock =
        lockManager.getLockByItemId(ProtectionType.ProcessingLock, lockControlKey);
    if (lock != null) {
      return true;
    }
    return false;
  }

  private void releaseProtectionLock(String lockControlKey) throws InsufficientRolesException,
      DatabaseInteractionException, InsufficientPermissionsException,
      InsufficientFeatureAccessException {
    ProtectionLockManager lockManager = getImpl(ProtectionLockManager.class);
    lockManager.releaseLock(ProtectionType.ProcessingLock, lockControlKey, getClientUuid());
  }

  private UUID createProtectionLock(String lockControlKey, String machine)
      throws DatabaseInteractionException, ResourceConflictException,
      InsufficientRolesException, TimeZoneNotFoundException, MaskException,
      InsufficientFeatureAccessException, UnsupportedSchemaVersionException,
      InsufficientPermissionsException {
    ProtectionLockRequest.Builder builder = new ProtectionLockRequest.Builder();
    builder.setUser(getUser());
    builder.setMachine(machine);
    builder.setClientUUID(getClientUuid());
    builder.setType(ProtectionType.ProcessingLock);
    builder.setLockId(lockControlKey);
    builder.setDuration(DEFAULT_LOCK_EXPIRY);
    ProtectionLockRequest request = builder.build();

    ProtectionLockManager lockManager = getImpl(ProtectionLockManager.class);
    return lockManager.createLock(request);
  }

  // this is for swagger and hidden annotation is not working

  private class DocumentMultipartDefinition {

    @Schema(description = "File metadata: DocumentDto Object as JSON string", type = "String",
        format = "object")
    String details;

    @Schema(description = "File binary", type = "Attachment", format = "binary")
    List<Attachment> document;

    public String getDetails() {
      return details;
    }

    public void setDetails(String details) {
      this.details = details;
    }

    public List<Attachment> getDocument() {
      return document;
    }

    public void setDocument(List<Attachment> document) {
      this.document = document;
    }
  }
}
