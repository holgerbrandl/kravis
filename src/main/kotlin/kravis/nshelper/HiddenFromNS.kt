import kravis.Aesthetic

// todo hide this from public namespace
fun <T> List<Pair<Aesthetic, T>>.skipNull(aes: Aesthetic, x: T?): List<Pair<Aesthetic, T>> {
    return if (x != null) this + Pair(aes, x) else this
}
