package com.zc.vndb.feature.login.domain.usecase

import com.zc.vndb.common.domain.model.Session
import com.zc.vndb.feature.login.domain.model.InvalidTokenFormatException
import com.zc.vndb.feature.login.domain.repository.LoginRepository

/**
 * Cleans up a pasted token (spaces, case, dashes) and checks its shape before asking VNDB.
 * Tokens are 32 lowercase z-base-32 characters, usually shown as
 * `xxxx-xxxxx-xxxxx-xxxx-xxxxx-xxxxx-xxxx`.
 */
class LogInUseCase(private val repository: LoginRepository) {

    suspend operator fun invoke(rawToken: String): Result<Session> {
        val token = normalize(rawToken) ?: return Result.failure(InvalidTokenFormatException())
        return repository.logIn(token)
    }

    companion object {
        private const val TOKEN_LENGTH = 32
        private val GROUPS = listOf(4, 5, 5, 4, 5, 5, 4)
        private const val Z_BASE_32 = "ybndrfg8ejkmcpqxot1uwisza345h769"

        /** The token in VNDB's dashed form, or null when [raw] cannot be a token. */
        fun normalize(raw: String): String? {
            val compact = raw.filterNot { it.isWhitespace() || it == '-' }.lowercase()
            if (compact.length != TOKEN_LENGTH || compact.any { it !in Z_BASE_32 }) return null
            var start = 0
            return GROUPS.joinToString("-") { size ->
                compact.substring(start, start + size).also { start += size }
            }
        }
    }
}
