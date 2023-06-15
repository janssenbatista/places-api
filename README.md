

<h1 style="align-items: center">
    Places API
</h1>
<div style="display: flex; justify-content: center">
<img src="https://img.shields.io/badge/spring--boot-v3.1.0-green" alt="Spring Boot version 3.0.1" /><img src="https://img.shields.io/badge/postgresql-v14-blue" alt="Postgresql version 14"/></div>

## **[Clickbus](https://www.clickbus.com.br/)** company **[back-end code challenge](https://github.com/RocketBus/quero-ser-clickbus/tree/master/testes/backend-developer)** resolution.



#### 🔨 Execute the application in a Docker Container

**Requirements**:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

Create the application's *.jar* file with the following command:

```bash
./gradlew clean build
```

Then upload the application and database containers with the following command:

```bash
docker compose up -d
```

### 📕 API Endpoints

POST [`/places`]() (create a place)

GET [`/places/{id}`]() (get a place by id)

GET [`/places[?name="query"]`]() (get all places or places filtered by containing name)

PUT [`/places/{id}`]() (update a place) 

DELETE [`/places/{id}`]() (delete a place by id)



❗To see details of all endpoints run the application and open the [Swagger UI](https://swagger.io/tools/swagger-ui/) documentation

url: **http://{host}:[port]/swagger-ui.html**