[TOC]: # " Contents"

# Contents
- [Milestoness](#milestoness)
    - [M1](#m1)
    - [M2](#m2)
- [java fx](#java-fx)
- [Geoms](#geoms)
    - [Scatter](#scatter)
    - [line plot with trends](#line-plot-with-trends)
    - [Rendering](#rendering)
        - [render in jupyter](#render-in-jupyter)
        - [basic reusable rendering device for kotlin repl](#basic-reusable-rendering-device-for-kotlin-repl)
        - [offscreen pdf renderin](#offscreen-pdf-renderin)
    - [svg output](#svg-output)
    - [Next steps](#next-steps)
    - [stats utils](#stats-utils)
    - [Reading List](#reading-list)



# Milestoness



## M1


geom
* barchart
* linebarcht with trend (lm and loess)
* scatter (size, alpha, text)
* histogram (color, alpha)
* plot chart background images (e.g. cells)

infrastrcuture
* render in jupyter
    * see https://stackoverflow.com/questions/39739476/use-javafx-chart-api-to-draw-chart-image
* 
* basic reusable rendering device for kotlin repl
* offscreen pdf renderin

## M2



# java fx


---
https://lankydanblog.com/2017/01/29/javafx-graphs-look-pretty-good/

write ui components with fxml

---
https://github.com/kairikozuma/scatter-plot

complete app article with table and data view


# Geoms

## Scatter

* different point sizes
* different alphas
* text labels
* color-map (gradient, discrete)
* legends for all aes

## line plot with trends

* trendline(s) overlay
scatter with trend

1. model with lm
2. sample points
3. spline through points using in http://fxexperience.com/2012/01/curve-fitting-and-styling-areachart/


## Rendering


### render in jupyter

https://stackoverflow.com/questions/13232578/rendering-javafx-2-charts-in-background

### basic reusable rendering device for kotlin repl

### offscreen pdf renderin



## svg output

show svg in javafx window

https://stackoverflow.com/questions/12436274/svg-image-in-javafx-2-2


https://stackoverflow.com/questions/26948700/convert-svg-to-javafx-image

as pdf use batik https://stackoverflow.com/questions/6875807/convert-svg-to-pdf
which has encoders for all types


## Next steps



## stats utils

http://commons.apache.org/proper/commons-math/javadocs/api-3.6/overview-summary.html



## Reading List

---
Explore https://github.com/hudsonb/kubed/


cool demos
/Users/brandl/projects/kotlin/misc/kubed/kubed-demos/src/main/kotlin/kubed/demo/StackedBarChartDemo.kt

---
https://github.com/HanSolo/charts



---
TornadoFX

https://github.com/edvin/tornadofx/wiki/Charts


https://edvin.gitbooks.io/tornadofx-guide/content/part1/8.%20Charts.html

https://github.com/edvin/tornadofx/wiki/Charts


for impl see /Users/brandl/projects/kotlin/misc/tornadofx/src/main/java/tornadofx/Charts.kt

---
JavaFx Charts

https://docs.oracle.com/javafx/2/charts/chart-overview.htm

![](.kravis_devel_notes_images/84bc367d.png)

custom chart are possible with fxml
https://stackoverflow.com/questions/33308877/fxml-custom-chart

https://stackoverflow.com/questions/26803380/how-to-combine-scatter-chart-with-line-chart-to-show-line-of-regression-javafx

https://gist.github.com/jewelsea/3668862

https://github.com/JKostikiadis/MultiAxisScatterChart

http://thorwin.blogspot.de/2015/03/trend-curveline-in-javafx-chart.html

---
https://github.com/jfree/jfreesvgO


