import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

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

// Rustine to allow publishing the shadow jar as the only jar to be published
configurations {
    named("runtimeElements") {
        outgoing {
            artifacts.clear()
            artifact(tasks.shadowJar)
        }
    }
    named("apiElements") {
        outgoing {
            artifacts.clear()
            artifact(tasks.shadowJar)
        }
    }
}

mavenPublishing {
    configure(JavaLibrary(
        javadocJar = JavadocJar.Empty(),
        sourcesJar = true
    ))

    publishToMavenCentral(automaticRelease = true)

    // See https://docs.gradle.org/current/userguide/publishing_maven.html
    // and https://github.com/gradle-nexus/publish-plugin
    /*
     * Identify the key
     * ```
     * gpg --list-secret-keys --keyid-format=short
     * ```
     *
     * NOTE: I wasn't able to sign the artefacts using only
     * `-Psigning.password="$(gpg --export-secret-keys --armor <key id>)` and `-Psigning.key="$(gpg --export --armor <key id>)"`
     * Got this error: `Cannot perform signing task ':signMavenPublication' because it has no configured signatory`
     *
     * First export the secring.gpg file:
     * ```
     * gpg --keyring secring.gpg --export-secret-keys > ~/.gnupg/secring.gpg
     * ```
     *
     * Then check sign works:
     * ```
     * ./gradlew sign \
     *      -PmavenCentralUsername=$(op item get "central" --fields publication.username) \
     *      -PmavenCentralPassword=$(op item get "central" --fields publication.password --reveal)
     *      -Psigning.secretKeyRingFile=$HOME/.gnupg/secring.gpg \
     *      -Psigning.password="$(op item get "gpg" --field passphrase --reveal)"
     *      -Psigning.keyId=<short key id>
     * ```
    */
    signAllPublications()

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
