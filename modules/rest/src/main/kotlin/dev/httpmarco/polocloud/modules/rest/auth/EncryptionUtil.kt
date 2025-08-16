package dev.httpmarco.polocloud.modules.rest.auth

import de.mkammerer.argon2.Argon2Factory

object EncryptionUtil {

    private val argon2 = Argon2Factory.create()

    fun encrypt(content: String): String {
        val contentCharArray = content.toCharArray()
        return try {
            this.argon2.hash(2, 65536, 1, contentCharArray)
        } finally {
            this.argon2.wipeArray(contentCharArray)
        }
    }

    fun verify(hash: String, content: String): Boolean {
        val contentCharArray = content.toCharArray()
        return try {
            this.argon2.verify(hash, contentCharArray)
        } finally {
            this.argon2.wipeArray(contentCharArray)
        }
    }
}