require(ggplot2)

# https://helenajambor.wordpress.com/2018/08/28/pick-n-mix-plots/
# see https://gist.github.com/dgrtwo/eb7750e74997891d7c20#file-geom_flat_violin-r
devtools::source_url("https://gist.githubusercontent.com/holgerbrandl/13074d2b4db2a0d654fff88274de4fe6/raw/5cdfe1b06cb25b8bde65fc549134f7b558bc9f3c/geom_flat_violin.R")

ggplot(data = iris, mapping = aes(x = Species, y = Sepal.Length, fill = Species)) +
    geom_flat_violin(scale = "count", trim = FALSE) +
    stat_summary(fun.data = mean_sdl, fun.args = list(mult = 1), geom = "pointrange", position = position_nudge(0.05)) +
    geom_dotplot(binaxis = "y", dotsize = 0.5, stackdir = "down", binwidth = 0.1, position = position_nudge(- 0.025)) +
    theme(legend.position = "none") +
    labs(x = "Species", y = "Sepal length (cm)")