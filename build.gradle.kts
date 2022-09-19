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
    implementation("com.github.Minestom:Minestom:18c46481f4")
    implementation("com.github.ALS-Project:Minestom-States:0a387c3237")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}