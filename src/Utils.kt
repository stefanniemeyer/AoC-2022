import java.math.BigInteger
import java.security.MessageDigest

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')


fun getClassName(): String =
    Throwable().stackTrace.first { it.className.contains("Day") }
        .className
        .substringBefore(".")
        .removeSuffix("Kt")
