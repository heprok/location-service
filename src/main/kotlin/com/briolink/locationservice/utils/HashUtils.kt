package com.briolink.locationservice.utils

import java.security.MessageDigest

object HashUtils {
    fun md5(s: ByteArray): String {
        val md = MessageDigest.getInstance("MD5")
        return md.digest(s).joinToString("") { String.format("%02x", it) }
    }
}
