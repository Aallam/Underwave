buildscript {
    repositories {
        mavenCentral()
        google()
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", "1.3.61"))
        classpath(kotlin("serialization", "1.3.61"))
        classpath(dependency.android.AndroidGradlePlugin())
    }
}

subprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}