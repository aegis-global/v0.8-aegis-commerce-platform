plugins {
    base
    alias(libs.plugins.versions)
    id("pl.allegro.tech.build.axion-release")
    id("realworld.jacoco-aggregation")
}

description = "Multi-Sided, Multi-Vendor, Multi-Tenant eCommerce Marketplace Application backend, API, and frontend built in Micronaut"

scmVersion {
    tag {
        prefix.set("v")
    }
}

dependencies {
    implementation(project(":service"))
    // add further dependencies here
    // e.g. implementation(project(":module-name"))
}

tasks {
    dependencyUpdates {
        checkConstraints = true
        resolutionStrategy {
            componentSelection {
                all {
                    val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview", "b", "ea")
                        .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-+]*") }
                        .any { it.matches(candidate.version) }
                    if (rejected) {
                        reject("Release candidate")
                    }
                }
            }
        }
    }
}
