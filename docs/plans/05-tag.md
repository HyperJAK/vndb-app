# Plan 05 — Tag

No Stitch screen exists. Per the evaluation, a tag "page" is Browse filtered to that tag, with a header for the tag itself.
API: [VNDB Kana](https://api.vndb.org/kana). Rules: `.claude/skills/*`.

## 1. Data
- `POST /tag` by id: `name, aliases, description, category, vn_count`. Descriptions use VNDB BBCode (`[url=…]…[/url]`).
- The list is the existing Browse query plus `["tag","=",id]`. That filter includes child tags and VNDB's default score and spoiler limits, and Browse adds its age filter, so "Found N titles" is usually lower than the tag's `vn_count` (Chuunibyou Protagonist: 33 on VNDB, 12 all-ages matches). The header labels `vn_count` as VNDB's figure.
- VN tag entries already carry the tag id (`g1628`); `TagDTO` / `NovelTag` gain an `id`.
- The API has no tag hierarchy (no parent / child tags), so there are no breadcrumbs.

## 2. Screen
`TagNavKey(id)` → `BrowseRoute(tagId = id)`:
- Top bar with Back (the Browse tab itself has none).
- Header card: tag name, category chip (Content / Sexual content / Technical), "N visual novels on VNDB", aliases, description (BBCode → paragraphs, Read more).
- Below it, the normal Browse controls and results (search, age, genres, sort, infinite scroll), all scoped to the tag.
- If the tag lookup fails, the header is left out and the list still works.

## 3. Entry points
Theme and technical chips on Title Detail open the tag. (Character traits are a different VNDB entity and are out of scope.)

## 4. Out of scope
Tag hierarchy, trait pages, tag search.
