package kravis


//todo redo dotdotdot
fun GGPlot.scaleXLog10(vararg dotdotdot: Pair<String, String>): GGPlot = appendSpec {
    addSpec("""scale_x_log10(${arg2string(*dotdotdot.asList().toTypedArray())})""")
}


//todo redo dotdotdot
fun GGPlot.scaleYLog10(vararg dotdotdot: Pair<String, String>): GGPlot = appendSpec {
    addSpec("""scale_y_log10(${arg2string(*dotdotdot.asList().toTypedArray())})""")
}

