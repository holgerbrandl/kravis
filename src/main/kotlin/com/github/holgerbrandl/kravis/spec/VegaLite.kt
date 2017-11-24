package com.github.holgerbrandl.kravis.spec

import com.github.holgerbrandl.kravis.StaticHTMLRenderer
import com.github.holgerbrandl.kravis.spec.Mark.guess
import com.squareup.moshi.Moshi
import krangl.ArrayUtils
import krangl.asDataFrame
import java.io.File
import java.util.*

/**
 * @author Holger Brandl
 */

// see https://medium.com/@dumazy/writing-dsls-in-kotlin-part-2-cd9dcd0c4715


enum class Mark { bar, circle, square, tick, line, area, point, rule, text, guess;

    fun defaultStyle(): Map<String, String> {
        return when (this) {
            bar -> TODO()
            Mark.circle -> TODO()
            square -> TODO()
            tick -> TODO()
            line -> TODO()
            area -> TODO()
            point -> TODO()
            rule -> TODO()
            text -> TODO()
            else -> TODO()
        }
    }

    fun toJson(vlBuilder: VLBuilder<*>): String {

        val dataTypes = vlBuilder.encodings.map { it.dataType }

        val actualMark = when {
            dataTypes.take(2).all { it == Type.quantitative } -> Mark.point
            else -> Mark.bar
        }

        return """
            "mark": "$actualMark"
        """.trimIndent().trim()
    }
}

enum class EncodingChannel {
    // Position Channels
    x,
    y, x2, y2,

    // Mark Properties Channels
    color,
    opacity, size, shape,

    // Text and Tooltip Channels
    text,
    tooltip,
    // Order Channel
    order,

    // Level of Detail Channel
    detail,

    // Facet Channels
    row,
    column;

    val label: String = this.toString()
}


@DslMarker
annotation class VegaLiteDSL


//@VegaLiteDSL
//class EncodingBuilder<T>(val encoding: Encoding) {
//    val label: String = encoding.label // this is not the original spec! Should we really keep it here?
//    val axis: Axis = Axis()
//    lateinit var extractor: PropExtractor<T>
//
//    fun build(): Pair<Encoding, PropExtractor<T>> {
//        return encoding to extractor
//    }
//}

enum class Type { quantitative, temporal, ordinal2, nominal; }

private val s = "quantitative"

class Encoding<T>(val encoding: EncodingChannel,
                  val label: String = encoding.label, // this is not the original spec!
                  val axis: Axis = Axis(),
                  val data: Lazy<List<Any?>>) {


    fun toJson() = """
        "${encoding.label}": {
          "field": "${label}",
          "type": "${dataType}"
        }
        """.trimIndent()

    internal val dataType: Type
        get() {
            val datas = data.value
            return when {
                isOfType<Number>(datas) -> Type.quantitative
                isOfType<String>(datas) -> Type.nominal
                else -> throw UnsupportedOperationException()
            }
        }
}


inline fun <reified T> isOfType(items: List<Any?>): Boolean {
    val it = items.iterator()

    while (it.hasNext()) {
        if (it.next() is T) return true
    }

    return false
}


@VegaLiteDSL
class VLBuilder<T>(val objects: Iterable<T>) {

    var mark: Mark = guess

    var title: String = ""

    val encodings = emptyList<Encoding<T>>().toMutableList()

    fun axis(block: AxisBuilder.() -> Unit) = axes.add(AxisBuilder().apply(block).build())

    private val axes = setOf<Axis>().toMutableSet()
    private val foo = listOf(1, 2, 3)


    //https:
    // vega.github.io/vega-lite/docs/channel.html
    fun encoding(
        channel: EncodingChannel,
        label: String = channel.label, // this is not the original spec!
        axis: Axis = Axis(),
        extractor: PropExtractor<T>
    ) {
        val data = lazy { objects.map { extractor(it) } }
        Encoding<T>(channel, label, axis, data).apply { encodings.add(this) }
    }


    fun render() {
        //        show("hallo simonm")
        // here comes the hard part:
        // we need to write the json spec of the plot

        // https://medium.com/square-corner-blog/kotlins-a-great-language-for-json-fcd6ef99256b


        val dataFile = File.createTempFile("kravis", ".tsv")


        val df = encodings.map { enc ->

            ArrayUtils.handleListErasure(enc.label, enc.data.value)
        }.asDataFrame().apply {
            //            writeCSV(dataFile, CSVFormat.TDF)
        }

        val moshi = Moshi.Builder()
            //            .add(KotlinJsonAdapterFactory())
            .build()

        val adapter = moshi.adapter(List::class.java)
        adapter.toJson(listOf(1, 2, 3))


        val dfJson = adapter.toJson(df.rows.toList())
        //https://stackoverflow.com/questions/13109588/base64-encoding-in-java
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs
        val base64Enc = Base64.getEncoder().encode(dfJson.toByteArray());
        val dataURL = "data:text/plain;base64," + String(base64Enc)


        // https://github.com/square/moshi

        //        println(moshi.adapter(DataFrame::class.java).toJson(df))


        val jsonSpec = "{" + listOf(
            """"${'$'}schema": "https://vega.github.io/schema/vega-lite/v2.json"""",
            //            """"data": {"url": "${dataFile.toURI().toURL()}", "format" : { "type":"tsv"}}""",
            """"data": {"url": "${dataURL}", "format" : { "type":"json"}}""",
            mark.toJson(this),
            """
            "encoding": {
                ${encodings.map { it.toJson() }.joinToString(",")}
            }
            """.trimIndent()
        ).joinToString(",\n") + "}"


        print(jsonSpec)
        StaticHTMLRenderer(jsonSpec).openInChrome()
        //        show(StaticHTMLRenderer(jsonSpec).pageHTML())

    }
}


//fun <T : Node> opcr(parent: EventTarget, node: T, op: (T.() -> Unit)? = null): T {
//    parent.addChildIfPossible(node)
//    op?.invoke(node)
//    return node
//}


//https://vega.github.io/vega-lite/docs/mark.html#mark-def
fun <T> VLBuilder<T>.mark(type: Mark, style: Map<String, String> = type.defaultStyle()) {
    mark = type
}


// see https://vega.github.io/vega-lite/docs/axis.html#general
@VegaLiteDSL
class AxisBuilder {

    var color = "blue"
    var grid = true
    var maxExtent: Double? = null
    var minExtent: Double? = null
    //    var orient = "bottom"

    // ... other properties

    //    private val addresses = mutableListOf<Address>()
    //
    //    fun address(block: AddressBuilder.() -> Unit) {
    //        addresses.add(AddressBuilder().apply(block).build())
    //    }

    internal fun build() = Axis()
}




typealias PropExtractor<T> = T.() -> Any?

class Axis {

}


// helper methods
fun <T : Any> plotOf(objects: List<T>, op: (VLBuilder<T>.() -> Unit)? = null): VLBuilder<T> {
    return VLBuilder(objects).apply { op?.invoke(this) }
}
