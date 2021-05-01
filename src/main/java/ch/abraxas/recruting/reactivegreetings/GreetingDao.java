package ch.abraxas.recruting.reactivegreetings;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class GreetingDao {

    private final PgPool dbClient;

    public GreetingDao(PgPool dbClient) {
        this.dbClient = dbClient;
    }

    public Multi<Greeting> getGreetings(int limit, int offset) {
        return dbClient
                .preparedQuery("SELECT id, name FROM Greetings ORDER BY id ASC LIMIT $1 OFFSET $2")
                .execute(Tuple.of(limit, offset))
                .onItem()
                .transformToMulti(set ->
                        Multi.createFrom().items(() -> StreamSupport.stream(set.spliterator(), false))
                ).onItem().transform(Greeting::from);
    }

    public Uni<Long> save(Greeting greeting) {
        return dbClient.preparedQuery("INSERT INTO Greetings (name) VALUES ($1) RETURNING (id)")
                .execute(Tuple.of(greeting.getName()))
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }
}