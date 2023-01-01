@file:Suppress("unused")

package de.niemeyer.aoc.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.absoluteValue

/**
 * Converts string to kotlin.aoc2022.niemeyer.aoc2022.md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')


fun getClassName(): String =
    Throwable().stackTrace.first { it.className.contains("Day") }
        .className
        .split(".")
        .last()
        .removeSuffix("Kt")

tailrec fun Int.gcd(other: Int): Int {
    return if (this == 0 || other == 0) {
        this + other
    } else {
        val bigger = maxOf(this.absoluteValue, other.absoluteValue)
        val smaller = minOf(this.absoluteValue, other.absoluteValue)
        (bigger % smaller).gcd(smaller)
    }
}

fun Int.lcm(other: Int) =
    if (this == 0 || other == 0) {
        0
    } else {
        (this * other).absoluteValue / this.gcd(other)
    }

