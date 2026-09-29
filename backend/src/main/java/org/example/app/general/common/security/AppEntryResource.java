package org.example.app.general.common.security;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/app")
public class AppEntryResource {
  @GET
  public Response entry() {
    return Response.ok("OK - logged in").build();
  }
}

