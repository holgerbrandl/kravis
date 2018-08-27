package com.github.holgerbrandl.kravis.spec

import com.github.holgerbrandl.kravis.SizeAdjustProxy
import com.github.holgerbrandl.kravis.spec.MarkType.guess
import com.github.salomonbrys.kotson.addProperty
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject
import com.squareup.moshi.Moshi
import krangl.ArrayUtils
import krangl.DataFrame
import krangl.DataFrameRow
import krangl.dataFrameOf
import java.awt.Color
import java.util.*

/**
 * @author Holger Brandl
 */

// see https://medium.com/@dumazy/writing-dsls-in-kotlin-part-2-cd9dcd0c4715


enum class MarkType { bar, circle, square, tick, line, area, point, rule, text, rect, guess;

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
            rect -> TODO()
            else -> TODO()
        }
    }

    fun resolve(vlBuilder: VLBuilder<*>): MarkType {

        val dataTypes = vlBuilder.encodings.map { it.dataType }

        val actualMark = when {
            this != guess -> this
            vlBuilder.encodings.any { it.bin != null } -> MarkType.bar
            dataTypes.take(2).all { it == Type.quantitative } -> MarkType.point
            else -> MarkType.bar
        }

        return actualMark
    }
}

enum class EncodingChannel {
    // Position Channels
    x,
    y,

