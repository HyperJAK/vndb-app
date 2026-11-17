# Plan 13 — Navigation restructure (per-feature NavKeys & entry extension functions)

**Status: Done on `enhancement/navigation-migration`, except fix 3 (deferred, see §6).**

Goal: stop `app/navigation/AppNavigation.kt` from being the one file that knows every screen. Each feature owns its NavKeys and an `EntryProviderScope<NavKey>` extension function that registers its entries; `app/` owns the scaffold, the tabs and the wiring between features. Pure refactor: **no user-visible behaviour change** except the small fixes in §6 (Login now hides the tab bar).

Rules: `.claude/skills/*` (architecture, structure, conventions, best-practices) — load before writing code. Navigation 3 reference: the `navigation-3` skill.

---

## 1. Problems today

- `AppNavigation.kt` holds 13 NavKeys, 2 marker interfaces, tab definitions, the scaffold and every `entry<>`. Each new screen edits it (Release, Tag, Character, Producer, Staff all did).
- A key and its route live far apart (`ListEditorNavKey` vs `ListEditorRoute`).
- `TagNavKey` / `ProducerNavKey` / `StaffNavKey` entries are three copies of the same code.
- `DetailRoute` receives 8 lambdas, each wrapped inline in the entry provider.
- Inconsistencies: `NovelDetailNavKey` vs `DetailRoute`; `LoginNavKey` is the only pushed page that keeps the bar visible without saying why; `ListEditorNavKey` stores a display title in the back stack.
- `Navigator.navigate` decides "tab or push" with `route in state.backStacks.keys`, an implicit check.
- No unit tests for `Navigator` or NavKey serialization.

## 2. Target structure

```text
common/ui/navigation/                      # shared, feature-agnostic navigation plumbing
├── FullScreenNavKey.kt                    # marker interface (moved from app)
├── OverlayNavKey.kt                       # marker interface (moved from app)
├── BottomSheetSceneStrategy.kt            # moved from app
├── NavigationState.kt                     # moved from app
└── Navigator.kt                           # moved from app

feature/<name>/presentation/navigation/    # one per feature that has a screen
├── <Name>NavKey.kt                        # @Serializable key(s) owned by this feature
└── <Name>Navigation.kt                    # fun EntryProviderScope<NavKey>.<name>Entry(...)

app/navigation/
├── AppNavigation.kt                       # NavigationSuiteScaffold + NavDisplay only
├── TopLevelDestination.kt                 # tab key, icon, label
└── AppEntryProvider.kt                    # entryProvider that calls every feature's entry function
```

Keys per feature:

| Feature | Keys (package `…feature.<name>.presentation.navigation`) |
|---|---|
| discover | `DiscoverNavKey` (tab) |
| browse | `BrowseNavKey` (tab), `TagNavKey`, `ProducerNavKey`, `StaffNavKey` |
| profile | `ProfileNavKey` (tab) |
| detail | `DetailNavKey` (renamed from `NovelDetailNavKey`) |
| character | `CharacterNavKey` |
| release | `ReleaseNavKey` |
| settings | `SettingsNavKey` |
| theme | `ThemeNavKey` |
| login | `LoginNavKey` |
| listeditor | `ListEditorNavKey` (`OverlayNavKey`) |

## 3. Entry extension functions

Each feature exposes one extension function that registers its entries. It knows its own key and its own Route, and receives everything that leaves the feature as lambdas. **Features never import each other's keys or anything else from another feature**; the dependency direction stays `app → feature`.

```kotlin
// feature/character/presentation/navigation/CharacterNavigation.kt
@Serializable
data class CharacterNavKey(val id: String) : FullScreenNavKey

fun EntryProviderScope<NavKey>.characterEntry(
    onNavigateBack: () -> Unit,
    onOpenNovel: (id: String) -> Unit,
    onOpenStaff: (id: String) -> Unit,
) {
    entry<CharacterNavKey> { key ->
        CharacterRoute(
            characterId = key.id,
            onNavigateBack = onNavigateBack,
            onOpenNovel = onOpenNovel,
            onOpenStaff = onOpenStaff,
        )
    }
}
```

Routes with many callbacks (Detail has 8) take a small actions class defined next to their key, instead of a long parameter list:

