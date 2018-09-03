package com.github.holgerbrandl.kravis

import java.io.*

/**
 * @author Holger Brandl
 */

sealed class REngine {
    internal abstract fun runRScript(script: String)
}

class LocalR : REngine() {
    override fun runRScript(script: String) {
        val scriptFile = createTempFile(suffix = ".R").apply { writeText(script) }

        val evalCmd = RUtils.evalCmd("/usr/local/bin/R", listOf("--vanilla", "--quiet", "--slave", "-f", scriptFile.absolutePath))
    }
}

var R_ENGINE = LocalR()


object RUtils {


    data class CmdResult(val exitCode: Int, val stdout: Iterable<String>, val stderr: Iterable<String>) {
        fun sout() = stdout.joinToString("\n").trim()

        fun serr() = stderr.joinToString("\n").trim()
    }

    fun runRScript(script: String): CmdResult {
        val scriptFile = createTempFile(suffix = ".R").apply { writeText(script) }

        return evalCmd("/usr/local/bin/R", listOf("--vanilla", "--quiet", "--slave", "-f", scriptFile.absolutePath))
    }


    fun evalCmd(executable: String, args: List<String>, showOutput: Boolean = false,
                redirectStdout: File? = null, redirectStderr: File? = null): CmdResult {

        try {
            var pb = ProcessBuilder(*(arrayOf(executable) + args)) //.inheritIO();
            pb.directory(File("."));
            var p = pb.start();

            val outputGobbler = StreamGobbler(p.getInputStream(), if (showOutput) System.out else null)
            val errorGobbler = StreamGobbler(p.getErrorStream(), if (showOutput) System.err else null)

            // kick them off
            errorGobbler.start()
            outputGobbler.start()

            // any error???
            val exitVal = p.waitFor()
            return CmdResult(exitVal, outputGobbler.sb.lines(), errorGobbler.sb.lines())
        } catch (t: Throwable) {
            throw RuntimeException(t)
        }
    }


    internal class StreamGobbler(var inStream: InputStream, val printStream: PrintStream?) : Thread() {
        var sb = StringBuilder()

        override fun run() {
            try {
                val isr = InputStreamReader(inStream)
                val br = BufferedReader(isr)
                for (line in br.linesJ7()) {
                    sb.append(line!! + "\n")
                    printStream?.println(line)
                }
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }

        private fun BufferedReader.linesJ7(): Iterable<String> = lineSequence().toList()


        val output: String get() = sb.toString()
    }

}
