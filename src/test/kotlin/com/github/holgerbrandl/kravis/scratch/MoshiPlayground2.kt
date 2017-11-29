package com.github.holgerbrandl.kravis.scratch

import com.squareup.moshi.*


/**
 * @author Holger Brandl
 */

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class HexColor

class ColorAdapter {
    @ToJson
    fun toJson(@HexColor rgb: String): String {
        return rgb + "::HEXHEX"
    }

    @FromJson
    @HexColor
    fun fromJson(rgb: String): String {
        return rgb.removeSuffix("::HEXHEX")
    }
}

fun main(args: Array<String>) {

    class Foo2 {
        val name = "hi moshi"

        @HexColor
        val tester = "tester"

        val bar = listOf<String>().toMutableList()

        fun addBarElem(s: String) = bar.add(s)
    }

    //    KotlinJsonAdapterFactory().

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(ColorAdapter())
        .build()


    val myFoo = Foo2()
    myFoo.bar.addAll(listOf("this", "should", "be", "jsonized"))

    println("the json is:" + moshi.adapter(Foo2::class.java).toJson(myFoo))
}