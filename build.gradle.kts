val kotlinVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        val kotlinVersion: String by project
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    java
}

allprojects {
    group = "com.psquare"
    version = "0.0.1"

    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenLocal()
        jcenter()
        maven { url = uri("https://kotlin.bintray.com/ktor") }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
        implementation("io.ktor:ktor-jackson:$ktorVersion")
        implementation("io.ktor:ktor-server-core:$ktorVersion")
        implementation("io.ktor:ktor-server-host-common:$ktorVersion")
        implementation("ch.qos.logback:logback-classic:$logbackVersion")

        testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    }
}

subprojects {
    version = "1.0"
}