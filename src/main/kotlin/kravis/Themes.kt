package kravis

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
fun GGPlot.themeBW(base_size: Int = 11, base_family: String = "") = appendSpec {
    addSpec(object : ThemeBuilder("bw", base_size, base_family) {}.toString())
}

/**
 * The signature ggplot2 theme with a grey background and white gridlines, designed to put the data forward yet make comparisons easy.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeGray(base_size: Int = 11, base_family: String = "") = appendSpec {
    addSpec(object : ThemeBuilder("gray", base_size, base_family) {}.toString())
}


/**
 * A theme similar to theme_linedraw but with light grey lines and axes, to direct more attention towards the data.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeLight(base_size: Int = 11, base_family: String = "") = appendSpec {
    addSpec(object : ThemeBuilder("light", base_size, base_family) {}.toString())
}


/**
 * The dark cousin of theme_light, with similar line sizes but a dark background. Useful to make thin coloured lines pop out.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeDark(base_size: Int = 11, base_family: String = "") = appendSpec {
    addSpec(object : ThemeBuilder("dark", base_size, base_family) {}.toString())
}

/**
 * A minimalistic theme with no background annotations.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeMinimal(base_size: Int = 11, base_family: String = "") = appendSpec {
    addSpec(object : ThemeBuilder("minimal", base_size, base_family) {}.toString())
}


/**
 * A classic-looking theme, with x and y axis lines and no gridlines.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeClassic(base_size: Int = 11, base_family: String = "") = appendSpec {
    addSpec(object : ThemeBuilder("classic", base_size, base_family) {}.toString())
}

/**
 * A theme for visual unit tests. It should ideally never change except for new features.
 *
 * Use theme() if you just need to tweak the display of an existing theme.
 */
fun GGPlot.themeVoid(base_size: Int = 11, base_family: String = "") = appendSpec {
    addSpec(object : ThemeBuilder("void", base_size, base_family) {}.toString())
}

open class ElementText(val args: String) {
    override fun toString() = "element_text($args)"
}

class ElementTextBlank : ElementText("") {
    override fun toString() = "element_blank()"
}

class ElementLine(val args: String) {
    override fun toString() = "element_line($args)"
}

class ElementRect(val args: String) {
    override fun toString() = "element_line($args)"
}

class ElementBlank(val args: String) {
    override fun toString() = "element_line($args)"
}


