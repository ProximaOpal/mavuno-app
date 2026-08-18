import React from 'react';
import Svg, { Circle, Line, G } from 'react-native-svg';
import { View, StyleSheet } from 'react-native';
import { colors } from '../theme/theme';

/**
 * Ambient full-bleed mandala: concentric rings + radiating tick marks,
 * echoing both the double-ring "M" logo mark and the radial tick clock
 * seen in the widget screenshots. Meant to sit at low opacity behind content.
 */
export default function MandalaBackdrop({
  color = colors.white,
  opacity = 0.06,
  size = 520,
  style,
  ticks = 60,
}) {
  const c = size / 2;
  return (
    <View pointerEvents="none" style={[styles.wrap, style]}>
      <Svg width={size} height={size} viewBox={`0 0 ${size} ${size}`} opacity={opacity}>
        <G>
          {[0.98, 0.8, 0.6, 0.4].map((f, i) => (
            <Circle key={i} cx={c} cy={c} r={c * f} stroke={color} strokeWidth={i === 0 ? 2 : 1} fill="none" />
          ))}
          {Array.from({ length: ticks }).map((_, i) => {
            const angle = (i * (360 / ticks) * Math.PI) / 180;
            const rOuter = c * 0.98;
            const rInner = c * (i % 5 === 0 ? 0.86 : 0.92);
            const x1 = c + Math.cos(angle) * rOuter;
            const y1 = c + Math.sin(angle) * rOuter;
            const x2 = c + Math.cos(angle) * rInner;
            const y2 = c + Math.sin(angle) * rInner;
            return <Line key={i} x1={x1} y1={y1} x2={x2} y2={y2} stroke={color} strokeWidth={1} />;
          })}
        </G>
      </Svg>
    </View>
  );
}

const styles = StyleSheet.create({
  wrap: {
    position: 'absolute',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
