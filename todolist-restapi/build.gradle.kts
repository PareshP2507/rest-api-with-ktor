plugins {
    application
}

application {
    mainClassName = "io.ktor.server.cio.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    val ktorVersion: String by project

    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")

    // JWT auth
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")

    val exposedVersion = "0.32.1"
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    val hikariCP = "3.4.5"
    val postgres = "42.2.1"
    implementation("com.zaxxer:HikariCP:$hikariCP") // JDBC Connection Pool
    implementation("org.postgresql:postgresql:$postgres") // JDBC Connector for PostgreSQL

    val koinVersion= "3.1.2"
    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    // SLF4J Logger
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")
}


