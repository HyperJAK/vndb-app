package com.zc.vndb.feature.listeditor.presentation

sealed interface ListEditorEffect {
    /** Saved, removed or nothing to do: the sheet closes. */
    data object Close : ListEditorEffect
}
