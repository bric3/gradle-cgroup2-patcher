plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.0"
}

group = "io.github.bric3.gradle.cgroup2.patcher"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.asm)
    implementation(libs.asm.commons)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks {
    test {
        useJUnitPlatform()
        // Ensure shadowJar is built before tests run
        dependsOn(shadowJar)
    }

    jar {
        enabled = false
    }

    shadowJar {
        // Relocate ASM to avoid conflicts with Gradle's own ASM
        relocate("org.objectweb.asm", "io.github.bric3.gradle.cgroup2.patcher.shadow.asm")

        // JVM Agent manifest
        manifest {
            attributes["Premain-Class"] = "io.github.bric3.gradle.cgroup2.patcher.AddCgroupV2SupportToGradle"
            attributes["Can-Redefine-Classes"] = "true"
            attributes["Can-Retransform-Classes"] = "true"
        }

        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }
}

