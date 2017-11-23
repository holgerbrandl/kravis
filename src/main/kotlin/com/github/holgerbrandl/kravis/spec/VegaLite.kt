package com.github.holgerbrandl.kravis.spec

import com.github.holgerbrandl.kravis.spec.Encoding.*
import com.github.holgerbrandl.kravis.spec.Mark.bar
import java.time.LocalDate

/**
 * @author Holger Brandl
 */


class VLBuilder<T> {

    lateinit var mark: Mark
    val encodings = emptyList<Encoding>().toMutableList()

    var title: String = ""

    val enc2data = emptyMap<Encoding, T.() -> Any>().toMutableMap()


    fun render() {


    }
}


fun <T : Any> plotOf(objects: List<T>, op: (VLBuilder<T>.() -> Unit)? = null): VLBuilder<T> {
    return VLBuilder<T>().apply { op?.invoke(this) }
}


//fun <T : Node> opcr(parent: EventTarget, node: T, op: (T.() -> Unit)? = null): T {
//    parent.addChildIfPossible(node)
//    op?.invoke(node)
//    return node
//}

enum class Mark { bar, circle, square, tick, line, area, point, rule, text;

    fun defaultStyle(): Map<String, String> {
        when (this) {
            bar -> TODO()
            Mark.circle -> TODO()
            square -> TODO()
            tick -> TODO()
            line -> TODO()
            area -> TODO()
            point -> TODO()
            rule -> TODO()
            text -> TODO()
        }
    }
}

//https://vega.github.io/vega-lite/docs/mark.html#mark-def
fun <T> VLBuilder<T>.mark(type: Mark, style: Map<String, String> = type.defaultStyle()) {
    mark = type
}


enum class Encoding {
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


class Axis {

}

fun main(args: Array<String>) {

    data class User(val name: String, val birthDay: LocalDate, val sex: String, val height: Double) {}

    val friends = listOf(
        User("Max", LocalDate.parse("2007-12-03"), "M", 1.89),
        User("Jane", LocalDate.parse("1980-07-03"), "F", 1.67),
        User("Anna", LocalDate.parse("1992-07-03"), "F", 1.32)
    )


    val plot = plotOf(friends) {
        mark(bar)

        encoding(x) {
            //            encodings // todo should not be visible here
            height
        }
        encoding(y) {
            birthDay.year
        }

        encoding(color) {
            sex
        }
    }

    plot.render()

    // build same plot but with more traditional api


    //    plotOf(friends)
    //        .setX("year of birth"){ birthday }
    //        .setY(){}
    //        ...


}

//https://vega.github.io/vega-lite/docs/encoding.html
fun <T> VLBuilder<T>.encoding(
    encoding: Encoding,
    label: String = encoding.label, // this is not the original spec! Should we really keep it here?
    axis: Axis = Axis(),
    extractor: T.() -> Any
) {
    encodings.add(encoding)
    enc2data.put(encoding, extractor)
}

