plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.serialization") version "1.7.22"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1") {
        because("I need this to parse the input in Day 13")
    }
    implementation("com.github.shiguruikai:combinatoricskt:1.6.0") {
        because("I need combinations of sets for Day 16 and this was a bug-free way to do it")
    }

}
