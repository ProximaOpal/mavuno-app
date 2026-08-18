import React from 'react';
import { View, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../theme/theme';

export default function CircleIcon({
  name,
  size = 46,
  iconSize = 20,
  color = colors.white,
  borderColor = 'rgba(255, 255, 255, 0.4)',
  filled = true,
  fillColor = colors.surfaceDark,
  onPress,
}) {
  return (
    <TouchableOpacity onPress={onPress} activeOpacity={0.75}>
      <View style={[styles.outerRing, { width: size + 6, height: size + 6, borderRadius: (size + 6) / 2 }]}>
        <View
          style={[
            styles.circle,
            {
              width: size,
              height: size,
              borderRadius: size / 2,
              borderColor: borderColor,
              backgroundColor: filled ? fillColor : 'transparent',
            },
          ]}
        >
          <Ionicons name={name} size={iconSize} color={color} />
        </View>
      </View>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  outerRing: {
    borderWidth: 1,
    borderColor: 'rgba(15, 23, 42, 0.12)',
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#0F172A',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 3,
  },
  circle: {
    borderWidth: 1.2,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
