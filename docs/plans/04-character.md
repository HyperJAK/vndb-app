# Plan 04 — Character

No Stitch screen exists; the page is built from the existing Nocturne VN components and follows the Title Detail layout.
API: [VNDB Kana](https://api.vndb.org/kana). Rules: `.claude/skills/*`.

## 1. Data
Two requests in parallel:
- `POST /character` by id: `name, original, aliases, description, image.*, age, birthday, blood_type, height, weight, bust, waist, hips, cup, sex, gender, traits.name, traits.group_name, traits.spoiler, traits.lie, vns.id, vns.title, vns.released, vns.rating, vns.image.*, vns.role, vns.spoiler`.
- `POST /vn` with `["character","=",["id","=",id]]`: `va.character.id, va.staff.name` → voice actor per VN. Optional: if it fails the page shows without CVs.

`sex` and `gender` are `[apparent, spoiler]` pairs; `vns` can list the same VN once per release, so appearances are de-duplicated by VN.

## 2. Page (top to bottom)
| Section | Content |
|---|---|
| Top bar | Back, wordmark, Share, Open on vndb.org (`vndb.org/c…`) |
| Id pill | `c6487` |
| Hero | Portrait, name, original name, sex (+ gender when different), then Age / Birthday / Blood type / Height / Weight / Measurements lines (only those VNDB has) |
| Aliases | Chips |
| Spoiler filter | None / Minor / Major (shared component) |
| Description | BBCode → paragraphs, `[spoiler]` from Minor up, Read more |
| Traits | Grouped by VNDB group (Hair, Eyes, Body, Clothes, Personality, Role, Engages in, Subject of), filtered by spoiler level; "lie" traits only at Major, marked as such; count of what the filter hides |
| Appears in | One row per VN: thumbnail, title, year, role, CV; main/primary first, then by date; filtered by the appearance's spoiler level; tap opens Title Detail |

The spoiler sex/gender (when it differs from the apparent one) only shows at Major.

## 3. Shared code moved to `common/`
`CharacterRole` (domain), `SectionCardView`, `SpoilerFilterView` + `SpoilerLevel`, and the synopsis card generalised to `ExpandableTextView`.

## 4. Navigation
`CharacterNavKey(id)`; Title Detail character cards become tappable; appearance rows push `NovelDetailNavKey`.

## 5. Out of scope
Trait pages, voice actor (staff) pages, per-release character info.
