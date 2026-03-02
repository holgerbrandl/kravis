package kravis.ggplot

fun main() {
    CoreRegressions().`theme adjustments`()
    GeomRegressions().`create factor ordered barchart with error bars`()
    CoreRegressions().`support custom r preamble for rendering`()
    GeomRegressions().`create factor ordered barchart with error bars`()
    CoreRegressions().`convert continues variable to discrete`()
//    GeomRegressions().`custom boxplot`()
    GeomRegressions().`grouped line plot`()
    CoreRegressions().`manipulate legends`()
    GeomRegressions().`simple heatmap`()
    ScaleRegressions().`allow for custom colors`()
    GeomRegressions().`text labels in plot`()
    CoreRegressions().`it should deparse collections and allow for axis options`()
}