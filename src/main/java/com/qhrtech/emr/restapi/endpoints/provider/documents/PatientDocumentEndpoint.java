
package com.qhrtech.emr.restapi.endpoints.provider.documents;

import com.qhrtech.emr.accdocs.api.docs.AccDocManager;
import com.qhrtech.emr.accdocs.model.docs.AccDoc;
import com.qhrtech.emr.accuro.api.docs.DocsAccblobManager;
import com.qhrtech.emr.accuro.api.docs.DocumentManager;
import com.qhrtech.emr.accuro.model.docs.DocsAccblob;
import com.qhrtech.emr.accuro.model.docs.Document;
import com.qhrtech.emr.accuro.model.docs.DocumentReview;
import com.qhrtech.emr.accuro.model.docs.FileType;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.DocumentDto;
import com.qhrtech.emr.restapi.models.dto.DocumentReviewDto;
import com.qhrtech.emr.restapi.models.dto.PatientFoldersDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.services.AcronDetailsService;
import com.qhrtech.emr.restapi.services.BlobIdentifier;
import com.qhrtech.emr.restapi.services.BlobIdentifier.BlobIdentifierBuilder;
import com.qhrtech.emr.restapi.services.BlobStorageService;
import com.qhrtech.emr.restapi.services.ModuleService;
import com.qhrtech.emr.restapi.services.impl.HostedDiscoveryDetailsService;
import com.qhrtech.emr.restapi.util.MimeTypeUtils;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


