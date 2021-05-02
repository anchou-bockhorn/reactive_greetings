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
            initDb();
        }
    }

    private void initDb() {
        client.query("DROP TABLE IF EXISTS Greetings").execute()
                .flatMap(r -> client.query("CREATE TABLE Greetings (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Hans')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Durian')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Ueli')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Peter')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Hans1')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Durian1')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Ueli1')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Peter1')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Hans2')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Durian2')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Ueli2')").execute())
                .flatMap(r -> client.query("INSERT INTO Greetings (name) VALUES ('Peter2')").execute())
                .await().indefinitely();
    }
}