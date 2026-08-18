import React, { useState } from 'react';
import { View, ScrollView, StyleSheet, Text, Image, TouchableOpacity } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import { Ionicons } from '@expo/vector-icons';
import MandalaBackdrop from '../components/MandalaBackdrop';
import ScreenHeader from '../components/ScreenHeader';
import Interactive3DCard from '../components/Interactive3DCard';
import OverlayModal from '../components/OverlayModal';
import { colors, radii } from '../theme/theme';

const SERMONS = [
  { id: '1', title: 'Rooted: Living from Purpose', speaker: 'Pastor James', duration: '34 min', category: 'Latest Series', image: '/sermon_hero_3d.png' },
  { id: '2', title: 'The Harvest Principle', speaker: 'Pastor James', duration: '41 min', category: 'Harvest Series', image: '/church_community_3d.png' },
  { id: '3', title: 'Faith That Moves Mountains', speaker: 'Pastor Grace', duration: '29 min', category: 'Faith', image: '/sermon_hero_3d.png' },
  { id: '4', title: 'Building on the Rock', speaker: 'Pastor James', duration: '37 min', category: 'Foundations', image: '/church_community_3d.png' },
];

export default function SermonsScreen() {
  const [modalVisible, setModalVisible] = useState(false);

  return (
    <View style={styles.root}>
      <StatusBar style="dark" />
      <MandalaBackdrop color={colors.orange} opacity={0.06} size={400} style={{ top: -100, right: -140 }} />
      
      <ScrollView contentContainerStyle={styles.content}>
        <ScreenHeader title="Sermons & Media" subtitle="Catch up on messages, live streams, and teaching series." />

        {SERMONS.map((s) => (
          <Interactive3DCard
            key={s.id}
            style={styles.card}
            tiltAmount={6}
            scaleHover={1.02}
            onPress={() => setModalVisible(true)}
          >
            <View style={styles.cardRow}>
              <View style={styles.thumbnailWrap}>
                <Image source={{ uri: s.image }} style={styles.thumbnail} resizeMode="cover" />
                <View style={styles.playOverlay}>
                  <Ionicons name="play" size={16} color={colors.white} />
                </View>
              </View>
              <View style={styles.info}>
                <Text style={styles.category}>{s.category.toUpperCase()}</Text>
                <Text style={styles.title}>{s.title}</Text>
                <Text style={styles.speaker}>{s.speaker} · {s.duration}</Text>
              </View>
              <TouchableOpacity onPress={() => setModalVisible(true)}>
                <Ionicons name="play-circle-outline" size={28} color={colors.orange} />
              </TouchableOpacity>
            </View>
          </Interactive3DCard>
        ))}
      </ScrollView>

      <OverlayModal visible={modalVisible} type="video" onClose={() => setModalVisible(false)} />
    </View>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1, backgroundColor: colors.background },
  content: { padding: 24, paddingTop: 60, paddingBottom: 40 },
  card: { marginBottom: 16, padding: 14 },
  cardRow: { flexDirection: 'row', alignItems: 'center' },
  thumbnailWrap: { width: 68, height: 68, borderRadius: radii.md, overflow: 'hidden', position: 'relative' },
  thumbnail: { width: '100%', height: '100%' },
  playOverlay: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: 'rgba(15, 23, 42, 0.3)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  info: { flex: 1, marginLeft: 14 },
  category: { fontSize: 9, fontWeight: '700', color: colors.orange, letterSpacing: 0.8 },
  title: { fontSize: 15, fontWeight: '700', color: colors.textPrimary, marginTop: 2 },
  speaker: { fontSize: 12, color: colors.textSecondary, marginTop: 2 },
});
