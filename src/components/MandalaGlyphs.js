import React from 'react';
import Svg, { Rect, Circle, Path, Line, G } from 'react-native-svg';
import { View } from 'react-native';
import { colors } from '../theme/theme';

// Each glyph mirrors one square in the Mavuno logo's bottom row:
// 1 concentric square  2 sunburst  3 target/bullseye
// 4 wave lines  5 basket weave  6 spike star

const SIZE = 28;

export const GlyphSquareSpiral = ({ color = colors.gold, size = SIZE }) => (
  <Svg width={size} height={size} viewBox="0 0 28 28">
    <Rect x="2" y="2" width="24" height="24" stroke={color} strokeWidth="1.4" fill="none" />
    <Rect x="7" y="7" width="14" height="14" stroke={color} strokeWidth="1.4" fill="none" />
    <Rect x="12" y="12" width="4" height="4" fill={color} />
  </Svg>
);

export const GlyphSunburst = ({ color = colors.gold, size = SIZE }) => (
  <Svg width={size} height={size} viewBox="0 0 28 28">
    <G origin="14,14">
      {Array.from({ length: 10 }).map((_, i) => {
        const angle = (i * 36 * Math.PI) / 180;
        const x2 = 14 + Math.cos(angle) * 12;
        const y2 = 14 + Math.sin(angle) * 12;
        return <Line key={i} x1="14" y1="14" x2={x2} y2={y2} stroke={color} strokeWidth="1.6" />;
      })}
    </G>
  </Svg>
);

export const GlyphTarget = ({ color = colors.gold, size = SIZE }) => (
  <Svg width={size} height={size} viewBox="0 0 28 28">
    <Circle cx="14" cy="14" r="11" stroke={color} strokeWidth="1.4" fill="none" />
    <Circle cx="14" cy="14" r="6.5" stroke={color} strokeWidth="1.4" fill="none" />
    <Circle cx="14" cy="14" r="2" fill={color} />
  </Svg>
);

export const GlyphWaves = ({ color = colors.gold, size = SIZE }) => (
  <Svg width={size} height={size} viewBox="0 0 28 28">
    {[6, 12, 18, 24].map((y) => (
      <Path key={y} d={`M2 ${y} Q 8 ${y - 5}, 14 ${y} T 26 ${y}`} stroke={color} strokeWidth="1.3" fill="none" />
    ))}
  </Svg>
);

export const GlyphWeave = ({ color = colors.gold, size = SIZE }) => (
  <Svg width={size} height={size} viewBox="0 0 28 28">
    {[4, 9, 14, 19, 24].map((x) => (
      <Line key={`v${x}`} x1={x} y1="2" x2={x} y2="26" stroke={color} strokeWidth="1.1" />
    ))}
    {[4, 9, 14, 19, 24].map((y) => (
      <Line key={`h${y}`} x1="2" y1={y} x2="26" y2={y} stroke={color} strokeWidth="1.1" opacity={0.5} />
    ))}
  </Svg>
);

export const GlyphSpikeStar = ({ color = colors.gold, size = SIZE }) => (
  <Svg width={size} height={size} viewBox="0 0 28 28">
    <G>
      {Array.from({ length: 8 }).map((_, i) => {
        const a1 = (i * 45 * Math.PI) / 180;
        const a2 = a1 + (22.5 * Math.PI) / 180;
        const x1 = 14 + Math.cos(a1) * 12;
        const y1 = 14 + Math.sin(a1) * 12;
        const x2 = 14 + Math.cos(a2) * 5;
        const y2 = 14 + Math.sin(a2) * 5;
        return <Line key={i} x1="14" y1="14" x2={x1} y2={y1} stroke={color} strokeWidth="1.6" />;
      })}
      <Circle cx="14" cy="14" r="2.2" fill={color} />
    </G>
  </Svg>
);

const GLYPHS = [GlyphSquareSpiral, GlyphSunburst, GlyphTarget, GlyphWaves, GlyphWeave, GlyphSpikeStar];

// The full row, as seen under the Mavuno wordmark
export function MandalaGlyphRow({ color = colors.gold, size = SIZE, gap = 10, style }) {
  return (
    <View style={[{ flexDirection: 'row' }, style]}>
      {GLYPHS.map((Glyph, i) => (
        <View key={i} style={{ marginRight: i === GLYPHS.length - 1 ? 0 : gap }}>
          <Glyph color={color} size={size} />
        </View>
      ))}
    </View>
  );
}

export default GLYPHS;
