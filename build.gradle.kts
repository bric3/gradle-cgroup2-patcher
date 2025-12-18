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
        dependsOn(shadowJar)
        useJUnitPlatform()
        doFirst {
            systemProperty("agentJar", shadowJar.flatMap { it.archiveFile }.get().asFile.absolutePath)
        }
    }

    jar {
        enabled = false
    }

    shadowJar {
        manifest {
            attributes["Premain-Class"] = "io.github.bric3.gradle.cgroup2.patcher.AddCgroupV2SupportToGradle"
            attributes["Can-Redefine-Classes"] = "true"
            attributes["Can-Retransform-Classes"] = "true"
        }
        relocate("org.objectweb.asm", "io.github.bric3.gradle.cgroup2.patcher.asm")
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }
}

