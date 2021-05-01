package ch.abraxas.recruting.reactivegreetings;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import java.util.stream.StreamSupport;

public class Greeting {

    public Long id;
    public String name;

    public Greeting() {
    }

    public Greeting(String name) {
        this.name = name;
    }

    public Greeting(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Multi<Greeting> findAll(PgPool client) {
        return client.query("SELECT id, name FROM Greetings ORDER BY name ASC").execute()
                // Create a Multi from the set of rows:
                .onItem().transformToMulti(set -> Multi.createFrom().items(() -> StreamSupport.stream(set.spliterator(), false)))
                // For each row create a Greeting instance
                .onItem().transform(Greeting::from);
    }

    public static Uni<Greeting> findById(PgPool client, Long id) {
        return client.preparedQuery("SELECT id, name FROM Greetings WHERE id = $1").execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Uni<Long> save(PgPool client) {
        return client.preparedQuery("INSERT INTO Greetings (name) VALUES ($1) RETURNING (id)").execute(Tuple.of(name))
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public Uni<Boolean> update(PgPool client) {
        return client.preparedQuery("UPDATE Greetings SET name = $1 WHERE id = $2").execute(Tuple.of(name, id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static Uni<Boolean> delete(PgPool client, Long id) {
        return client.preparedQuery("DELETE FROM Greetings WHERE id = $1").execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    private static Greeting from(Row row) {
        return new Greeting(row.getLong("id"), row.getString("name"));
    }
}