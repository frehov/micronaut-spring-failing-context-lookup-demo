import io.micronaut.gradle.MicronautRuntime.NETTY
import io.micronaut.gradle.MicronautTestRuntime.JUNIT_5

/**
 * Gradle build file.
 * Building the microservice with the Kotlin plugin for gradle.
 *
 * @see <a href="https://kotlinlang.org/docs/reference/using-gradle.html">Using Gradle in Official Kotlin doc.</a
 */

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("io.micronaut.minimal.application") version "4.5.3"
    id("org.jetbrains.kotlin.plugin.allopen")
}

group = "com.example.micronuaut.spring"

val version: String by project
val micronautVersion: String by project
val targetJvmVersion: String by project

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(targetJvmVersion)
    }
}

micronaut {
    version.set(micronautVersion)
    runtime.set(NETTY)
    testRuntime.set(JUNIT_5)
    processing {
        incremental.set(true)
        annotations.add("com.example.micronuaut.spring.*")
    }
}

dependencies {
    /**
     * Kotlin dependencies.
     */
    implementation(kotlin("reflect"))

    /**
     * Micronaut framework dependencies.
     *
     * micronaut-inject-java and micronaut-validation are omitted
     * due to the micronaut application plugin adding them by default.
     */
    kapt("io.micronaut:micronaut-http-validation")

    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")

    implementation("io.micronaut:micronaut-inject")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut:micronaut-jackson-databind")

    implementation("io.micronaut.spring:micronaut-spring-context")

    /**
     * Third-party dependencies.
     */
    runtimeOnly("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    runtimeOnly("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-parameter-names")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-blackbird")
    runtimeOnly("org.yaml:snakeyaml")

    /**
     * Test dependency configurations.
     */
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.assertj:assertj-core")
    testImplementation("io.mockk:mockk:1.12.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
    mainClass.set("com.example.micronuaut.spring")
}

tasks {
    test {
        systemProperty("micronaut.environments", "test")
        systemProperty("micronaut.env.deduction", false)
    }

    compileKotlin {
        kotlinOptions {
            javaParameters = true
        }
    }

    compileTestKotlin {
        kotlinOptions {
            javaParameters = true
        }
    }

    (run) {
        doFirst {
            jvmArgs = listOf("-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")
        }
    }
}