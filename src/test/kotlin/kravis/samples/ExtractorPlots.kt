package kravis.samples

import krangl.SleepPattern
import kravis.plot
import org.jetbrains.kotlinx.dataframe.api.head
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.api.toListOf
import org.jetbrains.kotlinx.dataframe.datasets.sleepData

internal object ExtractorPlots {
    @JvmStatic
    fun main(args: Array<String>) {
        val sleepPatterns = sleepData.toListOf<SleepPattern>()

        "to" to { it: SleepPattern -> it.awake }

        with(sleepPatterns) { listOf("awake" to map { it.awake }, "genus" to map { it.genus }) }.toDataFrame()
//        sleepPatterns.deparseRecords { mapOf("awake" to it.awake, "genus" to it.genus) }

        sleepPatterns.toDataFrame()
            .plot()


        with(sleepPatterns) {
            listOf(
                "foo" to map { it.awake },
                "bar" to map { it.brainwt?.plus(3) }
            )
        }.toDataFrame()
            .head()
            .print()
    }
}
