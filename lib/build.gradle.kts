plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
}

mavenPublishing {
    coordinates(
        groupId = "com.dontsaybojio",
        artifactId = "spindler",
        version = libs.versions.spindler.get()
    )
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
        developers {
            developer {
                id.set("delacrixmorgan")
                name.set("Morgan Koh")
                email.set("delacrixmorgan@gmail.com")
            }
        }
        scm {
            url.set("https://github.com/delacrixmorgan/spindler")
        }
    }
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
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
    namespace = "com.dontsaybojio.spindler"
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
