package org.example.app.task.service;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.example.app.task.common.TaskItemEto;
import org.example.app.task.common.TaskListCto;
import org.example.app.task.common.TaskListEto;
import org.example.app.task.logic.UcAddRandomActivityTaskItem;
import org.example.app.task.logic.UcDeleteTaskItem;
import org.example.app.task.logic.UcDeleteTaskList;
import org.example.app.task.logic.UcFindTaskItem;
import org.example.app.task.logic.UcFindTaskList;
import org.example.app.task.logic.UcSaveTaskItem;
import org.example.app.task.logic.UcSaveTaskList;

import java.net.URI;
import java.util.Map;

/**
 * Rest service for {@link org.example.app.task.common.TaskList}.
 */
@Path("/task")
public class TaskService {

  @Inject
  private UcFindTaskList ucFindTaskList;

  @Inject
  private UcSaveTaskList ucSaveTaskList;

  @Inject
  private UcDeleteTaskList ucDeleteTaskList;

  @Inject
  private UcFindTaskItem ucFindTaskItem;

  @Inject
  private UcSaveTaskItem ucSaveTaskItem;

  @Inject
  private UcDeleteTaskItem ucDeleteTaskItem;

  @Inject
  private UcAddRandomActivityTaskItem ucAddRandomActivityTask;

  /**
   * @param taskList the {@link TaskListEto} to save (insert or update).
   * @return response
   */
  @POST
  @Path("/list")
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Create or update task list", description = "Update a task list or creates a new one if the id is empty.")
  @APIResponse(responseCode = "200", description = "Task list successfully updated")
  @APIResponse(responseCode = "201", description = "Task list successfully created")
  @APIResponse(responseCode = "400", description = "Validation error")
  @APIResponse(responseCode = "500", description = "Server unavailable or a server-side error occurred")
  public Response saveTask(@Valid TaskListEto taskList) {

    Long taskListId = this.ucSaveTaskList.save(taskList);
    if (taskList.getId() == null || taskList.getId() != taskListId) {
      return Response.created(URI.create("/task/list/" + taskListId)).build();
    }
    return Response.ok().build();
  }

