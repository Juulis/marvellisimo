package malidaca.marvellisimo.utilities

import java.io.File
import java.security.MessageDigest

object HashHandler{

    val publicKey = "d9731b194c3568c373d0f35fa913389d"

    fun getHash(ts: String): String{
        val algorithm = "MD5"
        val privateKey = PrivateKeys.API
        val HEX_CHARS = "0123456789abcdef"
        println("ts1: $ts")
        val s = ts + privateKey + publicKey
        val keyBytes = MessageDigest.getInstance(algorithm).digest(s.toByteArray())

        return keyBytes.joinToString(separator = "", transform = {
            a -> String(charArrayOf(HEX_CHARS[a.toInt() shr 4 and 0x0f], HEX_CHARS[a.toInt() and 0x0f]))
        })
    }
}