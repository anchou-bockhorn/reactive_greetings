package ch.abraxas.recruting.reactivegreetings;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/greetings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GreetingsResource {

    private final GreetingDao greetingDao;

    public GreetingsResource(GreetingDao greetingDao) {
        this.greetingDao = greetingDao;
    }

    @GET
    public Multi<Greeting> getGreetings(@Min(1) @DefaultValue("10") @QueryParam("limit") int limit,
                                        @Min(0) @DefaultValue("0") @QueryParam("offset") int offset) {
        return greetingDao.getGreetings(limit, offset);
    }

    @POST
    public Uni<Response> create(@Valid Greeting greeting) {
        return greetingDao.save(greeting)
                .onItem().transform(id -> URI.create("/greetings/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }
}