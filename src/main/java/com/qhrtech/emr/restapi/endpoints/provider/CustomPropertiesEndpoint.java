
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.customfield.CustomFieldManager;
import com.qhrtech.emr.accuro.api.medicalhistory.ContactManager;
import com.qhrtech.emr.accuro.model.customfield.CustomField;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.medicalhistory.Contact;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.CustomFieldDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ContactDto;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code CustomPropertiesEndpoint} is designed to retrieve custom fields information.
 *
 * @RequestHeader Authorization provider level authorization grant
 *
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/custom-properties")
@Facet("provider-portal")
@Tag(name = "Custom fields Endpoints",
    description = "Exposes custom fields endpoints")
public class CustomPropertiesEndpoint extends AbstractEndpoint {

  /**
   * Retrieves all the global custom fields and the ones from the offices which user/client can
   * access.
   *
   * @return A list of {@link CustomFieldDto} .
   *
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasScope( 'PATIENT_DEMOGRAPHICS_READ' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' )")
  @Operation(
      summary = "Retrieves accessible custom fields",
      description = "Retrieves all the global custom fields and the ones from the offices "
          + "which user/client can access.",
      responses = {
          @ApiResponse(
              responseCode = "403",
              description = "If user doesn't have the permissions to access to any offices."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = CustomFieldDto.class))))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<CustomFieldDto> getCustomFields()
      throws ProtossException {
    validateUserId();

    CustomFieldManager customFieldManager = getImpl(CustomFieldManager.class);
    List<CustomField> customFields = customFieldManager.getCustomFields(null, true, getUser());

    return mapDto(customFields, CustomFieldDto.class, ArrayList::new);
  }

}
