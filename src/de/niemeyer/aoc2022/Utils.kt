@file:Suppress("unused")

package de.niemeyer.aoc2022

import java.math.BigInteger
import java.security.MessageDigest

/**
 * Converts string to kotlin.aoc2022.niemeyer.aoc2022.md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')


fun getClassName(): String {
    val className = Throwable().stackTrace.first { it.className.contains("Day") }
        .className
        .split(".")
        .last()
        .removeSuffix("Kt")

    return "resources/${className}"
}
