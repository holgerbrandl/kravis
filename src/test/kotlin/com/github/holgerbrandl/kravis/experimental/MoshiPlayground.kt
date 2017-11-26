package com.github.holgerbrandl.kravis.experimental

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi

/**
 * @author Holger Brandl
 */

fun main(args: Array<String>) {
    class Foo {
        val name = "hi moshi"
        private val bar = listOf<String>().toMutableList()

        fun addBarElem(s: String) = bar.add(s)
    }


    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    val myFoo = Foo()
    //    myFoo.bar.addAll(listOf("this", "should", "be", "jsonized"))
    listOf("this", "should", "be", "jsonized").map { myFoo.addBarElem(it) }

    println("the json is:" + moshi.adapter(Foo::class.java).toJson(myFoo))
}