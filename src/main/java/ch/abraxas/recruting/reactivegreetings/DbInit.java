package ch.abraxas.recruting.reactivegreetings;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class DbInit {

    private final PgPool client;
    private final boolean schemaCreate;

    public DbInit(PgPool client, @ConfigProperty(name = "myapp.schema.create", defaultValue = "true") boolean schemaCreate) {
        this.client = client;
        this.schemaCreate = schemaCreate;
    }

    void onStart(@Observes StartupEvent ev) {
        if (schemaCreate) {
            initdb();
        }
    }

    private void initdb() {
        client.query("DROP TABLE IF EXISTS Greetings").execute()
                .flatMap(r -> client.query("CREATE TABLE Greetings (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Kiwi')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Durian')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Pomelo')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Lychee')").execute())
                .await().indefinitely();
    }
}