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
                ClassLoader loader,
                String className,
                Class<?> classBeingRedefined,
                java.security.ProtectionDomain protectionDomain,
                byte[] classfileBuffer
        ) {
            if ("org/gradle/process/internal/health/memory/CGroupMemoryInfo".equals(className)) {
                System.err.println("Transforming class to support cgroup v2: " + className);
                System.out.println("  PR https://github.com/gradle/gradle/pull/34883/changes");
                return CGroupMemoryInfoDump.dump();
            }
            return null;
        }
    }
}
