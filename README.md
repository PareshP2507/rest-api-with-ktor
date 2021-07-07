# rest-api-with-ktor

### Overview
The example show how to create REST api using [Ktor](https://ktor.io) an asynchronous framework by Jetbrains. The example is having two tables `Users` and `Todos`, and one-to-many relationship between them.

Additionally, The example is having [JWT](https://ktor.io/docs/jwt.html#configuring-server-routes) auth mechanism to authenticate REST apis.

### What you'll need?
Since the example uses postgres DB, you need to create a database. Connection mechanism is written inside `intDB()` method of [`Application.kt`](https://github.com/PareshP2507/rest-api-with-ktor/blob/main/todolist-restapi/src/main/kotlin/Application.kt) file.

### Techniques used
- [CIO engine](https://ktor.io/docs/engines.html) - Embeded server
- [JWT](https://ktor.io/docs/jwt.html#configuring-server-routes) - Authentication mechanism
- [Exposed](https://github.com/JetBrains/Exposed) - ORM framework for kotlin
- [hikariCP](https://github.com/brettwooldridge/HikariCP) - A JDBC connection pool
- [PostgresSQL](https://github.com/JetBrains/Exposed/wiki/DataBase-and-DataSource#datasource) - A datasource
