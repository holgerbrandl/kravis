package kravis.render

import krangl.*
import org.rosuda.REngine.*
import org.rosuda.REngine.Rserve.RConnection
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

/**
 * @author Holger Brandl
 */


fun REXP.toPrettyString(): String {
    val o = this
    //assert o instanceof REXP : "Not an REXP object";
    if (o is REXPNull) {
        return "NULL"

    } else if (o.isList) {
        val l = (o as REXPGenericVector).asList()
        var s = ""
        for (k in l.keys()) {
            s = s + k + ": " + with(l.get(k)) { if (this is REXP) toPrettyString() else toString() } + "\n"
        }
        return s
    } else return if (o.isVector) {
        try {
            if (o.isRaw) {
                "[raw" + o.asBytes().size + "]"
            } else {
                val ss = o.asStrings()
                if (o.length() > 10) {
                    Arrays.asList(*arrayOf(ss[0], ss[1], "...(" + ss.size + ")...", ss[ss.size - 2], ss[ss.size - 1])).toString()
                } else {
                    Arrays.asList(*o.asStrings()).toString()
                }
            }
        } catch (ex: Exception) {
            throw ClassCastException("[toString] Cannot toString $o")
        }

    } else {
        try {
            o.asString()
        } catch (ex: Exception) {
            throw ClassCastException("[toString] Cannot toString $o")
        }

    }
}

/**
 * @author Holger Brandl
 */
//internal val NA_DOUBLE_VALUE = Double.MAX_VALUE - 1
//internal val NA_INT_VALUE = Int.MAX_VALUE - 1

// see de.mpicbg.knime.scripting.r.data.RDataFrameContainer#createDataFrame
@Suppress("ReplaceSingleLineLet")
internal fun RConnection.setTable(varName: String, data: DataFrame) {

    val col2data = data.cols.withIndex().map { (index, col) ->
        "col_$index" to when {

            col is DoubleCol -> col.asDoubles().map { if (it == null) REXPDouble.NA else it }.toDoubleArray().let { REXPDouble(it) }
            col is IntCol -> col.asInts().map { if (it == null) REXPInteger.NA else it }.toIntArray().let { REXPInteger(it) }
            col is BooleanCol -> col.asBooleans().map {
                if (it == null) {
                    REXPLogical.NA
                } else {
                    (if (it!!) 1 else 0).toByte()
                }
            }.toByteArray().let { REXPLogical(it) }
            col is StringCol -> col.asStrings().map { if (it == null) "NA" else it }.toTypedArray().let { REXPString(it) }
            else -> TODO("Unsupported type ${col::class.simpleName}")
        }
    }




    col2data.forEach { (colName, colData) ->
        assign(colName, colData)
    }


    // fix missing data in string columns
    // update missing values for string columns
    col2data.zip(data.cols).filter { it.first.second is REXPString }.forEach { (pair, kranglCol) ->
        val naIndicies = kranglCol.values().withIndex().filter { it.value == null }.map { it.index }
        voidEval("""${pair.first}[c(${naIndicies.joinToString(",")})] <- NA""")
    }


//    data.frame(cbind(1:3, c("foo", "bar", "test")))
    val createDF = varName + " = list(" + col2data.toMap().keys.joinToString(", ") + ")"
    voidEval(createDF)

    val removeString = "rm(" + col2data.toMap().keys.joinToString(", ") + ")"
//    print(eval("print(str($varName))").toPrettyString())
    voidEval(removeString)

//    val combineString = varName + " <- c(" + col2data.toMap().keys.joinToString(", ") + ")"

//    if (data.ncol > 0) {
    // combine chunks into one list
//        eval(combineString)
    // remove chunk objects
    // clean up env
    // convert list to dataframe
    // READABLE EXAMPLE:
    // attr(kIn,"row.names") <- .set_row_names(length(kIn[[1]]));
    // class(kIn) <- "data.frame";
//        	logger.debug("make dataframe");
//        eval("""data.frame(cbind(d1,d2))""")
//        eval("attr($varName, \"row.names\") <- .set_row_names(length($varName[[1]])); class($varName) <- \"data.frame\"; ")
    voidEval("""attr($varName, "row.names") <- .set_row_names(length($varName [[1]])); class($varName) <- "data.frame"; """);


    // push row names to R and assign to dataframe
//    val rowNames = data.cols.map { "`${it.name}`" }
    val colNames = data.cols.map { it.name }
//        set (varName + "_rownames", REXPString(rowNames.toTypedArray()))
    assign(varName + "_colnames", colNames.toTypedArray())
    voidEval("colnames(" + varName + ") <- " + varName + "_colnames")
    voidEval("rm(" + varName + "_rownames)")
//    }else{  // create a data frame with a given number of rows but no columns
//        note this cannot happen with krangl
//        eval(varName + " <- data.frame(matrix(nrow = " + data.nrow + ", ncol = 0))")
//    }

    // todo reenable
//    logger.debug("fix missing values")
//    val dataframeColumns = Arrays.asList(*(connection.eval("colnames($parName)") as REXPString).asStrings())
//
//    // update missing values for string columns
//    // e.g. kIn[,2][c(1,6,20,21)] <- NA
//    for (chunk in m_columnChunks.keys) {
//        for (col in m_columnChunks.get(chunk)) {
//            val missingIdxVec = "[c(" + col.getMissingIdx() + ")]"
//            connection.voidEval(parName + "[," + (dataframeColumns.indexOf(col.getName()) + 1) + "]" + missingIdxVec + " <- NA")
//        }
//    }
}

