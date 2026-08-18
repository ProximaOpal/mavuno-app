import React, { useEffect } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import Animated, { useSharedValue, useAnimatedStyle, withTiming, withDelay } from 'react-native-reanimated';
import { colors } from '../theme/theme';

export default function AccentStat({ eyebrow, value, caption, accentColor = colors.orange, delay = 0, big = true }) {
  const opacity = useSharedValue(0);
  const translate = useSharedValue(14);

  useEffect(() => {
    opacity.value = withDelay(delay, withTiming(1, { duration: 450 }));
    translate.value = withDelay(delay, withTiming(0, { duration: 450 }));
  }, []);

  const animStyle = useAnimatedStyle(() => ({
    opacity: opacity.value,
    transform: [{ translateX: translate.value }],
  }));

  return (
    <Animated.View style={[styles.row, animStyle]}>
      <View style={[styles.bar, { backgroundColor: accentColor }]} />
      <View style={{ flex: 1 }}>
        {eyebrow ? <Text style={styles.eyebrow}>{eyebrow}</Text> : null}
        <Text style={[styles.value, big && styles.valueBig]}>{value}</Text>
        {caption ? <Text style={styles.caption}>{caption}</Text> : null}
      </View>
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  row: { flexDirection: 'row', alignItems: 'stretch', marginBottom: 26 },
  bar: { width: 3, borderRadius: 2, marginRight: 14 },
  eyebrow: {
    color: colors.textMuted,
    fontSize: 11,
    letterSpacing: 1.4,
    textTransform: 'uppercase',
    marginBottom: 4,
    fontWeight: '600',
  },
  value: { color: colors.white, fontSize: 20, fontWeight: '700' },
  valueBig: { fontSize: 26 },
  caption: { color: colors.textMuted, fontSize: 12, marginTop: 4, lineHeight: 17 },
});
