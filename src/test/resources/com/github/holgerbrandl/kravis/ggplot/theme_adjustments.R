plot <- ggplot(mpg, aes(displ, hwy)) + geom_point()

plot + theme(panel.background = element_blank(), axis.text = element_blank())

plot + theme(
panel.background = element_blank(),
axis.title.y = element_blank(),
axis.text = element_text(size = 20, color = "red")
)

plot + theme(
axis.text = element_text(colour = "red", size = rel(1.5))
)

plot + theme(
axis.line = element_line(arrow = arrow())
)

plot + theme(
panel.background = element_rect(fill = "white"),
plot.margin = margin(2, 2, 2, 2, "cm"),
plot.background = element_rect(
fill = "grey90",
colour = "black",
size = 1
)
)


## todo build test for more complex theming ops

ggplot(data.frame(x = c(0, 1)), aes(x = x)) +
    stat_function(fun = dnorm, args = list(0.2, 0.1), aes(colour = "Group 1"), size = 1.5) +
    stat_function(fun = dnorm, args = list(0.7, 0.05), aes(colour = "Group 2"), size = 1.5) +
    scale_x_continuous(name = "Probability", breaks = seq(0, 1, 0.2), limits = c(0, 1)) +
    scale_y_continuous(name = "Frequency") +
    ggtitle("Normal function curves of probabilities") +
    scale_colour_brewer(palette = "Set1") +
    labs(colour = "Groups") +
    theme(axis.line = element_line(size = 1, colour = "black"),
    panel.grid.major = element_blank(),
    panel.grid.minor = element_blank(),
    panel.border = element_blank(),
    panel.background = element_blank(),
    axis.text.x = element_text(colour = "black", size = 12),
    axis.text.y = element_text(colour = "black", size = 12))