    // what are those?
    //x2, y2,

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


class Encoding(val encoding: EncodingChannel,
               val data: Lazy<Iterable<Any?>>?,  // can be null for aggregate columns
               val type: Type? = null,  // can be null for aggregate columns
               val label: String = encoding.label, // this is not the original spec!
               val axis: Axis? = null,
               val bin: Boolean? = null,
               val binParams: BinParams? = null,
               val scale: Scale? = null,
               val aggregate: Aggregate? = null,
               val value: Any? = null  // can be null for aggregate columns
) {

    fun toJson(): Pair<String, JsonObject> {

        // render axis


        //https://stackoverflow.com/questions/41861449/kotlin-dsl-for-creating-json-objects-without-creating-garbage
        val encProps = if (data != null) jsonObject(
            "field" to label,
            "type" to dataType.toString().replace("2", "")
        ) else if (value != null) {
            jsonObject(
                "value" to valueAsString(value)
            )
        } else {
            jsonObject()
        }

        if (binParams != null) encProps.add("bin", binParams.toJson())
        else if (bin != null) encProps.addProperty("bin", bin)

        if (aggregate != null) encProps.addProperty("aggregate", aggregate.toString())
        if (axis != null) encProps.addProperty("axis", jsonObject("title" to axis.title))
        if (scale != null) encProps.addProperty("scale", scale.toJson())


        return encoding.toString() to encProps
        //        return encBuilder.toString().run { substring(1, this.length - 1) }
    }

    private fun valueAsString(value: Any) = when (value) {
        is Color -> "#" + Integer.toHexString(value.getRGB()).substring(2);
        else -> value.toString()
    }


    val dataType: Type?
        get() {
            if (type != null) return type

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


inline fun <reified T> isOfType(items: Iterable<Any?>): Boolean {
    val it = items.iterator()

    while (it.hasNext()) {
        if (it.next() is T) return true
    }

    return false
}

@VegaLiteDSL
class VLDataFrameBuilder(val dataFrame: DataFrame) : VLBuilder<DataFrameRow>(dataFrame.rows) {

    // this is dataframe only, mayebe we could have a separate builder for it
    fun encoding(
        channel: EncodingChannel,
        column: String,
        type: Type? = null,
        axis: Axis? = null,
        aggregate: Aggregate? = null,
        scale: Scale? = null,
        bin: Boolean? = null,
        binParams: BinParams? = null
    ) {
        val data = lazy { dataFrame[column].values().asIterable() }
        //        val data = lazy { objects.map { (it as DataFrameRow)[column] } }
        Encoding(channel, data, type, column, axis, bin, binParams, scale, aggregate).apply {
            encodings.add(this)
        }
    }

    // creates overload ambiguity
    //    fun encoding(
    //        channel: EncodingChannel,
    //        axis: Axis? = null,
    //        aggregate: Aggregate? = null,
    //        scale: Scale? = null,
    //        bin: Boolean? = null,
    //        columnExtract: () -> String
    //    ) {
    //        val data = lazy { dataFrame[columnExtract()].values().asIterable() }
    //        //        val data = lazy { objects.map { (it as DataFrameRow)[column] } }
    //        Encoding(channel, data, columnExtract(), axis, bin, scale, aggregate).apply {
    //            encodings.add(this)
    //        }
    //    }
}

@VegaLiteDSL
open class VLBuilder<T>(val objects: Iterable<T>) {

    var mark: Mark = Mark(guess)

    var title: String = ""

    val encodings = emptyList<Encoding>().toMutableList()

    fun axis(block: AxisBuilder.() -> Unit) = axes.add(AxisBuilder().apply(block).build())

    private val axes = setOf<Axis>().toMutableSet()
    private val foo = listOf(1, 2, 3)


    fun mark(mark: MarkType) {
        this.mark = Mark(mark)
    }

    //https:
    // veg
    // a.github.io/vega-lite/docs/channel.html
    //    @JvmOverloads
    fun encoding(
        channel: EncodingChannel,
        axis: Axis? = null,
        label: String = channel.label, // this is not the original spec!
        type: Type? = null,
        aggregate: Aggregate? = null,
        scale: Scale? = null,
        bin: Boolean? = null,
        binParams: BinParams? = null,
        value: Any? = null,
        extractor: PropExtractor<T>? = null
    ) {

        val data = if (value == null && extractor != null) lazy { objects.map { extractor(it, it) } } else null
        Encoding(channel, data, type, label, axis, bin, binParams, scale, aggregate, value).apply {
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

    fun buildJson(): String {
        //        show("hallo simonm")
        // here comes the hard part:
        // we need to write the json spec of the plot

        // https://medium.com/square-corner-blog/kotlins-a-great-language-for-json-fcd6ef99256b


        // or we could use json
        //        val dataFile = File.createTempFile("kravis", ".tsv")


        val df = encodings.filter {
            it.data != null
        }.map { enc ->
            ArrayUtils.handleListErasure(enc.label, enc.data!!.value.toList())
        }.let { dataFrameOf(*it.toTypedArray()) }.apply {
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

        val jsonSpecNew = jsonObject(
            "${'$'}schema" to "https://vega.github.io/schema/vega-lite/v2.json",
            "data" to jsonObject(
                "url" to dataURL,
                "format" to jsonObject(
                    "type" to "json"
                )
            ),
            "mark" to mark.toJson(this),
            "encoding" to jsonObject(encodings.map { it.toJson() })
        )

        //        return encBuilder.toString().run { substring(1, this.length - 1) }
        return jsonSpecNew.toString()
    }
}


//fun <T : Node> opcr(parent: EventTarget, node: T, op: (T.() -> Unit)? = null): T {
//    parent.addChildIfPossible(node)
//    op?.invoke(node)
//    return node
//}


////https://vega.github.io/vega-lite/docs/mark.html#mark-def
//fun <T> VLBuilder<T>.mark(type: MarkType, style: Map<String, String> = type.defaultStyle()) {
//    mark = type
//}


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




typealias PropExtractor<T> = T.(T) -> Any?

data class Axis(val title: String? = null) {

}

internal val guess = Mark(guess)


data class Mark(val type: MarkType, val filled: Boolean? = null) {

    fun toJson(vlBuilder: VLBuilder<*>): JsonObject {
        val data = listOf<Pair<String, Any>>(
            "type" to type.resolve(vlBuilder).toString()
        ).toMutableList()

        // override fill default
        if (filled != null) data.add("filled" to filled)

        return jsonObject(data.asSequence())
    }
}

/** See https://vega.github.io/vega-lite/docs/bin.html#bin-parameters */
data class BinParams(val maxBins: Int? = null) {

    fun toJson(): JsonObject {
        val data = listOf<Pair<String, Any>>(
            "maxBins" to maxBins!!
        ).toMutableList()

        // override fill default
        //        if (filled != null) data.add("filled" to filled)

        return jsonObject(data.asSequence())
    }
}

/** See https://vega.github.io/vega-lite/docs/scale.html#type */
enum class ScaleType {
    // continuous scales
    Linear,
    Pow, Sqrt, Log, Time, UTC, Sequential,
    // discrete scales
    Band,
    Point,
    // discretize scales
    Ordinal,
    Nominal
}

/** See https://vega.github.io/vega-lite/docs/scale.html */
data class Scale(val type: ScaleType? = null, val zero: Boolean? = null) {

    // use built in serialization here if possible

    fun toJson(): JsonObject {
        val data = listOf<Pair<String, Any>>().toMutableList()


        if (type != null) data.add("type" to type.toString().toLowerCase())
        if (zero != null) data.add("zero" to zero)

        return jsonObject(data.asSequence())
    }
}


// helper methods
fun <T : Any> plotOf(objects: Iterable<T>, op: (VLBuilder<T>.() -> Unit)? = null): VLBuilder<T> {
    return VLBuilder(objects).apply { op?.invoke(this) }
}

fun plotOf(dataFrame: DataFrame, op: (VLDataFrameBuilder.() -> Unit)? = null): VLDataFrameBuilder {
    return VLDataFrameBuilder(dataFrame).apply { op?.invoke(this) }
}
