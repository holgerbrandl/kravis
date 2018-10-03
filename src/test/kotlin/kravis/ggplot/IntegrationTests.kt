package kravis.ggplot

import org.junit.Test

/**
 * @author Holger Brandl
 */
class IntegrationTests {

    @Test
    fun `it should run all dokka examples without exception`() {
        kravis.samples.doBarChart()
        kravis.samples.iteratorAPI()
        kravis.samples.doScatter()
    }

    @Test
    fun `it should run the readme examples`() {
        // todo we need to find a way to extract the readme examples
    }
}