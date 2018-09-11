package kravis.dokka

import krangl.sleepData
import krangl.sleepPatterns
import kravis.*
import kravis.ggplot.open


/**
 * @author Holger Brandl
 */

fun doBarChart() {

    fun main(args: Array<String>) {

        //        sleepData
        //            .plot()
        //            .x { "genus" } // or even do on the fly data transformation using krangl's table expressions
        //            //            .y { "Sepal.Length" }
        //            //            .color { "Species" }
        //            //            .title("Iris Flowers")
        //            //            .addBars(mapping{
        //            //
        //            //            })
        //            .show()
    }
}


fun doScatter() {

    // add layers
    sleepData.ggplot(x = "brainwt", y = "bodywt", alpha = "sleep_total")
        .geomPoint()
        .scaleXLog10()
        .scaleYLog10("labels" to "comma")
        .title("Correlation of body and brain weight")
        .open()
}

fun iteratorAPI() {
    sleepPatterns.ggplot(x = { sleep_total }, y = { sleep_cycle }).geomBar()

}

fun main(args: Array<String>) {
    //    doBarChart()
    doScatter()
}

