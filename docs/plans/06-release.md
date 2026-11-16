# Plan 06 — Release

No Stitch screen exists; the page reuses the Title Detail layout and components.
API: [VNDB Kana](https://api.vndb.org/kana). Rules: `.claude/skills/*`.

## 1. Data
One request: `POST /release` by id with `title, alttitle, languages.{lang,title,main,mtl}, platforms, media.{medium,qty}, vns.{id,title,rtype,released,image.*}, producers.{name,developer,publisher}, images.{type,url,thumbnail,sexual,violence}, released, minage, patch, freeware, uncensored, official, has_ero, resolution, engine, voiced, notes, gtin, catalog, extlinks.{label,url}`.

Shapes seen live: `media` qty is 0 for downloads; `resolution` is `[w, h]` or null; `voiced` is 1–4 (not voiced, only ero scenes, partially, fully); `images` is often empty (57 of Steins;Gate's 100 releases have one); non-main languages may have a null `title`.

## 2. Page (top to bottom)
| Section | Content |
|---|---|
| Top bar | Back, wordmark, Share, Open on vndb.org (`vndb.org/r…`) |
| Id pill | `r7668` |
| Hero | Package front (else first image), title, alt title, chips (Official / Unofficial, Patch, Freeware, Uncensored, 18+ content, age), then Released / Platforms / Media / Engine / Resolution / Voiced / Catalog / Barcode lines (only those present) |
| Links | Shared links row: vndb.org first, then the release's links (one per site) |
| Visual novels | One row per VN: thumbnail, title, year, Complete / Partial / Trial; tap opens Title Detail |
| Languages | Each language with its title when it differs, and a "Machine translation" chip |
| Producers | Name with Developer / Publisher chips |
| Images | Horizontal gallery with type labels (Package front, back, …, Digital); sensitive images blurred |
| Notes | BBCode → paragraphs, Read more |

## 3. Shared code moved to `common/`
`LinksRowView` + `LinkUIModel`.

## 4. Navigation
`ReleaseNavKey(id)`; edition rows on Title Detail become tappable.

## 5. Out of scope
Producer pages, full-screen image viewer.
