pluginManagement {
    includeBuild("gradle/plugins")
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("realworld.settings")
}

rootProject.name = "aegis-commerce-platform"
include("service-api")
include("service-bus")
include("service")
include("service-template")
// include("module-name")
