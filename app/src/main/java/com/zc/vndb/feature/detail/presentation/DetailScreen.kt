package com.zc.vndb.feature.detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.zc.vndb.R
import com.zc.vndb.common.ui.component.apptopbar.AppTopBarView
import com.zc.vndb.common.ui.component.chip.TagChipView
import com.zc.vndb.common.ui.component.error.ErrorView
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel
import com.zc.vndb.common.ui.component.expandabletext.ExpandableTextView
import com.zc.vndb.common.ui.component.linksrow.LinksRowView
import com.zc.vndb.common.ui.component.linksrow.model.LinkUIModel
import com.zc.vndb.common.ui.component.sectioncard.SectionCardView
import com.zc.vndb.common.ui.component.spoilerfilter.SpoilerFilterView
import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.detail.domain.model.PlatformFamily
import com.zc.vndb.feature.detail.presentation.component.charactercard.CharacterCardView
import com.zc.vndb.feature.detail.presentation.component.charactercard.model.CharacterCardUIModel
import com.zc.vndb.feature.detail.presentation.component.detailhero.DetailHeroView
import com.zc.vndb.feature.detail.presentation.component.detailhero.model.DetailHeroUIModel
import com.zc.vndb.feature.detail.presentation.component.editionrow.EditionRowView
import com.zc.vndb.feature.detail.presentation.component.editionrow.model.EditionRowUIModel
import com.zc.vndb.feature.detail.presentation.component.relationcard.RelationCardView
import com.zc.vndb.feature.detail.presentation.component.relationcard.model.RelationCardUIModel
import com.zc.vndb.feature.detail.presentation.component.scorecard.ScoreCardView
import com.zc.vndb.feature.detail.presentation.component.scorecard.model.ScoreUIModel
import com.zc.vndb.feature.detail.presentation.component.taggroups.TagGroupsView
import kotlinx.collections.immutable.persistentListOf

/** The page stays readable on wide screens instead of stretching edge to edge. */
private val MaxContentWidth = 720.dp

/** Callbacks built once from `onIntent`, so rows and cards get stable lambdas. */
@Immutable
private class DetailActions(
    val onLinkClick: (String) -> Unit,
    val onSpoilerLevelSelect: (SpoilerLevel) -> Unit,
    val onSynopsisToggle: () -> Unit,
    val onRelationClick: (String) -> Unit,
    val onCharacterClick: (String) -> Unit,
    val onTagClick: (String) -> Unit,
    val onEditionClick: (String) -> Unit,
)

@Composable
fun DetailScreen(
    state: DetailState,
    onIntent: (DetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val actions = remember(onIntent) {
        DetailActions(
            onLinkClick = { onIntent(DetailIntent.LinkClicked(it)) },
            onSpoilerLevelSelect = { onIntent(DetailIntent.SpoilerLevelSelected(it)) },
            onSynopsisToggle = { onIntent(DetailIntent.SynopsisToggled) },
            onRelationClick = { onIntent(DetailIntent.RelationClicked(it)) },
            onCharacterClick = { onIntent(DetailIntent.CharacterClicked(it)) },
            onTagClick = { onIntent(DetailIntent.TagClicked(it)) },
            onEditionClick = { onIntent(DetailIntent.EditionClicked(it)) },
        )
    }
    Scaffold(
        modifier = modifier,
        topBar = { DetailTopBar(onIntent) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        val contentModifier = Modifier
            .padding(padding)
            .fillMaxSize()
        when {
            state.error != null -> ErrorView(
                model = state.error,
                onRetry = { onIntent(DetailIntent.Retry) },
                modifier = contentModifier,
            )

            state.hero == null -> Box(contentModifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VndbTheme.colors.accent)
            }

            else -> Box(contentModifier, contentAlignment = Alignment.TopCenter) {
                LazyColumn(
                    modifier = Modifier
                        .widthIn(max = MaxContentWidth)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = VndbTheme.spacing.margin,
                        vertical = VndbTheme.spacing.sm,
                    ),
                    verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.md),
                ) {
                    detailSections(state, state.hero, actions)
                }
            }
        }
    }
}

@Composable
private fun DetailTopBar(onIntent: (DetailIntent) -> Unit) {
    AppTopBarView(
        onBack = { onIntent(DetailIntent.BackClicked) },
        actions = {
            IconButton(onClick = { onIntent(DetailIntent.ShareClicked) }) {
                Icon(
                    Icons.Outlined.Share,
                    contentDescription = stringResource(R.string.detail_share),
                )
            }
            IconButton(onClick = { onIntent(DetailIntent.OpenInBrowserClicked) }) {
                Icon(
                    Icons.AutoMirrored.Outlined.OpenInNew,
                    contentDescription = stringResource(R.string.detail_open_in_browser),
                )
            }
        },
    )
}

