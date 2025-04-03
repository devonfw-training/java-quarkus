package org.example.app.task.service;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.example.app.general.common.security.PermissionService;

import static org.example.app.general.common.security.ApplicationAccessControlConfig.*;


@Path("/task")
@Authenticated
public class TaskService {

  @Inject
  private PermissionService permissionService;

  /**
   * @return response
   */
  @GET
  @Path("/lists")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Fetch task lists", description = "Fetch all task lists")
  @APIResponse(responseCode = "200", description = "Task lists", content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @APIResponse(responseCode = "404", description = "Task lists not found")
  @APIResponse(responseCode = "500", description = "Server unavailable or a server-side error occurred")
  public Response findTaskLists() {
    Response permissionResponse = permissionService.checkPermission(PERMISSION_FIND_TASK_LIST);
    if (permissionResponse != null) {
      return permissionResponse;
    }
    return Response.ok().build();
  }
}
