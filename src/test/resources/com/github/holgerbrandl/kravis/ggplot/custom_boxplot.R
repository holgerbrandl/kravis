require(ggplot2)

ggplot(mpg, aes(class, hwy)) + geom_boxplot(notch = TRUE, fill = "orchid3", colour = "#3366FF")