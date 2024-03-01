
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.security.AccuroApiContextManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;
import com.qhrtech.emr.accuro.permissions.FeatureType;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeMapDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ExternalPatientDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/security/users")
@Tag(name = "Feature access",
    description = "Exposes feature access endpoint")
public class FeatureAccessEndpoint extends AbstractEndpoint {

  private static final Integer DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;

  /**
   * Retrieves all the features accessible to the user in all the offices. Permissions checks are
   * done if the data is requested for the user other than the logged in user. The results are
   * provided in a paginated form. Last id is the {@code officeId} of the last record of the current
   * page, and results will be ordered by this field. startingId by default is 0 or its value can be
   * set as {@code EnvelopeDto.lastId} of the previous page to request next set of records after
   * this id. {@code EnvelopeDto.count} is the total records returned in current page.
   * {@code EnvelopeDto.total} is the total records that can be returned irrespective of number of
   * pages.
   *
   * @param pageSize The size of the pages with default value 25 and maximum value 50.
   *        <p>
   *        If the page size is not provided or less than 1, the page size will be set to default
   *        value 25.
   *        </p>
   *        <p>
   *        If page size provided is more than maximum value, the page size will be set to the
   *        default maximum value 50.
   *        </p>
   * @param startingId startingId by default is 0 or its value can be set as
   *        {@code EnvelopeDto.lastId} of the previous page to request next set of records after
   *        this id.
   */
  @GET
  @Path("/{userId}/features")
  @Operation(
      summary = "Retrieves all the features accessible to the user in all the offices.",
      description = "Retrieves all the features accessible to the user in all the offices."
          + " Permissions checks are done if the data is requested for the user other than the"
          + " logged in user. The results are provided in a paginated form. Last id is the"
          + " **officeId** of the last record of  the current page, and results will be"
          + " ordered by this field. startingId by default is 0 or "
          + "its value can be set as **Envelope.lastId** of the previous page to request"
          + "next set of records after this id. **Envelope.count**"
          + " is the total records returned "
          + "in current page. **Envelope.total** is the total records that can be returned "
          + "irrespective of number of pages",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success. The content of Envelope contains the Map. The Map"
                  + "of key as an officeId and value as a collection of all the features.",
              content = @Content(
                  schema = @Schema(implementation = EnvelopeDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "pageSize",
              description = "The size of the pages with default and maximum value 50."
                  + " If the page size is not provided or less than 1, "
                  + " the page size will be set to default value 50."
                  + " If page size provided is more than maximum value, "
                  + " the page size will be set to the default maximum value 50.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "startingId",
              description = "This id is the last office id of the previous page(request)",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "userId",
              description = "User id for which feature access is requested",
              in = ParameterIn.PATH)
      })
  public EnvelopeMapDto<Integer, Set<FeatureType>> getFeatureAccess(
      @Parameter(hidden = true) @PathParam("userId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "userId field can only have numbers") String userId,
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

    Integer actualStartingId = StringUtils.isBlank(startingId) ? null
        : Integer.parseInt(startingId);

    Integer loggedInUserId = Integer.parseInt(userId);
    AccuroApiContext accuroApiContext = getAccuroApiContext();
    AccuroUser currentUser = accuroApiContext.getAccuroUser();

    if (currentUser == null) {
      throw Error.webApplicationException(Status.FORBIDDEN,
          "Insufficient permissions to view users features");
    }

    Map<Integer, Set<FeatureType>> featureAccess = new HashMap<>();

    if (currentUser.getUserId() == loggedInUserId) {
      featureAccess = accuroApiContext.getUserPermissions().getFeaturePermissions();

    } else if (currentUser.isSystemAdmin()) {
      AccuroApiContextManager contextManager = getImpl(AccuroApiContextManager.class);
      AccuroApiContext accuroApiContext1 =
          contextManager.getAccuroApiContext(loggedInUserId, false);

      if (accuroApiContext1.getUserPermissions() != null) {
        featureAccess = accuroApiContext1.getUserPermissions().getFeaturePermissions();
      }

    } else {
      throw Error.webApplicationException(Status.FORBIDDEN,
          "Insufficient permissions to view other users features");
    }

    return getPaginatedResult(featureAccess, actualPageSize, actualStartingId);
  }

  /**
   * This method will get the result in paginated form for given feature access map.
   *
   */
  private EnvelopeMapDto getPaginatedResult(
      Map<Integer, Set<FeatureType>> featureAccess, Integer pageSize, Integer startingId) {

    if (featureAccess.size() < 1) {
      return defaultEnvelope();
    }
    TreeMap<Integer, Set<FeatureType>> sorted = new TreeMap<>(featureAccess);
    SortedMap<Integer, Set<FeatureType>> result;
    Set<Integer> keySet = sorted.keySet();
    List<Integer> keyList = new ArrayList<>();
    keyList.addAll(keySet);
    Collections.sort(keyList);


    if (startingId != null) {
      startingId = getFloorValue(startingId, keyList);
      int lastKey = keyList.indexOf(startingId) + pageSize;
      int endingKey = lastKey > keyList.size() - 1 ? keyList.size() - 1 : lastKey;
      result = sorted.subMap(startingId, false, keyList.get(endingKey), true);
    } else {
      int endingKey = pageSize > keyList.size() ? keyList.size() - 1 : pageSize - 1;
      result = sorted.subMap(keyList.get(0), true, keyList.get(endingKey), true);
    }


    EnvelopeMapDto envelopeDto = new EnvelopeMapDto();
    envelopeDto.setContents(result);
    envelopeDto.setCount(result.size());
    envelopeDto.setTotal(featureAccess.size());
    Integer lastOfficeId = result.isEmpty() ? 0 : result.lastKey();
    envelopeDto.setLastId(Long.valueOf(lastOfficeId));
    return envelopeDto;
  }

  private EnvelopeMapDto defaultEnvelope() {
    EnvelopeMapDto envelopeMapDto = new EnvelopeMapDto();
    envelopeMapDto.setContents(new TreeMap<>());
    envelopeMapDto.setLastId(0L);
    return envelopeMapDto;
  }

  private int getFloorValue(int startingId, List<Integer> keyList) {

    int floorValue = keyList.indexOf(startingId);
    if (floorValue != -1) {
      return startingId;
    }


    if (keyList.get(0) > startingId) {
      return 0;
    }
    if (keyList.get(keyList.size() - 1) < startingId) {
      return keyList.get(keyList.size() - 1);
    }

    for (int keyIndex = 1; keyIndex < keyList.size(); keyIndex++) {

      if (startingId < keyList.get(keyIndex)) {
        startingId = keyList.get(keyIndex - 1);
        break;
      }
    }

    return startingId;

  }
}
