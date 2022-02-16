package me.liuli.jbs.obfuscator.impl

import jdk.internal.org.objectweb.asm.Opcodes
import jdk.internal.org.objectweb.asm.tree.ClassNode
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode
import me.liuli.jbs.Configuration
import me.liuli.jbs.executor.ModifiableEntry
import me.liuli.jbs.obfuscator.ITransformer
import me.liuli.jbs.utils.WordSelector

class PrivateMemberObfuscator : ITransformer {

    override val name = "PrivateMemberObfuscator"

    override fun transform(klass: ClassNode, entry: ModifiableEntry, configuration: Configuration) {
        if(klass.methods.isEmpty() || klass.fields.isEmpty()) return
        val fieldMap = mutableMapOf<String, String>()
        val methodMap = mutableMapOf<String, String>()
        val selector = WordSelector(configuration.randomWords)

        klass.fields.forEach { field ->
            if(field.access and Opcodes.ACC_PRIVATE != 0) {
                val name = selector.select()
                fieldMap[field.name] = name
                field.name = name
            }
        }

        klass.methods.forEach { method ->
            if(!method.name.startsWith("<") && !method.name.contains("lambda") && method.access and Opcodes.ACC_PRIVATE != 0) {
                val name = selector.select()
                methodMap[method.name + method.desc] = name
                method.name = name
            }
        }

        klass.methods.forEach { method ->
            method.instructions.iterator().forEach { insn ->
                if(insn is FieldInsnNode) {
                    if (insn.owner == klass.name) {
                        insn.name = fieldMap[insn.name] ?: insn.name
                    }
                } else if(insn is MethodInsnNode) {
                    if (insn.owner == klass.name) {
                        insn.name = methodMap[insn.name + insn.desc] ?: insn.name
                    }
                }
            }
        }
    }
}