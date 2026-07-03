plugins {
    java
    id("org.jetbrains.intellij.platform") version "2.17.0"
}

group = "dev.injectref"
version = "0.1.1"

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
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}