```kotlin
// feature/detail/presentation/navigation/DetailNavigation.kt
@Serializable
data class DetailNavKey(val id: String) : FullScreenNavKey

/** Everything Detail can ask the host app to open. */
data class DetailNavActions(
    val onBack: () -> Unit,
    val onOpenNovel: (id: String) -> Unit,
    val onOpenCharacter: (id: String) -> Unit,
    val onOpenTag: (id: String) -> Unit,
    val onOpenProducer: (id: String) -> Unit,
    val onOpenStaff: (id: String) -> Unit,
    val onOpenRelease: (id: String) -> Unit,
    val onOpenListEditor: (novelId: String) -> Unit,
)

fun EntryProviderScope<NavKey>.detailEntry(actions: DetailNavActions) {
    entry<DetailNavKey> { key -> DetailRoute(novelId = key.id, actions = actions) }
}
```

Use an actions class only when a Route has more than ~4 navigation callbacks; otherwise plain lambdas. `DetailRoute` / `DetailViewModel` signature changes accordingly (the Route takes the actions class instead of individual lambdas).

The app layer is the only place that knows every key and connects the features:

```kotlin
// app/navigation/AppEntryProvider.kt
fun appEntryProvider(navigator: Navigator) = entryProvider<NavKey> {
    val openNovel: (String) -> Unit = { navigator.navigate(DetailNavKey(it)) }
    val openStaff: (String) -> Unit = { navigator.navigate(StaffNavKey(it)) }

    discoverEntry(
        onOpenNovel = openNovel,
        onOpenBrowse = { navigator.navigate(BrowseNavKey) },
    )
    browseEntry(onOpenNovel = openNovel)
    browseScopedEntries(onNavigateBack = navigator::goBack, onOpenNovel = openNovel)
    detailEntry(
        DetailNavActions(
            onBack = navigator::goBack,
            onOpenNovel = openNovel,
            onOpenCharacter = { navigator.navigate(CharacterNavKey(it)) },
            onOpenTag = { navigator.navigate(TagNavKey(it)) },
            onOpenProducer = { navigator.navigate(ProducerNavKey(it)) },
            onOpenStaff = openStaff,
            onOpenRelease = { navigator.navigate(ReleaseNavKey(it)) },
            onOpenListEditor = { navigator.navigate(ListEditorNavKey(it)) },
        ),
    )
    characterEntry(onNavigateBack = navigator::goBack, onOpenNovel = openNovel, onOpenStaff = openStaff)
    // releaseEntry, profileEntry, settingsEntry, themeEntry, loginEntry, listEditorEntry
}
```

`AppNavDisplay` builds `appEntryProvider(navigator)` once with `remember(navigator)` and hands it to `navigationState.toDecoratedEntries`.

## 4. Marker interfaces and navigator move to `common`

`FullScreenNavKey` / `OverlayNavKey` must be reachable from features (they implement them), so they move to `common/ui/navigation`. `BottomSheetSceneStrategy` moves with them (the list editor entry uses `BottomSheetSceneStrategy.bottomSheet()`), and so do `NavigationState` and `Navigator` to keep the navigation plumbing in one package. Pure package moves, done first and on their own (§7 step 1).

## 5. Browse scope entries

`TagNavKey`, `ProducerNavKey`, `StaffNavKey` stay as three keys, but their entries collapse into one function in `BrowseNavigation.kt` built on a private helper:

```kotlin
fun EntryProviderScope<NavKey>.browseScopedEntries(
    onNavigateBack: () -> Unit,
    onOpenNovel: (id: String) -> Unit,
) {
    scopedBrowse<TagNavKey>(onNavigateBack, onOpenNovel) { BrowseScope.Tag(it.id) }
    scopedBrowse<ProducerNavKey>(onNavigateBack, onOpenNovel) { BrowseScope.Producer(it.id) }
    scopedBrowse<StaffNavKey>(onNavigateBack, onOpenNovel) { BrowseScope.Staff(it.id) }
}

private inline fun <reified K : NavKey> EntryProviderScope<NavKey>.scopedBrowse(
    noinline onNavigateBack: () -> Unit,
    noinline onOpenNovel: (id: String) -> Unit,
    crossinline scope: (K) -> BrowseScope,
) {
    entry<K> { key ->
        BrowseRoute(scope = scope(key), onNavigateBack = onNavigateBack, onOpenNovel = onOpenNovel)
    }
}
```

## 6. Small fixes bundled in

