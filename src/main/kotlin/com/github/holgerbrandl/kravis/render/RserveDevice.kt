package com.github.holgerbrandl.kravis.render

import com.github.holgerbrandl.kravis.GGPlot
import java.io.File

/**
 * @author Holger Brandl
 */
class RserveDevice : REngine() {
    override fun render(plot: GGPlot, format: File): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun runRScript(script: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

internal fun GGPlot.saveTempFile(format: String = ".png") = save(createTempFile(suffix = format))