/**
 * @param line All line elements (element_line)
 * @param rect All rectangular elements (element_rect)
 * @param text All text elements (element_text)
 * @param title All title elements: plot, axes, legends (element_text; inherits from text)
 * @param aspectRatio Aspect ratio of the panel
 * @param axisTitle Label of axes (element_text; inherits from text)
 * @param axisTitleX X axis label (element_text; inherits from axis.title)
 * @param axisTitleXTop X axis label on top axis (element_text; inherits from axis.title.x)
 * @param axisTitleXBottom X axis label on bottom axis (element_text; inherits from axis.title.x)
 * @param axisTitleY Y axis label (element_text; inherits from axis.title)
 * @param axisTitleYLeft Y axis label on left axis (element_text; inherits from axis.title.y)
 * @param axisTitleYRight Y axis label on right axis (element_text; inherits from axis.title.y)
 * @param axisText Tick labels along axes (element_text; inherits from text)
 * @param axisTextX X axis tick labels (element_text; inherits from axis.text)
 * @param axisTextXTop X axis tick labels on top axis (element_text; inherits from axis.text.x)
 * @param axisTextXBottom X axis tick labels on bottom axis (element_text; inherits from axis.text.x)
 * @param axisTextY Y axis tick labels (element_text; inherits from axis.text)
 * @param axisTextYLeft Y axis tick labels on left axis (element_text; inherits from axis.text.y)
 * @param axisTextYRight Y axis tick labels on right axis (element_text; inherits from axis.text.y)
 * @param axisTicks Tick marks along axes (element_line; inherits from line)
 * @param axisTicksX X axis tick marks (element_line; inherits from axis.ticks)
 * @param axisTicksXTop X axis tick marks on top axis (element_line; inherits from axis.ticks.x)
 * @param axisTicksXBottom X axis tick marks on bottom axis (element_line; inherits from axis.ticks.x)
 * @param axisTicksY Y axis tick marks (element_line; inherits from axis.ticks)
 * @param axisTicksYLeft Y axis tick marks on left axis (element_line; inherits from axis.ticks.y)
 * @param axisTicksYRight Y axis tick marks on right axis (element_line; inherits from axis.ticks.y)
 * @param axisTicksLength Length of tick marks (unit)
 * @param axisLine Lines along axes (element_line; inherits from line)
 * @param axisLineX Line along x axis (element_line; inherits from axis.line)
 * @param axisLineXTop Line along x axis on top axis (element_line; inherits from axis.line.x)
 * @param axisLineXBottom Line along x axis on bottom axis (element_line; inherits from axis.line.x)
 * @param axisLineY Line along y axis (element_line; inherits from axis.line)
 * @param axisLineYLeft Line along y axis on left axis (element_line; inherits from axis.line.y)
 * @param axisLineYRight Line along y axis on right axis (element_line; inherits from axis.line.y)
 * @param legendBackground Background of legend (element_rect; inherits from rect)
 * @param legendMargin The margin around each legend (margin)
 * @param legendSpacing The spacing between legends (unit)
 * @param legendSpacingX The horizontal spacing between legends (unit); inherits from legend.spacing
 * @param legendSpacingY The horizontal spacing between legends (unit); inherits from legend.spacing
 * @param legendKey Background underneath legend keys (element_rect; inherits from rect)
 * @param legendKeySize Size of legend keys (unit)
 * @param legendKeyHeight Key background height (unit; inherits from legend.key.size)
 * @param legendKeyWidth Key background width (unit; inherits from legend.key.size)
 * @param legendText Legend item labels (element_text; inherits from text)
 * @param legendTextAlign Alignment of legend labels (number from 0 (left) to 1 (right))
 * @param legendTitle Title of legend (element_text; inherits from title)
 * @param legendTitleAlign Alignment of legend title (number from 0 (left) to 1 (right))
 * @param legendPosition The position of legends ("none", "left", "right", "bottom", "top", or two-element numeric vector)
 * @param legendDirection Layout of items in legends ("horizontal" or "vertical")
 * @param legendJustificationPositionedOutsideThePlot Anchor point for positioning legend inside plot ("center" or two-element numeric vector) or the justification according to the plot area when
 * @param legendBox Arrangement of multiple legends ("horizontal" or "vertical")
 * @param legendBoxJust Justification of each legend within the overall bounding box, when there are multiple legends ("top", "bottom", "left", or "right")
 * @param legendBoxMargin Margins around the full legend area, as specified using margin()
 * @param legendBoxBackground Background of legend area (element_rect; inherits from rect)
 * @param legendBoxSpacing The spacing between the plotting area and the legend box (unit)
 * @param panelBackground Background of plotting area, drawn underneath plot (element_rect; inherits from rect)
 * @param panelBorderRect) Border around plotting area, drawn on top of plot so that it covers tick marks and grid lines. This should be used with fill=NA (element_rect; inherits from
 * @param panelSpacing Spacing between facet panels (unit)
 * @param panelSpacingX Horizontal spacing between facet panels (unit; inherits from panel.spacing)
 * @param panelSpacingY Vertical spacing between facet panels (unit; inherits from panel.spacing)
 * @param panelGrid Grid lines (element_line; inherits from line)
 * @param panelGridMajor Major grid lines (element_line; inherits from panel.grid)
 * @param panelGridMinor Minor grid lines (element_line; inherits from panel.grid)
 * @param panelGridMajorX Vertical major grid lines (element_line; inherits from panel.grid.major)
 * @param panelGridMajorY Horizontal major grid lines (element_line; inherits from panel.grid.major)
 * @param panelGridMinorX Vertical minor grid lines (element_line; inherits from panel.grid.minor)
 * @param panelGridMinorY Horizontal minor grid lines (element_line; inherits from panel.grid.minor)
 * @param panelOntop Option to place the panel (background, gridlines) over the data layers. Usually used with a transparent or blank panel.background. (logical)
 * @param plotBackground Background of the entire plot (element_rect; inherits from rect)
 * @param plotTitle Plot title (text appearance) (element_text; inherits from title) left-aligned by default
 * @param plotSubtitle Plot subtitle (text appearance) (element_text; inherits from title) left-aligned by default
 * @param plotCaption Caption below the plot (text appearance) (element_text; inherits from title) right-aligned by default
 * @param plotTag Upper-left label to identify a plot (text appearance) (element_text; inherits from title) left-aligned by default
 * @param plotTagPosition The position of the tag as a string ("topleft", "top", "topright", "left", "right", "bottomleft", "bottom", "bottomright) or a coordinate. If a string, extra space will be added to accomodate the tag.
 * @param plotMargin Margin around entire plot (unit with the sizes of the top, right, bottom, and left margins)
 * @param stripBackground Background of horizontal facet labels (element_rect; inherits from strip.background)
 * @param stripBackgroundX Background of vertical facet labels (element_rect; inherits from strip.background)
 * @param stripBackgroundY Placement of strip with respect to axes, either "inside" or "outside". Only important when axes and strips are on the same side of the plot.
 * @param stripPlacement Facet labels (element_text; inherits from text)
 * @param stripText Facet labels along horizontal direction (element_text; inherits from strip.text)
 * @param stripTextX Facet labels along vertical direction (element_text; inherits from strip.text)
 * @param stripTextY Space between strips and axes when strips are switched (unit)
 * @param stripSwitchPadGrid Space between strips and axes when strips are switched (unit)
 * @param stripSwitchPadWrap Additional element specifications not part of base ggplot2. If supplied validate needs to be set to FALSE.
 * @param complete Set this to TRUE if this is a complete theme, such as the one returned by theme_grey(). Complete themes behave differently when added to a ggplot object. Also,
 * @param validate TRUE to run validate_element, FALSE to bypass checks.
 */
