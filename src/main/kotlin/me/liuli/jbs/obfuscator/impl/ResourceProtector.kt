package me.liuli.jbs.obfuscator.impl

import jdk.internal.org.objectweb.asm.Opcodes
import jdk.internal.org.objectweb.asm.Type
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
import jdk.internal.org.objectweb.asm.tree.ClassNode
import jdk.internal.org.objectweb.asm.tree.InsnNode
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode
import jdk.internal.org.objectweb.asm.tree.TypeInsnNode
import me.liuli.jbs.Configuration
import me.liuli.jbs.executor.ModifiableEntry
import me.liuli.jbs.obfuscator.ITransformer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.jar.JarEntry
import java.util.zip.GZIPOutputStream

class ResourceProtector : ITransformer {

    override val name = "ResourceProtector"

    override fun transform(klass: ClassNode, entry: ModifiableEntry, configuration: Configuration) {
        klass.methods.forEach { method ->
            for(i in 0 until method.instructions.size()) {
                val insn = method.instructions.get(i)
                if(insn is MethodInsnNode) {
                    if (insn.opcode == Opcodes.INVOKEVIRTUAL && (insn.owner == "java/lang/Class" || insn.owner == "java/lang/ClassLoader") && insn.name == "getResourceAsStream" && insn.desc == "(Ljava/lang/String;)Ljava/io/InputStream;") {
                        var count = i - 1
                        var firstInsn: AbstractInsnNode? = null
                        while (true) {
                            val insn2 = method.instructions.get(count)
                            if (insn2 is LdcInsnNode) {
                                if(insn2.cst is Type) {
                                    firstInsn = insn2
                                    break
                                }
                            } else if(insn2 is MethodInsnNode) {
                                if(insn2.desc == "()Ljava/lang/Class;" && insn2.owner == "java/lang/Object" && insn2.name == "getClass") {
                                    firstInsn = method.instructions.get(count - 1)
                                    break
                                }
                            }
                            count--
                            if(count == 0) break
                        }
                        if(firstInsn == null) continue

                        method.instructions.insertBefore(firstInsn, TypeInsnNode(Opcodes.NEW, "java/util/zip/GZIPInputStream"))
                        method.instructions.insertBefore(firstInsn, InsnNode(Opcodes.DUP))
                        method.instructions.insert(insn, MethodInsnNode(Opcodes.INVOKESPECIAL, "java/util/zip/GZIPInputStream", "<init>", "(Ljava/io/InputStream;)V"))
                        break
                    }
                }
            }
        }
    }

    override fun entryTransform(entry: JarEntry, modifiableEntry: ModifiableEntry, configuration: Configuration) {
        val name = if(modifiableEntry.name.startsWith("/")) modifiableEntry.name.substring(1) else modifiableEntry.name
        if((configuration.protectedDictionaries.any { name.startsWith(it) }
            || configuration.protectedFiles.any { name == it })
            && !configuration.protectedExcludedFiles.any { name == it }) {
            val bos = ByteArrayOutputStream()
            val gos = GZIPOutputStream(bos)
            gos.write(modifiableEntry.bytes)
            gos.finish()
            gos.close()
            bos.close()
            modifiableEntry.bytes = bos.toByteArray()
        }
    }
}