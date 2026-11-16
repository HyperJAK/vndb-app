# Plan 07 — Title Detail revamp

Bring the VN page closer to vndb.org (e.g. `vndb.org/v13631`) while keeping the Nocturne VN design system.
API: [VNDB Kana](https://api.vndb.org/kana). Rules: `.claude/skills/*`. Decided 2026-11-16: tabs, lazy loading, age chip pops in.

## 1. Structure
Hero + score card, then a sticky, horizontally scrolling tab row (chip style):

| Tab | Content |
|---|---|
| **Overview** | Today's page (links, spoiler filter, synopsis, tags, relations) + "Also known as" (titles, aliases), play-time votes, and previews that open the other tabs: screenshots (first 4), main/primary characters, editions (one per platform) |
| **Media** | Screenshots grouped by release with content toggles — Safe / Suggestive / Explicit and Tame / Violent / Brutal, with counts; Safe + Tame on by default. Full-screen swipeable viewer with pinch-zoom. Covers carousel (release package fronts and digital art) below |
| **Cast & Staff** | Spoiler filter, all characters grouped by role (Main, Primary, Side, Appears) with CVs, staff grouped by edition (original / translations) |
| **Releases** | Every release grouped by language, paged (50), each opening the Release page |
| **Quotes** | Quote cards (text, character, score), paged (25), sorted by score; a "Reviews on vndb.org" link card (`/v…/reviews`) — the API has no reviews endpoint |

A tab's count (e.g. "Media 10") appears once it has loaded.

## 2. Lazy loading
Only `POST /vn` (Overview fields, now also `titles`, `aliases`, `length_votes`) runs when the page opens. Every other request runs the first time its section is needed — its tab is opened, or its Overview preview scrolls into view — and is cached in the ViewModel:

| Section | Request |
|---|---|
| Editions (Overview, hero age chip) | `POST /release` (100, oldest first) |
| Cast | `POST /character` + `POST /vn` (`staff`, `va`, `editions`) in parallel; credits are optional |
| Screenshots | `POST /vn` (`screenshots.*`) |
| Covers | `POST /release` (`images.*`) |
| Releases | `POST /release`, page by page |
| Quotes | `POST /quote` (`sort: score`, desc), page by page |

Each section is Idle → Loading → Loaded / Failed (own Retry). The spoiler filter and the screenshot toggles only re-filter loaded data.

## 3. Out of scope
Vote distribution, rank, recent votes and reviews (not in the API), staff and producer pages.
