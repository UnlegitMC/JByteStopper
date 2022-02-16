package me.liuli.jbs.obfuscator.impl

import jdk.internal.org.objectweb.asm.tree.ClassNode
import me.liuli.jbs.Configuration
import me.liuli.jbs.executor.ModifiableEntry
import me.liuli.jbs.obfuscator.ITransformer
import me.liuli.jbs.utils.WordSelector

class LocalVariableRenamer : ITransformer {

    override val name = "LocalVariableRenamer"

    override fun transform(klass: ClassNode, entry: ModifiableEntry, configuration: Configuration) {
        val selector = WordSelector(configuration.randomWords)
        klass.methods.forEach { method ->
            method.parameters?.forEach {
                it.name = selector.select()
            }
            method.localVariables?.forEach { variable ->
                variable.name = selector.select()
            }
        }
    }
}