package com.zc.vndb.feature.theme.domain.usecase

import com.zc.vndb.common.domain.model.AppSettings
import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.domain.model.TitleLanguage
import com.zc.vndb.common.domain.repository.FakeSettingsRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class SelectThemeUseCaseTest {

    @Test
    fun `saves the theme and leaves the other settings alone`() = runTest {
        val settings = FakeSettingsRepository(AppSettings(titleLanguage = TitleLanguage.Original))

        SelectThemeUseCase(settings)(AppTheme.AngelicSerenade)

        assertEquals(
            AppSettings(titleLanguage = TitleLanguage.Original, theme = AppTheme.AngelicSerenade),
            settings.current.value,
        )
    }
}
