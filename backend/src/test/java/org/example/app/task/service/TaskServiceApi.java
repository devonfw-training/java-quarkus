package org.example.app.task.service;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.example.app.task.common.TaskItemEto;
import org.example.app.task.common.TaskListCto;
import org.example.app.task.common.TaskListEto;

/**
 * Rest service API to call the service as client.
 */
@Path("/task")
@RegisterRestClient
@SuppressWarnings("javadoc")
public interface TaskServiceApi {

  @POST
  @Path("/list")
  Response saveTask(TaskListEto taskList);

  @GET
  @Path("/list/{id}")
  TaskListEto findTaskList(Long id);

  @GET
  @Path("/list-with-items/{id}")
  TaskListCto findTaskListWithItems(@PathParam("id") Long id);

  @DELETE
  @Path("/list/{id}")
  void deleteTaskList(@PathParam("id") Long id);

  @POST
  @Path("/item")
  Response saveTaskItem(TaskItemEto item);

  @GET
  @Path("/item/{id}")
  TaskItemEto findTaskItem(@PathParam("id") Long id);

  @DELETE
  @Path("/item/{id}")
  void deleteTaskItem(@PathParam("id") Long id);

}
