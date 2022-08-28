package kravis.demo

import krangl.DoubleCol
import krangl.IntCol
import krangl.dataFrameOf
import kravis.Aesthetic
import kravis.demo.IrisData.*
import kravis.geomPoint
import kravis.nshelper.plot
import org.jetbrains.kotlinx.dataframe.datasets.irisData

/**
 * @author Holger Brandl
 */

enum class IrisData {
    SepalLength, SepalWidth, PetalLength, PetalWidth, Species;

    override fun toString(): String {
        return super.toString().replace("(.)([A-Z])".toRegex()) {
            it.groupValues[1] + "." + it.groupValues[2]
        }
    }
}


val irisScatter by lazy {
    irisData.plot(
        PetalLength to Aesthetic.x,
        PetalWidth to Aesthetic.y,
        Species to Aesthetic.color
    ).geomPoint()
}




/**
 * Level of Lake Huron 1875-1972
 *
 * Annual measurements of the level, in feet, of Lake Huron 1875-1972.
 *
 * Source:   Brockwell, P. J. and Davis, R. A. (1991).  _Time Series and
 * Forecasting Methods_.  Second edition. Springer, New York. Series A, page 555.
 */
val lakeHuron by lazy {
    val levels = listOf(
        580.38, 581.86, 580.97, 580.80, 579.79, 580.39, 580.42, 580.82, 581.40, 581.32,
        581.44, 581.68, 581.17, 580.53, 580.01, 579.91, 579.14, 579.16, 579.55, 579.67,
        578.44, 578.24, 579.10, 579.09, 579.35, 578.82, 579.32, 579.01, 579.00, 579.80,
        579.83, 579.72, 579.89, 580.01, 579.37, 578.69, 578.19, 578.67, 579.55, 578.92,
        578.09, 579.37, 580.13, 580.14, 579.51, 579.24, 578.66, 578.86, 578.05, 577.79,
        576.75, 576.75, 577.82, 578.64, 580.58, 579.48, 577.38, 576.90, 576.94, 576.24,
        576.84, 576.85, 576.90, 577.79, 578.18, 577.51, 577.23, 578.42, 579.61, 579.05,
        579.26, 579.22, 579.38, 579.10, 577.95, 578.12, 579.75, 580.85, 580.41, 579.96,
        579.61, 578.76, 578.18, 577.21, 577.13, 579.10, 578.25, 577.91, 576.89, 575.96,
        576.80, 577.68, 578.38, 578.52, 579.74, 579.31, 579.89, 579.96
    )

    dataFrameOf(IntCol("year", (1875..1972).toList()), DoubleCol("level", levels))
}