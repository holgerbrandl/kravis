# Module kravis

Visualizing tabular and relational data is the core of data-science. `kravis` implements a grammar to create a wide range of plots using a standardized set of verbs.


The grammar implemented by `kravis` is inspired from [`ggplot2`](http://ggplot2.org/). In fact, all it provides is a more typesafe wrapper around it.  Internally, `ggplot2` is used as rendering engine. The API of `kravis` is highly similar to allow even reusing their excellent [cheatsheet](https://www.rstudio.com/resources/cheatsheets/#ggplot2).

R is required to use `ggplot`. However, `kravis` works with various integration backend ranging such as docker or remote webservices.

For details see [https://github.com/holgerbrandl/kravis](https://github.com/holgerbrandl/kravis)

## Features

 Visualize any `Iterable<T>`  or  [`DataFrame`](https://kotlin.github.io/dataframe) using the power of ggplot2.


# Package kravis

Contains all parts of the `kravis` API.