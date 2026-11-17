package com.zc.vndb.feature.character.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.zc.vndb.common.ui.navigation.Navigator
import com.zc.vndb.feature.character.presentation.CharacterRoute

fun EntryProviderScope<NavKey>.characterEntry(
    onNavigateBack: () -> Unit,
    onOpenNovel: (id: String) -> Unit,
    onOpenStaff: (id: String) -> Unit,
) {
    entry<CharacterNavKey> { key ->
        CharacterRoute(
            characterId = key.id,
            onNavigateBack = onNavigateBack,
            onOpenNovel = onOpenNovel,
            onOpenStaff = onOpenStaff,
        )
    }
}

/** Opens a character's page. */
fun Navigator.navigateToCharacter(id: String) = navigate(CharacterNavKey(id))
