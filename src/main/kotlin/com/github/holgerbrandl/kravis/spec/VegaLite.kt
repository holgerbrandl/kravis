package com.github.holgerbrandl.kravis.spec

import com.github.holgerbrandl.kravis.SizeAdjustProxy
import com.github.holgerbrandl.kravis.spec.Mark.guess
import com.github.salomonbrys.kotson.addProperty
import com.github.salomonbrys.kotson.jsonObject
import com.squareup.moshi.Moshi
import krangl.ArrayUtils
import krangl.asDataFrame
import java.util.*

/**
 * @author Holger Brandl
 */

// see https://medium.com/@dumazy/writing-dsls-in-kotlin-part-2-cd9dcd0c4715


enum class Mark { bar, circle, square, tick, line, area, point, rule, text, guess;

    fun defaultStyle(): Map<String, String> {
        return when (this) {
            bar -> TODO()
            circle -> TODO()
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
            this != guess -> this
            vlBuilder.encodings.any { it.bin != null } -> Mark.bar
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
enum class Aggregate { mean, sum, median, min, max, count; }


class Encoding<T>(val encoding: EncodingChannel,
                  val data: Lazy<List<Any?>>?,  // can be null for aggregate columns
                  val label: String = encoding.label, // this is not the original spec!
                  val axis: Axis? = null,
                  val bin: Boolean? = null,
                  val aggregate: Aggregate? = null) {

    fun toJson(): String {

        // render axis


        //https://stackoverflow.com/questions/41861449/kotlin-dsl-for-creating-json-objects-without-creating-garbage
        val encProps = jsonObject(
            "field" to label,
            "type" to dataType.toString()
        )

        if (bin != null) encProps.addProperty("bin", bin)
        if (aggregate != null) encProps.addProperty("aggregate", aggregate.toString())
        if (axis != null) encProps.addProperty("axis", jsonObject("title" to axis.title))


        val encBuilder = jsonObject(encoding.toString() to encProps)


        return encBuilder.toString().run { substring(1, this.length - 1) }
    }

    internal val dataType: Type
        get() {
            // if no data is there, it must be an aggregate and thus quantitative
            if (data == null) return Type.quantitative

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
        axis: Axis? = null,
        aggregate: Aggregate? = null,
        bin: Boolean? = null,
        extractor: PropExtractor<T>? = null
    ) {
        val data = if (extractor != null) lazy { objects.map { extractor(it) } } else null
        Encoding<T>(channel, data, label, axis, bin, aggregate).apply {
            encodings.add(this)
        }
    }


    override fun toString(): String {
        // render it as a side effect
        render()
        return super.toString()
    }

    // todo add device parameter here with sensible default
    fun render() {
        val jsonSpec = buildJson()

        //        print(jsonSpec)
        //        StaticHTMLRenderer(jsonSpec).openInChrome()

        SizeAdjustProxy.showPlot(jsonSpec)
        //        show(StaticHTMLRenderer(jsonSpec).pageHTML())
    }

    internal fun buildJson(): String {
        //        show("hallo simonm")
        // here comes the hard part:
        // we need to write the json spec of the plot

        // https://medium.com/square-corner-blog/kotlins-a-great-language-for-json-fcd6ef99256b


        // or we could use json
        //        val dataFile = File.createTempFile("kravis", ".tsv")


        val df = encodings.filter {
            it.data != null
        }.map { enc ->
            ArrayUtils.handleListErasure(enc.label, enc.data!!.value)
        }.asDataFrame().apply {
            //            writeCSV(dataFile, CSVFormat.TDF)
        }


        val moshi = Moshi.Builder()
            // see https://github.com/square/moshi/issues/396#issuecomment-346705574
            //            .add(KotlinJsonAdapterFactory())
            .build()

        val adapter = moshi.adapter(List::class.java)
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
                    ${encodings.map { it.toJson() }.joinToString(",\n")}
                }
                """.trimIndent()
        ).joinToString(",\n") + "}"
        return jsonSpec
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

class Axis(val title: String? = null) {

}


// helper methods
fun <T : Any> plotOf(objects: List<T>, op: (VLBuilder<T>.() -> Unit)? = null): VLBuilder<T> {
    return VLBuilder(objects).apply { op?.invoke(this) }
}
