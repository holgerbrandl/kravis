package kravis

class PositionJitter(val height: Double? = null, val width: Double? = null, val seed: Int? = null) : Position {
    override fun toString(): String {
        val args = mapOf("height" to height, "width" to width, "seed" to seed)
            .filter { it.value != null }
            .map { (key, value) -> "$key=$value" }
            .joinToString(", ")

        return "position_jitter($args)"
    }
}

class PositionIdentity() : Position {
    override fun toString() = "position_identity()"
}

/**
 * `position_nudge` is generally useful for adjusting the position of items on discrete scales by a small amount.
 * Nudging is built in to geom_text() because it's so useful for moving labels a small distance from what they're labelling.
 */
class PositionNudge(val x: Double = 0.0, val y: Double = 0.0) : Position {
    override fun toString(): String = "position_nudge(${x}, ${y})"
}

class PositionDodge2(
    val width: Double? = null,
    val preserve: String = "total",
    val padding: Double = 0.1,
    val reverse: Boolean = false
) : Position {
    override fun toString(): String {
        val arg2string = arg2string(
            "width" to width,
            "preserve" to preserve,
            "padding" to padding,
            "reverse" to reverse
        )

        return "position_dodge2($arg2string)"
    }
}

interface Position