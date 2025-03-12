package org.turter.patrocl.utils

fun <T> List<T>.mutate(mutation: MutableList<T>.() -> Unit)
        : List<T> {
    return this.toMutableList().apply(mutation).toList()
}

fun <T> Set<T>.mutate(mutation: MutableSet<T>.() -> Unit)
        : Set<T> {
    return this.toMutableSet().apply(mutation).toSet()
}