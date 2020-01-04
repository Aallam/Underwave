import com.diffplug.gradle.spotless.SpotlessExtension
import dependency.kotlinx.Coroutines
import dependency.lib.DiskLruCache
import dependency.test.AndroidTestExt
import dependency.test.AndroidTestRunner
import dependency.test.Mockk
import dependency.test.Robolectric
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("com.diffplug.gradle.spotless")
    id("org.jetbrains.dokka")
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
            assets.srcDirs("src/androidMain/asserts")
        }
        getByName("test") {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
            assets.srcDirs("src/androidTest/asserts")
        }
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }

    libraryVariants.all {
        generateBuildConfigProvider?.configure { enabled = false }
    }
}

kotlin {
    android()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib-common"))
                api(Coroutines("core-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Coroutines("core-common"))
                implementation(Coroutines("test"))
                implementation(Mockk())
            }
        }
        val androidMain by getting {
            dependencies {
                api(kotlin("stdlib-jdk8"))
                api(Coroutines("android"))
                implementation(DiskLruCache())
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(Coroutines("android"))
                implementation(AndroidTestRunner())
                implementation(AndroidTestExt())
                implementation(Robolectric())
                implementation(Mockk("android"))
            }
        }
    }
}

configure<SpotlessExtension> {
    kotlin {
        target("**/*.kt")
        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks {
    val dokka by getting(DokkaTask::class) {
        outputDirectory = "$rootDir/docs"
        outputFormat = "gfm"

        multiplatform{
            register("global") {
                perPackageOption {
                    includeNonPublic = false
                    skipDeprecated = false
                    reportUndocumented = false
                    skipEmptyPackages = true
                }
                perPackageOption {
                    prefix = "com.aallam.underwave.internal"
                    suppress = true
                }
                perPackageOption {
                    prefix = "com.aallam.underwave.load.impl"
                    suppress = true
                }
                sourceLink {
                    path = "src/main/kotlin"
                    url = "https://github.com/aallam/Underwave/blob/master/src/main/kotlin"
                    lineSuffix = "#L"
                }
            }
            register("android")
        }
    }
}

configurations.create("compileClasspath")
