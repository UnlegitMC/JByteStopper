package me.liuli.jbs.obfuscator.impl

import me.liuli.jbs.Configuration
import me.liuli.jbs.executor.ModifiableEntry
import me.liuli.jbs.obfuscator.ITransformer
import java.util.jar.JarEntry

class AntiGUIDeobfuscationToEachClass : ITransformer {

    override val name = "AntiGUIDeobfuscationToEachClass"

    override fun entryTransform(entry: JarEntry, modifiableEntry: ModifiableEntry, configuration: Configuration) {
        if(entry.name.endsWith(".class")) {
            modifiableEntry.name += "/"
        }
    }
}