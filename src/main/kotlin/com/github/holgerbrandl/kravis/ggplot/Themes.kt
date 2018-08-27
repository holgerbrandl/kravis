package com.github.holgerbrandl.kravis.ggplot

import kotlin.math.roundToInt

/**
 * @author Holger Brandl
 *
 * See https://ggplot2.tidyverse.org/reference/ggtheme.html
 */

internal interface Theme

internal abstract class ThemeBuilder(
    val themeName: String,
    val base_size: Int = 11,
    val base_family: String = "",
    val base_line_size: Int = (base_size.toDouble() / 22).roundToInt(),
    val base_rect_size: Int = (base_size.toDouble() / 22).roundToInt()
) {

    override fun toString(): String {
        val args = arg2string(
            "base_size" to base_size,
            "base_family" to base_family,
            "base_line_size" to base_line_size,
            "base_rect_size" to base_rect_size
        )

        return "theme_${themeName}(${args})"
    }
}

/**
 * The classic dark-on-light ggplot2 theme. May work better for presentations displayed with a projector.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeBW(base_size: Int = 11, base_family: String = "") = apply {
    addLayer(object : ThemeBuilder("bw", base_size, base_family) {}.toString())
}

/**
 * The signature ggplot2 theme with a grey background and white gridlines, designed to put the data forward yet make comparisons easy.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeGray(base_size: Int = 11, base_family: String = "") = apply {
    addLayer(object : ThemeBuilder("gray", base_size, base_family) {}.toString())
}


/**
 * A theme similar to theme_linedraw but with light grey lines and axes, to direct more attention towards the data.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeLight(base_size: Int = 11, base_family: String = "") = apply {
    addLayer(object : ThemeBuilder("light", base_size, base_family) {}.toString())
}


/**
 * The dark cousin of theme_light, with similar line sizes but a dark background. Useful to make thin coloured lines pop out.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeDark(base_size: Int = 11, base_family: String = "") = apply {
    addLayer(object : ThemeBuilder("dark", base_size, base_family) {}.toString())
}

/**
 * A minimalistic theme with no background annotations.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeMinimal(base_size: Int = 11, base_family: String = "") = apply {
    addLayer(object : ThemeBuilder("minimal", base_size, base_family) {}.toString())
}


/**
 * A classic-looking theme, with x and y axis lines and no gridlines.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeClassic(base_size: Int = 11, base_family: String = "") = apply {
    addLayer(object : ThemeBuilder("classic", base_size, base_family) {}.toString())
}

/**
 * A theme for visual unit tests. It should ideally never change except for new features.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeVoid(base_size: Int = 11, base_family: String = "") = apply {
    addLayer(object : ThemeBuilder("void", base_size, base_family) {}.toString())
}
