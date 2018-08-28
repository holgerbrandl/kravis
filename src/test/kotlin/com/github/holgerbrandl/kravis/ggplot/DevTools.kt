package com.github.holgerbrandl.kravis.ggplot

import java.io.File

/**
 * @author Holger Brandl
 */
object PrepareArgList {
    @JvmStatic
    fun main(args: Array<String>) {
        File("/Users/brandl/Library/Preferences/IntelliJIdea2018.2/scratches/scratch_5.txt").useLines { line ->
            line.map { it.split(".").map { it.capitalize() }.joinToString("") }
                .toList()
                .flatMap { it.split(", ") }
                .map { it.replace("= NULL", "") }
                .map { "val $it: Any? = null" }
                .forEach(::println)
        }
    }
}