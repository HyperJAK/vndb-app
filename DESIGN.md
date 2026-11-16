---
name: Nocturne VN
colors:
  surface: '#0d131f'
  surface-dim: '#0d131f'
  surface-bright: '#333947'
  surface-container-lowest: '#080e1a'
  surface-container-low: '#161c28'
  surface-container: '#1a202c'
  surface-container-high: '#242a37'
  surface-container-highest: '#2f3542'
  on-surface: '#dde2f4'
  on-surface-variant: '#bdc8d1'
  inverse-surface: '#dde2f4'
  inverse-on-surface: '#2b303e'
  outline: '#87929a'
  outline-variant: '#3e484f'
  surface-tint: '#7bd0ff'
  primary: '#8ed5ff'
  on-primary: '#00354a'
  primary-container: '#38bdf8'
  on-primary-container: '#004965'
  inverse-primary: '#00668a'
  secondary: '#ffb95f'
  on-secondary: '#472a00'
  secondary-container: '#ee9800'
  on-secondary-container: '#5b3800'
  tertiary: '#aecfff'
  on-tertiary: '#00315c'
  tertiary-container: '#8ab4ec'
  on-tertiary-container: '#124577'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#c4e7ff'
  primary-fixed-dim: '#7bd0ff'
  on-primary-fixed: '#001e2c'
  on-primary-fixed-variant: '#004c69'
  secondary-fixed: '#ffddb8'
  secondary-fixed-dim: '#ffb95f'
  on-secondary-fixed: '#2a1700'
  on-secondary-fixed-variant: '#653e00'
  tertiary-fixed: '#d3e4ff'
  tertiary-fixed-dim: '#a2c9ff'
  on-tertiary-fixed: '#001c38'
  on-tertiary-fixed-variant: '#17487a'
  background: '#0d131f'
  on-background: '#dde2f4'
  surface-variant: '#2f3542'
  surface-card: '#1A233A'
  surface-elevated: '#232F4D'
  spoiler-none: '#10B981'
  spoiler-minor: '#F59E0B'
  spoiler-major: '#EF4444'
  tag-nsfw: '#F43F5E'
  border-subtle: '#253352'
typography:
  headline-lg:
    fontFamily: Space Grotesk
    fontSize: 30px
    fontWeight: '700'
    lineHeight: 36px
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Space Grotesk
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 30px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Space Grotesk
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 26px
  headline-sm:
    fontFamily: Space Grotesk
    fontSize: 17px
    fontWeight: '600'
    lineHeight: 22px
  body-lg:
    fontFamily: Hanken Grotesk
    fontSize: 15px
    fontWeight: '400'
    lineHeight: 22px
  body-md:
    fontFamily: Hanken Grotesk
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
  body-sm:
    fontFamily: Hanken Grotesk
    fontSize: 11px
    fontWeight: '400'
    lineHeight: 15px
  label-lg:
    fontFamily: Hanken Grotesk
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.01em
  label-md:
    fontFamily: JetBrains Mono
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
  label-sm:
    fontFamily: JetBrains Mono
    fontSize: 9px
    fontWeight: '600'
    lineHeight: 12px
    letterSpacing: 0.04em
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  gutter: 0.75rem
  gutter-desktop: 1.25rem
  margin: 1rem
  margin-desktop: 2rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 0.75rem
  space-lg: 1rem
  space-xl: 1.5rem
---

## Brand & Style

This design system modernizes the quintessential visual novel catalog aesthetic into a focused, OLED-optimized mobile interface. Built for passionate readers, archival collectors, and casual visual novel enthusiasts, the interface pairs deep cinematic immersion with rigorous data clarity.

The visual direction combines **Modern High-Density Minimalism** with **Tonal Glassmorphism**:
- Deep obsidian and midnight slate foundations eliminate backlight distraction during long nighttime reading or browsing sessions.
- High-contrast chromatic indicators prioritize legibility: electric azure guides navigation, while anime-accurate warm amber highlights community consensus and score metrics.
- Information architecture respects VNDB’s legacy of meticulous categorization—content warnings, staff rosters, translation releases, and spoiler tiers are structured into scannable visual units rather than dense blocks of plain text.

## Colors

The palette transforms VNDB’s traditional web navy into a calibrated dark palette tailored for mobile screens:

- **Primary (`#38BDF8`)**: Electric Sky Blue. Applied to active navigation tabs, interactive primary links, tag filters, and key action triggers.
- **Secondary (`#F59E0B`)**: Radiant Amber. Reserved for rating scores, user favorite stars, bookmark states, and moderate spoiler warnings.
- **Tertiary (`#18497B`)**: Classic VNDB Deep Blue. Provides brand continuity across secondary container fills, selected filter pills, and grouped metadata headers.
- **Neutral (`#0D131F`)**: Midnight Obsidian. The root canvas tone engineered for power efficiency and high-contrast foreground separation.

### Functional & Semantic Colors
- `surface-card` (`#1A233A`): Base container color for novel catalog items, release accordions, and character modules.
- `surface-elevated` (`#232F4D`): Modals, floating action sheets, bottom sheets, and elevated badges.
- `spoiler-none` (`#10B981`): Safe / Non-spoiler tag indicators.
- `spoiler-minor` (`#F59E0B`): Moderate / Implied spoiler tag warnings.
- `spoiler-major` (`#EF4444`): Severe plot reveals; requires explicit user touch to unblur.
- `tag-nsfw` (`#F43F5E`): Mature content and age rating badges.

