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
    implementation("com.github.ALS-Project:Minestom-States:d628c0750f")
    implementation("com.github.ALS-Project:Minestom-Utilities:b65a76ab44")
}