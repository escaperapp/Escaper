import org.gradle.plugin.compatibility.compatibility

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.publish.plugin)
}

group = "io.escaper"
version = "1.0.2"

dependencies {
    compileOnly(libs.compose.multiplatform.plugin)
}

gradlePlugin {
    plugins {
        create("compose-exe-manifest") {
            compatibility {
                features {
                    configurationCache = true
                }
            }
            id = "io.escaper.compose-exe-manifest"
            implementationClass = "io.escaper.manifest.EmbedPlugin"
            description = "Embeds application manifest XML file in Compose Multiplatform desktop exe file"
            displayName = "Compose Exe Manifest"
            website = "https://github.com/escaperapp/Escaper"
            vcsUrl = "https://github.com/escaperapp/Escaper.git"
            tags = listOf(
                "compose-multiplatform",
                "application-manifest",
                "manifest",
                "exe"
            )
        }
    }
}