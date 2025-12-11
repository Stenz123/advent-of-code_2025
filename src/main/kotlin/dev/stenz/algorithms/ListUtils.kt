package dev.stenz.algorithms

fun <T> allPairs(list: List<T>): List<Pair<T, T>> =
    list.flatMap { a -> list.map { b -> a to b } }
