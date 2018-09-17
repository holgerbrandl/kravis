package kravis


enum class Stat {
    count,
    /** The identity statistic leaves the data unchanged. */
    identity,

    boxplot, bin
}