
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.office.OfficeManager;
import com.qhrtech.emr.accuro.api.security.AccuroUserManager;
import com.qhrtech.emr.accuro.api.tasks.TaskManager;
import com.qhrtech.emr.accuro.model.demographics.Office;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.model.security.AccuroUserSearchInfo;
import com.qhrtech.emr.accuro.model.tasks.Task;
import com.qhrtech.emr.restapi.models.dto.AccuroUserDto;
import com.qhrtech.emr.restapi.models.dto.OfficeDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.tasks.UserTaskDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>UserSecurityEndpoint</code> collection is designed to expose the AccuroUser DTO and
 * related endpoints.
 *
 * @RequestHeader Authorization Client, Provider level authorization grant.
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/users")
@Tag(name = "AccuroUser Endpoints",
    description = "Exposes Accuro user endpoints")
public class UserSecurityEndpoint extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;

  /**
   * Retrieves Accuro user by id.
   *
   * @return The AccuroUser model object
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/{id}")
  @PreAuthorize(" #oauth2.hasScope( 'user/provider.User.read' )")
  @Operation(
      summary = "Retrieves Accuro user by id",
      description = "Retrieves Accuro user by the given id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = AccuroUserDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client, provider level authorization grant.",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "id",
              description = "The accuro user id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  public AccuroUserDto getAccuroUserById(@Parameter(hidden = true) @PathParam("id") int id)
      throws ProtossException {
    AccuroUserManager userManager = getImpl(AccuroUserManager.class);

    return mapDto(userManager.getAccuroUser(id), AccuroUserDto.class);
  }

  /**
   * Search Accuro users which meet the specified filters. The results will be provided in a
   * paginated form. Set the startingId to the {@code EnvelopeDto.lastId} of the previous page to
   * request the next page. Last id is the {@code AccuroUser} id of the last record of the page, and
   * results will be ordered by this field.
   *
   * <p>
   * All of the parameters are optional. At least one parameter is expected. If multiple filters are
   * provided, they will be combined with AND operator. For first name and last name, wild card
   * search starting with the characters passed in the parameter. globalSearch will search for
   * firstNames, LastNames or Usernames containing searched string. When global search field is
   * passed, rest of the fields are ignored. For rest of the parameters fields, it is exact search.
   * </p>
   *
   * @param userName Name of the Accuro user. Optional
   * @param systemId External system Id - External identity system. Has to be passed along with
   *        externalUserId.
   * @param externalUserId External User Id - External User Identifier. Optional.
   * @param firstName First name, requiring minimum 2 characters. Optional.
   * @param lastName Last name, requiring minimum 2 characters. Optional.
   * @param globalSearch min. 2 characters. Will search for firstNames, LastNames or Usernames
   *        containing searched string. All other fields would be ignored if this field is
   *        passed.Optional.
   * @param activeDirectoryUser Active directory user in Accuro. Optional.
   * @param startingId The starting {@code userId} (exclusive) of the next page of data. Typically
   *        this is the {@code EnvelopeDto.lastId} from the last page.
   * @param pageSize The size of the pages with default value 25 and maximum value 50.
   *        <p>
   *        If the page size is not provided or less than 1, the page size will be set to default
   *        value 25.
   *        </p>
   *        <p>
   *        If page size provided is more than maximum value, the page size will be set to the
   *        default maximum value 50.
   *        </p>
   * @return A page of {@link AccuroUserDto}'s
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 400 If provided firstName, lastName is less than minimum characters. And if systemId is
   *       passed alone.
   */
  @GET
  @PreAuthorize(" #oauth2.hasAnyScope( 'user/provider.User.read' )")
  @Operation(
      summary = "Retrieves all Accuro Users which meet the specified filters",
      description = "The results will be provided in a paginated form. "
          + " To request the next page, specify the startingId which is same as "
          + " **EnvelopeDto.lastId** of the previous page."
          + " Last id is the **userId** of the last record of the page, "
          + "and results will be ordered by this field i.e **userId**. "
          + "All the parameters are optional, will return all users if none"
          + " passed. If multiple filters are"
          + " provided, they will be combined with AND operator."
          + " For first name and last name, it's wildcard"
          + " search starting with the characters passed in."
          + " For rest of the fields, it is exact search."
          + " 'globalSearch' will search for firstName, LastName or Username "
          + " which contains the searched string."
          + " When 'globalSearch' is provided, the rest of other fields will be ignored.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If provided firstName or lastName is less than minimum characters,"
                  + " or if systemId is passed alone without externalUserId."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(ref = "EnvelopeDtoAccuroUserDto")))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client, provider level authorization grant.",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "userName",
              description = "Name of the Accuro user. Optional.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "systemId",
              description = "External system Id - External"
                  + " identity system. Has to be passed along" + " with externalUserId.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "externalUserId",
              description = "External User Id - External User Identifier. Optional.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "firstName",
              description = "Size must be between 2 and 100. Optional.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "globalSearch",
              description = "Search for firstName, lastName or username which contains the"
                  + " searched string. When this field is provided, all other fields will be"
                  + " ignored. Minimum 2 characters are required.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "lastName",
              description = "Size must be between 2 and 100. Optional.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "activeDirectoryUser",
              description = "Active directory user in Accuro. Optional.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "startingId",
              description = "This id is **EnvelopeDto.lastId** of the previous page(request)."
                  + "It is the same as the **userId** of the last record of the previous results.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "pageSize",
              description = "The size of the pages with default value 25 and maximum value 50."
                  + " If the page size is not provided or less than 1, "
                  + " the page size will be set to default value 25."
                  + " If page size provided is more than maximum value, "
                  + " the page size will be set to the default maximum value 50.",
              in = ParameterIn.QUERY)
      })
  public EnvelopeDto<AccuroUserDto> searchUser(
      @Parameter(hidden = true) @QueryParam("userName") String userName,
      @Parameter(hidden = true) @QueryParam("systemId") Long systemId,
      @Parameter(hidden = true) @QueryParam("externalUserId") String externalUserId,
      @Parameter(hidden = true) @QueryParam("firstName") @Size(
          min = 2, max = 100,
          message = "firstName field size must be between 2 and 100") String firstName,
      @Parameter(hidden = true) @QueryParam("lastName") @Size(
          min = 2, max = 100,
          message = "lastName field size must be between 2 and 100") String lastName,
      @Parameter(hidden = true) @QueryParam("globalSearch") @Size(
          min = 2,
          message = "globalSearch field should be over 2 characters") String globalSearch,
      @Parameter(hidden = true) @QueryParam("activeDirectoryUser") String activeDirectoryUser,
      @Parameter(hidden = true) @QueryParam("startingId") Integer startingId,
      @Parameter(hidden = true) @QueryParam("pageSize") Integer pageSize)
      throws ProtossException {

    if (pageSize == null || pageSize <= 0) {
      pageSize = DEFAULT_PAGE_SIZE;
    }

    if (pageSize > MAX_PAGE_SIZE) {
      pageSize = MAX_PAGE_SIZE;
    }
    if (Objects.isNull(globalSearch)) {
      if (systemId != null && externalUserId == null) {
        throw Error.webApplicationException(Response.Status.BAD_REQUEST,
            "System Id cannot be searched alone. "
                + "It has to be passed along with the external "
                + "user Id.");
      }
    }

    AccuroUserManager accuroUserManager = getImpl(AccuroUserManager.class);
    AccuroUserSearchInfo accuroUserSearchInfo = new AccuroUserSearchInfo.Builder()
        .setUserName(userName)
        .setExtIdentitySystemId(systemId)
        .setIdentity(externalUserId)
        .setFirstName(firstName)
        .setLastName(lastName)
        .setActiveDirectoryUser(activeDirectoryUser)
        .setStartingId(startingId)
        .setGlobalSearchField(globalSearch)
        .build();
    Envelope<AccuroUser> accuroUsers =
        accuroUserManager.searchAccuroUser(accuroUserSearchInfo, pageSize);

    EnvelopeDto<AccuroUserDto> envelopeDto = new EnvelopeDto<>();

    envelopeDto.setContents(mapDto(accuroUsers.getContents(), AccuroUserDto.class, ArrayList::new));
    envelopeDto.setCount(accuroUsers.getCount());
    envelopeDto.setTotal(accuroUsers.getTotal());
    envelopeDto.setLastId(accuroUsers.getLastId());
    return envelopeDto;

  }

  /**
   * Retrieve all offices the user has access to.
   * <p>
   * The results are provided in a paginated form. Last id is the {@code officeId} of the last
   * record of the current page, and results will be ordered by this field. StartingId by default is
   * 0 or its value can be set as {@code EnvelopeDto.lastId} of the previous page to request next
   * set of records after this id. {@code EnvelopeDto.count} is the total records returned in
   * current page. {@code EnvelopeDto.total} is the total records that can be returned irrespective
   * of number of pages.
   * </p>
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
   * @param startingId startingId by default is 0 or its value can be set as {@code
   * EnvelopeDto.lastId} of the previous page to request next set of records after this id.
   */
  @GET
  @Path("/{userId}/offices")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Office.read')")
  @Operation(
      summary = "Retrieve user accessible offices.",
      description = "Retrieve all offices the user has access to."
          + " Permissions checks are done if the data is requested for the user other than the"
          + " logged in user. The results are provided in a paginated form. Last id is the"
          + " officeId of the last record of  the current page, and results will be"
          + " ordered by this field. StartingId by default is 0 or "
          + "its value can be set as EnvelopeDto.lastId of the previous page to request"
          + "next set of records after this id. EnvelopeDto.count"
          + " is the total records returned "
          + "in current page. EnvelopeDto.total is the total records that can be returned "
          + "irrespective of number of pages",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      ref = "EnvelopeDtoOffices")))})
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
              description = "User id for which offices is requested",
              in = ParameterIn.PATH)
      })
  public EnvelopeDto<OfficeDto> getOfficesForUser(
      @PathParam("userId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "userId field can only have numbers") String userId,
      @QueryParam("pageSize") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "pageSize field can only have numbers") String pageSize,
      @QueryParam("startingId") @Pattern(
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
    OfficeManager officeManager = getImpl(OfficeManager.class);
    Envelope<Office> result =
        officeManager.getOfficesForUser(loggedInUserId, actualStartingId, actualPageSize);
    EnvelopeDto<OfficeDto> envelopeDto = new EnvelopeDto<>();
    envelopeDto
        .setContents(mapDto(result.getContents(), OfficeDto.class, ArrayList::new));
    envelopeDto.setCount(result.getCount());
    envelopeDto.setTotal(result.getTotal());
    envelopeDto.setLastId(result.getLastId());

    return envelopeDto;
  }

  /**
   * Retrieve all Tasks for user id.
   *
   * The user can only access the tasks if the user is assigned to .
   *
   * All the filters are optional and if provided, they will be combined with AND operator on top of
   * the user accessible tasks.
   *
   * @param userId The patient id.
   * @param taskDueDate due date for the task.
   * @param createdStartDate created on start date.
   * @param createdEndDate created on end date.
   * @param startingId the starting id to search from.
   * @param pageSize page size of the results.
   * @return A list of all tasks ascendingly ordered by id.
   * @throws DataAccessException If there has been a database error.
   */
  @Operation(
      summary = "Retrieves all tasks for the user id",
      description = "Gets all tasks for the user id."
          + "The user can access"
          + " the tasks assigned to the user, "
          + " OR created by the user"
          + " all filters are optional and if provided,"
          + " they will be combined with AND operator on top of"
          + " the user accessible tasks. "
          + "Represents a Task Object with a deleted field indicating "
          + "whether the task has been deleted.",

      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = UserTaskDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              required = true,
              description = "Provider credentials bearer token",
              in = ParameterIn.HEADER),
          @Parameter(
              name = "userId",
              description = "The user id., "
                  + "the user id to retreive the tasks assigned to.",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "includeCompleted",
              description = "Include completed tasks or not. Set to TRUE in order to "
                  + "include completed tasks. By default the result will not include "
                  + "completed tasks.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "boolean")),
          @Parameter(
              name = "taskDueDate",
              description = "Task due date. If provided, tasks which are due on "
                  + "that day will be returned.",
              in = ParameterIn.QUERY,
              example = "2020-10-30"),
          @Parameter(
              name = "createdStartDate",
              description = "The created startDate. ",
              in = ParameterIn.QUERY,
              example = "2020-10-30"),
          @Parameter(
              name = "createdEndDate",
              description = "The created endDate.  ",
              in = ParameterIn.QUERY,
              example = "2020-10-30"),
          @Parameter(
              name = "startingId",
              description = "This id is **EnvelopeDto.lastId** of the previous page(request)"
                  + "It is same as the **taskId** of the last records of the previous results.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "pageSize",
              description = "The size of the pages with default value 25 and maximum value 50."
                  + " If the page size is not provided or less than 1, "
                  + " the page size will be set to default value 25."
                  + " If page size provided is more than maximum value, "
                  + " the page size will be set to the default maximum value 50.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer"))
      })
  @GET
  @Path("{userId}/tasks")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.UserTask.read' )")
  public EnvelopeDto<UserTaskDto> getTasks(
      @Parameter(hidden = true) @PathParam("userId") Integer userId,
      @Parameter(hidden = true) @QueryParam("includeCompleted") boolean includeCompleted,
      @Parameter(hidden = true) @QueryParam("taskDueDate") String taskDueDate,
      @Parameter(hidden = true) @QueryParam("createdStartDate") String createdStartDate,
      @Parameter(hidden = true) @QueryParam("createdEndDate") String createdEndDate,
      @Parameter(hidden = true) @QueryParam("startingId") Integer startingId,
      @Parameter(hidden = true) @QueryParam("pageSize") Integer pageSize) throws ProtossException {

    LocalDate startDate =
        createdStartDate == null ? null : dateParser(createdStartDate, "createdStartDate");
    LocalDate endDate =
        createdEndDate == null ? null : dateParser(createdEndDate, "createdEndDate");

    if (endDate != null && startDate != null && startDate.isAfter(endDate)) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "createdStartDate cannot be greater than createdEndDate.");
    }
    if (pageSize == null || pageSize <= 0) {
      pageSize = DEFAULT_PAGE_SIZE;
    }

    if (pageSize > MAX_PAGE_SIZE) {
      pageSize = MAX_PAGE_SIZE;
    }

    LocalDate dueDate = taskDueDate == null ? null : dateParser(taskDueDate, "taskDueDate");
    TaskManager userTaskManager = getImpl(TaskManager.class);
    Envelope<Task> userTasks =
        userTaskManager.getTasksForUser(userId, null, null, includeCompleted, false, dueDate,
            startDate, endDate, startingId, pageSize);

    if (userTasks == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource not found.");
    }

    EnvelopeDto<UserTaskDto> envelope = new EnvelopeDto<>();

    envelope.setContents(mapDto(userTasks.getContents(), UserTaskDto.class, ArrayList::new));

    envelope.setCount(userTasks.getCount());
    envelope.setTotal(userTasks.getTotal());
    envelope.setLastId(userTasks.getLastId());
    return envelope;
  }


  /**
   * Reassign task to new user.
   *
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If any required field to create in missing.
   * @HTTP 409 If any of the userIds passed is already an assigned user to a task.
   */
  @Operation(
      summary = "Reassign the task to new User.",
      description = "Reassign the task to new User.  \n"
          + "* The endpoint would assign the task from source to destination user even when"
          + "the task is deleted.\n"
          + "* If tasks is not linked to the source user, it will be ignored and not assigned to"
          + " the destination user.\n"
          + "* If no action is being taken during assign operation, the endpoint would simply"
          + " return Success.\n"
          + "* Either request body or Query params should be passed.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "If the resource is not found"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameters(
      value = {
          @Parameter(
              name = "existingUserId",
              description = "The source user id whose tasks needs to be assigned ",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"),
              required = true),
          @Parameter(
              name = "newUserId",
              description = "The destination user id to whom tasks needs to be assigned to. ",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "String"),
              required = true),
          @Parameter(
              name = "includeCompleted",
              description = "Include completed tasks or not. Set to true in order to "
                  + "include completed tasks. Possible values are true or false. If value is "
                  + "set to other than true or false, it would be considered false. ",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "boolean")),
          @Parameter(
              name = "taskDueDateStart",
              description = "The task due date start. "
                  + "If only taskDueDateStart is provided, all tasks with due date on and after "
                  + "the value would be selected. If both taskDueDateStart and taskDueDateEnd is "
                  + "provided, all the tasks between taskDueDateStart and taskDueDateEnd would "
                  + "be selected (both dates inclusive)",
              in = ParameterIn.QUERY,
              example = "2020-10-30"),
          @Parameter(
              name = "taskDueDateEnd",
              description = "The task due date end. "
                  + "If only taskDueDateEnd is provided, all tasks with due date on and before "
                  + "the value would be returned. If both taskDueDateStart and taskDueDateEnd is "
                  + "provided, all the tasks between taskDueDateStart and taskDueDateEnd would "
                  + "be selected (both dates inclusive)",
              in = ParameterIn.QUERY,
              example = "2020-10-30"),
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)
      })
  @PUT
  @Path("/{existingUserId}/assign-tasks")
  @Consumes(MediaType.APPLICATION_JSON)
  @PreAuthorize("#oauth2.hasScope( 'user/provider.UserTask.update' )")
  public Response reassignTasks(
      @Parameter(hidden = true) @PathParam("existingUserId") Integer existingUserId,
      @Parameter(hidden = true) @QueryParam("newUserId") Integer newUserId,
      @Parameter(hidden = true) @QueryParam("includeCompleted") Boolean includeCompleted,
      @Parameter(hidden = true) @QueryParam("taskDueDateStart") String startDate,
      @Parameter(hidden = true) @QueryParam("taskDueDateEnd") String endDate,
      @RequestBody(description = "TaskIds to assign to new user") List<Integer> taskIds)
      throws ProtossException {

    if (newUserId == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "newUserId must be provided");
    }

    if (taskIds != null) {
      taskIds.removeAll(Collections.singleton(null));
    } else {
      taskIds = new ArrayList<>();
    }

    if (newUserId.equals(existingUserId)) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "source user and destination user cannot be same");
    }

    if (!taskIds.isEmpty() && (includeCompleted != null || startDate != null
        || endDate != null)) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Conflicting Parameters Detected. "
              + "Please do not include both 'taskIds' and filter parameters "
              + "(e.g., 'startDate', 'endDate', 'includeCompleted') in the same request");

    }

    // if the user is passing tasksIds, set the default value for the includeCompleted
    if (!taskIds.isEmpty()) {
      includeCompleted = true;
    } else {
      if (includeCompleted == null) {
        includeCompleted = false;
      }
    }

    LocalDate start = startDate == null ? null : dateParser(startDate, "startDate");
    LocalDate end = endDate == null ? null : dateParser(endDate, "endDate");

    if (start != null && end != null && start.isAfter(end)) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "taskDueDateStart cannot be greater than taskDueDateEnd.");
    }

    TaskManager userTaskManager = getImpl(TaskManager.class);

    userTaskManager.updateTaskUser(existingUserId, newUserId, taskIds, includeCompleted, start, end,
        getUser());

    return Response.status(Status.NO_CONTENT).build();
  }


}
