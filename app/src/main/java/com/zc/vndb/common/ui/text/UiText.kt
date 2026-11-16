package com.zc.vndb.common.ui.text

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.zc.vndb.common.ui.resource.StringResource as StringResourceId

/**
 * Text shown in the UI layer. Lets ViewModels and UI models expose strings without
 * resolving resources themselves; resolve with [asString] at the edge (Composable or [Context]).
 */
@Immutable
sealed interface UiText {

    /** Hardcoded or already-resolved text (e.g. data coming from the API). */
    data class StringValue(val value: String) : UiText

    /** A `R.string.*` resource, optionally formatted with [args]. */
    data class StringResource(
        @StringRes val id: StringResourceId,
        val args: List<Any> = emptyList(),
    ) : UiText

    /** A `R.plurals.*` resource picked by [quantity], formatted with [args]. */
    data class StringPlural(
        @PluralsRes val id: Int,
        val quantity: Int,
        val args: List<Any> = emptyList(),
    ) : UiText

    @Composable
    fun asString(): String = when (this) {
        is StringValue -> value
        is StringResource -> stringResource(id, *args.toTypedArray())
        is StringPlural -> pluralStringResource(id, quantity, *args.toTypedArray())
    }

    fun asString(context: Context): String = when (this) {
        is StringValue -> value
        is StringResource -> context.getString(id, *args.toTypedArray())
        is StringPlural -> context.resources.getQuantityString(id, quantity, *args.toTypedArray())
    }
}
