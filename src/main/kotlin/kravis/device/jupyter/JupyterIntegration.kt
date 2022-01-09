package kravis.device.jupyter

import kravis.GGPlot
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration

// reference https://github.com/Kotlin/kotlin-jupyter/blob/master/libraries/kravis.json

@Suppress("unused")
internal class JupyterIntegration : JupyterIntegration() {
    override fun Builder.onLoaded() {
        import("kravis.*")
        render<GGPlot> { it.show() }
    }
}