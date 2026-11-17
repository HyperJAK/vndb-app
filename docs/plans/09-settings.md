# Plan 09 — Settings

A Settings screen for user preferences and app info, opened from a gear icon on the Profile tab (guests and signed-in users).
Design (reference only): Stitch "VNDB – Settings". API: [VNDB Kana](https://api.vndb.org/kana). Rules: `.claude/skills/*`.
Decided 2026-11-16: no theme picker (the app was dark-only; a picker was added later, see [plan 10](10-themes.md)), no offline cover caching (no offline data), no account sync row, no bottom-bar reordering or start tab.

## 1. Screen
Top bar with Back · sections of rows (title, one-line explanation, control on the right):

| Section | Option | Control | Default |
|---|---|---|---|
| **Display** | Title language — which title comes first: romanized (`title`) or original script (`alttitle`) | Romanized / Japanese | Romanized |
| **Spoilers** | Default spoiler level on VN and character pages | None / Minor / Major | None |
| | Spoil me if finished — VNs on your Finished list open with every spoiler shown. Signed-in only (hidden for guests) | Switch | Off |
| **Adult content** | Show sensitive content — covers aren't blurred, screenshots start with every content level on, sexual tags are listed | Switch, confirm "I'm 18+" when turning on | Off |
| | Include 18+ titles in Browse — Browse starts unfiltered instead of All Ages | Switch | Off |
| **Discover** | New & Trending languages | Multi-select of release languages | English, Japanese |
| **Storage** | Image cache — size on disk | "142 MB · Clear" (confirm dialog) | — |
| **About** | Data from vndb.org (credit + link) · Source code (GitHub repo) · Privacy: "Your API token is encrypted, stays on this device and is only sent to api.vndb.org" · Version `1.0.0 (1)` from `BuildConfig` | Links / text | — |

Changes apply immediately, and screens already open pick them up the next time they load (or at once, where the screen observes settings).

## 2. What each option changes
| Option | Where | How |
|---|---|---|
| Title language | Discover, Browse, Detail (hero, relations), Character (appearances), Release (VNs), Profile (entries) | Mappers get the preference and put the chosen title first, the other second. Queries that only ask for `title` also ask for `alttitle` (`vns.alttitle`, `relations.alttitle`, Discover cards). |
| Default spoiler level | Detail, Character | Initial `spoilerLevel` of the screen state; the on-page filter still changes it for that page. |
| Spoil me if finished | Detail | When signed in and on, Detail asks `POST /ulist` (`filters: ["id", "=", vn]`, `fields: labels.id`) once; if the entry has label 2 (Finished), the page starts at Major. The use case lives in Profile's domain (it owns `/ulist`). |
| Show sensitive content | Everywhere covers show; Detail tags and media | Covers: `CoverImageView` stops blurring, through a `CompositionLocal` set at the app root. Media: screenshot toggles start with every level on. Tags: the Sexual category is listed like the others instead of only a content note. |
| Include 18+ in Browse | Browse tab and tag pages | New `AgeRating.Any` (no release-age predicate) and a third chip "Any"; the setting picks the starting value (Any vs All Ages). |
| Trending languages | Discover | `GetTrendingReleasesUseCase` takes the languages from settings instead of the hard-coded `en`, `ja`; the section chip shows them ("English & Japanese", "3 languages"). Languages offered: the common VNDB release languages (en, ja, zh-Hans, zh-Hant, ko, es, de, fr, ru, pt-br, it, pl, vi, id, th). |
| Image cache | Coil disk cache | The image loader gets an explicit disk cache (`cacheDir/image_cache`); Settings shows its size and clears the disk and memory caches. |

## 3. Code
- **Storage:** a second Preferences DataStore file, `settings`. `CommonModule` then provides two `DataStore<Preferences>`, told apart with `@Named("session")` / `@Named("settings")`.
- **Shared (`common`)** — five features read settings, so like the session they live in `common`:
  - `common/domain/model/AppSettings.kt` (+ `TitleLanguage`, `SpoilerPreference` enums — the UI's `SpoilerLevel` stays in `common/ui`)
  - `common/domain/repository/SettingsRepository.kt` — `settings: Flow<AppSettings>` and one `suspend` setter per option
  - `common/domain/usecase/ObserveSettingsUseCase.kt` (read side, used by the other features)
  - `common/data/local/SettingsLocalDataSource.kt`, `common/data/repository/SettingsRepositoryImpl.kt`
  - `common/data/local/ImageCacheDataSource.kt` (Coil `DiskCache` / `MemoryCache` size and clear) — needs a `tech-stack` change: Coil allowed in `common/data` for cache management only
- **Feature `feature/settings`:** `domain/usecase` (update use cases, `GetImageCacheSizeUseCase`, `ClearImageCacheUseCase`), `presentation` (`SettingsRoute/Screen/ViewModel/State/Intent/Effect`, row components: `SettingsSection`, switch row, choice row with a dialog, language multi-select dialog, about rows), `di/SettingsModule`.
- **App:** `SettingsNavKey` pushed on the Profile tab; Profile top bar gets the gear next to Log out. A small root ViewModel reads "show sensitive content" and provides the `CompositionLocal`.
- **Other features:** Detail, Character, Discover, Browse, Profile, Release ViewModels get `ObserveSettingsUseCase` and pass the values to their mappers / initial state.

## 4. Tests
Settings repository (DataStore round-trip, defaults, `IOException` → defaults), `SettingsViewModel`, each consumer's new behaviour (title order, starting spoiler level, Finished → Major, Any rating query, trending languages in the request, sexual tags listed), image cache size/clear with a fake.

## 5. Out of scope
Theme / light mode, offline mode and cover preloading, account management in Settings (Log out stays on Profile), bottom-bar order and start tab, app UI language.
