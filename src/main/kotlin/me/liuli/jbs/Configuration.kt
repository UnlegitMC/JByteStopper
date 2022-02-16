package me.liuli.jbs

class Configuration(
    val inputFile: String,
    val outputFile: String,
    val allowedPackages: List<String> = emptyList(),
    val excludedPackages: List<String> = emptyList(),
    val usedFeatures: List<String>,
    val sourceName: String = "JBS", // used in SourceWaterMarker
    val antiGuiDeobfClass: String = "<html><img src=\"https:\nJ\nB\ny\nt\ne\nS\nt\no\np\np\ne\nr", // used in AntiGUIDeobfuscation
//    val randomGeneratedClass: String = "obfuscated_class_name",
//    val resourceObfuscationDecodeClass: String = "<html><img src=\"https:\nJ\nB\ny\nt\ne\nS\nt\no\np\np\ne\nr\nR\ne\ns\nO\nb\nf", // used in ResourceObfuscation
    val randomWords: List<String> = listOf("J\nBYTE\nSTOPPER"),
    val protectedDictionaries: List<String> = emptyList(), // ResourceProtector
    val protectedFiles: List<String> = emptyList(), // ResourceProtector
    val protectedExcludedFiles: List<String> = emptyList(), // ResourceProtector
)