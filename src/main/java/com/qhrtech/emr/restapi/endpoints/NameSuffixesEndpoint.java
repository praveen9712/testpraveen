
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.medicalhistory.SelectionListManager;
import com.qhrtech.emr.accuro.api.provider.ProviderManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.models.dto.ProviderDto;
import com.qhrtech.emr.restapi.models.dto.ProviderTypeDto;
import com.qhrtech.emr.restapi.models.dto.SelectionListName;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * This <code>NameSuffixesEndpoint</code> exposes all the default and custom name suffixes. Requires
 * client, provider, or patient level authorization.
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @see ProviderDto
 * @see ProviderTypeDto
 * @see ProviderManager
 */
@Component
@Path("/v1/enumerations/name-suffixes")
@Tag(name = "Name Suffixes Endpoint",
    description = "Exposes name suffixes endpoint")
public class NameSuffixesEndpoint extends AbstractEndpoint {

  /**
   * Get a list of all name suffixes(built-in and customs).
   *
   * @return Get a list of all name suffixes
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Operation(
      summary = "Retrieves list of all name suffixes",
      description = "Retrieves list of all name suffixes(both built-in and custom).",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = String.class, example = "Sr"))))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<String> getNameSuffixes() throws ProtossException {
    // Add default values:
    List<String> nameSuffixes = new ArrayList<>(Arrays.asList("Sr.", "Jr.", "III", "IV", "V"));

    SelectionListManager selectionListManager = getImpl(SelectionListManager.class);
    List<String> customSuffixes =
        selectionListManager.getSelectionList(SelectionListName.NAME_SUFFIXES.getListName());
    if (CollectionUtils.isNotEmpty(customSuffixes)) {
      nameSuffixes.addAll(customSuffixes);
    }
    return nameSuffixes;
  }

}
