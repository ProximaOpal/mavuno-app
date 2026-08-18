import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import Pressy from './Pressy';
import { colors, radii } from '../theme/theme';

export default function ListCard({ eyebrow, title, meta, icon = 'chevron-forward', accentColor = colors.orange, delay = 0, onPress }) {
  return (
    <View>
      <Pressy onPress={onPress} rippleColor="rgba(255,255,255,0.08)">
        <View style={styles.card}>
          <View style={[styles.accent, { backgroundColor: accentColor }]} />
          <View style={{ flex: 1 }}>
            {eyebrow ? <Text style={styles.eyebrow}>{eyebrow}</Text> : null}
            <Text style={styles.title}>{title}</Text>
            {meta ? <Text style={styles.meta}>{meta}</Text> : null}
          </View>
          <Ionicons name={icon} size={18} color={colors.textMuted} />
        </View>
      </Pressy>
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.charcoal2,
    borderRadius: radii.md,
    padding: 16,
    marginBottom: 12,
  },
  accent: { width: 3, alignSelf: 'stretch', borderRadius: 2, marginRight: 14 },
  eyebrow: { color: colors.textMuted, fontSize: 10, letterSpacing: 1.2, textTransform: 'uppercase', marginBottom: 3, fontWeight: '600' },
  title: { color: colors.white, fontSize: 15, fontWeight: '700' },
  meta: { color: colors.textMuted, fontSize: 12, marginTop: 3 },
});
