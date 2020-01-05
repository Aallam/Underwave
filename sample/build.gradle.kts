import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("kotlinx-serialization")
    id("com.diffplug.gradle.spotless")
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

    packagingOptions {
        pickFirst("META-INF/kotlinx-coroutines-core.kotlin_module")
    }

    buildTypes {
        val debug by getting {
            // MPP libraries don't currently get this resolution automatically
            matchingFallbacks = listOf("release")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":underwave")) // implementation("com.aallam.underwave:underwave-android:<version>")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.android.material:material:1.0.0")
    implementation("com.squareup.leakcanary:leakcanary-android:2.1")
}

configure<SpotlessExtension> {
    kotlin {
        target("**/*.kt")
        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
    }
}
