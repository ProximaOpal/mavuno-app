import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { MandalaGlyphRow } from './MandalaGlyphs';
import { colors } from '../theme/theme';

export default function ScreenHeader({ title, subtitle }) {
  return (
    <View style={styles.wrap}>
      <Text style={styles.title}>{title}</Text>
      {subtitle ? <Text style={styles.subtitle}>{subtitle}</Text> : null}
      <MandalaGlyphRow color={colors.gold} size={16} gap={8} style={{ marginTop: 14 }} />
    </View>
  );
}

const styles = StyleSheet.create({
  wrap: { marginBottom: 26 },
  title: { color: colors.white, fontSize: 26, fontWeight: '800', letterSpacing: 0.3 },
  subtitle: { color: colors.textMuted, fontSize: 13, marginTop: 6, lineHeight: 18 },
});
