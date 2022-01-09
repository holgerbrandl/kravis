package kravis.render

import krangl.writeTSV
import kravis.GGPlot
import kravis.SessionPrefs
import java.awt.Dimension
import java.io.File
import java.nio.file.Path
import java.util.*
import kotlin.io.path.copyTo
import kotlin.io.path.exists
import kotlin.io.path.extension

/**
 * Note: The default container won't provide svg support. For svg support use `holgerbrandl/kravis_core:3.5.1`
  */
class Docker(var image: String = "rocker/tidyverse:3.5.1") : AbstractLocalRenderEngine() {

    val KRAVIS_DOCKER_CACHE_DIR by lazy { File(System.getProperty("user.home")!!, ".kravis").apply { mkdir() } }
    val KRAVIS_DOCKER_DATA_CACHE_DIR by lazy { File(KRAVIS_DOCKER_CACHE_DIR, "data").apply { mkdir() } }


    private val DOCKER_PLOT_MNT = """/plot"""
    private val DOCKER_DATA_MNT = """/data"""

    override fun render(plot: GGPlot, outputFile: Path, preferredSize: Dimension?): Path {
        // save all the data
        // todo hash dfs where possible to avoid IO


        val data2Files = plot.dataRegistry.mapValues {
            val dataFile = File(KRAVIS_DOCKER_DATA_CACHE_DIR, it.value.hashCode().toString() + ".txt")

            dataFile.deleteOnExit()

            if (!dataFile.isFile) {
                it.value.writeTSV(dataFile)
            }

            dataFile
        }

        val dataIngest = data2Files.map { (dataVar, file) ->
            """${dataVar} = read_tsv("$DOCKER_DATA_MNT/${file.name}")"""
        }.joinToString("\n")


        // setup directory structure
        // val tmpDir = createTempDir("kravis").apply { deleteOnExit()} // would be better but can not be mounted by docker
        val plotDir = KRAVIS_DOCKER_CACHE_DIR.let { cacheDir ->
            val timeStamp = "plot_" + UUID.randomUUID().toString().substring(0, 8)
            File(cacheDir, timeStamp).apply { mkdir(); deleteOnExit() }
        }

        // in theory we could try to bind the output-path directly in the container, but this may file depending on the fs
        // that's why we render into a file we can certainly write to and copy it to the destination
        val plotOutputName = "$DOCKER_PLOT_MNT/plot." + outputFile.extension
        val rScript = compileScript(plot, dataIngest, plotOutputName, preferredSize)

        File(plotDir, "plot.R").writeText(rScript)


        val result = dockerRun(plotDir, KRAVIS_DOCKER_DATA_CACHE_DIR)
        if (result.exitCode != 0) {
            throw LocalRenderingFailedException(rScript, result)
        }

        val tmpOutputFile = File(plotDir, File(plotOutputName).name).toPath()
        require(tmpOutputFile.exists()) { System.err.println("Image generation failed") }
        //        require(outputFile.exists()) { System.err.println("Image generation failed") }

        tmpOutputFile.copyTo(outputFile, overwrite = true)

        return outputFile
    }


    fun dockerRun(plotDirWithScript: File, dataCacheDir: File): RUtils.CmdResult {
       RUtils.requireInPath("docker")

        //not really  necessary, because would docker do this automatcially, but since this will take a while we want to log it
        if(RUtils.evalBash("docker images -q $image").sout().isBlank()) {
            System.err.println("image '$image' is not yet present on your machine. Pulling image with `docker pull $image`...")
            RUtils.evalCmd("docker", listOf("pull", image), showOutput = true)
        }

        val dockerCmd = """run -v ${plotDirWithScript}:$DOCKER_PLOT_MNT -v ${dataCacheDir}:$DOCKER_DATA_MNT --rm ${image} R -f $DOCKER_PLOT_MNT/plot.R"""

        return RUtils.evalCmd("docker", dockerCmd.split(" "))
    }

}


fun main() {
    //    Docker().runRScript("cat(1+1)").apply { println(this) }
    SessionPrefs.RENDER_BACKEND = Docker()

}

