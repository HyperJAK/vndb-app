package com.zc.vndb.common.data.local

/** Reversible stand-in for the Keystore cipher, so tests can check the token is not stored as is. */
class FakeTokenCipher : TokenCipher {
    override fun encrypt(plain: String) = "enc:" + plain.reversed()
    override fun decrypt(encoded: String) = encoded.removePrefix("enc:").reversed()
}
