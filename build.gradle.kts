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
    implementation("org.ow2.asm:asm:9.7.1")
    implementation("org.ow2.asm:asm-commons:9.9.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    test {
        useJUnitPlatform()
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

