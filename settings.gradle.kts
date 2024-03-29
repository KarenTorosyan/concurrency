/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 * For more detailed information on multi-project builds, please refer to https://docs.gradle.org/8.4/userguide/building_swift_projects.html in the Gradle documentation.
 * This project uses @Incubating APIs which are subject to change.
 */

rootProject.name = "concurrency"

dependencyResolutionManagement.versionCatalogs.create("libs") {
    plugin("spring-boot", "org.springframework.boot").version("3.2.1")
    plugin("spring-bom", "io.spring.dependency-management").version("1.1.4")
}