## Typography

Typography balances character and technical cataloging efficiency:

- **Display & Section Titles (Space Grotesk)**: Lends a subtle futuristic, geometric edge fitting visual novel media while ensuring bold readability in compressed mobile layouts.
- **Narrative & Metadata (Hanken Grotesk)**: Neutral, hyper-legible sans-serif that ensures high comfort across lengthy synopsis passages, staff lists, and release descriptions.
- **Data & Tags (JetBrains Mono)**: Injects precision into technical VNDB parameters—release dates, VNDB ID tags (`v17`, `r498`), engine classifications, duration estimates, and score values.

## Layout & Spacing

The layout leverages a compact 4-column layout on mobile, transitioning to 8 columns on tablet and 12 columns on large displays:

- **Mobile Canvas**: Outer boundary margins sit strictly at `1rem` to maximize screen real estate for artwork thumbnails and multi-line metadata.
- **Card Grids**: Visual novel cover grids use a 2-column or 3-column split with `0.75rem` (`gutter`) gaps.
- **Data Rhythms**: Inner-card paddings utilize `0.5rem` to `0.75rem` (`space-sm` to `space-md`), preventing loose, empty regions and maximizing vertical scan efficiency.
- **Reflow Behavior**: Screen sizes beyond 768px expand detail pages from single-column vertical stacks into side-by-side hero cover galleries and structured metadata tab sheets.

## Elevation & Depth

This system avoids heavy drop shadows, relying on tonal surface stepping and luminous boundary strokes:

- **Level 0 (Canvas)**: Background `#0D131F`. Non-interactive backdrop.
- **Level 1 (Card Default)**: Surface `#1A233A` bordered by a 1px crisp outline of `#253352`. No diffuse shadow.
- **Level 2 (Active/Pressed Card)**: Surface `#232F4D` paired with a focused border illumination of `rgba(56, 189, 248, 0.35)` and a micro ambient glow: `0 4px 16px rgba(2, 132, 199, 0.15)`.
- **Level 3 (Modals & Bottom Navigation)**: Glassmorphic surface rendered with `#141C2E` at 85% opacity, `backdrop-filter: blur(16px)`, and an elevated hairline top edge `rgba(255, 255, 255, 0.08)`.

## Shapes

The design system maintains a modern, disciplined aesthetic using a **Soft (`1`)** roundedness profile:

- Standard controls, cards, and input fields utilize `0.25rem` (4px) to `0.375rem` (6px) corner radiuses, reflecting database structure and efficiency.
- Visual novel portrait cover cards use `0.375rem` radiuses to frame cover illustrations cleanly without cropping edge artwork.
- Badge indicators, spoiler tags, and score chips use a rounded-full pill treatment (`9999px`) to create clear differentiation between rectangular media cards and contextual inline data.

## Components

### Cover Cards
- **Portrait Layout**: Standard 3:4 aspect ratio with subtle 1px border (`#253352`).
- **Score Overlay**: Anchored in the top-right corner using a semi-translucent dark pill (`#0D131F`, 85% opacity) containing an amber star icon (`#F59E0B`) and score text in `label-md` JetBrains Mono.
- **Length & Platform Indicators**: Positioned along the card bottom edge over a slight dark gradient scrim to preserve artwork visibility.

### Tags & Spoiler Pills
- **Spoiler Level 0 (None)**: Background `rgba(16, 185, 129, 0.12)`, text `#10B981`, border `rgba(16, 185, 129, 0.25)`.
- **Spoiler Level 1 (Minor)**: Background `rgba(245, 158, 11, 0.12)`, text `#F59E0B`, border `rgba(245, 158, 11, 0.25)`.
- **Spoiler Level 2 (Major)**: Background `rgba(239, 68, 68, 0.15)`, text `#EF4444`, border `rgba(239, 68, 68, 0.35)`. By default, text is masked with a blur filter (`filter: blur(4px)`); a single tap reveals the label.

### Buttons & Quick Actions
- **Primary CTA (e.g., "Add to List", "Track")**: Background `#38BDF8`, text `#0D131F` bold. Pressed state darkens to `#0284C7`.
- **Secondary CTA (e.g., "Wishlist", "Vote")**: Background `#1A233A`, text `#38BDF8`, 1px border `#253352`.
- **Filter Chips**: Compact height (28px), inactive in `#1A233A` with neutral text, active in `#18497B` with `#38BDF8` text and border.

### Input Fields & Search Bars
- Search bars employ an inset appearance with background `#141C2E`, border `1px solid #253352`, placeholder text in `#64748B`, and leading search icon in `#38BDF8`.
- Focus state highlights the border with `#38BDF8` and applies an outer ring of `0 0 0 2px rgba(56, 189, 248, 0.2)`.

### Tracking Status Badges
- Dedicated compact pills for visual novel reading states:
  - *Playing*: Cyan border and dot indicator (`#38BDF8`).
  - *Finished*: Emerald green tint (`#10B981`).
  - *Stalled / Dropped*: Muted slate tint (`#64748B`).
  - *Wishlist*: Warm amber tint (`#F59E0B`).