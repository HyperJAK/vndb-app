plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.compose.gradle.plugin)
    compileOnly(libs.detekt.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.vndb.android.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("detekt") {
            id = libs.plugins.vndb.detekt.get().pluginId
            implementationClass = "DetektConventionPlugin"
        }
    }
}