fun GGPlot.theme(
    line: ElementLine? = null,
    rect: ElementRect? = null,
    text: ElementText? = null,
    title: ElementText? = null,
    aspectRatio: Double? = null,
    axisTitle: ElementText? = null,
    axisTitleX: ElementText? = null,
    axisTitleXTop: ElementText? = null,
    axisTitleXBottom: ElementText? = null,
    axisTitleY: ElementText? = null,
    axisTitleYLeft: ElementText? = null,
    axisTitleYRight: ElementText? = null,
    axisText: ElementText? = null,
    axisTextX: ElementText? = null,
    axisTextXTop: ElementText? = null,
    axisTextXBottom: ElementText? = null,
    axisTextY: ElementText? = null,
    axisTextYLeft: ElementText? = null,
    axisTextYRight: ElementText? = null,
    axisTicks: ElementLine? = null,
    axisTicksX: ElementLine? = null,
    axisTicksXTop: ElementLine? = null,
    axisTicksXBottom: ElementLine? = null,
    axisTicksY: ElementLine? = null,
    axisTicksYLeft: ElementLine? = null,
    axisTicksYRight: ElementLine? = null,
    axisTicksLength: ElementLine? = null,
    axisLine: ElementLine? = null,
    axisLineX: ElementLine? = null,
    axisLineXTop: ElementLine? = null,
    axisLineXBottom: ElementLine? = null,
    axisLineY: ElementLine? = null,
    axisLineYLeft: ElementLine? = null,
    axisLineYRight: ElementLine? = null,
    legendBackground: ElementText? = null,
    legendMargin: Double? = null,
    legendSpacing: Double? = null,
    legendSpacingX: Double? = null,
    legendSpacingY: Double? = null,
    legendKey: ElementText? = null,
    legendKeySize: Int? = null,
    legendKeyHeight: Double? = null,
    legendKeyWidth: Double? = null,
    legendText: ElementText? = null,
    legendTextAlign: Double? = null,
    legendTitle: ElementText? = null,
    legendTitleAlign: ElementText? = null,
    legendPosition: String? = null,
    legendDirection: ElementText? = null, // ("horizontal" or "vertical")
    legendJustification: ElementText? = null,
    legendBox: ElementText? = null,
    legendBoxJust: ElementText? = null,
    legendBoxMargin: ElementText? = null,
    legendBoxBackground: ElementText? = null,
    legendBoxSpacing: Double? = null,
    panelBackground: ElementText? = null,
    panelBorder: ElementText? = null,
    panelSpacing: Double? = null,
    panelSpacingX: Double? = null,
    panelSpacingY: Double? = null,
    panelGrid: ElementText? = null,
    panelGridMajor: ElementText? = null,
    panelGridMinor: ElementText? = null,
    panelGridMajorX: ElementText? = null,
    panelGridMajorY: ElementText? = null,
    panelGridMinorX: ElementText? = null,
    panelGridMinorY: ElementText? = null,
    panelOntop: ElementText? = null,
    plotBackground: ElementText? = null,
    plotTitle: ElementText? = null,
    plotSubtitle: ElementText? = null,
    plotCaption: ElementText? = null,
    plotTag: ElementText? = null,
    plotTagPosition: ElementText? = null,
    plotMargin: ElementText? = null,
    stripBackground: ElementText? = null,
    stripBackgroundX: ElementText? = null,
    stripBackgroundY: ElementText? = null,
    stripPlacement: ElementText? = null,
    stripText: ElementText? = null,
    stripTextX: ElementText? = null,
    stripTextY: ElementText? = null,
    stripSwitchPadGrid: ElementText? = null,
    stripSwitchPadWrap: ElementText? = null,
    complete: Boolean? = null,
    other: String? = null
) = appendSpec {

    val args = arg2string(
        "line" to line,
        "rect" to rect,
        "text" to text,
        "title" to title,
        "aspect.ratio" to aspectRatio,
        "axis.title" to axisTitle,
        "axis.title.x" to axisTitleX,
        "axis.title.xtop" to axisTitleXTop,
        "axis.title.xbottom" to axisTitleXBottom,
        "axis.title.y" to axisTitleY,
        "axis.title.yleft" to axisTitleYLeft,
        "axis.title.yright" to axisTitleYRight,
        "axis.text" to axisText,
        "axis.text.x" to axisTextX,
        "axis.text.xtop" to axisTextXTop,
        "axis.text.xbottom" to axisTextXBottom,
        "axis.text.y" to axisTextY,
        "axis.text.yleft" to axisTextYLeft,
        "axis.text.yright" to axisTextYRight,
        "axis.ticks" to axisTicks,
        "axis.ticks.x" to axisTicksX,
        "axis.ticks.xtop" to axisTicksXTop,
        "axis.ticks.xbottom" to axisTicksXBottom,
        "axis.ticks.y" to axisTicksY,
        "axis.ticks.yleft" to axisTicksYLeft,
        "axis.ticks.yright" to axisTicksYRight,
        "axis.ticks.length" to axisTicksLength,
        "axis.line" to axisLine,
        "axis.line.x" to axisLineX,
        "axis.line.xtop" to axisLineXTop,
        "axis.line.xbottom" to axisLineXBottom,
        "axis.line.y" to axisLineY,
        "axis.line.yleft" to axisLineYLeft,
        "axis.line.yright" to axisLineYRight,
        "legend.background" to legendBackground,
        "legend.margin" to legendMargin,
        "legend.spacing" to legendSpacing,
        "legend.spacing.x" to legendSpacingX,
        "legend.spacing.y" to legendSpacingY,
        "legend.key" to legendKey,
        "legend.key.size" to legendKeySize,
        "legend.key.height" to legendKeyHeight,
        "legend.key.width" to legendKeyWidth,
        "legend.text" to legendText,
        "legend.text.align" to legendTextAlign,
        "legend.title" to legendTitle,
        "legend.title.align" to legendTitleAlign,
        "legend.position" to legendPosition,
        "legend.direction" to legendDirection,
        "legend.justification" to legendJustification,
        "legend.box" to legendBox,
        "legend.box.just" to legendBoxJust,
        "legend.box.margin" to legendBoxMargin,
        "legend.box.background" to legendBoxBackground,
        "legend.box.spacing" to legendBoxSpacing,
        "panel.background" to panelBackground,
        "panel.border" to panelBorder,
        "panel.spacing" to panelSpacing,
        "panel.spacing.x" to panelSpacingX,
        "panel.spacing.y" to panelSpacingY,
        "panel.grid" to panelGrid,
        "panel.grid.major" to panelGridMajor,
        "panel.grid.minor" to panelGridMinor,
        "panel.grid.major.x" to panelGridMajorX,
        "panel.grid.major.y" to panelGridMajorY,
        "panel.grid.minor.x" to panelGridMinorX,
        "panel.grid.minor.y" to panelGridMinorY,
        "panel.ontop" to panelOntop,
        "plot.background" to plotBackground,
        "plot.title" to plotTitle,
        "plot.subtitle" to plotSubtitle,
        "plot.caption" to plotCaption,
        "plot.tag" to plotTag,
        "plot.tag.position" to plotTagPosition,
        "plot.margin" to plotMargin,
        "strip.background" to stripBackground,
        "strip.background.x" to stripBackgroundX,
        "strip.background.y" to stripBackgroundY,
        "strip.placement" to stripPlacement,
        "strip.text" to stripText,
        "strip.text.x" to stripTextX,
        "strip.text.y" to stripTextY,
        "strip.switch.pad.grid" to stripSwitchPadGrid,
        "strip.switch.pad.wrap" to stripSwitchPadWrap,
        "complete" to complete,
        "other" to other
    )

    addSpec("theme(${args})")
}
