package ch.abraxas.recruting.reactivegreetings;

import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;
import java.util.Arrays;

@ApplicationScoped
public class DbInit {

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        initdb();
    }

    @Transactional
    private void initdb() {
        Arrays.asList(
                "Kiwi",
                "Durian",
                "Pomelo",
                "Lychee",
                "Kiwi1",
                "Durian1",
                "Pomelo1",
                "Lychee1",
                "Kiwi2",
                "Durian2",
                "Pomelo2",
                "Lychee2"
        ).forEach(s -> {
            Greeting g = new Greeting();
            g.setName(s);
            g.persist();
        });
    }
}