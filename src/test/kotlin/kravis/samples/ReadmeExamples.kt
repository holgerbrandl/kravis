import krangl.irisData
import krangl.toDoubleMatrix
import kravis.geomHistogram
import kravis.plot

object PersonsDemo{
    @JvmStatic
    fun main(args: Array<String>) {
            irisData.plot(x="Sepal.Length").geomHistogram().show()
    }
}