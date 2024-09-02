plugins {
    id("realworld.java-conventions")
    alias(libs.plugins.micronaut.library)
}

micronaut {
    importMicronautPlatform = false
    processing {
        annotations("com.aegis.commerce.application.*")
    }
}

dependencies {
    annotationProcessor(mn.lombok)
    compileOnly(mn.lombok)
}