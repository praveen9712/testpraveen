
package com.qhrtech.emr.restapi.endpoints.tasks;

import static com.qhrtech.emr.restapi.config.serialization.AccuroObjectMapperFactory.newJsonObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.qhrtech.emr.accuro.api.security.UserAuthenticationManager;
import com.qhrtech.emr.accuro.api.tasks.TaskManager;
import com.qhrtech.emr.accuro.api.tasks.TaskReasonManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.tasks.Task;
import com.qhrtech.emr.accuro.model.tasks.TaskReason;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.tasks.TaskReasonDto;
import com.qhrtech.emr.restapi.models.dto.tasks.UserTaskDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.util.PATCH;
import com.qhrtech.emr.restapi.validators.CheckNull;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


/**
 * This <code>UserTaskEndpoint</code> collection is designed to expose assigned tasks. These are
 * tasks which are either assigned to, or created by the current logged in user or a role group they
 * are in.
 *
 * @RequestHeader Authorization Provider Credentials bearer token.
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 */
@Component
@Path("/v1/provider-portal/tasks")
@Facet("provider-portal")
@Tag(name = "User Task Endpoints", description = "Exposes assigned tasks Endpoints.")
public class UserTaskEndpoint extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;

  /**
   * Retrieve all Tasks for logged in user.
   *
   * The logged in user can only access the tasks if the user is assigned to OR created by OR has
   * access to role which the task assigned to for the respective office(assigned to).
   *
   * Except createdStartDate and createdEndDate all the filters are optional and if provided, they
   * will be combined with AND operator on top of the user accessible tasks. Difference between
   * createdStartDate and createdEndDate should not be more than 30 days.
   *
   * @param patientId The patient id.
   * @param reason reason provided to the task.
   * @param taskDueDate due date for the task.
   * @param createdStartDate created on start date.
   * @param createdEndDate created on end date.
   * @param startingId the starting id to search from.
   * @param pageSize page size of the results.
   * @return A list of all tasks descending ordered by id.
   * @throws DataAccessException If there has been a database error.
   */
  @Operation(
      summary = "Retrieves all tasks for the logged in user",
      description = "Gets all tasks for the logged in user."
          + "The logged in user can only access"
          + " the tasks assigned to the user, "
          + " OR created by the user, OR the user has the roles"
          + " the tasks assigned to for the office(assigned to)."
          + " Except createdStartDate and createdEndDate "
          + " all other filters are optional and if provided,"
          + " they will be combined with AND operator on top of"
          + " the user accessible tasks."
          + " Difference between createdStartDate and createdEndDate"
          + " should not be more than 30 days.",
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
              name = "patientId",
              description = "The patient id. If this field is provided, "
                  + "the user tasks will be filtered by patient id.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "reason",
              description = "The task reason. If this field is provided, "
                  + "the user tasks will be filtered by the given reason.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "String")),
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
              required = true,
              description = "The created startDate. This field is mandatory, "
                  + "the user tasks will be filtered by createdStartDate.",
              in = ParameterIn.QUERY,
              example = "2020-10-30"),
          @Parameter(
              name = "createdEndDate",
              required = true,
              description = "The created endDate.This field is mandatory,"
                  + "the user tasks will be filtered by createdEndDate. "
                  + "The max date allowed is '9999-12-30' ",
              in = ParameterIn.QUERY,
              example = "2020-10-30"),
          @Parameter(
              name = "startingId",
              description = "This id is **EnvelopeDto.lastId** of the previous page(request)"
                  + "It is same as the **taskId** of the last records of the previous results. "
                  + "The max date allowed is '9999-12-30' ",
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
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Task.read' )")
  public EnvelopeDto<UserTaskDto> getTasks(
      @Parameter(hidden = true) @QueryParam("patientId") Integer patientId,
      @Parameter(hidden = true) @QueryParam("reason") String reason,
      @Parameter(hidden = true) @QueryParam("includeCompleted") boolean includeCompleted,
      @Parameter(hidden = true) @QueryParam("taskDueDate") String taskDueDate,
      @Parameter(hidden = true) @QueryParam("createdStartDate") String createdStartDate,
      @Parameter(hidden = true) @QueryParam("createdEndDate") String createdEndDate,
      @Parameter(hidden = true) @QueryParam("startingId") Integer startingId,
      @Parameter(hidden = true) @QueryParam("pageSize") Integer pageSize) throws ProtossException {

    validateUserForOffice();
    if (createdEndDate == null || createdStartDate == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "createdStartDate and createdEndDate must be provided");
    }
    LocalDate startDate = dateParser(createdStartDate, "createdStartDate");
    LocalDate endDate = dateParser(createdEndDate, "createdEndDate");

    if (endDate != null && startDate != null && startDate.isAfter(endDate)) {
      LocalDate temp = startDate;
      startDate = endDate;
      endDate = temp;
    }
    int diff = Days.daysBetween(startDate, endDate).getDays();
    if (diff > 30) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Difference between createdStartDate and createdEndDate should not be more than 30 days");
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
        userTaskManager.getTasksForUser(getUser(), patientId, reason, includeCompleted, dueDate,
            startDate, endDate, startingId, pageSize);

    EnvelopeDto<UserTaskDto> envelope = new EnvelopeDto<>();

    envelope.setContents(mapDto(userTasks.getContents(), UserTaskDto.class, ArrayList::new));

    envelope.setCount(userTasks.getCount());
    envelope.setTotal(userTasks.getTotal());
    envelope.setLastId(userTasks.getLastId());
    return envelope;
  }

  /**
   * Retrieve a user task by task id.
   *
   * @param taskId User Task id.
   * @return A User task
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the user task id does not exist on the server.
   */
  @Operation(
      summary = "Retrieves a user task by task id",
      description = "Gets a user task by task id."
          + "The logged in user can only access"
          + " the tasks assigned to the user, OR created by the user, OR the user has the roles"
          + " the tasks assigned to for the office(assigned to).",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "If the task does not exist"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = UserTaskDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider credential bearer token",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/{taskId}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Task.read' )")
  public UserTaskDto getTaskById(
      @Parameter(description = "task id") @PathParam("taskId") int taskId)
      throws ProtossException {

    validateUserForOffice();

    TaskManager userTaskManager = getImpl(TaskManager.class);
    Task userTask = userTaskManager.getById(taskId, getUser());
    if (userTask == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Task not found.");
    }
    return mapDto(userTask, UserTaskDto.class);
  }

  /**
   * Creates a new user task.
   * <P>
   * Create task requires at least one user or a role to be passed as assignedUserIds or
   * assignedRoleIds fields.
   * </P>
   *
   * <p>
   * The fields createdBy, createdById, and createdDate are not expected as they are set by default
   * corresponding to logged in user and application date respectively.
   * </p>
   * The required fields are: At least one user or role to assign the task, officeIdAssignedTo,
   * officeIdAssignedFrom, reason, and taskDueDate.
   * <p>
   * And the fields related to complete a task which includes completed, completedBy, completedDate
   * are set to default null and are not respected for create as task needs to be created first.
   * </p>
   * <p>
   * The field priority is set to "Normal" if not passed
   * </p>
   *
   * @return task id
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If any required field to create in missing.
   */
  @Operation(
      summary = "Creates a new task",
      description = "Creates a new user task."
          + " Creating task requires at least one user or one role"
          + " to be passed as assignedUserIds or assignedRoleIds field."
          + " The fields of createdBy, createdById,"
          + " and createdDate are not expected as they"
          + " will be set by default corresponding to logged in"
          + " user and the application date."
          + " The required fields are - at least one user or one role"
          + " to assign, officeIdAssignedTo,"
          + " officeIdAssignedFrom, reason and taskDueDate."
          + " The fields related to complete a task which"
          + " include completed, completedBy, completedDate"
          + " will be set to default and are not respected for"
          + " this create endpoint as the task needs to be created first."
          + " The field 'priority' will be set to 'Normal' if not provided.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If any required field "
                  + "is missing or not valid"),
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns task id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @PreAuthorize("#oauth2.hasScope('user/provider.Task.create')")
  public Response createTask(
      @RequestBody(description = "New Task for the user") @Valid UserTaskDto userTask)
      throws ProtossException {

    validateUserForOffice();

    if (userTask == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Task information is missing");
    }
    TaskManager userTaskManager = getImpl(TaskManager.class);

    int id = userTaskManager.create(mapDto(userTask, Task.class), getUser());

    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * Update a user task.
   *
   * <p>
   * The assignedUserIds and assignedRoleIds replace all existing links as an update if passed are
   * different ids(not same as existing). For example: A task is created with assigned to users
   * {1,2,3} If you pass users{2,3,4} as an update then the task is updated to with assigned users
   * as{2,3,4}
   * </p>
   *
   * No change expected to fields createdBy, createdById, and createdDate.
   *
   * And the fields related to complete a task which includes completed, completedBy, completedDate
   * are set to default values corresponding to logged in user and application time respectively.
   *
   * The field priority is set to "Normal" if not passed.
   *
   * @param taskId id
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If any required field to update in missing.
   */
  @Operation(
      summary = "Updates a user task",
      description = "Updates a user task."
          + " The fields of createdBy, createdById, and createdDate should not be changed."
          + " And the fields related to complete a task"
          + " including completed, completedBy,"
          + " completedDate will be set to default values corresponding"
          + " to logged in user and application time."
          + " The field 'priority' is set to 'Normal' if not provided."
          + " The assignedUserIds and assignedRoleIds will replace"
          + " all existing ids if different ids were provided.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If any required field "
                  + "is missing"),
          @ApiResponse(
              responseCode = "204",
              description = "Updated successfully")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PUT
  @Path("/{taskId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @PreAuthorize("#oauth2.hasScope('user/provider.Task.update')")
  public Response updateTask(
      @Parameter(description = "Task id") @PathParam("taskId") Integer taskId,
      @RequestBody(description = "Update task") @Valid UserTaskDto userTask)
      throws ProtossException {

    if (userTask == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Task information is missing");
    }

    validateUserForOffice();

    if (taskId != userTask.getId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Task id in the body doesn't match with the Id in the url");
    }
    TaskManager userTaskManager = getImpl(TaskManager.class);

    Task userTaskProtoss = userTaskManager.getById(userTask.getId(), getUser());

    if (userTaskProtoss == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid task ID.");
    }

    Task userTaskUpdated = mapDto(userTask, Task.class);

    userTaskManager.update(getUpdated(userTaskProtoss, userTaskUpdated), getUser());

    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Update a User task. This endpoint allows partial update, i.e., update only those fields which
   * need to be updated instead of updating entire UserTask object. If there are any invalid fields
   * in the request, they will be ignored and the valid ones will be updated.
   * <P>
   * For update on just task completed field, the related fields completedBy and completedDate are
   * set to defaults. i.e no need to pass the related fields(just pass the complete field) to mark a
   * task complete.
   * </P>
   * <p>
   * Patch on the fields assignedUserIds and assignedRoleIds, behaves same like the PUT where the
   * existing ids are removed and replaced with new ones.
   * </p>
   *
   * @param taskId id
   * @param patch JSON representation of the user task fields which need to be updated or modified.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If any required field to update in missing.
   */
  @Operation(
      summary = "Patches a user task",
      description = "Partial updates a task. This endpoint allows partial update, i.e., update "
          + "only those fields which need to be updated instead of updating entire UserTask "
          + "object. If there are any invalid fields in the request, they will be ignored and "
          + "the valid ones will be updated."
          + " For update just task the field of 'completed', the related fields completedBy and"
          + " completedDate will be set to the default value.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If any required field "
                  + "is missing"),
          @ApiResponse(
              responseCode = "204",
              description = "Updated successfully")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PATCH
  @Path("/{taskId}")
  @Consumes("application/merge-patch+json")
  @PreAuthorize("#oauth2.hasScope('user/provider.Task.update')")
  public Response patchTask(
      @Parameter(description = "Task id") @PathParam("taskId") Integer taskId,
      @RequestBody(description = "Updated task fields") JsonMergePatch patch)
      throws ProtossException, JsonPatchException, JsonProcessingException {

    if (patch == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patch fields information is missing");
    }

    validateUserForOffice();

    TaskManager userTaskManager = getImpl(TaskManager.class);

    Task userTask = userTaskManager.getById(taskId, getUser());
    if (userTask == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid task ID.");
    }

    UserTaskDto userTaskDto = mapDto(userTask, UserTaskDto.class);

    final ObjectMapper objectMapper = newJsonObjectMapper();
    JsonNode patched = patch.apply(objectMapper.convertValue(userTaskDto, JsonNode.class));
    UserTaskDto userTaskPatchedDto = objectMapper.treeToValue(patched, UserTaskDto.class);

    if (userTaskPatchedDto.getId() != taskId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          " Id is not expected in the body. "
              + "And task id in url does not match the id "
              + " provided in the body.");
    }

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<UserTaskDto>> violations =
        validator.validate(userTaskPatchedDto);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException("Request Validation Exception", violations);
    }

    Task userTaskUpdated = mapDto(userTaskPatchedDto, Task.class);

    userTaskManager.update(getUpdated(userTask, userTaskUpdated), getUser());

    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Delete a task.
   *
   * @param taskId id
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If any required field to update in missing.
   */
  @Operation(
      summary = "Deletes a user task",
      description = "Deletes a user task.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "If the resource is not found "),
          @ApiResponse(
              responseCode = "204",
              description = "Deleted successfully")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @DELETE
  @Path("/{taskId}")
  @PreAuthorize("#oauth2.hasScope('user/provider.Task.delete')")
  public Response deleteTask(
      @Parameter(description = "Task id") @PathParam("taskId") Integer taskId)
      throws ProtossException {

    validateUserForOffice();

    TaskManager userTaskManager = getImpl(TaskManager.class);

    userTaskManager.delete(taskId, getUser());
    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Add users to a task.
   *
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If any required field to create in missing.
   * @HTTP 409 If any of the userIds passed is already an assigned user to a task.
   */
  @Operation(
      summary = "Adds users to the task",
      description = "Assigns users to the task.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "If the resource is not found"),
          @ApiResponse(
              responseCode = "409",
              description = "If try to add an already assigned user"),
          @ApiResponse(
              responseCode = "204",
              description = "Successfully added users")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @POST
  @Path("/{taskId}/users")
  @Consumes(MediaType.APPLICATION_JSON)
  @PreAuthorize("#oauth2.hasScope('user/provider.Task.update')")
  public Response addUsersToTask(
      @Parameter(description = "Task id") @PathParam("taskId") Integer taskId,
      @RequestBody(
          description = "UserIds to assign to a task") @Valid Set<@CheckNull Integer> userIds)
      throws ProtossException {

    validateUserForOffice();

    if (CollectionUtils.isEmpty(userIds)) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "UserIds are missing");
    }

    TaskManager userTaskManager = getImpl(TaskManager.class);

    userTaskManager.addTaskUsers(taskId, userIds, getUser());

    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Add roles to a task.
   *
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If any required field to create in missing.
   * @HTTP 409 If any of the roleIds passed is already an assigned role to a task.
   */
  @Operation(
      summary = "Adds roles to the task",
      description = "Assigns roles to the task.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "If the resource is not found"),
          @ApiResponse(
              responseCode = "409",
              description = "If try to add an already assigned role"),
          @ApiResponse(
              responseCode = "204",
              description = "Successfully added roles")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @POST
  @Path("/{taskId}/roles")
  @Consumes(MediaType.APPLICATION_JSON)
  @PreAuthorize("#oauth2.hasScope('user/provider.Task.update')")
  public Response addRolesToTask(
      @Parameter(description = "Task id") @PathParam("taskId") Integer taskId,
      @RequestBody(
          description = "RoleIds to assign to a task") @Valid Set<@CheckNull Integer> roleIds)
      throws ProtossException {

    validateUserForOffice();

    if (CollectionUtils.isEmpty(roleIds)) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "RoleIds are missing");
    }

    TaskManager userTaskManager = getImpl(TaskManager.class);

    userTaskManager.addTaskRoles(taskId, roleIds, getUser());

    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Retrieve all Task reasons.
   *
   * @param officeId the office id is an optional filter. If passed, the results include all reasons
   *        for the specific office and global(accessed by all offices).
   * @return A list of all tasks reasons descending ordered by id.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If any required field to create in missing.
   */
  @Operation(
      summary = "Retrieves task reasons",
      description = "Gets task reasons."
          + " Office id is an optional filter."
          + " If not provided, all reasons for all the offices will be returned.",
      parameters = @Parameter(
          name = "authorization",
          required = true,
          description = "Provider credentials bearer token",
          in = ParameterIn.HEADER),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = TaskReasonDto.class))))})
  @GET
  @Path("/reasons")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Task.read' )")
  public List<TaskReasonDto> getTaskReasonsForOffice(@QueryParam("officeId") Integer officeId)
      throws ProtossException {

    validateUserForOffice();

    TaskReasonManager reasonManager = getImpl(TaskReasonManager.class);
    List<TaskReason> reasons =
        reasonManager.getAll(officeId);
    return mapDto(reasons, TaskReasonDto.class, ArrayList::new);
  }

  private Task getUpdated(Task userTask, Task userTaskUpdated) {

    userTaskUpdated.setRecurUntil(userTask.getRecurUntil());
    userTaskUpdated.setRecurringTimingUnit(userTask.getRecurringTimingUnit());
    userTaskUpdated.setRecurringTimingValue(userTask.getRecurringTimingValue());
    userTaskUpdated.setAttachmentCount(userTask.getAttachmentCount());
    userTaskUpdated.setAttachmentTypes(userTask.getAttachmentTypes());
    userTaskUpdated.setOwnerUserId(userTask.getOwnerUserId());
    userTaskUpdated.setLegacyRead(userTask.isLegacyRead());
    userTaskUpdated.setDefinitionId(userTask.getDefinitionId());
    userTaskUpdated.setAlertMatchId(userTask.getAlertMatchId());
    userTaskUpdated.setCheckoutDate(userTask.getCheckoutDate());
    userTaskUpdated.setAlertDetails(userTask.getAlertDetails());

    return userTaskUpdated;
  }

  private void validateUserForOffice() throws DatabaseInteractionException {
    if (getUser().getUserId() == null) {
      return;
    }
    UserAuthenticationManager authManager = getImpl(UserAuthenticationManager.class);

    if (authManager.getOfficeIds(getUser().getUserId()).isEmpty()) {
      throw Error.webApplicationException(Status.FORBIDDEN,
          "User must have access to at least one office");
    }
  }
}
