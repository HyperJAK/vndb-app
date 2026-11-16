# Plan 08 — Profile

The Profile tab: a guest state with a Log In button, a login screen that takes a VNDB API token, and the signed-in user's VN lists by category.
API: [VNDB Kana — User Authentication](https://api.vndb.org/kana#user-authentication). Designs (reference only): Stitch "Profile – Guest", "VNDB – Log In", "Profile & Lists". Rules: `.claude/skills/*`.

## 1. Authentication
- The user creates a token at `https://vndb.org/u/tokens` and pastes it in the app. Format: `xxxx-xxxxx-xxxxx-xxxx-xxxxx-xxxxx-xxxx` (z-base-32, dashes optional); input is trimmed and lower-cased, and checked before any request.
- `GET /authinfo` with `Authorization: Token …` validates it (401 = invalid) and returns `id`, `username`, `permissions` (`listread`, `listwrite`).
- The app only reads lists. `listread` lets private labels and entries show; without it, only public ones do (the profile says so).
- The token is encrypted with an Android Keystore AES-GCM key and stored, with the user id, name and permissions, in a Preferences DataStore (`session`). It never leaves the device except in the `Authorization` header, which the debug network log already masks.
- Log out (top bar, with confirmation) deletes it. A 401 on a later request also signs the user out, with a message.

## 2. Screens
| Screen | Content |
|---|---|
| **Profile — guest** | Centered card: person icon, "Not logged in", why to log in, **Log in** button, "Your token stays on this device" note |
| **Log in** (pushed on the Profile tab) | Back · "Connect account" · intro card · 3 steps: ① open the VNDB tokens page (button), ② allow access to your list, ③ copy the token · token field (masked, show/hide, Paste) · **Connect** (spinner while checking; inline error) · "Continue as guest" |
| **Profile — signed in** | Top bar with Log out · user card (initial, username, `u…` pill, play-time votes `GET /user`) · label chips with counts (`GET /ulist_labels?fields=count`; empty labels hidden; Playing selected when it has entries) · entries of the selected label |

Entry card: cover, title, original title, my vote, started / finished dates, other labels, notes (2 lines), "Updated 3 days ago"; tap opens Title Detail. List editing ("Update list entry") is out of scope.

## 3. Lists
`POST /ulist` per label: `filters: ["label", "=", id]`, `sort: lastmod` desc, 25 per page, loaded when the label is first selected and cached; more load at the end of the list. Pull to refresh reloads labels, user and the selected label. (`count: true` is rejected by `/ulist`, so counts come from `/ulist_labels`.)

## 4. Code
`feature/profile/{data,domain,di,presentation}`: `SessionRepository` (observe / log in / log out) and `ProfileRepository` (user, labels, entries); `ProfileViewModel` (observes the session) and `LoginViewModel`. `LoginNavKey` is pushed on the Profile stack. DataStore is added to the version catalog.

## 5. Out of scope
Editing list entries, votes and notes; other users' profiles; settings.
