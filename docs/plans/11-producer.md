# Plan 11 — Producer

No Stitch screen exists. Like the tag page (plan 05), a producer "page" is Browse scoped to the producer, with a header card for the producer itself.
API: [VNDB Kana](https://api.vndb.org/kana). Rules: `.claude/skills/*` (load all six before writing code).
Builds the scope mechanism that [plan 12 (Staff)](12-staff.md) reuses, so do this plan first.

## 1. Why this lives in `browse`, not a new `producer` feature

The list under the header is the Browse list. The architecture rules let `presentation` use another feature's `domain` only, never its `presentation`, so a separate `producer` feature could not embed Browse's screen. The tag page already solved this by putting the header, use case and route parameter inside `browse`; the producer page does the same. Extracting the Browse list into `common/ui` would also work but is a much larger change for no user-visible gain.

## 2. Data

Two requests, in parallel:
- `POST /producer` by id: `name, original, aliases, lang, type, description`. `type` is `co` (company), `in` (individual) or `ng` (amateur group). Descriptions use VNDB BBCode.
- `POST /vn` with `["developer","=",["id","=",id]]` plus the existing Browse filters, sort and paging.

Publisher-only credits: `developer` only matches developers. A "Published" tab (`POST /release` with `producer`, grouped by VN) is out of scope for v1; the list is limited to what the producer developed.

To verify against the live API before coding, as plans 01 and 05 did:
- The filter shape (`["developer","=",["id","=","p30"]]`) and whether `count` equals VNDB's own figure.
- That `description` is returned for producers and how often it is null.
- `lang` values and the `type` codes.

## 3. Domain, data and presentation changes

| Layer | Change |
|---|---|
| `browse/domain/model` | `BrowseFilter` gains a producer scope next to `tagId` (see the `BrowseScope` note below). New `ProducerInfo(id, name, original, aliases, type, language, description)` and `ProducerType`. |
| `browse/domain` | `GetProducerUseCase` and `BrowseRepository.getProducer(id)`, mirroring `GetTagUseCase`. |
| `browse/data` | `BrowseService.queryProducer`, `ProducerDTO` + page DTO + mapper, `producerQuery(id)` beside `tagQuery`. `BrowseQueryDomainMapper.toFilters()` adds `["developer","=",["id","=",producerId]]`. |
| `browse/presentation` | `BrowseRoute(producerId = …)` passed to the ViewModel through `parametersOf`; a `ProducerHeaderView` shown above the controls, as the tag header is. `BrowseState` holds the header state; if the producer lookup fails the header is omitted and the list still works. |
| `app/navigation` | `ProducerNavKey(id) : FullScreenNavKey` and an entry that calls `BrowseRoute(producerId = key.id, …)`. |

`BrowseViewModel` currently takes `tagId`. Replace it with one `BrowseScope` (`None`, `Tag(id)`, `Producer(id)`; plan 12 adds `Staff(id)`) so the ViewModel, route and filter do not grow a parameter per entity. This is a small refactor of the tag page and must keep its tests green.

## 4. Screen

`ProducerNavKey(id)` → `BrowseRoute` scoped to the producer:
- Top bar with Back and "Open on vndb.org" (`vndb.org/p…`).
- Header card: name, original name, type chip (Company / Individual / Amateur group), language, aliases as chips, description (BBCode → paragraphs, Read more). Reuse the tag header's expandable text.
- Below, the normal Browse controls and infinite-scroll results (search, age, genres, sort), all scoped to the producer.
- Default sort for a producer page: **Newest**, not Popularity, since users arrive looking for "what else did they make". Keep the sort chips.

`POST /producer` has no VN count (unlike `/tag`), so the header shows none; the "Found N titles" line under the filters is the count, and it reflects Browse's age filter.

## 5. Entry points

Producers currently have no id anywhere in the app.
- **Title Detail.** `DeveloperDTO` (in `common/data/dto`) gains `id`; request `developers.id` in `DetailQueryDomainMapper`, `BROWSE_FIELDS` and the Discover queries. The developer name in the detail hero becomes a tappable chip (`onOpenProducer`).
- **Release screen.** `ReleaseProducerDTO` gains `id` (`producers.id` in the release query); `ProducersSection` rows become tappable.
- **Browse and Discover cards** show the developer name only as text; leave them as they are (the card already opens the VN).

Domain models that carry developers (`NovelDetails`, `BrowseNovel`, release producers) change from `List<String>` to a small `(id, name)` type, so this touches several mappers and tests.

## 6. Tests

- `toFilters()` with a producer id: predicate present, combined correctly with age, search and genres, and still not `["and"]` with a single predicate.
- Producer DTO → `ProducerInfo` mapper: type codes, null description, empty aliases.
- `BrowseViewModel` with a producer scope: header loaded, header failure leaves the list working, scope survives a filter change.
- Existing tag-page tests still pass after the `BrowseScope` refactor.

## 7. Build order

1. `BrowseScope` refactor with the tag page as the only user; tests green.
2. `producerId` filter, query and DTOs, with the use case.
3. Header view, string resources, `ProducerNavKey` and the nav entry.
4. Ids on developers and producers, then tappable chips in Title Detail and Release.
5. Detekt and unit tests.

## 8. Out of scope

Publisher-only listings, producer relations (parent / child studios are not exposed by the API), producer search, a producers tab in Browse.
