# Plan 03 — Title Detail

Design: Stitch project "VNDB Mobile Client", screen **VNDB - Title Detail (Steins;Gate)** (390 × 1835).
API: [VNDB Kana](https://api.vndb.org/kana). Rules: `.claude/skills/*`.

## 1. Screen → data

Three requests in parallel when the screen opens: `POST /vn` (one id), `POST /character` (`["vn","=",["id","=",id]]`), `POST /release` (same filter, up to 100, by date).
Only the `/vn` call is required; if characters or releases fail, that section is left out.

| UI block | Source | Notes |
|---|---|---|
| Top bar: back, wordmark, Share, Open in browser | `https://vndb.org/{id}` | Share = Android share sheet; Open = browser |
| Id pill (`v2002`) | id | The design's heart and second "open" button are dropped (favourites need sign-in) |
| Hero: cover + ★, title, original title, 2 top tags, age chip, Dev / Released / Length | `title, alttitle, image.*, rating, developers.name, released, length, length_minutes, length_votes, tags.*` + releases' `minage` | Age chip: "All Ages", "18+", or a range like "All Ages / 15+" from the official releases. Length: "30-50h • 44h avg" |
| Score card | `rating` (Bayesian), `average`, `votecount` | The design's RANK / POP and the 1–10 vote histogram are not in the API and are dropped |
| List status + Vote | — | Needs sign-in (Profile feature); dropped for now |
| Links row | `extlinks.label, extlinks.url` | vndb.org link first, then one link per site (deduped by label), up to 4 |
| Spoiler filter None / Minor / Major | tags' and characters' `spoiler` | Default None. Hidden items are counted ("3 hidden by spoiler filter") instead of blurred |
| Synopsis + Read more | `description` | BBCode → plain text with paragraphs; `[spoiler]` blocks only shown at Minor or above; collapsed to 4 lines |
| Tags & Content | `tags` | Groups: Themes (content tags, top 12), Technical (top 6). Content notes: "Sexual content" (rose) when the VN has sexual tags, else "No sexual content" (green). Sexual tags themselves are not listed |
| Characters ("N main") | `/character`: `name, original, image.*, vns.id, vns.role, vns.spoiler`; voice actors from `/vn` `va.character.id, va.staff.name` | Main + primary roles, main first, up to 12; role, name, original name, CV |
| Releases & Editions | `/release`: `title, released, platforms, languages.lang, official, patch, minage` | Earliest official non-patch release per platform, up to 6; icon + badge by platform family (PC / Console / Mobile / Web / Other) |
| Relations | `relations.id, title, relation, relation_official, released, rating` | Official first, then by date, up to 10; tap opens that VN's detail |

## 2. Shared code moved to `common/`
- `NovelLength` (domain) and the minutes / 1–5 estimate → length mapping (data), now used by Browse and Detail.
- Length labels → `common/ui/mapper/NovelLengthUIModelMapper.kt`; `common/ui/mapper/` is added to the `structure` skill for presentation mappers of `common/domain` types.

## 3. Feature `feature/detail`
- **domain**: `NovelDetail`, `ExternalLink`, `RelatedNovel`, `RelationType`, `NovelCharacter`, `CharacterRole`, `Edition`, `PlatformFamily`; `DetailRepository`; `GetNovelDetailUseCase`, `GetNovelCharactersUseCase`, `GetNovelEditionsUseCase`.
- **data**: `DetailService`, DTOs, query builders, DTO mappers, `DetailRepositoryImpl`.
- **presentation**: `DetailState/Intent/Effect/ViewModel/Route/Screen`; components `detailhero`, `scorecard`, `linksrow`, `spoilerfilter`, `synopsis`, `taggroups`, `charactercard`, `editionrow`, `relationcard`, `sectioncard`.
- **Navigation**: `NovelDetailNavKey(id)` → `DetailRoute`; relations push another detail.

## 4. Out of scope
Favourites, list status and votes (need sign-in), rank/popularity, vote histogram, full release list, character pages.
