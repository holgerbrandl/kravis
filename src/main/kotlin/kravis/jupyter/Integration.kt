package kravis.jupyter

import kravis.GGPlot
import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration

// reference https://github.com/Kotlin/kotlin-jupyter/blob/master/libraries/kravis.json

@JupyterLibrary
internal class Integration : JupyterIntegration() {
    override fun Builder.onLoaded() {
        import("kravis.*")
        render<GGPlot> { it.show() }
    }
}