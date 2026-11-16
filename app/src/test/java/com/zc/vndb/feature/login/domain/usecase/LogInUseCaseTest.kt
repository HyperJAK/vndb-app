package com.zc.vndb.feature.login.domain.usecase

import com.zc.vndb.feature.login.domain.model.InvalidTokenFormatException
import com.zc.vndb.feature.login.domain.model.LoginSamples
import com.zc.vndb.feature.login.domain.repository.FakeLoginRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class LogInUseCaseTest {

    private val repository = FakeLoginRepository().apply {
        acceptedSession =
            LoginSamples.session()
    }
    private val useCase = LogInUseCase(repository)

    @Test
    fun `normalizes spacing, case and dashes to VNDB's dashed form`() {
        val expected = LoginSamples.TOKEN
        assertEquals(expected, LogInUseCase.normalize(expected))
        assertEquals(
            expected,
            LogInUseCase.normalize("  HSOO YBWS4 j8yb9 qxkw 5obay px8to bfyk \n"),
        )
        assertEquals(expected, LogInUseCase.normalize("hsooybws4j8yb9qxkw5obaypx8tobfyk"))
    }

    @Test
    fun `rejects text that cannot be a token`() {
        assertNull(LogInUseCase.normalize(""))
        assertNull(LogInUseCase.normalize("hsoo-ybws4-j8yb9"))
        // "l" and "2" are not z-base-32
        assertNull(LogInUseCase.normalize("lsoo-ybws4-j8yb9-qxkw-5obay-px8to-bfy2"))
    }

    @Test
    fun `a malformed token fails without calling VNDB`() = runTest {
        assertIs<InvalidTokenFormatException>(useCase("not a token").exceptionOrNull())
        assertTrue(repository.loggedInTokens.isEmpty())
    }

    @Test
    fun `a well-formed token is sent normalized`() = runTest {
        val session = useCase(LoginSamples.TOKEN.uppercase()).getOrThrow()

        assertEquals(listOf(LoginSamples.TOKEN), repository.loggedInTokens)
        assertEquals("u2", session.userId)
    }
}
