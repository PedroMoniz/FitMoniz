plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.moniz.fitmoniz"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.moniz.fitmoniz"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":app_base"))
    implementation(project(":ui"))
    implementation(project(":domain"))
    implementation(project(":repository"))
    implementation(project(":feature_dashboard"))

    api(libs.androidx.compose.material3.adaptive.navigation.suite)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.navigation3.runtime)
    api(libs.androidx.navigation3.ui)
    api(libs.androidx.lifecycle.runtime.compose)
    api(libs.lifecycle.viewmodel.navigation3)
    api(libs.hilt.android)
    api(libs.hilt.navigation.compose)
    api(libs.kotlinx.serialization.json)
    api(libs.timber)
    implementation(libs.androidx.startup.runtime)

    ksp(libs.hilt.compiler)
}
