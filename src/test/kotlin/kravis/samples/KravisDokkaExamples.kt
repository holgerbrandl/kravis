package kravis.samples

import krangl.sleepData
import krangl.sleepPatterns
import kravis.*
import java.io.File


/**
 * @author Holger Brandl
 */

fun doBarChart() {
    sleepData
        .plot(x="vore")
        .geomBar()
        .show()
}


fun doScatter() {

    // add layers
    sleepData.plot(x = "brainwt", y = "bodywt", alpha = "sleep_total")
        .geomPoint()
        .scaleXLog10()
        .scaleYLog10("labels" to "comma".asRExpression)
        .title("Correlation of body and brain weight")
        .save(File.createTempFile("kravis",".png"))
//        .open()
}

fun iteratorAPI() {
    // using the famous sleep patterns dataset
    sleepPatterns.plot(x = { sleep_total }, y = { sleep_cycle }).geomBar()

    // using a custom data class
    data class Person(val name: String, val male: Boolean, val heightCm: Int, val weightKg: Double)

    val persons = listOf(
        Person("Max", true, 192, 80.3),
        Person("Anna", false, 162, 56.3),
        Person("Maria", false, 172, 66.3)
    )

    persons.plot(x = { heightCm }, y = { weightKg }, color = { male }).geomPoint().show()

}

fun main() {
    //    doBarChart()
//    doScatter()
    iteratorAPI()
}

