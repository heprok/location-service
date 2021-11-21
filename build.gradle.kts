import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    id("org.springframework.boot") version Versions.SPRING_BOOT
    id("io.spring.dependency-management") version Versions.SPRING_DEPENDENCY_MANAGEMENT
    kotlin("jvm") version Versions.KOTLIN
    kotlin("kapt") version Versions.KOTLIN
    kotlin("plugin.spring") version Versions.KOTLIN
}

group = "com.briolink"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = Versions.JAVA

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-autoconfigure-processor:${Versions.SPRING_BOOT}")
    kapt("org.springframework.boot:spring-boot-configuration-processor:${Versions.SPRING_BOOT}")
    implementation("me.paulschwarz:spring-dotenv:${Versions.DOTENV}")
    // FasterXML
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    //CSV
    implementation("com.opencsv:opencsv:${Versions.OPEN_CSV}")


    // Liquibase
    implementation("org.liquibase:liquibase-core:${Versions.LIQUIBASE_CORE}")

    // Kotlin Logging
    implementation("io.github.microutils:kotlin-logging-jvm:${Versions.KOTLIN_LOGGING_JVM}")

    // postgtrsql JDBC Driver
    runtimeOnly("org.postgresql:postgresql:${Versions.POSTGRESQL}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = Versions.JVM
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
