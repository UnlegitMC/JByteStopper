package me.liuli.jbs.obfuscator

import jdk.internal.org.objectweb.asm.tree.ClassNode
import me.liuli.jbs.Configuration
import me.liuli.jbs.executor.ModifiableEntry
import java.util.jar.JarEntry

interface ITransformer {

    val name: String

    fun transform(klass: ClassNode, entry: ModifiableEntry, configuration: Configuration) { }

    fun entryTransform(entry: JarEntry, modifiableEntry: ModifiableEntry, configuration: Configuration) { }
}