package com.cloudyengineering.actions;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/api/action")
public class ActionResource {

    @Inject
    ActionService service;

    @POST
    @Consumes("application/json")
    @Produces(value = {"application/json"})
    public Response createAction(@QueryParam("action_name") String name) {
        if (name == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Long actionId = this.service.createAction(name);
        return Response.created(URI.create(String.format("/api/action/%d", actionId))).build();
    }

    @GET
    @Produces("application/json")
    @Path("/{actionId}")
    public Response getActionById(@PathParam("actionId") Long actionId) {
        ActionMessage message = this.service.getActionById(actionId);

        if(message == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(message).build();
    }
}
