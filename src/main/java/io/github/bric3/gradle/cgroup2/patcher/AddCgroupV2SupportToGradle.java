package io.github.bric3.gradle.cgroup2.patcher;

import java.lang.instrument.ClassFileTransformer;

/**
 * JVM Agent to add cgroup v2 support to Gradle's OsMemoryInfo implementations
 */
public class AddCgroupV2SupportToGradle {
    public static void premain(String agentArgs, java.lang.instrument.Instrumentation inst) {
        inst.addTransformer(new CGroupV2MemoryInfoTransformer(), true);
    }

    private static class CGroupV2MemoryInfoTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(
                Module module,
                ClassLoader loader,
                String className,
                Class<?> classBeingRedefined,
                java.security.ProtectionDomain protectionDomain,
                byte[] classfileBuffer
        ) {
            if ("org/gradle/process/internal/health/memory/CGroupMemoryInfo".equals(className)) {
                System.err.println("Transforming class to support cgroup v2: " + className);
                System.out.println("  commit https://github.com/Megmeehey/gradle/commit/1d26bf4522a37e559d911a0f9e14d153d0525ead");
                return CGroupMemoryInfoDump.dump();
            }
            return null;
        }
    }
}
