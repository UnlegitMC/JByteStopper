package me.liuli.jbs

import com.beust.klaxon.Klaxon
import me.liuli.jbs.executor.ObfuscateExecutor
import me.liuli.jbs.obfuscator.ObfuscateManager
import me.liuli.jbs.obfuscator.impl.ResourceProtector
import java.io.File
import java.util.zip.GZIPInputStream

object JByteStopper {

    lateinit var configuration: Configuration
        private set

    val obfuscateManager = ObfuscateManager()

    init {
        obfuscateManager.init()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        if(args.isEmpty()) {
            println("Usage: JByteStopper <configuration file>")
            return
        }
        val configFile = File(args[0])
        if(!configFile.exists()) {
            println("Configuration file not found: ${configFile.absolutePath}")
            return
        }
        configuration = Klaxon().parse<Configuration>(configFile)!!

        ObfuscateExecutor(configuration).executeConfig()
    }
}