See https://github.com/knowm/XChart/issues/69

https://ggplot2.tidyverse.org/reference/geom_boxplot.html

* map colors to color Type RColor
```
p + geom_boxplot(outlier.colour = "red", outlier.shape = 1)

```



# Todo

* refac out vega lite bits and cut down dependencies to bare minimum **{done}**
* constrain alpha to 0,1 **{done}**
* krangl: summarizeEach **{done}**
* iterator builder for graph (docs) **{done}**

* Complete cheatsheet commands
* Render with current panel size

* add basic usage example to all geoms
* consider to use actual defaul (this will give a much more natural api but more bloated plotting commands) Note: first seems more important
* write section in krangl manuea
* Support alternative rendering engine (docker, rserve)


# Backlog

* render svg
  * https://stackoverflow.com/questions/12436274/svg-image-in-javafx-2-2
  * https://stackoverflow.com/questions/20664107/draw-svg-images-on-a-jpanel


###  rserve backend

See https://www.rforge.net/Rserve/

Use existing docker https://github.com/stevenpollack/docker-rserve

Rserve examples
* https://stackoverflow.com/questions/17395651/calling-ggplot-from-rserve-blank-png-image-of-1kb
* Pushing data into rserve seems reasonably easy https://github.com/knime-mpicbg/knime-scripting/blob/84693d96ff8d3282d0477432fe5435ae1cca3ebe/de.mpicbg.knime.scripting.r/src/de/mpicbg/knime/scripting/r/AbstractRScriptingNodeModel.java#L295

# R packaging

Seems possible for windows https://sourceforge.net/projects/rportable/

Seems possible for macos
* https://superuser.com/questions/939070/a-self-contained-r-in-os-x
* https://superuser.com/questions/946083/will-my-portable-app-portable-r-work-on-both-my-pc-and-mac

Not feasible for
https://stackoverflow.com/questions/11871394/create-r-binary-packages-for-linux-that-can-be-installed-on-different-machines

Maybe usefful:
* https://github.com/r-hub/homebrew-cran building statically linked R binary packages for MacOS based on homebrew
