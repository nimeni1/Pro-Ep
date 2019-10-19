package driver.service.endpoint;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("driver")
public class DriverResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello!";
    }

    @Path("logIn")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response logIn(@FormParam("email") String email, @FormParam("pass") String password) {
        if (email.equals("emil") && password.equals("123"))
            return Response.ok().build();
        else return Response.serverError().build();
    }

    @Path("acceptFare")
    @POST
    public Response acceptFare() {
        return Response.ok().build();
    }

    @Path("createFare")
    @POST
    public Response createFare() {
        return Response.ok().build();
    }
}
