package me.liuli.jbs.executor

import me.liuli.jbs.Configuration
import me.liuli.jbs.JByteStopper
import me.liuli.jbs.obfuscator.IAdditioner
import me.liuli.jbs.obfuscator.ITransformer
import me.liuli.jbs.utils.ClassUtils
import java.io.File
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

class ObfuscateExecutor(private val configuration: Configuration) {

    private val transformers: MutableList<ITransformer> =
        JByteStopper.obfuscateManager.transformers.filter { configuration.usedFeatures.contains(it.name) }.toMutableList()
    private val additioners: MutableList<IAdditioner> =
        JByteStopper.obfuscateManager.additioners.filter { configuration.usedFeatures.contains(it.name) }.toMutableList()

    fun executeConfig() {
        val jarFile = JarFile(configuration.inputFile)
        val outputFile = File(configuration.outputFile)
        if(outputFile.exists()) {
            outputFile.delete()
        }
        val jos = JarOutputStream(outputFile.outputStream())
        execute(jarFile, jos)
        jos.close()
    }

    fun execute(file: JarFile, jos: JarOutputStream) {
        file.stream().forEach { entry ->
            if(entry.name.endsWith(".class")) {
                val modifiableEntry = ModifiableEntry(entry.name, file.getInputStream(entry).readBytes())
                val klass = ClassUtils.toClassNode(modifiableEntry.bytes)
                if(configuration.allowedPackages.any { klass.name.startsWith(it) }
                    && !configuration.excludedPackages.any { klass.name.startsWith(it) }) {
                    transformers.forEach { it.transform(klass, modifiableEntry, configuration) }
                    modifiableEntry.bytes = ClassUtils.toBytes(klass)
                }
                transformers.forEach { it.entryTransform(entry, modifiableEntry, configuration) }
                jos.putNextEntry(JarEntry(modifiableEntry.name))
                jos.write(modifiableEntry.bytes)
                jos.closeEntry()
            }
        }
        file.stream().forEach { entry ->
            if(!entry.name.endsWith(".class")) {
                val modifiableEntry = ModifiableEntry(entry.name, file.getInputStream(entry).readBytes())
                transformers.forEach { it.entryTransform(entry, modifiableEntry, configuration) }
                jos.putNextEntry(JarEntry(modifiableEntry.name))
                jos.write(modifiableEntry.bytes)
                jos.closeEntry()
            }
        }
        additioners.forEach { it.add(jos, configuration) }
    }
}