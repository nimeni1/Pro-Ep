package admin.service.endpoint;

import admin.service.model.Car;
import admin.service.model.Driver;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("driver")
public class DriverResource {

    private static final String CONNECTION_STRING = "HELLO";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String check() {
        return "Hello check!";
    }

    @Path("createDriver")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDriverAccount(Driver driver) {
        //MongoClient mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        return Response.accepted().build();
    }

    @Path("createCar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDriverCar(Car car) {
        //MongoClient mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        return Response.accepted().build();
    }

    @Path("updateDriver")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDriverAccount(Driver driver) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        return Response.accepted().build();
    }

    @Path("updateCar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDriverCar(Car driver) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        return Response.accepted().build();
    }

    @Path("deletion/driver")
    @DELETE
    public Response deleteDriverAccount(@QueryParam("id")int id) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        return Response.ok().build();
    }

    @Path("deletion/car")
    @DELETE
    public Response deleteCarAccount(@QueryParam("id")int id) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        return Response.ok().build();
    }

    @Path("driverOverview")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriverOverview(int id) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        return Response.ok().build();
    }

    @Path("payments")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentsOverview() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        return  Response.ok().build();
    }
}
