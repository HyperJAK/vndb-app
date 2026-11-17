package com.zc.vndb.feature.detail.presentation.navigation

/** Everything Detail can ask the host app to open. */
data class DetailNavActions(
    val onNavigateBack: () -> Unit,
    val onOpenNovel: (id: String) -> Unit,
    val onOpenCharacter: (id: String) -> Unit,
    val onOpenTag: (id: String) -> Unit,
    val onOpenProducer: (id: String) -> Unit,
    val onOpenStaff: (id: String) -> Unit,
    val onOpenRelease: (id: String) -> Unit,
    val onOpenListEditor: (novelId: String, title: String) -> Unit,
)
