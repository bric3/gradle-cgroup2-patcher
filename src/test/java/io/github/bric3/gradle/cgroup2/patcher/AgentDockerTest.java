package io.github.bric3.gradle.cgroup2.patcher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class AgentDockerTest {
    @Test
    void agentTransformsCGroupMemoryInfoInGradle814(@TempDir Path tempDir) throws IOException {
        Path agentJar = Path.of(System.getProperty("agentJar"));
        assertThat(agentJar).exists()
                .withFailMessage("Agent JAR not found at: " + agentJar);

        Path testProjectDir = tempDir.resolve("test-project");
        Files.createDirectories(testProjectDir);

        Files.writeString(
                testProjectDir.resolve("settings.gradle.kts"),
                """
                    rootProject.name = "correctly-patching"
                    """
        );

        Files.writeString(testProjectDir.resolve("build.gradle.kts"), """
                tasks.register("forceLoadCGroupMemoryInfo") {
                    doLast {
                        println(org.gradle.process.internal.health.memory.CGroupMemoryInfo().getOsSnapshot().getPhysicalMemory().getFree())
                    }
                }
                """);

        try (var container = new GenericContainer<>(
                new ImageFromDockerfile()
                        .withDockerfileFromBuilder(builder -> builder
                                .from("gradle:8.14-jdk21")
                                .workDir("/test")
                                .copy("/cgroup-info-patcher-agent.jar", "/cgroup-info-patcher-agent.jar")
                                .copy("/test-project", "/test")
                                .run("echo 'org.gradle.jvmargs=-javaagent:/cgroup-info-patcher-agent.jar' > gradle.properties")
                                .cmd("gradle", "forceLoadCGroupMemoryInfo", "--info")
                                .build())
                        .withFileFromPath("/cgroup-info-patcher-agent.jar", agentJar)
                        .withFileFromPath("/test-project", testProjectDir)
        )) {
            var toStringConsumer = new ToStringConsumer();
            container
                    .withCreateContainerCmdModifier(cmd -> cmd.getHostConfig()
                            .withMemory(256 * 1024 * 1024L))
                    .withLogConsumer(toStringConsumer)
                    .waitingFor(Wait.forLogMessage(".*BUILD SUCCESSFUL.*", 1))
                    .withStartupTimeout(Duration.ofMinutes(3))
                    .start();

            var logs = toStringConsumer.toUtf8String();

            assertThat(logs)
                    .withFailMessage("Agent JAR not loaded in daemon command")
                    .contains("-javaagent:/cgroup-info-patcher-agent.jar");

            assertThat(logs)
                    .withFailMessage("Agent did not transform CGroupMemoryInfo class")
                    .contains("Transforming class to support cgroup v2: org/gradle/process/internal/health/memory/CGroupMemoryInfo");;
        }
    }
}