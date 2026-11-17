package com.zc.vndb.feature.theme.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.ui.component.apptopbar.AppTopBarView
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.theme.presentation.component.themepreview.ThemePreviewView
import com.zc.vndb.feature.theme.presentation.mapper.themeState

private val MaxContentWidth = 640.dp
private val MinPreviewWidth = 100.dp

@Composable
fun ThemeScreen(state: ThemeState, onIntent: (ThemeIntent) -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { AppTopBarView(onBack = { onIntent(ThemeIntent.BackClicked) }) },
        containerColor = Color.Transparent,
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(MinPreviewWidth),
                modifier = Modifier
                    .widthIn(max = MaxContentWidth)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = VndbTheme.spacing.margin,
                    vertical = VndbTheme.spacing.sm,
                ),
                horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.lg),
            ) {
                item(key = "title", span = { GridItemSpan(maxLineSpan) }, contentType = "title") {
                    Text(
                        text = UiText.StringResource(StringResource.theme_title).asString(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                items(state.themes, key = { it.theme.name }, contentType = { "theme" }) { model ->
                    ThemePreviewView(
                        model = model,
                        onSelect = { onIntent(ThemeIntent.ThemeSelected(model.theme)) },
                    )
                }
            }
        }
    }
}

@AdaptivePreviews
@Composable
private fun ThemeScreenPreview() {
    VndbTheme {
        ThemeScreen(state = themeState(AppTheme.AngelicSerenade), onIntent = {})
    }
}
