package com.zc.vndb.common.data.local

/** Encrypts the API token at rest. */
interface TokenCipher {

    fun encrypt(plain: String): String

    /** Null when [encoded] can no longer be decrypted (e.g. the key was removed). */
    fun decrypt(encoded: String): String?
}
