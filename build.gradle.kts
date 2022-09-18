plugins {
    id("java")
}

group = "fr.bretzel.minestomplacement"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("com.github.Minestom:Minestom:18c46481f4")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}