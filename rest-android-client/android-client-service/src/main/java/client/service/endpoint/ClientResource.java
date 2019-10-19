package client.service.endpoint;

import client.service.model.Client;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("client")
public class ClientResource {

    private static final String CONNECTION_STRING = "HELLO";
    private static MongoClient mongoClient;
    public ClientResource() {
            //connection to database
           mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://MihaiMotpan:proepproject@proepproject-l768t.mongodb.net/test?retryWrites=true"));
        try {
            mongoClient.getAddress();
            System.out.println("merge frate");
            System.out.println( logIn("testclient", "testclient"));
        } catch (Exception e) {
            System.out.println("Mongo is down");
            mongoClient.close();
            return;
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String welcome() {
        return "Welcome!";
    }

    @Path("logIn")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response logIn(@FormParam("email") String email, @FormParam("pass") String password) {

        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        query.append("password",password);
        DB db = mongoClient.getDB("ProEPProject");
        DBCollection cll = db.getCollection("clients");
        if (cll.find(query).hasNext()) return Response.ok().build();
            else return Response.serverError().build();


        //if (db.getCollection("client").find() ) { return Response.ok().build();}
        //    else {return Response.serverError().build();}
    }

    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(Client client) {
        if (client != null)
            return Response.noContent().build();
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
