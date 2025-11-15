import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "1.9.20"
    id("io.qameta.allure") version "2.11.2"
}

group = "com.realworld.tests"
version = "1.0.0"

repositories {
    mavenCentral()
}

val selenideVersion = "7.0.2"
val restAssuredVersion = "5.3.2"
val testNgVersion = "7.8.0"
val allureVersion = "2.24.0"
val slf4jVersion = "2.0.9"
val kotlinLoggingVersion = "3.0.5"

dependencies {
    implementation(kotlin("stdlib"))
    
    // Testing
    testImplementation("org.testng:testng:$testNgVersion")
    testImplementation(kotlin("test"))
    testImplementation("com.codeborne:selenide:$selenideVersion")
    testImplementation("io.rest-assured:rest-assured:$restAssuredVersion")
    testImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")
    
    // Allure
    testImplementation("io.qameta.allure:allure-testng:$allureVersion")
    testImplementation("io.qameta.allure:allure-rest-assured:$allureVersion")
    testImplementation("io.qameta.allure:allure-selenide:$allureVersion")
    
    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    
    // JSON
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.3")
}

allure {
    version.set(allureVersion)
}

tasks.test {
    useTestNG {
        suites("src/test/resources/testng.xml")
    }
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        showStandardStreams = true
    }
    systemProperty("allure.results.directory", "build/allure-results")
}

kotlin {
    jvmToolchain(17)
}
