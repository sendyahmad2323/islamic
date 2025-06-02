// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.10.1")
        classpath ("com.google.gms:google-services:4.3.15")
    }
}


allprojects {
    configurations.all {
        resolutionStrategy {

        }
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) version "4.4.2" apply false
}