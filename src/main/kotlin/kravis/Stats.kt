package kravis

interface Stat
/** The identity statistic leaves the data unchanged. */
class StatIdentity : Stat {
    override fun toString(): String = "identity".quoted
}

class StatCustom(val custom: String) : Stat {
    override fun toString() = custom
}

class StatBxoplot() : Stat {
    override fun toString(): String = "boxplot".quoted
}