/**
 * This <code>PatientDocumentEndpoint</code> collection is designed to expose {@link DocumentDto}
 * and related public endpoints. Provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component("provider-patient-document-endpoint")
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientDocument Endpoints",
    description = "Exposes patient document endpoints")
public class PatientDocumentEndpoint extends AbstractEndpoint {

  @Autowired
  private ModuleService moduleService;

  @Autowired
  private BlobStorageService blobStorageService;

  @Autowired
  private AcronDetailsService discoveryDetailsService;

  /**
   * Gets the media type by Document Type ID
   *
   * @param accuroFileTypes A map of Accuro file types
   * @param documentTypeId Document Type ID
   * @return A Medeo Type
   */
  private static String getMediaType(
      Map<Integer, FileType> accuroFileTypes,
      Integer documentTypeId) {

    if (documentTypeId == null) {
      return MimeTypeUtils.MIME_APPLICATION_OCTET_STREAM;
    }
    FileType type = accuroFileTypes.get(documentTypeId);
    if (type == null) {
      return MimeTypeUtils.MIME_APPLICATION_OCTET_STREAM;
    } else {
      return MimeTypeUtils.getMediaType(type.getType());
    }
  }

  /**
   * Get a collection of base document filters
   *
   * @return A predicate of base document filters.
   */
  private static Predicate<Document> getBaseDocumentFilters() {
    Predicate<Document> basePredicate = d -> d.getDeleted().equals(0);
    basePredicate.and(d -> d.getArchived().equals(0))
        .and(d -> d.getActive().equals(1));
    return basePredicate;
  }

  /**
   * Builds a predicate based on the combination of parameters passed and the base document filters.
   *
   * @param folder A folder name
   * @param subType A folder sub-type name
   * @return a predicate of document filters
   */
  private static Predicate<Document> getDocumentFilters(
      String folder,
      String subType) {
    Predicate<Document> basePredicate = getBaseDocumentFilters();
    if (folder == null && subType == null) {
      return basePredicate;
    } else if (folder != null && subType != null) {
      return basePredicate.and(d -> d.getPathType().equalsIgnoreCase(folder))
          .and(d -> d.getSubtype().equalsIgnoreCase(subType));
    } else if (folder != null) {
      return basePredicate.and(d -> d.getPathType().equalsIgnoreCase(folder));
    } else {
      return basePredicate.and(d -> d.getSubtype().equalsIgnoreCase(subType));
    }
  }

  /**
   * Retrieves a set of path type(folder) including sub type(sub folder).
   *
   * @param patientId Patient ID
   * @return path type(folder) and sub type(sub folder) of type {@link PatientFoldersDto}
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/documents/folders/")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_READ', 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess( 'DOCUMENTS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient's folders",
      description = "Retrieves path types(folders) including "
          + "sub types(sub folders) of the patient.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = PatientFoldersDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "The patient id",
              in = ParameterIn.PATH,
              required = true,
              schema = @Schema(type = "integer"))})
  public PatientFoldersDto getPatientFolders(
      @Parameter(hidden = true) @PathParam("patientId") Integer patientId) throws ProtossException {

    DocumentManager documentManager = getImpl(DocumentManager.class);
    Set<Document> docs = documentManager.getDocumentsForPatient(patientId)
        .stream()
        .filter(getBaseDocumentFilters())
        .collect(Collectors.toSet());

    // get path type(folder) and sub type(sub folder) of this patient
    PatientFoldersDto patientFolders = new PatientFoldersDto();
    Set<String> folders = new HashSet<>();
    Set<String> subType = new HashSet<>();

    docs.forEach(doc -> {
      folders.add(doc.getPathType());

      if (!StringUtils.isBlank(doc.getSubtype())) {
        subType.add(doc.getSubtype());
      }
    });

    patientFolders.setPatientId(patientId);
    patientFolders.setFolders(folders);
    patientFolders.setSubtypes(subType);

    return patientFolders;
  }

  /**
   * Retrieves a list of all the documents of a patient, filtered by folder and/or sub type if set.
   *
   * @param patientId Patient ID to search documents for.
   * @param folder Optional top level folder for the search
   * @param subType Optional subtype for the search.
   * @return A set of {@link DocumentDto}
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/documents/")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_READ', 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess( 'DOCUMENTS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient's documents",
      description = "Retrieves all the documents of the patient, "
          + "filtered by folder and/or sub type if provided.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = DocumentDto.class))))})
  @Parameters(value = {
      @Parameter(
          name = "authorization",
          description = "Provider authorization grant",
          in = ParameterIn.HEADER,
          required = true),
      @Parameter(
          name = "patientId",
          description = "The patient id",
          in = ParameterIn.PATH,
          required = true,
          schema = @Schema(type = "integer")),
      @Parameter(
          name = "folder",
          description = "Folder to filter the results",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "subtype",
          description = "Sub type to filter the results",
          in = ParameterIn.QUERY)})
  public Set<DocumentDto> getDocuments(
      @Parameter(hidden = true) @PathParam("patientId") Integer patientId,
      @Parameter(hidden = true) @QueryParam("folder") String folder,
      @Parameter(hidden = true) @QueryParam("subtype") String subType) throws ProtossException {

    DocumentManager documentManager = getImpl(DocumentManager.class);
    Set<Document> docsFiltered = documentManager.getDocumentsForPatient(patientId)
        .stream()
        .filter(getDocumentFilters(folder, subType))
        .collect(Collectors.toSet());

    if (docsFiltered.isEmpty()) {
      return Collections.emptySet();
    }

    Set<DocumentReview> reviews = documentManager.getReviewsForPatient(patientId);

    Map<Integer, Set<DocumentReviewDto>> reviewsMap = new HashMap<>();
    for (DocumentReview review : reviews) {
      DocumentReviewDto reviewDto = mapDto(review, DocumentReviewDto.class);
      Set<DocumentReviewDto> reviewSet = new HashSet<>();

      if (reviewsMap.containsKey(review.getDocumentId())) {
        Set<DocumentReviewDto> reviewDtos = reviewsMap.get(review.getDocumentId());
        reviewsMap.remove(review.getDocumentId());
        reviewSet.addAll(reviewDtos);
      }
      reviewSet.add(reviewDto);
      reviewsMap.put(review.getDocumentId(), reviewSet);
    }

    Set<DocumentDto> documents = docsFiltered.stream().map(d -> {
      DocumentDto document = mapDto(d, DocumentDto.class);
      int documentId = document.getDocumentId();
      document.setReviews(reviewsMap.get(documentId));
      return document;
    }).collect(Collectors.toSet());

    return documents;
  }

  /**
   * Gets the actual bytes by Document ID.
   *
   * @param patientId Patient ID
   * @param documentId Document ID
   * @return The actual bytes of the document file
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/documents/{documentId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_READ', 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess( 'DOCUMENTS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the contents of the patient's document",
      description = "Retrieves the contents of the document by the given document id and patient "
          + "id. The media type of the file depends upon the file extension. "
          + "By default it is application/octet-stream. For pdf file, it is application/pdf "
          + "and so on.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success, the file to be downloaded",
              content = @Content(
                  mediaType = "application/pdf",
                  schema = @Schema(
                      type = "string",
                      format = "binary",
                      example = "Binary content of the file"))),
          @ApiResponse(
              responseCode = "404",
              description = "Document not found")})

  @Parameters(
      value = {@Parameter(
          name = "authorization",
          description = "Provider authorization grant",
          in = ParameterIn.HEADER,
          required = true),
          @Parameter(
              name = "patientId",
              description = "The patient id",
              in = ParameterIn.PATH,
              required = true,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "documentId",
              description = "The document id",
              in = ParameterIn.PATH,
              required = true,
              schema = @Schema(type = "integer"))})
  public Response getDoc(
      @Parameter(hidden = true) @PathParam("patientId") Integer patientId,
      @Parameter(hidden = true) @PathParam("documentId") Integer documentId)
      throws ProtossException {

    DocumentManager documentManager = getImpl(DocumentManager.class);
    Document document = documentManager.getDocumentById(documentId);

    if (document == null || document.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Document not found");
    }

    Map<Integer, FileType> fileTypes = fileTypesById(documentManager);
    String mediaType = getMediaType(fileTypes, document.getFileType());

    if (moduleService.isAccBlobEnabled()) {

      DocsAccblobManager docsAccblobManager = getImpl(DocsAccblobManager.class);

      DocsAccblob docsAccblob =
          docsAccblobManager.getByDocumentId(documentId);

      if (docsAccblob == null) {
        throw Error.webApplicationException(Status.NOT_FOUND, "The document is not found");
      }

      CloudStorageAccount cloudStorageAccount =
          discoveryDetailsService.getCloudStorageAccount(getTenantId());

      BlobIdentifier blobIdentifier = new BlobIdentifierBuilder()
          .withContainerName(cloudStorageAccount.getContainerName())
          .withStorageAccount(cloudStorageAccount.getStorageAccountName())
          .withBlobName(docsAccblob.getBlobName())
          .build();

      byte[] docsContent = blobStorageService.downloadBlob(blobIdentifier);
      return Response.ok(docsContent, mediaType).build();
    }

    AccDocManager accDocManager;
    accDocManager = getImpl(AccDocManager.class);
    AccDoc accDoc = accDocManager.getAccDocByFileName(
        document.getFileName(),
        document.getPathName());

    if (accDoc == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Document not found");
    }
    return Response.ok(accDoc.getContents(), mediaType).build();
  }

  /**
   * Returns a map of file types by ID.
   *
   * @param manager Documentation Manager object
   * @return A map of file types by ID
   * @throws DataAccessException If there has been a database error.
   */
  private Map<Integer, FileType> fileTypesById(
      DocumentManager manager)
      throws ProtossException {
    return manager.getFileTypes().stream().collect(
        Collectors.toMap(FileType::getTypeId, t -> t));
  }

}
