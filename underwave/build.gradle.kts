import dependency.data.DiskLruCache
import dependency.test.AndroidTestExt
import dependency.test.AndroidTestRunner
import dependency.test.Robolectric

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

group = Library.group
version = Library.version

android {
    compileSdkVersion(Android.compileSDK)
    defaultConfig {
        minSdkVersion(Android.minSDK)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        textReport = true
        textOutput("stdout")
    }

    libraryVariants.all {
        generateBuildConfig?.enabled = false
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
        getByName("test") {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true
}

kotlin {
    android()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(kotlin("stdlib-jdk8"))
                implementation(DiskLruCache())
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(AndroidTestRunner())
                implementation(AndroidTestExt())
                implementation(Robolectric())
            }
        }
    }
}

configurations.create("compileClasspath")