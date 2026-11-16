# Plan 01 — Discover (first feature)

Design: Stitch project "VNDB Mobile Client", screen **VNDB - Discover** (390 × 1362).
API: [VNDB Kana](https://api.vndb.org/kana). Rules: `.claude/skills/*` (architecture, structure, conventions, tech-stack, library-management, best-practices) — load all six before writing code.

---

## 1. What the screen shows → where the data comes from

| UI block (top → bottom) | Content | Source |
|---|---|---|
| Top bar | `VNDB` wordmark, `v2.5` mono pill, icon action (top-right) | Static. Icon action = no-op for now (see §8). |
| Header | "CURATED ARCHIVE" overline, **Discover** title, **Random Roll** button | Static + `RollRandomNovelUseCase` |
| Hero "VN of the Week" | Cover, ★ 8.92 / 10, developer (TYPE-MOON), scenario writer (Kinoko Nasu), title, 3-line description, tag chips, age chip ("All Ages"), **View Details →** | `POST /vn` (by id) |
| **Top Rated** row | Horizontal covers with ★ badge, title, developer; "See all" | `POST /vn` sort `rating` desc, `votecount >= 500` |
| **New & Trending** list | Thumbnail, badge (NEW TRANSLATION / RECENT PATCH / RE-RELEASE) · month year, title, platform chips, ★ | `POST /release` sort `released` desc, lang filter |
| Filter chip "English & JP" | Release language filter | Static in v1 (en + ja) |
| Stats footer | "44,820 Visual Novels — Indexed & curated by readers", chevron | `GET /stats` → `vn` |
| Bottom nav | Discover · Browse · Profile | `NavigationSuiteScaffold`; only Discover is real (§8) |

All requests are unauthenticated. One screen load = 4 requests (stats, top rated, hero details, releases) — well within 200 req / 5 min.

### API queries (verified against the live API on 2026-11-16)

```jsonc
// Top Rated (also the pool the weekly hero is picked from)
POST /vn
{ "filters": ["votecount", ">=", 500], "sort": "rating", "reverse": true, "results": 50,
  "fields": "title, rating, image.thumbnail, image.sexual, image.violence, developers.name" }

// Hero details (single id)
POST /vn
{ "filters": ["id", "=", "v2002"], "results": 1,
  "fields": "title, rating, description, image.url, image.sexual, image.violence, developers.name, staff.name, staff.role, tags.name, tags.rating, tags.spoiler, tags.category" }

// New & Trending
POST /release
{ "filters": ["and", ["released", "<=", "today"], ["or", ["lang", "=", "en"], ["lang", "=", "ja"]]],
  "sort": "released", "reverse": true, "results": 10,
  "fields": "title, released, patch, official, platforms, minage, languages.lang, languages.mtl, vns.id, vns.title, vns.olang, vns.released, vns.rating, vns.image.thumbnail, vns.image.sexual" }

// Footer
GET /stats            // { "vn": 68053, ... }

// Random Roll: pick n in 1..maxId, then first VN with id >= n (ids are sparse)
POST /vn  { "filters": ["id", ">=", "v<n>"], "sort": "id", "results": 1, "fields": "title" }
```

Notes from the live responses:
- `rating` is 10–100 and **nullable** (new VNs) → UI shows `rating / 10` with one decimal, or hides the badge.
- `released` may be `"TBA"`, `"2024"`, `"2024-10"` or a full date → parse into a domain `ReleaseDate` (year / year-month / full / TBA), never assume `LocalDate`.
- `description` contains BBCode (`[url=/c735]Rance[/url]`, `[spoiler]`) → strip to plain text in the UI mapper (hero shows 3 lines).
- `image` is nullable; `image.sexual` / `violence` are 0–2 averages.

### Derived rules (domain, unit-tested)

- **VN of the Week** — deterministic: `pool[isoWeekNumber(today) % pool.size]` from the Top Rated pool, so it changes weekly and is the same for every user. `Clock` is injected so tests can pin the date.
- **Release badge** — `patch == true` → RECENT PATCH; else if the release has a language ≠ `vn.olang` and `vn.released` is earlier than this release → NEW TRANSLATION; else if `vn.released` is earlier → RE-RELEASE; else NEW RELEASE. Releases with `mtl == true` only are excluded. Dedupe by VN id (keep newest).
- **Scenario writer** — first `staff` with `role == "scenario"`.
- **Tag chips** — top 2 by `rating`, `spoiler == 0`, `category == "cont"` (content tags); skip `ero`.
- **Age chip** — heuristic only, no extra request: cover `sexual < 0.5` and no `ero`-category tags → "All Ages", else the chip is omitted. (Decided §9.)
- **Cover safety** — `image.sexual >= 1` (or `violence >= 1`) → `isCoverSensitive = true` → blurred by `CoverImageView`. Settings toggle comes later.

---

## 2. Foundation work (first feature pays for it)

The repo currently has only `app/MainActivity.kt` and `common/ui/{theme,text,resource}`. Before the feature:

### 2.1 Dependencies (`library-management`)
Add to `gradle/libs.versions.toml` with one version key per train, then to `app/build.gradle.kts`:
- Plugin: `kotlin-serialization`.
- Ktor (`core`, `okhttp`, `content-negotiation`, `serialization-kotlinx-json`, `logging`) as `bundles.ktor.client`; `kotlinx-serialization-json`.
- Koin BOM + `koin-android`, `koin-androidx-compose`.
- Navigation 3 (`runtime`, `ui`), `lifecycle-viewmodel-navigation3`, `lifecycle-runtime-compose`, `lifecycle-viewmodel-compose`.
- Material 3 Adaptive (`adaptive`, `adaptive-layout`, `adaptive-navigation3`, `material3-adaptive-navigation-suite`).
- Coil 3 (`coil-compose`, `coil-network-ktor3`).
- `kotlinx-datetime`, `kotlinx-collections-immutable`, `kotlinx-coroutines-android`, `material-icons-extended` (if icons are missing from core).
- Test bundle: `kotlin-test`, `kotlinx-coroutines-test`, `turbine`, `ktor-client-mock`, `koin-test`.
- DataStore and RichText are **not** needed for this screen — skip them until a feature needs them.

### 2.2 `common/`
| File | Purpose |
|---|---|
| `common/data/network/HttpClientFactory.kt` | `createHttpClient(json, engine)` + `appJson` (base URL `https://api.vndb.org/kana/`, `expectSuccess`, timeouts, logging in debug only) |
| `common/data/util/SafeApiCall.kt` | exceptions → `DomainError` (incl. 429 → `RateLimited`) |
| `common/domain/model/DomainError.kt` | sealed error hierarchy |
| `common/domain/util/DispatcherProvider.kt` | interface + default |
| `common/di/CommonModule.kt` | `Json`, `HttpClient`, `DispatcherProvider`, `Clock` |
| `common/ui/util/AdaptivePreviews.kt`, `ObserveAsEvents.kt` | previews + effect collection |
| `common/ui/component/error/{model,mapper}/`, `ErrorView.kt` | `ErrorUIModel`, `Throwable.toErrorUIModel()`, retry UI |
| `common/ui/component/coverimage/CoverImageView.kt` | Coil `AsyncImage`, 2:3, placeholder, blur when sensitive |
| `common/ui/component/ratingbadge/RatingBadgeView.kt` | ★ score pill (used by hero, cards, list rows; reused by later features) |
| `common/ui/component/chip/TagChipView.kt` | mono-font chip used for tags/platforms (design-system token) |
| `res/drawable/cover_placeholder.xml`, error strings in `strings.xml` | |

### 2.3 `app/`
| File | Purpose |
|---|---|
| `app/AppApplication.kt` | `startKoin`, Coil `SingletonImageLoader.Factory` with Ktor fetcher; register in manifest |
| `app/di/AppModule.kt` | `includes(commonModule, discoverModule)` |
| `app/navigation/AppNavigation.kt` | `DiscoverNavKey`, `BrowseNavKey`, `ProfileNavKey`; `NavigationSuiteScaffold` with Discover / Browse / Profile (the last two render an empty placeholder); `NavDisplay` with saveable-state + ViewModel-store decorators |
| `app/MainActivity.kt` | `setContent { VndbTheme { AppNavigation() } }`, edge-to-edge |

---

## 3. Feature `feature/discover`

### 3.1 `domain`
```
domain/model/
  DiscoverNovel.kt          id, title, rating: Int?, coverUrl, isCoverSensitive, developerName
  FeaturedNovel.kt          + description, scenarioWriter, tags: List<String>, isAllAges
  TrendingRelease.kt        releaseId, novelId, title, kind: ReleaseKind, releaseDate: ReleaseDate, platforms: List<Platform>, rating, thumbnailUrl, isCoverSensitive
  ReleaseKind.kt            enum NEW_TRANSLATION, RECENT_PATCH, RE_RELEASE, NEW_RELEASE
  ReleaseDate.kt            sealed: Full(LocalDate) / YearMonth / Year / Tba
  Platform.kt               enum of VNDB platform codes used in the UI (win, lin, mac, swi, ps4, and, ios, web, …) + Other(code)
  DatabaseStats.kt          novelCount
domain/repository/
  DiscoverRepository.kt     getTopRated(limit), getNovel(id), getRecentReleases(langs, limit), getStats(), getFirstNovelFrom(id)
domain/usecase/
  GetTopRatedNovelsUseCase.kt
  GetFeaturedNovelUseCase.kt      picks weekly id from top-rated pool (Clock) → getNovel
  GetTrendingReleasesUseCase.kt   badge derivation, mtl filter, dedupe
  GetDatabaseStatsUseCase.kt
  RollRandomNovelUseCase.kt       random id in 1..maxId → first VN id ≥ it; returns id
```
`Platform`, `ReleaseDate` and `DomainError` are candidates for `common/domain` once a second feature uses them — keep them in the feature until then (`structure` §3).

### 3.2 `data`
```
data/remote/DiscoverService.kt     queryNovels(VisualNovelQueryDTO), queryReleases(ReleaseQueryDTO), getStats()
data/dto/  (one per file)          VisualNovelQueryDTO, ReleaseQueryDTO, VisualNovelPageDTO, VisualNovelDTO,
                                   ImageDTO, DeveloperDTO, StaffDTO, TagDTO, ReleasePageDTO, ReleaseDTO,
                                   ReleaseLanguageDTO, ReleaseVisualNovelDTO, StatsDTO
data/mapper/                       VisualNovelDTOMapper.kt, ReleaseDTOMapper.kt, StatsDTOMapper.kt,
                                   ReleaseDateDTOMapper.kt (string → ReleaseDate)
data/repository/DiscoverRepositoryImpl.kt   safeApiCall around every call
```
Filters are heterogeneous JSON arrays → build them as `JsonArray` (`buildJsonArray`) inside the query DTO, not a typed DTO tree. Field lists live as constants in the service.

### 3.3 `presentation`
```
DiscoverState.kt        isLoading, error: ErrorUIModel?, featured: FeaturedNovelUIModel?, topRated: ImmutableList<NovelCardUIModel>,
                        trending: ImmutableList<TrendingReleaseUIModel>, stats: StatsUIModel?, isRolling: Boolean
DiscoverIntent.kt       Load, Retry, Refresh, RollRandom, OpenNovel(id), SeeAllTopRated, OpenStats
DiscoverEffect.kt       NavigateToNovel(id), NavigateToBrowse, ShowMessage(UiText)
DiscoverViewModel.kt    parallel loads (coroutineScope + async); each section fails independently
                        (hero/top-rated failure = full error; trending/stats failure = section hidden)
DiscoverRoute.kt        koinViewModel(), ObserveAsEvents → callbacks
DiscoverScreen.kt       LazyColumn; header + sections; adaptive: expanded width = hero and trending side by side,
                        Top Rated as a wider row
component/
  featurednovel/        FeaturedNovelView + model/FeaturedNovelUIModel + mapper/
  novelcard/            NovelCardView (Top Rated item) + model/NovelCardUIModel + mapper/
  trendingrelease/      TrendingReleaseView + model/TrendingReleaseUIModel + mapper/ (badge text/colour, "Oct 2024", platform labels)
  sectionheader/        SectionHeaderView (icon, title, trailing slot: "See all" / filter chip)
  statsfooter/          StatsFooterView (formatted count, chevron)
```
Mappers own all formatting: rating `89` → `"8.9"`, BBCode stripping, `ReleaseDate` → "Oct 2024" (`UiText`), platform code → label, badge → `UiText` + `ReleaseBadgeTone` (maps to `secondary`/`primary`/`tertiary` tokens from `DESIGN.md`).

### 3.4 `di/DiscoverModule.kt`
`singleOf(::DiscoverService)`, `singleOf(::DiscoverRepositoryImpl) { bind<DiscoverRepository>() }`, `factoryOf` each use case, `viewModelOf(::DiscoverViewModel)`.

---

## 4. Theme work
`DESIGN.md` (Nocturne VN) is already in `common/ui/theme`. Check that it covers what this screen needs, and add whatever is missing:
- extended colours `surface-card`, `surface-elevated`, `border-subtle`, badge tones (via a `VndbExtendedColors` CompositionLocal)
- Space Grotesk headline / mono label styles (overline "CURATED ARCHIVE", chips, version pill)
- the gradient scrim on the hero cover

---

## 5. Tests (`best-practices` §6)
- **Use cases**: weekly pick is stable within a week and changes across weeks (fixed `Clock`); badge derivation table (patch / translation / re-release / new; mtl-only excluded; dedupe); random roll falls back when nothing is ≥ n.
- **Data**: `DiscoverService` with `MockEngine` — the JSON request body (filters/fields/sort) matches §1; responses decode from recorded fixtures (`src/test/resources/discover/*.json`, captured from the live API); `ReleaseDateDTOMapper` for `TBA` / `2024` / `2024-10` / full date; 429 → `DomainError.RateLimited`.
- **Presentation**: `DiscoverViewModel` with fake use cases + Turbine — loading → success, hero failure → error + Retry, trending failure → section hidden, RollRandom → `NavigateToNovel` effect; UI mapper tests (rating format, BBCode strip, null rating).
- **DI**: `KoinModulesTest` `appModule.verify()`.
- Previews: `@AdaptivePreviews` on `DiscoverScreen` (loading / content / error) and every `*View`.

---

## 6. Order of work (each step builds and passes `./gradlew detekt testDebugUnitTest`)
1. Dependencies + serialization plugin (§2.1).
2. `common` networking/error/dispatcher + `app` Koin/Coil/Application wiring (§2.2–2.3, no UI yet).
3. Discover `domain` + use-case tests.
4. Discover `data` + MockEngine/fixture tests.
5. Theme additions + shared `common/ui/component`s with previews.
6. Discover `presentation` + ViewModel/mapper tests.
7. `AppNavigation` with `NavigationSuiteScaffold`; run on emulator, compare to Stitch at 390 dp and an expanded width.
8. Run every check listed in the skills (`architecture` / `structure` / `tech-stack` grep checks print nothing).

---

## 7. Done when
- Discover renders live VNDB data matching the Stitch layout on compact; uses an adaptive two-pane arrangement on expanded.
- Random Roll and every card emit `NavigateToNovel(id)` (wired to a placeholder detail destination until feature 02).
- Cold-start load is 4 requests; rotation does not reload (ViewModel `init` load).
- No-network / 429 shows `ErrorView` with Retry.
- Detekt, unit tests and the skill grep checks all pass.

## 8. Out of scope (next features)
- **Title Detail** screen (already designed in Stitch) — feature 02; Discover only emits the navigation effect.
- **Browse** (target of "See all" and stats footer) and **Profile** (needs auth token + `/ulist`) — bottom-nav items open an empty placeholder screen; "See all" / stats footer navigate to the Browse placeholder.
- Language filter picker (chip is static "English & JP" in v1), NSFW settings toggle (DataStore), top-right icon action.

## 9. Decisions (2026-11-16)
1. **Age chip** — guessed from the cover flag + tags; no additional request.
2. **VN of the Week** — weekly rotation over the top-50 rated pool.
3. **Browse / Profile tabs** — enabled in the bottom nav; each opens an empty placeholder screen (`BrowseNavKey`, `ProfileNavKey` → a shared empty placeholder composable in `app/navigation/`) until their features land.
