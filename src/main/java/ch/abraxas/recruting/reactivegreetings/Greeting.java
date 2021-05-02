package ch.abraxas.recruting.reactivegreetings;

import io.vertx.mutiny.sqlclient.Row;

import javax.validation.constraints.NotBlank;

public class Greeting {

    private Long id;

    @NotBlank
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Greeting(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Greeting from(Row row) {
        return new Greeting(row.getLong("id"), row.getString("name"));
    }
}