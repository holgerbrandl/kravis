import kravis.geomHistogram
import kravis.plot
import org.jetbrains.kotlinx.dataframe.datasets.irisData

object PersonsDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        irisData.plot(x = "Sepal.Length")
            .geomHistogram()
            .show()
    }
}