| # | Change | Notes |
|---|---|---|
| 1 | Rename `NovelDetailNavKey` → `DetailNavKey` | Matches `DetailRoute` / `feature/detail`. |
| 2 | `LoginNavKey : FullScreenNavKey` | **Behaviour change:** the tab bar now hides on the login page. Verify the page handles bottom insets (it loses the bar's padding). |
| 3 | ~~`ListEditorNavKey(novelId)` only; drop `novelTitle`~~ **Deferred.** The editor loads only the user's list entry (`GetMyListEntryUseCase` → `/ulist`), which is null when the VN is not on the list, and no VN-title lookup exists outside Detail. Dropping the title needs a new request on every sheet open or a cross-feature dependency on Detail's use case, which is a design change rather than a move. Kept as `ListEditorNavKey(novelId, novelTitle)`; decide separately. |
| 5 | `Navigator.navigate` uses an explicit `topLevelRoutes` set | Add `val topLevelRoutes: Set<NavKey>` to `NavigationState`; replace `route in state.backStacks.keys`. |

(Decision 6 — keep marker interfaces instead of entry metadata — means no change there.)

## 7. Migration order

Each step must leave the app building, `detekt` clean and tests green. One commit per step. **Start from a clean tree:** commit or stash the in-progress `feature/producer` work first, then branch (e.g. `refactor/navigation`).

1. **Move plumbing to `common/ui/navigation`.** Move `Navigator`, `NavigationState`, `BottomSheetSceneStrategy`; extract `FullScreenNavKey` and `OverlayNavKey` from `AppNavigation.kt`. Add `topLevelRoutes` to `NavigationState` and use it in `Navigator` (fix 5). Only imports change elsewhere.
2. **Extract app pieces.** Create `TopLevelDestination.kt`; move the `entryProvider` into `AppEntryProvider.kt` (still containing all entries, unchanged). `AppNavigation.kt` keeps its keys for now.
3. **Migrate leaf features** (one commit each): `character`, `release`, `theme`, `settings`, `login` (+ fix 2), `listeditor` (+ fix 3, as its own commit). Per feature: create `presentation/navigation/`, move the key, add the `<name>Entry` extension, replace the inline entry in `AppEntryProvider.kt` with a call to it, delete the old key from `AppNavigation.kt`.
4. **Migrate `browse`.** Move `BrowseNavKey`, `TagNavKey`, `ProducerNavKey`, `StaffNavKey`; add `browseEntry` and `browseScopedEntries` (§5).
5. **Migrate `detail`** with the rename (fix 1) and `DetailNavActions`, then `profile` and `discover`.
6. **Clean up.** `AppNavigation.kt` is scaffold + `NavDisplay` only; no key declarations and no feature Route imports; `AppEntryProvider.kt` only calls entry functions.
7. **Tests** (§8).
8. **Skills and docs** (§9): update every project skill that mentions navigation, as a dedicated final commit.

## 8. Tests

Location: `app/src/test/java/com/zc/vndb/…`. Style as in existing tests (JUnit4, see `AppViewModelTest`).

**`common/ui/navigation/NavigatorTest`** — build `NavigationState` directly (it takes a `MutableState` and a map of `NavBackStack`, no Compose runtime needed). If `NavBackStack` cannot be created on the JVM, introduce a thin `BackStacks` interface rather than skipping the tests. Cases:

- Navigating to the current tab is a no-op.
- Switching tab changes `topLevelRoute`, keeps both stacks, and resets the target tab's top screen state.
- Navigating to a non-tab key pushes onto the current tab's stack only.
- `reselect` pops to the tab root and keeps the root.
- `goBack` pops within a stack; at a non-start tab's root it returns to the start tab; at the start tab's root it does not crash.
- An overlay key pushed over a tab / full-screen page is popped by `goBack` without changing the tab.

**`common/ui/navigation/NavKeySerializationTest`** — `rememberNavBackStack` serializes keys through the reflection-based `NavKeySerializer` on Android, so no manual `SerializersModule` registration is needed. Guard against regressions anyway:

- Round-trip a sample of every concrete `NavKey` (all keys in §2, including the `OverlayNavKey`) through `NavKeySerializer` and assert equality.
- Keep the sample list in one place, with a comment: new key → add it here.
- Manual check once after step 6: `./gradlew assembleDevelopmentRelease`, install, open Detail → Character, background the app, kill the process, reopen. R8 must not strip the keys (add a keep rule for `NavKey` implementations if the restore fails).

## 9. Skills and docs to update

The project skills currently describe the old layout (all keys and entries in `app/navigation/AppNavigation.kt`). They must be updated in the same PR, in a dedicated final commit, so future code generation follows the new structure. Known places, found with `grep -n -i "navigation\|navkey" .claude/skills/*/SKILL.md`:

- **`structure`**
  - §1 package tree: replace the `app/navigation/AppNavigation.kt` entry with `AppNavigation.kt`, `TopLevelDestination.kt`, `AppEntryProvider.kt`; add `common/ui/navigation/` (marker interfaces, `Navigator`, `NavigationState`, `BottomSheetSceneStrategy`); add `presentation/navigation/` (`<Name>NavKey.kt`, `<Name>Navigation.kt`) to the feature tree.
  - Drop the "@Serializable NavKeys" mention from the `AppNavigation.kt` description.
  - Add a file-placement rule: NavKeys and entry extension functions live in the owning feature's `presentation/navigation/`.
- **`architecture`**
  - Add a navigation section: a feature owns its keys and an `EntryProviderScope<NavKey>` extension function; everything that leaves the feature is a lambda (or a `*NavActions` class past ~4 callbacks); features never import each other's navigation code; `app/navigation/AppEntryProvider.kt` is the only place that wires features together.
  - Update the Route/Effect wording (line ~160) only if it implies the app owns the entries.
- **`conventions`**
  - Naming table (line ~32): the Navigation 3 key row moves from `app/navigation/AppNavigation.kt` to `feature/<name>/presentation/navigation/<Name>NavKey.kt`; add rows for `<name>Entry` extension functions (`<Name>Navigation.kt`) and `<Name>NavActions`.
  - Rules: a NavKey carries ids only, never display data; full-screen and overlay keys implement `FullScreenNavKey` / `OverlayNavKey`; a new key is added to `NavKeySerializationTest`.
  - ViewModel example comment (line ~453): `NovelDetailNavKey.id` becomes `DetailNavKey.id`.
  - §5.1 Route rules (lines ~524-529): the entry extension function, not the app `NavDisplay`, supplies the key's arguments and the navigation lambdas.
  - New-feature checklist (line ~688, step "`app/navigation/AppNavigation.kt`: add `[Name]NavKey` and its `entry<>`"): replace with "add `[Name]NavKey` and `[name]Entry` in `presentation/navigation/`, then call `[name]Entry(...)` from `app/navigation/AppEntryProvider.kt`".
- **`tech-stack`**
  - Allowed-layers matrix (lines ~94 and ~100): `NavKey`s and kotlinx.serialization for NavKeys are allowed in `presentation/navigation/` of every feature and `common/ui/navigation`, not only in `app`; `NavDisplay`, the back stack and `Navigator` stay `app` / `common/ui/navigation` only. Adjust the "app means …" note (line ~87) accordingly.
- **`best-practices`**
  - Testing section: add "Navigator and NavKey serialization are unit tested" and the rule that a new NavKey is added to the serialization test.
  - Review checklist (line ~681): add "no feature imports another feature's navigation package".
- **`library-management`**: no change expected (no new dependencies); re-run the grep to confirm.
- **`CLAUDE.md` / `README.md`**: only if they describe navigation (they currently do not beyond the stack list).
- Finally, re-run the grep over `.claude/skills/*/SKILL.md` and `CLAUDE.md` for `AppNavigation`, `NovelDetailNavKey` and `app/navigation` to make sure no stale reference remains.
- Mark this plan **Done** at the top when finished.

## 10. Definition of done

- `AppNavigation.kt` contains no key declarations and no feature Route imports; only `AppEntryProvider.kt` imports feature entries and keys.
- No feature imports another feature's navigation package.
- Every screen reachable as before; the only differences are the two behaviour changes in §6 (login hides the bar; list editor loads its own title).
- Project skills (`structure`, `architecture`, `conventions`, `tech-stack`, `best-practices`) describe the new navigation structure and contain no stale `AppNavigation` / `NovelDetailNavKey` references.
- `./gradlew detekt testDevelopmentDebugUnitTest assembleDevelopmentRelease` pass.
- Manual smoke test: all three tabs, Detail → Character / Release / Tag / Producer / Staff and back, list editor from Detail and from Profile, login and settings → theme, process-death restore.

## 11. Risks

- **List editor title** needs a data source (fix 3); the only part that is more than a move. Scope it before step 3's `listeditor` commit and keep it a separate commit.
- **Tab bar visibility on Login** (fix 2) can expose missing insets.
- **`NavBackStack` on the JVM** may not be constructible in unit tests; fallback in §8.
- **`AppEntryProvider.kt` grows with each feature.** Accepted: it is the single explicit place where features connect, and it is flat, one call per feature.
