# Mavuno Church — App

A React Native (Expo) app, built by cloning the UX/layout/iconography of four
reference widget screenshots and recoloring everything into the Mavuno
Church palette (orange · white · black), with the mandala motifs from the
church logo and pattern artwork woven through as the visual signature.

## What maps to what

| Screen | Cloned from | Notes |
|---|---|---|
| `SplashScreen` | Photo 4 (orange quote widget) | Same orange gradient bg, big bold statement, ring progress in place of the "91%" arc, curved white footer with a play button, replaced with the Mavuno mark + wordmark and an animated draw-in logo. |
| `HomeScreen` | Photo 3 (dark panel + red sidebar) | Same 90/10 split: dark content panel with stacked "accent-bar" stat rows (time → verse of the day → next service, in place of time → forecast → battery), plus the colored vertical icon rail — recolored orange, icons swapped for church actions (live, sermons, events, give, prayer, community, notifications, chat, contact). |
| `TodayScreen` | Photo 2 (minimal dark widget) | Same dashed radial tick ring + countdown badge, big percentage ring (giving goal instead of battery), "MUSIC" bars → "LIVE" bars, and the row of outline circle icons (call/chat/give). |
| `SermonsScreen`, `EventsScreen`, `GiveScreen` | Extensions in the same visual language | Minimal list cards with the same accent-bar treatment, so the app doesn't stop at the three source screens. |

**Mandala treatment:** `MandalaGlyphs.js` recreates the 6 small African
pattern icons from under the Mavuno wordmark (concentric-square spiral,
sunburst, bullseye/target, waves, basket-weave, spike star) as reusable SVG
components, used as section dividers. `MandalaBackdrop.js` recreates the
concentric-ring + radial-tick motif (echoing both the church logo's double
ring and the widgets' tick-mark clock face) as a large, low-opacity ambient
background layer behind headers and the splash screen.

**Motion:** every screen uses staggered entrance animation (Moti/Reanimated).
`Pressy.js` is the shared touch target — it scales down and pulses a
translucent ripple on press (the "touch ripple" you asked for), and on web
builds it also lifts slightly on mouse hover, since that's the closest
equivalent to a cursor effect on a platform with no cursor.

## Running it

This is an Expo-managed React Native project — not a native Android Studio
project by itself. To get it running:

```bash
npm install
npx expo start
```

Then either scan the QR code with Expo Go on your phone, or press `a` to
launch an Android emulator (needs Android Studio's emulator + SDK installed
and `ANDROID_HOME` set).

**If you specifically need to open this in Android Studio as a native
project** (e.g. to add a native module later), run:

```bash
npx expo prebuild
```

This generates an `android/` folder you can open directly in Android
Studio and build/run like any native project.

## Structure

```
App.js
src/
  theme/theme.js          — colors, spacing, type tokens
  components/
    MandalaGlyphs.js       — the 6 small African pattern icons
    MandalaBackdrop.js      — ambient ring/tick background
    MavunoMark.js            — animated logo mark
    Pressy.js                — ripple/hover touch wrapper
    RingProgress.js          — animated % ring
    CircleIcon.js             — outline icon button (photo 2 style)
    AccentStat.js              — vertical-bar stat row (photo 3 style)
    IconRail.js                 — colored sidebar icon rail (photo 3 style)
    ListCard.js                  — list item card (Sermons/Events/Give)
    ScreenHeader.js
  screens/
    SplashScreen.js, HomeScreen.js, TodayScreen.js,
    SermonsScreen.js, EventsScreen.js, GiveScreen.js
  navigation/index.js       — stack (Splash → Main) + bottom tabs
```

## Things you'll probably want to change

- Swap the placeholder sermon/event copy for real content, ideally from an
  API or CMS rather than hardcoded arrays.
- The Mavuno logo mark is redrawn in SVG (not your original logo file) —
  drop your real logo asset into `src/assets` and use it in `MavunoMark.js`
  or `SplashScreen.js` if you'd rather use the exact file.
- Ring/percentage values (giving goal, countdown) are placeholder data —
  wire these to real numbers.
