# Plan 12 — Staff

No Stitch screen exists. A staff "page" is Browse scoped to a person, with a header card, the same pattern as the tag page (plan 05) and the producer page ([plan 11](11-producer.md)).
API: [VNDB Kana](https://api.vndb.org/kana). Rules: `.claude/skills/*`.
Depends on plan 11: it introduces `BrowseScope` and the scoped header. Do plan 11 first, then this one is mostly new data plus entry points.

## 1. Data

Two requests, in parallel:
- `POST /staff` by id: `name, original, lang, gender, aliases, extlinks`, plus `description` if the live API returns it for staff (verify; the docs list it for producers but not clearly for staff).
- `POST /vn` with `["staff","=",["id","=",id]]` plus the Browse filters, sort and paging, so the person's works are the normal Browse list.

Voice actors are staff too. VNs where the person voices a character match the `staff` filter only if VNDB counts the `va` credit; to verify against the live API, because if it does not, a second filter on `va` is needed (`["or", ["staff", …], ["character", "=", …]]` is not equivalent). The decision rule: if voice roles are missing, the header says "Staff credits" and voice roles are listed from `POST /vn` with `va.staff.id`, as the Character page already does for one character.

Identity: VNDB distinguishes a staff **entry** (`id`, `s123`) from its **aliases** (`aid`, one per name the person has worked under). A VN's `staff.id` is the entry id and `staff.name` is the alias used for that credit, so the page keys on `id` and shows the main name with the other names as aliases. The `ismain` filter exists to get one result per entry; this plan queries by `id`, so it is not needed.

To verify before coding:
- The `staff` filter shape on `/vn`, and that matching by entry `id` includes credits under every alias.
- Whether the result count equals VNDB's own credit count.
- `gender` and `lang` values for the header.

## 2. Domain, data and presentation changes

| Layer | Change |
|---|---|
| `browse/domain/model` | `BrowseScope.Staff(id)` (added next to `Tag` and `Producer` from plan 11). New `StaffInfo(id, name, original, aliases, language, gender, links)`. |
| `browse/domain` | `GetStaffUseCase` and `BrowseRepository.getStaff(id)`. |
| `browse/data` | `BrowseService.queryStaff`, `StaffDTO` + page DTO + mapper (a different DTO from the Discover and Detail staff DTOs), `staffQuery(id)`. `BrowseQueryDomainMapper.toFilters()` adds the staff predicate. |
| `browse/presentation` | `StaffHeaderView` above the controls; same failure behaviour as the tag and producer headers. |
| `app/navigation` | `StaffNavKey(id) : FullScreenNavKey` and its entry. |

Three staff models already exist: `discover` `StaffDTO` / `StaffCredit`, `detail` `StaffCreditDTO` / `StaffCredit`, and `character` `VoiceStaffDTO`. This plan adds a fourth, narrower one in `browse` (the page needs only the header). Do not try to unify them in this change; each feature keeps the data it needs and only gains an `id`. Unifying is worth a separate refactor if a fifth use appears.

## 3. Screen

`StaffNavKey(id)` → `BrowseRoute` scoped to the person:
- Top bar with Back and "Open on vndb.org" (`vndb.org/s…`).
- Header card: name, original name, language, gender (only when VNDB has it), aliases as chips, description if available, external links as chips (Twitter, Wikipedia and so on, as Title Detail's links row already does; reuse `linksrow`).
- Below, the normal Browse controls and infinite-scroll results, scoped to the person. Default sort **Newest**.
- No role breakdown in v1 (a person can be both writer and composer). Showing the role per VN would need a field the list endpoint does not return in this shape; revisit if users ask.

## 4. Entry points

Credits already have ids; voice actors do not.
- **Title Detail, staff section.** `StaffCreditDTO.id` exists. `toFacts()` in `DetailCastUIModelMapper` currently joins names into one string ("Scenario: A, B"), so it cannot be tapped per name. `FactUIModel` is shared, so add a dedicated `StaffRoleUIModel(label, people: List<(id, name, note)>)` for this section and render each name as a link in a flow row. The staff section is the only user.
- **Title Detail, voice actor on character cards.** `VoiceActorStaffDTO` gains `id` (`va.staff.id` in the query); the credit becomes tappable.
- **Character screen, appearance rows.** `VoiceStaffDTO` gains `id`; the CV name opens the staff page.

Each of those screens gets an `onOpenStaff: (String) -> Unit` and the nav entries pass `navigator.navigate(StaffNavKey(id))`.

## 5. Tests

- `toFilters()` with the staff scope: correct predicate, combined with age, search and genres.
- Staff DTO → `StaffInfo`: null gender, empty aliases, missing links.
- `StaffRoleUIModel` mapper: names grouped by role, notes kept, VNDB's role order preserved (the existing mapper's behaviour).
- `BrowseViewModel` with a staff scope: header loaded, header failure leaves the list working.

## 6. Build order

1. Confirm plan 11's `BrowseScope` is merged; add `Staff(id)`, filter, query and DTOs.
2. Header view, string resources, `StaffNavKey` and the entry.
3. Per-name tappable credits in Title Detail's staff section.
4. Voice actor ids and links on character cards and the Character screen.
5. Detekt and unit tests.

## 7. Out of scope

Role breakdown per VN, staff search, merging or aliasing staff entries, a "voiced characters" gallery (the Character page already lists CVs per VN).
