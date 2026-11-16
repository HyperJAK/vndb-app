package com.zc.vndb.common.ui.text

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Text shown in the UI layer. Lets ViewModels and UI models expose strings without
 * resolving resources themselves; resolve with [asString] at the edge (Composable or [Context]).
 *
 * This is the only place that resolves resources (`stringResource`, `getString`, …), which is why
 * they are called by their full names here: Detekt forbids importing them anywhere else.
 */
@Immutable
sealed interface UiText {

    /** Hardcoded or already-resolved text (e.g. data coming from the API). */
    data class StringValue(val value: String) : UiText

    /** A `StringResource.*` resource, optionally formatted with [args]. */
    data class StringResource(@StringRes val id: Int, val args: List<Any> = emptyList()) : UiText

    /** A `PluralResource.*` resource picked by [quantity], formatted with [args]. */
    data class StringPlural(
        @PluralsRes val id: Int,
        val quantity: Int,
        val args: List<Any> = emptyList(),
    ) : UiText

    /** Arguments that are themselves [UiText] are resolved first, so labels can be composed. */
    @Composable
    fun asString(): String = when (this) {
        is StringValue -> value

        is StringResource -> androidx.compose.ui.res.stringResource(
            id,
            *args.map { if (it is UiText) it.asString() else it }.toTypedArray(),
        )

        is StringPlural ->
            androidx.compose.ui.res.pluralStringResource(
                id,
                quantity,
                *args.map { if (it is UiText) it.asString() else it }.toTypedArray(),
            )
    }

    fun asString(context: Context): String = when (this) {
        is StringValue -> value

        is StringResource -> context.getString(
            id,
            *args.map { if (it is UiText) it.asString(context) else it }.toTypedArray(),
        )

        is StringPlural -> context.resources.getQuantityString(
            id,
            quantity,
            *args.map { if (it is UiText) it.asString(context) else it }.toTypedArray(),
        )
    }
}
