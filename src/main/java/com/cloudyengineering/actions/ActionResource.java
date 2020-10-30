package com.cloudyengineering.actions;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/api/action")
public class ActionResource {

    @Inject
    ActionService service;

    @POST
    @Consumes("application/json")
    public Response createAction(@QueryParam("action_name") String name) {
        Long actionId = this.service.createAction(name);
        return Response.created(URI.create(String.format("/api/action/%d", actionId))).build();
    }
}
