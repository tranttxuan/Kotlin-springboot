import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    val kotlinVersion = "1.6.10"
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

    kotlin("jvm") version "1.8.21"
}

group = "kotlinspring"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

extra["testcontainersVersion"] = "1.16.2"

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    //runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")

    //logging
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("io.mockk:mockk:1.10.4")
    testImplementation("com.ninja-squad:springmockk:3.0.1")

    //test-containers
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

sourceSets {
    test {
        java {
            setSrcDirs(listOf("src/test/unit", "src/test/integration"))
        }
    }
}