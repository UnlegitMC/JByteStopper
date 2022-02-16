package me.liuli.jbs.obfuscator.impl

import jdk.internal.org.objectweb.asm.tree.AnnotationNode
import jdk.internal.org.objectweb.asm.tree.ClassNode
import me.liuli.jbs.Configuration
import me.liuli.jbs.executor.ModifiableEntry
import me.liuli.jbs.obfuscator.ITransformer


class DecompileCrasher : ITransformer {

    override val name = "DecompileCrasher"

    private val filledString: String

    init {
        val sb = StringBuilder()
        repeat(10000) {
            sb.append("\n")
        }
        filledString = sb.toString()
    }

    override fun transform(klass: ClassNode, entry: ModifiableEntry, configuration: Configuration) {
        klass.methods.forEach { method ->
            if (method.invisibleAnnotations == null) {
                method.invisibleAnnotations = ArrayList()
            }
            repeat(100) {
                method.invisibleAnnotations.add(AnnotationNode("${configuration.antiGuiDeobfClass}1"))
            }
        }
    }
}