// ─────────────────────────────────────────────────────────
// MAVUNO CHURCH — DESIGN TOKENS (LIGHT MODE DEEP WHITE)
// Derived from: Mavuno brand identity, Deep White backdrop,
// dark circular button widgets, vibrant orange/gold accents,
// and modern 3D depth elevation.
// ─────────────────────────────────────────────────────────

export const colors = {
  // Deep White core light mode background & surfaces
  background: '#FAFAFC',    // crisp deep white app canvas
  surface: '#FFFFFF',       // elevated 3D cards
  surfaceAlt: '#F1F5F9',    // secondary subtle surface / inputs
  surfaceDark: '#0F172A',   // dark circular icon buttons & badges (from photo)
  
  // Brand accents
  orange: '#F5821F',        // primary brand orange
  orangeDeep: '#D9660B',    // gradient shadow / active orange
  orangeSoft: '#FFEDD5',    // warm soft orange tint
  gold: '#D97706',          // mandala accent gold
  goldSoft: '#FEF3C7',      // gold tint badge

  // Typography & Lines
  textPrimary: '#0F172A',   // deep charcoal text
  textSecondary: '#475569', // slate secondary text
  textMuted: '#94A3B8',     // muted caption text
  textOnDark: '#FFFFFF',    // white text on dark surfaces
  textOnOrange: '#FFFFFF',  // white text on orange buttons

  // Borders & Glass
  line: 'rgba(15, 23, 42, 0.08)',
  lineSubtle: 'rgba(15, 23, 42, 0.04)',
  glassBorder: 'rgba(255, 255, 255, 0.8)',
  shadow3D: '0 20px 40px -15px rgba(15, 23, 42, 0.08), 0 0 0 1px rgba(15, 23, 42, 0.04)',
};

export const radii = {
  sm: 10,
  md: 18,
  lg: 28,
  xl: 36,
  pill: 999,
};

export const spacing = (n) => n * 4;

export const type = {
  display: { fontWeight: '800', letterSpacing: -0.5 },
  heading: { fontWeight: '700', letterSpacing: -0.2 },
  label: { fontWeight: '600', letterSpacing: 1.2, textTransform: 'uppercase' },
  body: { fontWeight: '400', letterSpacing: 0.1 },
};

export default { colors, radii, spacing, type };
