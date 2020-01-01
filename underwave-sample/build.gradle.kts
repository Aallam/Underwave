import dependency.kotlin.Kotlinx

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("kotlinx-serialization")
}

android {
    compileSdkVersion(Android.compileSDK)
    defaultConfig {
        applicationId = "com.aallam.underwave.sample"
        setMinSdkVersion(Android.minSDK)
        setTargetSdkVersion(Android.targetSDK)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(Kotlinx("serialization-runtime"))
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.android.material:material:1.0.0")
    implementation(project(":underwave"))
}