internal fun RConnection.createImageOld(script: String, width: Int, height: Int, device: String): BufferedImage? {

    // check preferences
    val useEvaluate = false

    val tempFileName = "rmPlotFile.$device"

    // create plot device on R side
    val deviceArgs = if (device == "jpeg") "quality=97," else ""

    val xp: REXP? = null

    val openDevice = "try($device('$tempFileName',$deviceArgs width = $width, height = $height))"
    //        try {
    eval(openDevice)
    //            if (xp.inherits("try-error")) { // if the result is of the class try-error then there was a problem
    //                // this is analogous to 'warnings', but for us it's sufficient to get just the 1st warning
    //                REXP w = connection.eval("if (exists('last.warning') && length(last.warning)>0) names(last.warning)[1] else 0");
    //                if (w.isString()) System.err.println(w.asString());
    //                {
    //                    throw new KnimeScriptingException("Can't open " + device + " graphics device:\n" + xp.asString());
    //                }
    //            }
    //        } catch (RserveException | REXPMismatchException e) {
    //            throw new KnimeScriptingException("Failed to open image device from R:\n" + openDevice);
    //        }
    // the device should be fine


    //todo reenable
    // parse script
    //        String preparedScript = fixEncoding(script);
    //        try {
    //            parseScript(connection, preparedScript);
    //        } catch (RserveException | KnimeScriptingException | REXPMismatchException e) {
    //            throw new KnimeScriptingException("Failed to parse the script:\n" + e.getMessage());
    //        }

    // evaluate script
    //        try {
    if (useEvaluate) {
        // parse and run script
        // evaluation list, can be used to create a console view
        //                evaluateScript(preparedScript, connection);
    } else {
        // parse and run script
        //                evalScript(connection, preparedScript);
        val fixedScript = fixEncoding(script)

        voidEval("try({\n$fixedScript\n}, silent = FALSE)")
//        eval("try({\n$fixedScript\n}, silent = FALSE)")
//        eval("try({\n$fixedScript\n}, silent = TRUE)")

    }
    //        } catch (RserveException | REXPMismatchException | KnimeScriptingException e) {
    //            throw new KnimeScriptingException("Failed to evaluate the script:\n" + e.getMessage());
    //        }

    // close the image
    var image: ByteArray? = null
    //        try {
    eval("dev.off();")
    // check if the plot file has been written
    val xpInt = eval("file.access('$tempFileName',0)").asInteger()
    if (xpInt == -1) throw RServeExceptionException("Plot could not be created. Please check your script or submit a ticket to https://github.com/holgerbrandl/kravis")

    // we limit the file size to 1MB which should be sufficient and we delete the file as well
    val imagesBytes = eval("try({ binImage <- readBin('$tempFileName','raw',2024*2024); unlink('$tempFileName'); binImage })")

    //            if (xp.inherits("try-error")) { // if the result is of the class try-error then there was a problem
    //                throw new KnimeScriptingException(xp.asString());
    //            }
    image = imagesBytes.asBytes()

    //        } catch (REXPMismatchException e) {
    //            throw new KnimeScriptingException("Failed to close image device and to read in plot as binary:+\n" + e.getMessage());
    //        }

    // create image object from bytes
    var img: BufferedImage? = null
    try {
        img = ImageIO.read(ByteArrayInputStream(image))
    } catch (e: IOException) {
        throw RServeExceptionException(e.message ?: "")
    }

    return img
}
