package com.aallam.underwave.extension

import java.math.BigInteger
import java.security.MessageDigest

/**
 * Hash a string to an MD5.
 *
 * @return MD5 hash corresponding to the given string
 */
internal fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}