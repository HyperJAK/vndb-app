package com.zc.vndb.common.ui.component.coverimage

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val SensitiveBlur = 24.dp
private val SensitiveIconSize = 24.dp
private val PreviewCoverWidth = 120.dp

/**
 * A VNDB cover, cropped to fill the size the caller gives it, with the cover shape.
 * Sensitive covers are blurred; before Android 12 (no blur support) they are not loaded at all.
 */
@Composable
fun CoverImageView(
    url: String?,
    isSensitive: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: UiText? = null,
) {
    val placeholder = ColorPainter(VndbTheme.colors.surfaceCard)
    val canBlur = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    Box(
        modifier = modifier
            .clip(VndbTheme.shapes.cover)
            .background(VndbTheme.colors.surfaceCard),
        contentAlignment = Alignment.Center,
    ) {
        if (!isSensitive || canBlur) {
            AsyncImage(
                model = url,
                contentDescription = contentDescription?.asString(),
                contentScale = ContentScale.Crop,
                placeholder = placeholder,
                error = placeholder,
                fallback = placeholder,
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (isSensitive) Modifier.blur(SensitiveBlur) else Modifier),
            )
        }
        if (isSensitive) {
            Icon(
                imageVector = Icons.Outlined.VisibilityOff,
                contentDescription = stringResource(R.string.cd_sensitive_cover),
                tint = VndbTheme.colors.placeholder,
                modifier = Modifier.size(SensitiveIconSize),
            )
        }
    }
}

@AdaptivePreviews
@Composable
private fun CoverImageViewPreview() {
    VndbTheme {
        Row {
            CoverImageView(
                url = null,
                isSensitive = false,
                modifier = Modifier.width(
                    PreviewCoverWidth,
                ).aspectRatio(VndbTheme.dimens.coverAspectRatio),
            )
            CoverImageView(
                url = null,
                isSensitive = true,
                modifier = Modifier.width(
                    PreviewCoverWidth,
                ).aspectRatio(VndbTheme.dimens.coverAspectRatio),
            )
        }
    }
}
