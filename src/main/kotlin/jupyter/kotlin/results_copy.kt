@file:Suppress("unused")
package jupyter.kotlin

fun mimeResult(vararg mimeToData: Pair<String, String>): MimeTypedResult = MimeTypedResult(mapOf(*mimeToData))
fun textResult(text: String): MimeTypedResult = MimeTypedResult(mapOf("text/plain" to text))

class MimeTypedResult(mimeData: Map<String, String>, var isolatedHtml: Boolean = false) : Map<String, String> by mimeData

val ReplOutputs = mutableListOf<Any?>()