package me.liuli.jbs.obfuscator

import me.liuli.jbs.obfuscator.impl.*

class ObfuscateManager {

    val transformers = mutableListOf<ITransformer>()
    val additioners = mutableListOf<IAdditioner>()

    fun init() {
        register(PrivateMemberObfuscator())
        register(SourceWaterMarker())
        register(AntiGUIDeobfuscation())
        register(AntiGUIDeobfuscationToEachClass())
        register(LocalVariableRenamer())
        register(DecompileCrasher())
        register(ResourceProtector())
    }

    fun register(obj: Any) {
        if(obj is ITransformer) {
            transformers.add(obj)
        }
        if(obj is IAdditioner) {
            additioners.add(obj)
        }
    }
}