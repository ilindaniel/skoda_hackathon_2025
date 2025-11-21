plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "2.2.21"
    application
}

application {
    mainClass.set("MainKt")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Server
    val ktorVersion = "3.3.2"
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")


    // SQL library
    val exposedVersion = "0.61.0"
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.postgresql:postgresql:42.7.8")

    // xlsx files parsing
    implementation("org.apache.poi:poi-ooxml:5.5.0")

    // logging
    implementation("net.logstash.logback", "logstash-logback-encoder", "8.1")
    implementation("ch.qos.logback", "logback-classic", "1.5.19")
    implementation("io.github.microutils", "kotlin-logging", "2.0.6")

    // testing
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}