plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    id("maven-publish")
}

group = "com.github.delacrixmorgan"
version = libs.versions.spindler.get()

kotlin {
    androidTarget()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "Spindler"
            isStatic = true
        }
    }
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
        version = libs.versions.spindler.get()
        consumerProguardFiles("consumer-rules.pro")
    }

    publishing {
        singleVariant("release")
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            groupId = "com.github.delacrixmorgan"
            artifactId = if (name == "kotlinMultiplatform") {
                "spindler"
            } else {
                "spindler-$name"
            }
        }
    }
}
