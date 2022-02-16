package me.liuli.jbs.obfuscator.impl

import jdk.internal.org.objectweb.asm.tree.ClassNode
import me.liuli.jbs.Configuration
import me.liuli.jbs.obfuscator.IAdditioner
import me.liuli.jbs.utils.ClassUtils
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

class AntiGUIDeobfuscation : IAdditioner {

    override val name = "AntiGUIDeobfuscation"

    override fun add(jos: JarOutputStream, configuration: Configuration) {
        jos.putNextEntry(JarEntry("${configuration.antiGuiDeobfClass}.class"))
        val klass = ClassNode()
        klass.name = configuration.antiGuiDeobfClass
        jos.write(ClassUtils.toBytes(klass))
        jos.closeEntry()
    }
}