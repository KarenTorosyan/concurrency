/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * To learn more about Gradle by exploring our Samples at https://docs.gradle.org/8.4/samples
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.bom)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
}