# reactive_greetings project

## Prerequisites

- Java 11
- Apache Maven 3.6.2+
- Docker

## Starting postgresql DB

```shell script
docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name postgres-quarkus-reactive -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:11.2
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using (precondition is a running DB):
```shell script
./mvnw compile quarkus:dev
```

## Testing the application

Add a new Greeting
```shell script
curl  -ivX POST -w "\n" -H 'Content-type: application/json' -d '{"name": "Bernhard"}' http://localhost:8080/greetings
```

Fetch all present Greetings
```shell script
curl -w "\n" 'http://localhost:8080/greetings'
```
