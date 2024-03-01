
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.office.OfficeManager;
import com.qhrtech.emr.accuro.api.security.UserAuthenticationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.models.dto.OfficeDto;
import com.qhrtech.emr.restapi.models.endpoints.Details;
import com.qhrtech.emr.restapi.security.AccuroUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Component;

/**
 * <p>
 * This endpoint collection is designed to expose a user's <code>Details</code>.
 * </p>
 * <p>
 * Requires provider or patient level authorization. This end point will return nothing if
 * authenticated with a client_credentials grant (as there is no authenticated user).
 * </p>
 * <p>
 * Any end points in this collection are purely for testing purposes, and may contain breaking
 * changes between any API versions.
 * </p>
 */
@Component
@Path("/v1/details")
@Tag(name = "Details Endpoints",
    description = "Exposes details endpoints")
public class DetailsEndpoint extends AbstractEndpoint {

  /**
   * Retrieves information about the currently authenticated user.
   *
   * @return The Details model object
   *
   * @throws DataAccessException If there has been a database error.
   *
   * @HTTP 200 Authenticated as patient or provider
   * @HTTP 204 Authenticated as client
   * @HTTP 401 Unauthenticated
   */
  @GET
  @Operation(
      summary = "Retrieves user details",
      description = "Retrieves information about the currently authenticated user.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Authenticated as client",
              content = @Content(
                  schema = @Schema(
                      implementation = Details.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Details getDetails() throws ProtossException {

    Details details = new Details();

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // User details and OAuth 2.0 authentication are expected
    if (!(auth.getPrincipal() instanceof UserDetails)
        || !(auth instanceof OAuth2Authentication)) {
      return null;
    }
    UserDetails userDetails = (UserDetails) auth.getPrincipal();
    OAuth2Authentication oauth = (OAuth2Authentication) auth;

    // Fill details with the OAuth related information
    OAuth2Request clientAuthentication = oauth.getOAuth2Request();
    Set<String> scopes = clientAuthentication.getScope();
    details.setScopes(scopes);
    details.setGrantType(clientAuthentication.getGrantType());

    // Set username
    details.setUserName(userDetails.getUsername());

    OfficeManager officeManager = getImpl(OfficeManager.class);

    // Handle additional details of an Accuro user
    if (userDetails instanceof AccuroUserDetails) {
      AccuroUserDetails accuroUserDetails = (AccuroUserDetails) userDetails;
      UserAuthenticationManager userAuthenticationInterface =
          getImpl(UserAuthenticationManager.class);
      Set<Integer> officeIds =
          userAuthenticationInterface.getOfficeIds(accuroUserDetails.getUserId());

      Set<OfficeDto> offices =
          mapDto(officeManager.getOfficesById(officeIds), OfficeDto.class, HashSet::new);

      List<String> officeNames =
          offices.stream().map(OfficeDto::getName).collect(Collectors.toList());
      details.setOfficeNames(officeNames);
    }

    // Extract the current office if it has been set in the authentication token
    Serializable officeExtension = clientAuthentication.getExtensions().get("office");
    if (officeExtension instanceof Integer) {
      int officeId = (int) officeExtension;
      OfficeDto office = mapDto(officeManager.getOfficeById(officeId), OfficeDto.class);
      details.setCurrentOffice(office);
    }
    return details;
  }
}
