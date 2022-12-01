import java.math.BigInteger
import java.security.MessageDigest

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun Iterable<Int>.product(): Int =
    this.reduce(Int::times)

fun Iterable<Long>.product(): Long =
    this.reduce(Long::times)

fun Char.asLong(): Long =
    this.digitToInt().toLong()
