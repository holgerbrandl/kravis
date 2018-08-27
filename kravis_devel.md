See https://github.com/knowm/XChart/issues/69

https://ggplot2.tidyverse.org/reference/geom_boxplot.html

* map colors to color Type RColor
```
p + geom_boxplot(outlier.colour = "red", outlier.shape = 1)

```



# Todo


* Render with current panel size
* Support alternative rendering engine (docker, remote, rserve)
* add basic usage example to all geoms
* constrain alpha to 0,1
* consider to use actual defaul (this will give a much more natural api but more bloated plotting commands) Note: first seems more important
