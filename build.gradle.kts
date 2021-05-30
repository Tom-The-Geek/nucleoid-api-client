plugins {
    java
    `maven-publish`
}

val ENV = System.getenv()

group = "com.github.tom_the_geek"
version = if (ENV["RELEASE_VERSION"] != null) {
    ENV["RELEASE_VERSION"]!!
} else {
    "1.0.0+local"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    // Target this version because that's what latest Minecraft (1.16.5) does
    implementation("com.google.code.gson:gson:2.8.0")
}

// Separate file bc I CBA to figure out maven-publish in kotlin buildscript
apply(from = "publishing.gradle")
