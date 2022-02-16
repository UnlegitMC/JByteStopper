package me.liuli.jbs.obfuscator.impl

import jdk.internal.org.objectweb.asm.tree.ClassNode
import me.liuli.jbs.Configuration
import me.liuli.jbs.executor.ModifiableEntry
import me.liuli.jbs.obfuscator.ITransformer

class SourceWaterMarker : ITransformer {

    override val name = "SourceWaterMarker"

    override fun transform(klass: ClassNode, entry: ModifiableEntry, configuration: Configuration) {
        klass.sourceFile = configuration.sourceName
    }
}