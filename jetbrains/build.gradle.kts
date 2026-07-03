plugins {
    java
    id("org.jetbrains.intellij.platform") version "2.17.0"
}

group = "dev.injectref"
version = "0.1.0"

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        webstorm("2026.1.4")
    }

    testImplementation("junit:junit:4.13.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnit()
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}
