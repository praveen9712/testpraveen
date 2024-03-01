
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.medicalhistory.ContactManager;
import com.qhrtech.emr.accuro.api.medicalhistory.ExternalContactIdentifierManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.medicalhistory.Contact;
import com.qhrtech.emr.accuro.model.medicalhistory.ExternalContactIdentifier;
import com.qhrtech.emr.accuro.model.medicalhistory.ExternalContactSystem;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.restapi.models.dto.AccuroUserDto;
import com.qhrtech.emr.restapi.models.dto.AddressContactType;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ContactDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ExternalContactIdentifierDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.util.PaginationConstant;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code ContactEndpoint} is designed to retrieve contact information for Contacts and
 * Pharmacies.
 * <P>
 * Contact and Pharmacy are part of address book feature in Accuro. The
 * {@link com.qhrtech.emr.restapi.models.dto.AddressContactType} determines whether it is Contact or
 * Pharmacy.
 * </P>
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @HTTP 404 Resource Not Found
 */
@Component
@Path("/v1/contacts")
@Tag(name = "Contact Endpoints",
    description = "Exposes contact endpoints")
public class ContactEndpoint extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();
  private static final int MAX_ALIAS_SIZE = 50;

  /**
   * Get contact information by id.
   *
   * @param contactId Contact id
   * @return A {@link ContactDto} if found.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the contact id does not exist.
   */
  @GET
  @Path("/{contactId}")
  @Operation(
      summary = "Retrieves contact information by id",
      description = "Retrieves contact information for contacts and pharmacies of address book "
          + "in Accuro.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = ContactDto.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public ContactDto getContactById(
      @Parameter(description = "Contact id") @PathParam("contactId") int contactId)
      throws ProtossException {

    ContactManager contactManager = getImpl(ContactManager.class);
    Contact contact = contactManager.getById(contactId);

    return mapDto(contact, ContactDto.class);
  }

  /**
   * Get external contact information by contact id and external contact system: system identifier
   * All timestamps are in UTC
   *
   * @RequestHeader Requires provider level authorization or client credentials with first party
   *                scope
   *
   * @param contactId Contact id
   * @param systemIdentifier string value of the system identifier required
   * @return A {@link ExternalContactIdentifierDto} if found.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the contact id does not exist.
   */
  @GET
  @Path("/{contactId}/external-contact-identifiers/{systemIdentifier}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Contacts.read' ) ")
  @Operation(
      summary = "Retrieves external contact identifiers information by contact id",
      description = "Retrieves a external contact identifier for the specified contact and "
          + "external system. All timestamps are in UTC",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = ExternalContactIdentifierDto.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Client level authorization grant with first party scope, "
          + "or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public ExternalContactIdentifierDto getExternalContactIdentifierByExternalSystem(
      @Parameter(description = "Contact id") @PathParam("contactId") int contactId,
      @Parameter(
          description = "System Identifier") @PathParam("systemIdentifier") String systemIdentifier)
      throws DataAccessException {

    ExternalContactIdentifierManager externalContactIdentifierManager =
        getImpl(ExternalContactIdentifierManager.class);
    ExternalContactIdentifier externalContactIdentifier =
        externalContactIdentifierManager.getExternalContactIdentifier(contactId, systemIdentifier);

    ExternalContactIdentifierDto externalContactIdentifierDto =
        mapDto(externalContactIdentifier, ExternalContactIdentifierDto.class);

    return externalContactIdentifierDto;
  }

  /**
   * Get all external contact identifiers for a contact specified by contact id. All timestamps are
   * in UTC
   *
   * @RequestHeader Requires provider level authorization or client credentials with first party
   *                scope
   *
   * @param contactId Contact id
   * @return A Set of {@link ExternalContactIdentifierDto}. Empty set if none found for contact
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{contactId}/external-contact-identifiers")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Contacts.read' ) ")
  @Operation(
      summary = "Retrieves external contact identifiers for the specified contact id",
      description = "Retrieves a set of external contact identifiers for the specified contact",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = ExternalContactIdentifierDto.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Client level authorization grant with first party scope, "
          + "or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<ExternalContactIdentifierDto> getAllExternalContactIdentifiersForContact(
      @Parameter(description = "Contact id") @PathParam("contactId") int contactId)
      throws ProtossException {

    ExternalContactIdentifierManager externalContactIdentifierManager =
        getImpl(ExternalContactIdentifierManager.class);
    Set<ExternalContactIdentifier> externalContactIdentifierSet =
        externalContactIdentifierManager.getExternalContactIdentifiersByContactId(contactId);

    return mapDto(externalContactIdentifierSet, ExternalContactIdentifierDto.class, HashSet::new);
  }

  /**
   * Saves external contact identifiers associated with the specified contact id.
   *
   * @RequestHeader Requires provider level authorization or client credentials with first party
   *                scope
   *
   * @param contactId The contact Id
   *
   * @return no content in 201(created) response
   *
   * @throws DatabaseInteractionException If there has been a database error.
   * @throws UnsupportedSchemaVersionException If the schema version is unsupported.
   */
  @POST
  @Path("/{contactId}/external-contact-identifiers")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Contacts.create' ) ")
  @Operation(
      summary = "Saves external contact identifiers associated with the specified contact id",
      description = "Saves external contact identifiers associated with the specified contact id",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid contact id"),
          @ApiResponse(
              responseCode = "201",
              description = "Created")
      })
  @Parameter(
      name = "authorization",
      description = "Client level authorization grant with first party scope, "
          + "or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(@PathParam("contactId") int contactId,
      @RequestBody(
          description = "ExternalContactIdentifier") @Valid ExternalContactIdentifierDto identifier)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    if (contactId != identifier.getContactId()) {
      throw new IllegalArgumentException(
          "Contact id in the path does not match with the body.");
    }

    ExternalContactIdentifierManager manager = getImpl(ExternalContactIdentifierManager.class);
    manager.createExternalContactIdentifier(mapDto(identifier, ExternalContactIdentifier.class));
    return Response.status(Status.CREATED).build();
  }

  /**
   * Creates a contact.
   *
   * @RequestHeader Requires provider level authorization or client credentials with first party
   *                scope
   *
   *
   * @return contactId in 201(created) response
   *
   * @throws DatabaseInteractionException If there has been a database error.
   * @throws UnsupportedSchemaVersionException If the schema version is unsupported.
   */
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Contacts.create' ) ")
  @Operation(
      summary = "Creates a contact",
      description = "Creates a contact",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(type = "Integer",
                      example = "1")))
      })
  @Parameter(
      name = "authorization",
      description = "Client level authorization grant with first party scope, "
          + "or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(
      @RequestBody(description = "New contact") @Valid ContactDto contact)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException,
      TimeZoneNotFoundException {
    if (contact == null) {
      throw new IllegalArgumentException(
          "Contact data transfer object is required.");
    }
    if (contact.getAliases() != null) {
      contact.setAliases(validateAlias(contact.getAliases()));
    }

    ContactManager contactManager = getImpl(ContactManager.class);

    Contact protossContact = mapDto(contact, Contact.class);
    int id = contactManager.create(protossContact, getUser());

    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * Get all active contacts which meet the specified filters. The results will be provided in a
   * paginated form. Set the startingId to the {@code EnvelopeDto.lastId} of the previous page to
   * request the next page. Last id is the {@code contactId} of the last record of the page, and
   * results will be ordered by this field.
   *
   * <p>
   * On providing contactName filter, results will be returned containing the contact name " +
   * matching with the beginning of that filter. + For rest of the filters, the results are returned
   * on the basis of exact match. The returned result set will match each of the search terms in an
   * AND type behavior. Providing no search terms will return a page of all contacts with no
   * filters.
   * </p>
   *
   * @param contactType Contact Type {@link AddressContactType}
   * @param contactName Contact name, requiring minimum 2 characters.
   * @param externalContactId The externalContactId which matches a
   *        {@link ExternalContactIdentifierDto#getValue()}
   * @param externalContactSystemId The externalSystemContactId which matches a
   *        {@link ExternalContactSystem#getSystemIdentifier()}
   * @param pageSize The size of the pages with default value 25 and maximum value 50.
   *        <p>
   *        If the page size is not provided or less than 1, the page size will be set to default
   *        value 25.
   *        </p>
   *        <p>
   *        If page size provided is more than maximum value, the page size will be set to the
   *        default maximum value 50.
   *        </p>
   * @param startingId The starting {@code contactId} (exclusive) of the next page of data.
   *        Typically this is the {@code EnvelopeDto.lastId} from the last page.
   * @return A page of {@link ContactDto}'s
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 400 If provided contactName is less than minimum characters.
   */
  @GET
  @Path("/search")
  @Operation(
      summary = "Retrieves all active contacts which meet the specified filters",
      description = "The results will be provided in a paginated form. "
          + " To request the next page, specify the startingId which is the same as "
          + " **EnvelopeDto.lastId** of the previous page."
          + " Last id is the **contactId** of the last record of the page, "
          + "and the results will be sorted by the field of **contactId**. <br /><br />"
          + "On providing contactName filter, results will be returned containing the contact name "
          + "matching with the beginning of that filter. "
          + "For rest of the filters, the results are returned on the basis of exact match."
          + "Providing no search terms will return a page of all contacts with no filters.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If provided contactName is less than minimum characters."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(ref = "EnvelopeDtoContactDto")))})
  @Parameters(
      value = {
          @Parameter(
              name = "contactType",
              description = "Contact type",
              example = "Pharmacy",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "contactName",
              description = "Name of the contact. Size must be between 2 and 200",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "externalContactId ",
              description = "The externalContactId which matches "
                  + "ExternalContactIdentifierDto#getValue()",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "externalContactSystemId",
              description = "The externalSystemContactId which matches an "
                  + "ExternalContactSystem.SystemIdentifier",
              example = "healthcare_directory",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "pageSize",
              description = "The size of the pages with default value 25 and maximum value 50."
                  + " If the page size is not provided or less than 1, "
                  + " the page size will be set to default value 25."
                  + " If page size provided is more than maximum value, "
                  + " the page size will be set to the default maximum value 50.",
              in = ParameterIn.QUERY),
          @Parameter(name = "startingId",
              description = "This id is **EnvelopeDto.lastId** of the previous page(request)"
                  + "It is same as the **contactId** of the last records of the previous results.",
              in = ParameterIn.QUERY)
      })

  public EnvelopeDto<ContactDto> search(
      @QueryParam("contactType") String contactType,
      @QueryParam("contactName") @Size(min = 2, max = 200,
          message = "contactName field size must be between 2 and 200.") String contactName,
      @QueryParam("pageSize") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "pageSize field can only have numbers") String pageSize,
      @QueryParam("startingId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "startingId field can only have numbers") String startingId,
      @QueryParam("externalContactId") @Size(max = 64) String externalContactId,
      @QueryParam("externalContactSystemId") @Size(max = 50) String externalContactSystemId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

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

    ContactManager contactManager = getImpl(ContactManager.class);
    com.qhrtech.emr.accuro.model.medicalhistory.AddressContactType protossAddressType = null;

    // check Address contact type enum and convert to protoss enum
    if (StringUtils.isNotBlank(contactType)) {
      AddressContactType contactTypeDto = AddressContactType.fromString(contactType);
      protossAddressType =
          com.qhrtech.emr.accuro.model.medicalhistory.AddressContactType
              .lookupType(contactTypeDto.getTypeId());
    }

    Envelope<Contact> results = contactManager.search(protossAddressType, contactName,
        externalContactId, externalContactSystemId, actualStartingId, actualPageSize);


    EnvelopeDto<ContactDto> envelopeDto = new EnvelopeDto<>();

    envelopeDto.setContents(mapDto(results.getContents(), ContactDto.class, ArrayList::new));
    envelopeDto.setCount(results.getCount());
    envelopeDto.setTotal(results.getTotal());
    envelopeDto.setLastId(results.getLastId());

    return envelopeDto;
  }

  /**
   * Updates a contact.
   *
   * @RequestHeader Requires provider level authorization or client credentials with first party
   *                scope
   *
   *
   * @return contactId in 201(created) response
   *
   * @throws DatabaseInteractionException If there has been a database error.
   * @throws UnsupportedSchemaVersionException If the schema version is unsupported.
   */
  @PUT
  @Path("/{contactId}")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Hidden
  @Operation(
      summary = "Updates a contact",
      description = "Updates a contact",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = ExternalContactIdentifierDto.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Client level authorization grant with first party scope, "
          + "or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response update(
      @RequestBody @Valid ContactDto contact,
      @PathParam("contactId") Integer contactId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException,
      TimeZoneNotFoundException {
    if (contact == null) {
      throw new IllegalArgumentException(
          "Contact data tranfer object is required.");
    }

    if (contact.getContactId() != contactId) {
      throw new IllegalArgumentException(
          "Contact id in path does not match with the body.");
    }
    if (contact.getAliases() != null) {
      contact.setAliases(validateAlias(contact.getAliases()));
    }

    ContactManager contactManager = getImpl(ContactManager.class);

    Contact protossContact = mapDto(contact, Contact.class);
    contactManager.update(protossContact, getUser());

    return Response.status(Status.OK).build();
  }

  /**
   * Deletes a contact.
   *
   * @RequestHeader Requires provider level authorization or client credentials with first party
   *                scope
   *
   *
   * @return contactId in 200(success) response
   *
   * @throws DatabaseInteractionException If there has been a database error.
   * @throws UnsupportedSchemaVersionException If the schema version is unsupported.
   */
  @DELETE
  @Path("/{contactId}")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Hidden
  @Operation(
      summary = "Deletes a contact",
      description = "Updates a contact",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = ExternalContactIdentifierDto.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Client level authorization grant with first party scope, "
          + "or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response delete(
      @PathParam("contactId") Integer contactId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException,
      TimeZoneNotFoundException, NoDataFoundException {

    ContactManager contactManager = getImpl(ContactManager.class);
    contactManager.delete(contactId, getUser());

    return Response.status(Status.OK).build();
  }

  private List<String> validateAlias(List<String> alias) {
    List<String> filteredList =
        alias.stream().filter(t -> StringUtils.isNotBlank(t)).collect(Collectors.toList());
    if (filteredList.stream().anyMatch(s -> s.length() > MAX_ALIAS_SIZE)) {
      throw new IllegalArgumentException("Alias size cannot exceed "
          + MAX_ALIAS_SIZE + " characters.");
    }
    return filteredList;
  }

}
