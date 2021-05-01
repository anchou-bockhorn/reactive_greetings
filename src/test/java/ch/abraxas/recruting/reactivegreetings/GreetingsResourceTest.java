package ch.abraxas.recruting.reactivegreetings;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class GreetingsResourceTest {

    @InjectMock
    GreetingDao greetingDao;

    @Test
    public void testGetGreetings() {
        Stream<Greeting> tenGreetings = Stream.of("Hans", "Peter", "Ueli")
                .map(n -> new Greeting(0L, n));

        when(greetingDao.getGreetings(10, 0))
                .thenReturn(Multi.createFrom().items(tenGreetings));

        given()
                .when().get("/greetings")
                .then()
                .statusCode(200)
                .body(is("[{\"id\":0,\"name\":\"Hans\"},{\"id\":0,\"name\":\"Peter\"},{\"id\":0,\"name\":\"Ueli\"}]"));
    }

    @Test
    public void testGetGreetingsOffsetOutOfBounds() {
        given()
                .when().get("/greetings?offset=-1")
                .then()
                .statusCode(400);
        verifyNoInteractions(greetingDao);
    }

    @Test
    public void testGetGreetingsLimitOutOfBounds() {
        given()
                .when().get("/greetings?limit=0")
                .then()
                .statusCode(400);
        verifyNoInteractions(greetingDao);
    }

    @Test
    public void testSave() {
        Greeting greetHans = new Greeting(11L, "Hans");
        when(greetingDao.save(greetHans))
                .thenReturn(Uni.createFrom().item(() -> 11L));

        given().body("{\"name\":\"Hans\"}")
                .contentType("application/json")
                .when()
                .post("/greetings")
                .then()
                .statusCode(201)
                .body(is(""));

        verify(greetingDao, Mockito.times(1)).save(any(Greeting.class));
        verifyNoMoreInteractions(greetingDao);
    }
}