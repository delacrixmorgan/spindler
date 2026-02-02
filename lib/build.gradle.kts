plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
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
    androidLibrary {
        namespace = "com.dontsaybojio.spindler.lib"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
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
