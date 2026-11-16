plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.detekt.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("detekt") {
            id = libs.plugins.vndb.detekt.get().pluginId
            implementationClass = "DetektConventionPlugin"
        }
    }
}
