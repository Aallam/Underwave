package com.aallam.underwave.internal.extension

import java.math.BigInteger
import java.security.MessageDigest

/**
 * Hash a string to an MD5.
 *
 * @return MD5 hash corresponding to the given string
 */
internal fun String.md5(): String {
    val messageDigest: MessageDigest = MessageDigest.getInstance("MD5")
    val digest: ByteArray = messageDigest.digest(toByteArray())
    return BigInteger(1, digest).toString(16).padStart(32, '0')
}