  /**
   * @param id the {@link TaskListEto#getId() primary key} of the requested {@link TaskListEto}.
   * @return the {@link TaskListEto} for the given {@code id}.
   */
  @GET
  @Path("/list/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Fetch task list", description = "Fetch a task list")
  @APIResponse(responseCode = "200", description = "Task list", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TaskListEto.class)))
  @APIResponse(responseCode = "404", description = "Task list not found")
  @APIResponse(responseCode = "500", description = "Server unavailable or a server-side error occurred")
  public TaskListEto findTaskList(
      @Parameter(description = "The id of the task list to retrieve", required = true, example = "1", schema = @Schema(type = SchemaType.INTEGER)) @PathParam("id") Long id) {

    TaskListEto task = this.ucFindTaskList.findById(id);
    if (task == null) {
      throw new NotFoundException("TaskList with id " + id + " does not exist.");
    }
    return task;
  }

  /**
   * @param id the {@link TaskListEto#getId() primary key} of the requested {@link TaskListEto}.
   * @return the {@link TaskListEto} for the given {@code id}.
   */
  @GET
  @Path("/list-with-items/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Fetch task list with tasks", description = "Fetch a task list including all of its task items")
  @APIResponse(responseCode = "200", description = "Task list with task items", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TaskListCto.class)))
  @APIResponse(responseCode = "404", description = "Task list not found")
  @APIResponse(responseCode = "500", description = "Server unavailable or a server-side error occurred")
  public TaskListCto findTaskListWithItems(
      @Parameter(description = "The id of the task list to retrieve", required = true, example = "1", schema = @Schema(type = SchemaType.INTEGER)) @PathParam("id") Long id) {

    TaskListCto task = this.ucFindTaskList.findWithItems(id);
    if (task == null) {
      throw new NotFoundException("TaskList with id " + id + " does not exist.");
    }
    return task;
  }

  /**
   * @param id the {@link TaskListEto#getId() primary key} of the {@link TaskListEto} to delete.
   */
  @DELETE
  @Path("/list/{id}")
  @Operation(summary = "Delete task list", description = "Deletes an entire task list")
  @APIResponse(responseCode = "204", description = "Task list deleted")
  @APIResponse(responseCode = "201", description = "Task list successfully created")
  @APIResponse(responseCode = "500", description = "Server unavailable or a server-side error occurred")
  public void deleteTaskList(
      @Parameter(description = "The id of the task list to delete", required = true, example = "1", schema = @Schema(type = SchemaType.INTEGER)) @PathParam("id") Long id) {

    this.ucDeleteTaskList.delete(id);
  }

  /**
   * @param id the {@link TaskListEto#getId() primary key} of the {@link TaskListEto} for which to add a random activity
   *        as a task.
   * @return response
   */
  @POST
  @Path("/list/{id}/random-activity")
  @Operation(summary = "Add random activity", description = "Add a random activity to this task list")
  @APIResponse(responseCode = "201", description = "Task item successfully added")
  @APIResponse(responseCode = "500", description = "Server unavailable or a server-side error occurred")
  public Response addRandomActivity(
      @Parameter(description = "The id of the task list for which to add the task", required = true, example = "1", schema = @Schema(type = SchemaType.INTEGER)) @PathParam("id") Long id) {

    Long taskItemId = this.ucAddRandomActivityTask.addRandom(id);
    return Response.created(URI.create("/task/item/" + taskItemId)).build();
  }

  @POST
  @Path("/list/multiple-random-activities")
  @Consumes(MediaType.TEXT_PLAIN)
  @Operation(summary = "Create new task list with multiple items", description = "Create a new task list with the given name and add multiple random activities to the new list")
  @APIResponse(responseCode = "201", description = "Task list with items successfully created")
  @APIResponse(responseCode = "500", description = "Server unavailable or a server-side error occurred")
  public Response addMultipleRandomActivities(@Schema(required = true, example = "Shopping list", description = "Title of the task list") String listTitle) {

    TaskListEto taskList = new TaskListEto();
    taskList.setTitle(listTitle);

    Long taskListId = this.ucSaveTaskList.save(taskList);
    this.ucAddRandomActivityTask.addMultipleRandom(taskListId);

    return Response.created(URI.create("/task/list/" + taskListId)).build();
  }

  @POST
  @Path("/list/ingredient-list")
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Add all ingredients from recipe to a new task list", description = "Extract all ingredients from the given recipe and add them to a newly created task list")
  @APIResponse(responseCode = "201", description = "Task list with ingredients successfully created")
  @APIResponse(responseCode = "500", description = "Server unavailable or a server-side error occurred")
  public Response addExtractedIngredients(@Schema(required = true, example = """
          {"listTitle": "Shopping list",
           "recipe": "Take flour, sugar and chocolate and mix everything."}""",
          description = "The JSON containing task list title and the recipe") Map<String, String> requestData) {

    String listTitle = requestData.get("listTitle");
    String recipe = requestData.get("recipe");

    if (listTitle == null || listTitle.isBlank() || recipe == null || recipe.isBlank()) {
      return Response.status(Response.Status.BAD_REQUEST)
              .entity("Missing or empty required fields: listTitle, recipe")
              .build();
    }

    TaskListEto taskList = new TaskListEto();
    taskList.setTitle(listTitle);

    Long taskListId = this.ucSaveTaskList.save(taskList);
    this.ucAddRandomActivityTask.addExtractedIngredients(taskListId, recipe);

    return Response.created(URI.create("/task/list/" + taskListId)).build();
  }

  /**
   * @param item the {@link TaskItemEto} to save (insert or update).
   * @return response
   */
  @POST
  @Path("/item")
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Add or update task item", description = "Update a task item or add it as a new item if the id is empty")
  @APIResponse(responseCode = "200", description = "Task successfully updated")
  @APIResponse(responseCode = "201", description = "Task successfully created")
  @APIResponse(responseCode = "400", description = "Validation error")
  @APIResponse(responseCode = "500", description = "Server unavailable or a server-side error occurred")
  public Response saveTaskItem(@Valid TaskItemEto item) {

    Long taskItemId = this.ucSaveTaskItem.save(item);
    if (item.getId() == null || item.getId() != taskItemId) {
      return Response.created(URI.create("/task/item/" + taskItemId)).entity(taskItemId).build();
    }
    return Response.ok(taskItemId).build();
  }

  /**
   * @param id the {@link TaskItemEto#getId() primary key} of the {@link TaskItemEto} to find.
   * @return the {@link TaskItemEto} for the given {@code id}.
   */
  @GET
  @Path("/item/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Fetch task item", description = "Fetch a task item")
  @APIResponse(responseCode = "200", description = "Task item", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TaskItemEto.class)))
  @APIResponse(responseCode = "404", description = "Task item not found")
  @APIResponse(responseCode = "500", description = "Server unavailable or a server-side error occurred")
  public TaskItemEto findTaskItem(
      @Parameter(description = "The id of the task item to retrieve", required = true, example = "1", schema = @Schema(type = SchemaType.INTEGER)) @PathParam("id") Long id) {

    TaskItemEto item = this.ucFindTaskItem.findById(id);
    if (item == null) {
      throw new NotFoundException("TaskItem with id " + id + " does not exist.");
    }
    return item;
  }

  /**
   * @param id the {@link TaskItemEto#getId() primary key} of the {@link TaskItemEto} to delete.
   */
  @DELETE
  @Path("/item/{id}")
  @Operation(summary = "Delete task item", description = "Delete a task item")
  @APIResponse(responseCode = "204", description = "Task list deleted")
  @APIResponse(responseCode = "500", description = "Server unavailable or a server-side error occurred")
  public void deleteTaskItem(
      @Parameter(description = "The id of the task item to delete", required = true, example = "1", schema = @Schema(type = SchemaType.INTEGER)) @PathParam("id") Long id) {

    this.ucDeleteTaskItem.delete(id);
  }

}
