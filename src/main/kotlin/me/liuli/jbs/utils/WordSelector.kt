package me.liuli.jbs.utils

import me.liuli.jbs.JByteStopper

class WordSelector(val words: List<String> = JByteStopper.configuration.randomWords) {
    var count = 0

    fun select(): String {
        val index = count++ % words.size
        return words[index] + if(count / words.size > 1) count / words.size else ""
    }
}