import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import org.gradle.api.publish.tasks.GenerateModuleMetadata

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
    `java-library`
    alias(libs.plugins.shadow)
    alias(libs.plugins.maven.publish)
}

group = "io.github.bric3.gradle.cgroup2.patcher"
version = "0.0.1-SNAPSHOT"
description = "Java agent to patch Gradle 8.x with cgroup v2 support for better memory detection in containers"

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
    compileJava {
        options.release.set(8)
    }

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
        archiveClassifier = ""

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

mavenPublishing {
    configure(JavaLibrary(
        javadocJar = JavadocJar.Empty(),
        sourcesJar = true
    ))

    publishToMavenCentral()
//    signAllPublications()

    coordinates("${project.group}", "${project.name}", "${project.version}")

    pom {
        name = "Gradle CGroup v2 Patcher"
        description = "Java agent to patch Gradle 8.x with cgroup v2 support for better memory detection in containers"
        inceptionYear = "2025"
        url = "https://github.com/bric3/gradle-cgroup2-patcher"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }

        developers {
            developer {
                id = "bric3"
                name = "Brice Dutheil"
                url = "https://github.com/bric3"
            }
        }

        scm {
            url = "https://github.com/bric3/gradle-cgroup2-patcher"
            connection = "scm:git:git://github.com/bric3/gradle-cgroup2-patcher.git"
            developerConnection = "scm:git:ssh://git@github.com/bric3/gradle-cgroup2-patcher.git"
        }
    }
}

// Rustine to make `com.vanniktech.maven.publish` able to publish the shadow jar
// Unfortunately, `com.vanniktech.maven.publish` creates the "maven" publication, in an afterEvaluate block
// and doesn't allow to customize which artifacts / components are published.
// TODO, since `com.vanniktech.maven.publish` uses the java components, maybe it's possible to modify the java
//  component to use the shadow jar before the afterEvaluate block?
afterEvaluate {
    publishing {
        publications {
            named<MavenPublication>("maven") {
                // Clear artifacts from java component and add shadow jar artifacts
                artifacts.clear()
                artifact(tasks.shadowJar) {
                    classifier = ""
                }
                artifact(tasks.named("sourcesJar")) {
                    classifier = "sources"
                }
            }
        }
    }
    
    // Suppress Gradle module metadata generation as we're customizing artifacts
    tasks.withType<GenerateModuleMetadata> {
        enabled = false
    }
}

