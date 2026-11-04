// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val agpVersion = "8.5.0"
        val kotlinVersion = "1.9.24"
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
    extra["composeCompilerVersion"] = "1.5.14"
    extra["kotlinVersion"] = "1.9.24"
    extra["agpVersion"] = "8.5.0"
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}