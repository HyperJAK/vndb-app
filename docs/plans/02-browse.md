# Plan 02 — Browse

Design: Stitch project "VNDB Mobile Client", screen **VNDB - Browse & Search** (390 × 1123).
API: [VNDB Kana](https://api.vndb.org/kana). Rules: `.claude/skills/*`.

## 1. Screen → data

| UI block | Content | Source |
|---|---|---|
| Top bar | `VNDB` wordmark + version pill | Same as Discover → shared `AppTopBarView` |
| Search field | "Search titles…", clear button | `search` filter on `POST /vn` (matches titles and aliases only; the design's "characters, developers" wording is dropped) |
| Age chips | **All Ages** (default) / **18+**, exclusive | All Ages = `["release","!=",["minage",">=",18]]` (~35.9k VNs); 18+ = `["release","=",["minage",">=",18]]` (~32.2k). Decided 2026-11-16. |
| Sort menu | Popularity ▾ | Relevance (only with a query, `searchrank`), Popularity (`votecount` desc), Rating (`rating` desc), Newest (`released` desc), Title (`title` asc) |
| Genre chips | Romance, Mystery, Sci-Fi, Horror, Psychological, Fantasy, Drama, Comedy, Action | Multi-select, combined with AND: `["tag","=","g96"]`… |
| "Found 142 titles" · INDEXED DATABASE | Total matches | `count: true` on page 1 only |
| Result cards | Cover + ★ score, developers, year, title, original title, 3 tags, length ("30-50h") | `title, alttitle, released, rating, length, length_minutes, image.*, developers.name, tags.*` |
| Infinite list | 20 per page | `page`, `more` |

Tag ids (from `POST /tag`): Romance g96, Mystery g19, Science Fiction g105, Horror g7, Psychological Problems g253, Fantasy g2, Drama g147, Comedy g104, Action g12.

Payload: with `tags.*` a 20-result page is ~197 KB of JSON but ~32 KB gzipped on the wire (OkHttp decompresses transparently), ~0.8 s.

### Rules (domain, unit-tested)
- Query is trimmed; Relevance without a query falls back to Popularity.
- Card tags: top 3 spoiler-free content tags by rating.
- Length: `length_minutes` bucketed into VNDB's ranges (<2h, 2–10h, 10–30h, 30–50h, >50h); falls back to the 1–5 `length` estimate; otherwise hidden.

## 2. Shared code moved to `common/` (second user)
- `common/domain/model`: `Cover`, `ReleaseDate`, `NovelTag`, `TagCategory`.
- `common/data/dto`: `QueryDTO` (+ `page`, `count`), `ImageDTO`, `DeveloperDTO`, `TagDTO`; `common/data/mapper`: `ImageDTOMapper`, `TagDTOMapper`, `ReleaseDateDTOMapper`. The `structure` skill gains these two folders.
- `common/ui/component/ratingbadge/mapper/RatingUIModelMapper.kt` (score labels).
- `common/ui/component/apptopbar/AppTopBarView.kt` (wordmark + version pill).

## 3. Feature `feature/browse`
- **domain**: `BrowseNovel`, `BrowsePage`, `BrowseFilter`, `AgeRating`, `BrowseSort`, `Genre`, `NovelLength`; `BrowseRepository`; `SearchNovelsUseCase`.
- **data**: `BrowseService`, `BrowseNovelDTO`, `BrowseNovelPageDTO`, `BrowseQueryDomainMapper`, `BrowseNovelDTOMapper`, `BrowseRepositoryImpl`.
- **presentation**: `BrowseState/Intent/Effect/ViewModel/Route/Screen`; components `searchfield`, `filterbar` (age chips + sort menu), `genrechips`, `browsenovelcard`. Typing is debounced (400 ms); filter changes reload at once; reaching the last 5 items loads the next page; a failed next page shows a retry footer.
- **Adaptive**: one column on compact, two on medium/expanded (`LazyVerticalGrid`).
- **Navigation**: `BrowseNavKey` → `BrowseRoute`; result taps open the novel placeholder.

## 4. Tests
Use case rules, query builder JSON, DTO mapper (length buckets, nulls), repository with a live-captured fixture, ViewModel (debounce, filter reload, paging, load-more failure, retry), UI mappers.

## 5. Out of scope
Title Detail (feature 03), saved filters, more genres / tag search, character and developer search.
