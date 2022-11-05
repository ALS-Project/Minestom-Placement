plugins {
    `kotlin-dsl`
    `maven-publish`
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

var minestomVersion = "18c46481f4"
var statesVersion = "d42b0086f1"
var utilityVersion = "ba76b43d5b"
var version = "1.1.1"

group = "fr.bretzel.minestom.placement"
version = "$version"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven(url = "https://jitpack.io")
}

java {
    withSourcesJar()
}

dependencies {
    implementation("com.github.Minestom:Minestom:$minestomVersion")
    implementation("com.github.ALS-Project:Minestom-States:$statesVersion")
    implementation("com.github.ALS-Project:Minestom-Utilities:$utilityVersion")
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        minimize()
        exclude("com.github.Minestom:Minestom:*")
    }
}



publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "fr.bretzel.minestom.placement"
            artifactId = "MinestomPlacement"
            version = "$version"

            from(components["java"])
        }
    }
}

