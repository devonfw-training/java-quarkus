package org.example.app.task.service;

import io.quarkus.runtime.Application;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.example.app.task.logic.*;


import java.util.NoSuchElementException;

@Path("/task")
public class TaskService {

    @Inject
    private UcFindTaskItem ucFindTaskItem;

    @Inject
    private UcFindTaskList ucFindTaskList;

    @Inject
    private UcDeleteTaskItem ucDeleteTaskItem;

    @Inject
    private UcDeleteTaskList ucDeleteTaskList;

    @Inject
    private UcSaveTaskItem ucSaveTaskItem;

    @Inject
    private UcSaveTaskList ucSaveTaskList;

    @GET
    @Path("/item/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieve task item", description = "JSUT SOME DESCRIPTION")
    @APIResponse(responseCode = "200", description = "Task Item!", content = @Content(mediaType
            = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TaskItemEto.class)))
    @APIResponse(responseCode = "404", description = "NOT FOUND!")
    @APIResponse(responseCode = "500", description = "SERVER ERROR!!!!!!!")
    public TaskItemEto fetchTaskItem(@Parameter(description = "Task item id as paramSeter!", required = true, example = "2", schema
            = @Schema(type = SchemaType.INTEGER)) @PathParam("id") Long id) {
        TaskItemEto eto = this.ucFindTaskItem.findById(id);
        if(eto==null) {
            throw new NoSuchElementException();
        }
        return eto;
    }

    @GET
    @Path("/list/{id}")
    public TaskListEto fetchTaskList(@PathParam("id") Long id) {
        TaskListEto eto = this.ucFindTaskList.findById(id);
        if(eto==null) {
            throw new NoSuchElementException();
        }
        return eto;
    }


    @DELETE
    @Path("/item/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteTaskItem(@PathParam("id") Long id) {
        this.ucDeleteTaskItem.deleteById(id);
    }

    @DELETE
    @Path("/list/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteTaskList(@PathParam("id") Long id) {
        this.ucDeleteTaskList.deleteById(id);
    }


    @POST
    @Path("/item")
    @Produces(MediaType.APPLICATION_JSON)
    public void addOrUpdateTaskItem(TaskItemEto taskItemEto) {
        this.ucSaveTaskItem.save(taskItemEto);
    }

    @POST
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public void addOrUpdateTaskList(TaskListEto taskListEto) {
        this.ucSaveTaskList.save(taskListEto);
    }
/**
    @GET
    @Path("/list-with-items/{id}")
    public TaskListCto fetchTaskListWithItems(@PathParam("id") Long id) {
        TaskListCto cto = this.ucFindTaskList.findTaskListWithItems(id);
        if(cto==null) {
            throw new NoSuchElementException();
        }
        return cto;
    }

    */




}
