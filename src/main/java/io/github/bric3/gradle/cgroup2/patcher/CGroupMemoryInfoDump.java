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
 *
 * However this file is an ASM dump of the original class https://github.com/gradle/gradle/blob/c43142f2b72aa41d60cb8c10cf839f4f9082e643/platforms/core-runtime/process-memory-services/src/main/java/org/gradle/process/internal/health/memory/CGroupMemoryInfo.javas.
 * And the instructions to get this compiled code are a derived from the compiled class, and I believe - I'm not a lawyer - they are covered by the original license of the file.
 */
package io.github.bric3.gradle.cgroup2.patcher;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;

/**
 * Dump of <code>org.gradle.process.internal.health.memory.CGroupMemoryInfo</code>
 * with cgroup v2 support that is shipped in Gradle 9.3.0-rc1.
 *
 * <a href="https://github.com/gradle/gradle/blob/c43142f2b72aa41d60cb8c10cf839f4f9082e643/platforms/core-runtime/process-memory-services/src/main/java/org/gradle/process/internal/health/memory/CGroupMemoryInfo.java">Source file</a>
 */
public class CGroupMemoryInfoDump implements Opcodes {

    public static byte[] dump() {

        ClassWriter classWriter = new ClassWriter(0);
        FieldVisitor fieldVisitor;
        RecordComponentVisitor recordComponentVisitor;
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "org/gradle/process/internal/health/memory/CGroupMemoryInfo", null, "java/lang/Object", new String[]{"org/gradle/process/internal/health/memory/OsMemoryInfo"});

