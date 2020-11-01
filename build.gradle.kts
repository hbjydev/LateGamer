plugins {
    kotlin("jvm") version Versions.kotlin
}

group = "dev.hbjy"
version = "0.1.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(Dependencies.discordkt)
    implementation(Dependencies.gson)
    implementation(Dependencies.joda)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}