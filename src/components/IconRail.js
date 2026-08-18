import React from 'react';
import { View, StyleSheet, TouchableOpacity } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../theme/theme';

const DEFAULT_ITEMS = [
  { name: 'play-circle-outline', key: 'live' },
  { name: 'book-outline', key: 'sermons' },
  { name: 'calendar-outline', key: 'events' },
  { name: 'heart-outline', key: 'give' },
  { name: 'hand-left-outline', key: 'prayer' },
  { name: 'people-outline', key: 'community' },
  { name: 'notifications-outline', key: 'notifications' },
  { name: 'chatbubble-outline', key: 'chat' },
  { name: 'call-outline', key: 'contact' },
];

export default function IconRail({ items = DEFAULT_ITEMS, onSelect, width = 64 }) {
  return (
    <LinearGradient
      colors={[colors.orange, colors.orangeDeep]}
      start={{ x: 0, y: 0 }}
      end={{ x: 0, y: 1 }}
      style={[styles.rail, { width }]}
    >
      {items.map((item) => (
        <TouchableOpacity
          key={item.key}
          onPress={() => onSelect?.(item.key)}
          activeOpacity={0.7}
          style={styles.iconWrap}
        >
          <Ionicons name={item.name} size={22} color={colors.white} />
        </TouchableOpacity>
      ))}
    </LinearGradient>
  );
}

const styles = StyleSheet.create({
  rail: {
    paddingVertical: 24,
    alignItems: 'center',
    justifyContent: 'space-evenly',
    borderTopLeftRadius: 28,
    borderBottomLeftRadius: 28,
    shadowColor: '#F5821F',
    shadowOffset: { width: -4, height: 0 },
    shadowOpacity: 0.25,
    shadowRadius: 16,
    elevation: 8,
  },
  iconWrap: {
    padding: 10,
    borderRadius: 20,
    backgroundColor: 'rgba(255, 255, 255, 0.12)',
    marginVertical: 4,
  },
});
