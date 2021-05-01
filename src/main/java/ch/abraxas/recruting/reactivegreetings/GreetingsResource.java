package ch.abraxas.recruting.reactivegreetings;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/greetings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GreetingsResource {

    private final PgPool dbClient;

    public GreetingsResource(PgPool dbClient) {
        this.dbClient = dbClient;
    }

    @GET
    public Multi<Greeting> getGreetings() {
        return Greeting.findAll(dbClient);
    }

    @GET
    public Multi<Greeting> getFirstGreetings(@DefaultValue("10") @QueryParam("numRecords") int valInt) {
//        todo: limit by paging
        return Greeting.findAll(dbClient);
    }

    @POST
    public Uni<Response> create(Greeting greeting) {
        return greeting.save(dbClient)
                .onItem().transform(id -> URI.create("/greetings/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }
}