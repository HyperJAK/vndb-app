package com.zc.vndb.common.ui.component.pulltorefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

/**
 * Pull to refresh around a scrollable [content] (a lazy list or grid), with the indicator in the
 * app's accent color.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshView(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = state,
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = state,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = VndbTheme.colors.surfaceInset,
                color = VndbTheme.colors.accent,
            )
        },
        content = content,
    )
}

@AdaptivePreviews
@Composable
private fun PullToRefreshViewPreview() {
    VndbTheme {
        PullToRefreshView(isRefreshing = true, onRefresh = {}) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item { Box { Text("Content") } }
            }
        }
    }
}
