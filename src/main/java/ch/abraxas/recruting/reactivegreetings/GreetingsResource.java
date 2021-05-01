package ch.abraxas.recruting.reactivegreetings;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/greetings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GreetingsResource {

    @GET
    public Uni<PanacheQuery<PanacheEntityBase>> getFirstGreetings(@DefaultValue("10") @QueryParam("limit") int limit,
                                                                  @DefaultValue("0") @QueryParam("offset") int offset) {
        return Uni.createFrom().item(Greeting::findAll);
    }

    @POST
    @Transactional
    public Uni<Response> create(String greeting) {
        return Uni.createFrom()
                .item(() -> {
                    Greeting g = new Greeting();
                    g.setName(greeting);
                    g.persist();
                    return greeting;
                })
                .onItem().transform(id -> URI.create("/greetings/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }
}