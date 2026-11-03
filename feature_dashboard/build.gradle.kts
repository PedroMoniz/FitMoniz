plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.moniz.app_dashboard"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(project(":app_base"))
    implementation(project(":domain"))
    implementation(project(":ui"))
    implementation(libs.androidx.core.ktx)

    api(libs.lifecycle.viewmodel.navigation3)
    api(libs.androidx.lifecycle.runtime.compose)
    api(libs.hilt.android)
    api(libs.hilt.navigation.compose)
    api(libs.androidx.compose.material3.adaptive.navigation.suite)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.navigation3.runtime)
    api(libs.androidx.navigation3.ui)

    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
