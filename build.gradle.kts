/*
 * Copyright 2025 Brice Dutheil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    java
    alias(libs.plugins.shadow)
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
    testRuntimeOnly(libs.slf4j.simple)
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

        // Include license files in the JAR
        from(rootProject.projectDir) {
            include("LICENSE", "NOTICE")
            into("META-INF")
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

