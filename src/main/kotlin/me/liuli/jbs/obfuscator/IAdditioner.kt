package me.liuli.jbs.obfuscator

import me.liuli.jbs.Configuration
import java.util.jar.JarOutputStream

interface IAdditioner {

    val name: String

    fun add(jos: JarOutputStream, configuration: Configuration)
}