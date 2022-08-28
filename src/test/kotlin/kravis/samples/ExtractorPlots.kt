package kravis.samples

import krangl.*
import kravis.plot
import org.jetbrains.kotlinx.dataframe.api.asKotlinDF

internal object ExtractorPlots {
    @JvmStatic
    fun main(args: Array<String>) {
        val sleepPatterns = krangl.sleepData.rowsAs<SleepPattern>()

        "to" to { it: SleepPattern -> it.awake }
        sleepPatterns.deparseRecords { mapOf("awake" to it.awake, "genus" to it.genus) }

        sleepPatterns.asDataFrame().asKotlinDF().plot()


        krangl.sleepPatterns.deparseRecords(
            "foo" with { awake },
            "bar" with { it.brainwt?.plus(3) }
        ).head().print()
    }
}
