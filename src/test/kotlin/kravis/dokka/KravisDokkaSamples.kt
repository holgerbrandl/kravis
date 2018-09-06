package kravis.dokka

import krangl.sleepData
import kravis.geomPoint
import kravis.ggplot
import kravis.ggplot.open
import kravis.scaleXLog10
import kravis.scaleYLog10


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

fun main(args: Array<String>) {
    //    doBarChart()
    doScatter()
}