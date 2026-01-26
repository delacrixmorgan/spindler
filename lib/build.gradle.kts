import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    id("com.vanniktech.maven.publish") version "0.28.0"
}

mavenPublishing {
    // Define coordinates for the published artifact
    coordinates(
        groupId = "io.github.delacrixmorgan",
        artifactId = "spindler",
        version = "0.1.0"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("Spindler")
        description.set("GEDCOM Kotlin Multiplatform Parser \uD83C\uDF33")
        inceptionYear.set("2025")
        url.set("https://github.com/delacrixmorgan/spindler")

        licenses {
            license {
                name.set("GNU General Public License v3.0")
                url.set("https://github.com/delacrixmorgan/spindler/blob/main/LICENSE.md")
            }
        }

        // Specify developers information
        developers {
            developer {
                id.set("delacrixmorgan")
                name.set("Morgan Koh")
                email.set("delacrixmorgan@gmail.com")
            }
        }

        // Specify SCM information
        scm {
            url.set("https://github.com/delacrixmorgan/spindler")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}

kotlin {
    androidTarget()
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { target ->
//        target.binaries.framework {
//            baseName = "Spindler"
//            isStatic = true
//        }
//    }
    jvm("desktop")
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "io.dontsayboj.spindler"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        version = "0.0.6"
        consumerProguardFiles("consumer-rules.pro")
    }

    publishing {
        singleVariant("release")
    }
}
