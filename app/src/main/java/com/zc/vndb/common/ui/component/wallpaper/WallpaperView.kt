package com.zc.vndb.common.ui.component.wallpaper

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.zc.vndb.common.ui.theme.VndbTheme

/**
 * App-wide backdrop: the current theme's wallpaper, if it has one, at low opacity over the page
 * background. Screens draw on top of it with transparent containers.
 */
@Composable
fun WallpaperView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        VndbTheme.wallpaper?.let { wallpaper ->
            Image(
                painter = painterResource(wallpaper.image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = wallpaper.alpha,
            )
        }
        content()
    }
}
