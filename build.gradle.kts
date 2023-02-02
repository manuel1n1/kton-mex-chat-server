val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val hikariVersion: String by project
val postgresVersion: String by project
val flywayVersion: String by project
val jbcryptVersion: String by project
val koinVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.8.0"
    id("io.ktor.plugin") version "2.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id("org.flywaydb.flyway") version "9.11.0"
}

group = "com.manuel1n1"
version = "0.0.1"
application {
    mainClass.set("com.manuel1n1.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    //Logger
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Koin for Ktor
    implementation ("io.insert-koin:koin-ktor:$koinVersion")
    // SLF4J Logger
    implementation ("io.insert-koin:koin-logger-slf4j:$koinVersion")

    //Hikari
    implementation("com.zaxxer:HikariCP:$hikariVersion")

    //Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")

    //FlywayDB
    implementation("org.flywaydb:flyway-core:$flywayVersion")

    //Password encryption
    implementation("org.mindrot:jbcrypt:$jbcryptVersion")

    //PostgreSQL
    implementation("org.postgresql:postgresql:$postgresVersion")

    //Test
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}