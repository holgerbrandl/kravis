package kravis.demo

import krangl.irisData
import kravis.Aesthetic
import kravis.demo.IrisData.*
import kravis.geomPoint
import kravis.nshelper.ggplot

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


val irisScatter by lazy { irisData.ggplot(PetalLength to Aesthetic.x, PetalWidth to Aesthetic.y, Species to Aesthetic.color).geomPoint() }
