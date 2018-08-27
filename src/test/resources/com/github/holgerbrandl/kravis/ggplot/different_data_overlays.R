library(ggplot2)
library(dplyr)
library(readr)

irisSummary = iris %>%
    group_by(Species) %>%
    summarize(
    Sepal.Length.Mean = mean(Sepal.Length),
    Petal.Length.Mean = mean(Petal.Length),
    )
ggplot(mapping = aes(x = `Sepal.Length`, y = `Petal.Length`, color = `Species`), data = iris) +
    geom_point(alpha = 0.3) +
    geom_point(mapping = aes(x = `Sepal.Length.Mean`, y = `Petal.Length.Mean`), data = irisSummary, shape = 4, stroke = 4)