        classWriter.visitSource("CGroupMemoryInfo.java", null);

        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_FINAL | ACC_STATIC, "CG1_MEM_USAGE_FILE", "Ljava/lang/String;", null, "/sys/fs/cgroup/memory/memory.usage_in_bytes");
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_FINAL | ACC_STATIC, "CG1_MEM_TOTAL_FILE", "Ljava/lang/String;", null, "/sys/fs/cgroup/memory/memory.limit_in_bytes");
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_FINAL | ACC_STATIC, "CG2_MEM_USAGE_FILE", "Ljava/lang/String;", null, "/sys/fs/cgroup/memory.current");
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_FINAL | ACC_STATIC, "CG2_MEM_TOTAL_FILE", "Ljava/lang/String;", null, "/sys/fs/cgroup/memory.max");
            fieldVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(26, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLocalVariable("this", "Lorg/gradle/process/internal/health/memory/CGroupMemoryInfo;", null, label0, label1, 0);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "getOsSnapshot", "()Lorg/gradle/process/internal/health/memory/OsMemoryStatus;", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(34, label0);
            methodVisitor.visitTypeInsn(NEW, "java/io/File");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitLdcInsn("/sys/fs/cgroup/memory.current");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
            methodVisitor.visitVarInsn(ASTORE, 1);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(35, label1);
            methodVisitor.visitTypeInsn(NEW, "java/io/File");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitLdcInsn("/sys/fs/cgroup/memory.max");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
            methodVisitor.visitVarInsn(ASTORE, 2);
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(36, label2);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "exists", "()Z", false);
            Label label3 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, label3);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "exists", "()Z", false);
            methodVisitor.visitJumpInsn(IFEQ, label3);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLineNumber(37, label4);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitLineNumber(38, label5);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "org/gradle/process/internal/health/memory/CGroupMemoryInfo", "readStringFromFile", "(Ljava/io/File;)Ljava/lang/String;", false);
            methodVisitor.visitVarInsn(ALOAD, 2);
            Label label6 = new Label();
            methodVisitor.visitLabel(label6);
            methodVisitor.visitLineNumber(39, label6);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "org/gradle/process/internal/health/memory/CGroupMemoryInfo", "readStringFromFile", "(Ljava/io/File;)Ljava/lang/String;", false);
            Label label7 = new Label();
            methodVisitor.visitLabel(label7);
            methodVisitor.visitLineNumber(37, label7);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/gradle/process/internal/health/memory/CGroupMemoryInfo", "getOsSnapshotFromCgroup", "(Ljava/lang/String;Ljava/lang/String;)Lorg/gradle/process/internal/health/memory/OsMemoryStatusSnapshot;", false);
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(42, label3);
            methodVisitor.visitFrame(Opcodes.F_APPEND, 2, new Object[]{"java/io/File", "java/io/File"}, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitTypeInsn(NEW, "java/io/File");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitLdcInsn("/sys/fs/cgroup/memory/memory.usage_in_bytes");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
            Label label8 = new Label();
            methodVisitor.visitLabel(label8);
            methodVisitor.visitLineNumber(43, label8);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "org/gradle/process/internal/health/memory/CGroupMemoryInfo", "readStringFromFile", "(Ljava/io/File;)Ljava/lang/String;", false);
            methodVisitor.visitTypeInsn(NEW, "java/io/File");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitLdcInsn("/sys/fs/cgroup/memory/memory.limit_in_bytes");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
            Label label9 = new Label();
            methodVisitor.visitLabel(label9);
            methodVisitor.visitLineNumber(44, label9);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "org/gradle/process/internal/health/memory/CGroupMemoryInfo", "readStringFromFile", "(Ljava/io/File;)Ljava/lang/String;", false);
            Label label10 = new Label();
            methodVisitor.visitLabel(label10);
            methodVisitor.visitLineNumber(42, label10);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/gradle/process/internal/health/memory/CGroupMemoryInfo", "getOsSnapshotFromCgroup", "(Ljava/lang/String;Ljava/lang/String;)Lorg/gradle/process/internal/health/memory/OsMemoryStatusSnapshot;", false);
            methodVisitor.visitInsn(ARETURN);
            Label label11 = new Label();
            methodVisitor.visitLabel(label11);
            methodVisitor.visitLocalVariable("this", "Lorg/gradle/process/internal/health/memory/CGroupMemoryInfo;", null, label0, label11, 0);
            methodVisitor.visitLocalVariable("cg2Usage", "Ljava/io/File;", null, label1, label11, 1);
            methodVisitor.visitLocalVariable("cg2Total", "Ljava/io/File;", null, label2, label11, 2);
            methodVisitor.visitMaxs(5, 3);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PRIVATE | ACC_STATIC, "readStringFromFile", "(Ljava/io/File;)Ljava/lang/String;", null, null);
            methodVisitor.visitParameter("file", 0);
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/io/IOException");
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(50, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/nio/charset/Charset", "defaultCharset", "()Ljava/nio/charset/Charset;", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/google/common/io/Files", "asCharSource", "(Ljava/io/File;Ljava/nio/charset/Charset;)Lcom/google/common/io/CharSource;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/io/CharSource", "readFirstLine", "()Ljava/lang/String;", false);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(51, label2);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/io/IOException"});
            methodVisitor.visitVarInsn(ASTORE, 1);
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(52, label3);
            methodVisitor.visitTypeInsn(NEW, "java/lang/UnsupportedOperationException");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            methodVisitor.visitLdcInsn("Unable to read system memory from ");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "getAbsoluteFile", "()Ljava/io/File;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/UnsupportedOperationException", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", false);
            methodVisitor.visitInsn(ATHROW);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLocalVariable("e", "Ljava/io/IOException;", null, label3, label4, 1);
            methodVisitor.visitLocalVariable("file", "Ljava/io/File;", null, label0, label4, 0);
            methodVisitor.visitMaxs(4, 2);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(0, "getOsSnapshotFromCgroup", "(Ljava/lang/String;Ljava/lang/String;)Lorg/gradle/process/internal/health/memory/OsMemoryStatusSnapshot;", null, null);
            methodVisitor.visitParameter("memUsageString", 0);
            methodVisitor.visitParameter("memTotalString", 0);
            {
                annotationVisitor0 = methodVisitor.visitAnnotation("Lcom/google/common/annotations/VisibleForTesting;", false);
                annotationVisitor0.visitEnd();
            }
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/NumberFormatException");
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(63, label0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "parseLong", "(Ljava/lang/String;)J", false);
            methodVisitor.visitVarInsn(LSTORE, 3);
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(65, label3);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "parseLong", "(Ljava/lang/String;)J", false);
            methodVisitor.visitVarInsn(LSTORE, 5);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLineNumber(66, label4);
            methodVisitor.visitInsn(LCONST_0);
            methodVisitor.visitVarInsn(LLOAD, 5);
            methodVisitor.visitVarInsn(LLOAD, 3);
            methodVisitor.visitInsn(LSUB);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(JJ)J", false);
            methodVisitor.visitVarInsn(LSTORE, 7);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(69, label1);
            Label label5 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label5);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(67, label2);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/lang/NumberFormatException"});
            methodVisitor.visitVarInsn(ASTORE, 9);
            Label label6 = new Label();
            methodVisitor.visitLabel(label6);
            methodVisitor.visitLineNumber(68, label6);
            methodVisitor.visitTypeInsn(NEW, "java/lang/UnsupportedOperationException");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitLdcInsn("Unable to read system memory");
            methodVisitor.visitVarInsn(ALOAD, 9);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/UnsupportedOperationException", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", false);
            methodVisitor.visitInsn(ATHROW);
            methodVisitor.visitLabel(label5);
            methodVisitor.visitLineNumber(71, label5);
            methodVisitor.visitFrame(Opcodes.F_APPEND, 3, new Object[]{Opcodes.LONG, Opcodes.LONG, Opcodes.LONG}, 0, null);
            methodVisitor.visitTypeInsn(NEW, "org/gradle/process/internal/health/memory/OsMemoryStatusSnapshot");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(LLOAD, 5);
            methodVisitor.visitVarInsn(LLOAD, 7);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "org/gradle/process/internal/health/memory/OsMemoryStatusSnapshot", "<init>", "(JJ)V", false);
            methodVisitor.visitInsn(ARETURN);
            Label label7 = new Label();
            methodVisitor.visitLabel(label7);
            methodVisitor.visitLocalVariable("memUsage", "J", null, label3, label2, 3);
            methodVisitor.visitLocalVariable("memTotal", "J", null, label4, label2, 5);
            methodVisitor.visitLocalVariable("memAvailable", "J", null, label1, label2, 7);
            methodVisitor.visitLocalVariable("e", "Ljava/lang/NumberFormatException;", null, label6, label5, 9);
            methodVisitor.visitLocalVariable("this", "Lorg/gradle/process/internal/health/memory/CGroupMemoryInfo;", null, label0, label7, 0);
            methodVisitor.visitLocalVariable("memUsageString", "Ljava/lang/String;", null, label0, label7, 1);
            methodVisitor.visitLocalVariable("memTotalString", "Ljava/lang/String;", null, label0, label7, 2);
            methodVisitor.visitLocalVariable("memUsage", "J", null, label5, label7, 3);
            methodVisitor.visitLocalVariable("memTotal", "J", null, label5, label7, 5);
            methodVisitor.visitLocalVariable("memAvailable", "J", null, label5, label7, 7);
            methodVisitor.visitMaxs(6, 10);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }
}