private fun LazyListScope.detailSections(
    state: DetailState,
    hero: DetailHeroUIModel,
    actions: DetailActions,
) {
    item(key = "id", contentType = "id") { TagChipView(label = UiText.StringValue(state.novelId)) }
    item(
        key = "hero",
        contentType = "hero",
    ) { DetailHeroView(model = hero, modifier = Modifier.fillMaxWidth()) }
    state.score?.let { score ->
        item(
            key = "score",
            contentType = "score",
        ) { ScoreCardView(model = score, modifier = Modifier.fillMaxWidth()) }
    }
    if (state.links.isNotEmpty()) {
        item(
            key = "links",
            contentType = "links",
        ) { LinksRowView(links = state.links, onLinkClick = actions.onLinkClick) }
    }
    item(key = "spoilers", contentType = "spoilers") {
        SpoilerFilterView(
            level = state.spoilerLevel,
            onLevelSelect = actions.onSpoilerLevelSelect,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    state.synopsis?.let { synopsis ->
        item(key = "synopsis", contentType = "synopsis") {
            ExpandableTextView(
                title = UiText.StringResource(R.string.detail_synopsis),
                text = synopsis,
                isExpanded = state.isSynopsisExpanded,
                onToggle = actions.onSynopsisToggle,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
    item(key = "tags", contentType = "tags") {
        TagGroupsView(
            groups = state.tagGroups,
            hiddenLabel = state.hiddenTagsLabel,
            notes = state.contentNotes,
            onTagClick = actions.onTagClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    charactersSection(state, actions)
    editionsSection(state, actions)
    relationsSection(state, actions)
}

private fun LazyListScope.charactersSection(state: DetailState, actions: DetailActions) {
    if (state.characters.isEmpty() && state.hiddenCharactersLabel == null) return
    item(key = "characters", contentType = "characters") {
        SectionCardView(
            title = UiText.StringResource(R.string.detail_characters),
            trailing = state.mainCharactersLabel?.let { label ->
                { TagChipView(label = label, colors = VndbTheme.colors.statusPlaying) }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.md)) {
                items(
                    items = state.characters,
                    key = { it.id },
                ) { character ->
                    CharacterCardView(
                        model = character,
                        onClick = { actions.onCharacterClick(character.id) },
                    )
                }
            }
            state.hiddenCharactersLabel?.let { hidden -> MutedNote(hidden) }
        }
    }
}

private fun LazyListScope.editionsSection(state: DetailState, actions: DetailActions) {
    if (state.editions.isEmpty()) return
    item(key = "editions", contentType = "editions") {
        SectionCardView(
            title = UiText.StringResource(R.string.detail_releases),
            modifier = Modifier.fillMaxWidth(),
        ) {
            state.editions.forEach { edition ->
                EditionRowView(
                    model = edition,
                    onClick = { actions.onEditionClick(edition.releaseId) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private fun LazyListScope.relationsSection(state: DetailState, actions: DetailActions) {
    if (state.relations.isEmpty()) return
    item(key = "relations", contentType = "relations") {
        SectionCardView(
            title = UiText.StringResource(R.string.detail_relations),
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm)) {
                items(items = state.relations, key = { it.id }) { relation ->
                    RelationCardView(
                        model = relation,
                        onClick = { actions.onRelationClick(relation.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun MutedNote(text: UiText) {
    Row {
        Text(
            text = text.asString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private val previewState = DetailState(
    novelId = "v2002",
    hero = DetailHeroUIModel(
        title = UiText.StringValue("Steins;Gate"),
        originalTitle = UiText.StringValue("シュタインズ・ゲート"),
        coverUrl = null,
        isCoverSensitive = false,
        ratingLabel = UiText.StringValue("9.02"),
        developerLabel = UiText.StringValue("5pb. / Nitroplus"),
        releasedLabel = UiText.StringValue("2009-10-15"),
        lengthLabel = UiText.StringValue("30-50h"),
        averagePlayLabel = UiText.StringValue("44h avg"),
        ageLabel = UiText.StringValue("All Ages / 15+"),
        tags = persistentListOf(UiText.StringValue("Sci-Fi"), UiText.StringValue("Time Travel")),
    ),
    score = ScoreUIModel(
        UiText.StringValue("9.02"),
        UiText.StringValue("17,205 recorded votes"),
        UiText.StringValue("Average 9.02"),
    ),
    links = persistentListOf(
        LinkUIModel(UiText.StringValue("vndb.org/v2002"), "https://vndb.org/v2002", isVndb = true),
    ),
    synopsis = UiText.StringValue(
        "Set in Akihabara in summer 2010, self-proclaimed mad scientist Rintaro Okabe…",
    ),
    characters = persistentListOf(
        CharacterCardUIModel(
            id = "c1",
            name = UiText.StringValue("Okabe Rintarou"),
            originalName = UiText.StringValue("岡部 倫太郎"),
            roleLabel = UiText.StringValue("ROLE: MAIN"),
            isMain = true,
            voiceActorLabel = UiText.StringValue("CV: Miyano Mamoru"),
            imageUrl = null,
            isImageSensitive = false,
        ),
    ),
    mainCharactersLabel = UiText.StringValue("1 Main"),
    editions = persistentListOf(
        EditionRowUIModel(
            key = "r1-win",
            releaseId = "r1",
            title = UiText.StringValue("Steins;Gate"),
            platformLabel = UiText.StringValue("Windows"),
            languagesLabel = UiText.StringValue("Japanese"),
            dateLabel = UiText.StringValue("2010-08-26"),
            family = PlatformFamily.Pc,
            familyLabel = UiText.StringValue("PC"),
        ),
    ),
    relations = persistentListOf(
        RelationCardUIModel(
            id = "v17102",
            title = UiText.StringValue("Steins;Gate 0"),
            relationLabel = UiText.StringValue("SIDE STORY"),
            isOfficial = true,
            yearLabel = UiText.StringValue("2015"),
            scoreLabel = UiText.StringValue("Score: 8.08"),
        ),
    ),
)

private class DetailStateProvider : PreviewParameterProvider<DetailState> {
    override val values = sequenceOf(
        previewState,
        DetailState(novelId = "v2002", isLoading = true),
        DetailState(
            novelId = "v2002",
            error = ErrorUIModel(
                UiText.StringResource(R.string.error_not_found),
                isRetryable = false,
            ),
        ),
    )
}

@AdaptivePreviews
@Composable
private fun DetailScreenPreview(@PreviewParameter(DetailStateProvider::class) state: DetailState) {
    VndbTheme {
        DetailScreen(state = state, onIntent = {})
    }
}
