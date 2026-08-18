import React, { useState } from 'react';
import { View, ScrollView, StyleSheet, Text, Image } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import MandalaBackdrop from '../components/MandalaBackdrop';
import ScreenHeader from '../components/ScreenHeader';
import Interactive3DCard from '../components/Interactive3DCard';
import OverlayModal from '../components/OverlayModal';
import { colors, radii } from '../theme/theme';
import CircleIcon from '../components/CircleIcon';

const EVENTS = [
  { id: '1', title: 'Sunday Service — Second Service', meta: 'Sun · 9:00 AM · Main Auditorium', eyebrow: 'This Week', image: '/church_community_3d.png' },
  { id: '2', title: 'Youth & Young Adults Night', meta: 'Fri · 6:30 PM · Youth Hall', eyebrow: 'This Week', image: '/sermon_hero_3d.png' },
  { id: '3', title: 'Mid-Week Prayer & Fasting', meta: 'Wed · 5:30 AM · Online & Prayer Room', eyebrow: 'Ongoing', image: '/church_community_3d.png' },
  { id: '4', title: 'Marriage Encounter Retreat', meta: 'Sep 12–13 · Naivasha Resort', eyebrow: 'Upcoming', image: '/sermon_hero_3d.png' },
];

export default function EventsScreen() {
  const [modalVisible, setModalVisible] = useState(false);

  return (
    <View style={styles.root}>
      <StatusBar style="dark" />
      <MandalaBackdrop color={colors.gold} opacity={0.06} size={380} style={{ top: -100, left: -140 }} />

      <ScrollView contentContainerStyle={styles.content}>
        <ScreenHeader title="Upcoming Events" subtitle="Gatherings, youth nights, and retreats at Mavuno Church." />

        {EVENTS.map((e) => (
          <Interactive3DCard
            key={e.id}
            style={styles.card}
            tiltAmount={6}
            scaleHover={1.02}
            onPress={() => setModalVisible(true)}
          >
            <View style={styles.cardRow}>
              <Image source={{ uri: e.image }} style={styles.eventImage} resizeMode="cover" />
              <View style={{ flex: 1, marginLeft: 14 }}>
                <Text style={styles.eyebrow}>{e.eyebrow.toUpperCase()}</Text>
                <Text style={styles.cardTitle}>{e.title}</Text>
                <Text style={styles.cardMeta}>{e.meta}</Text>
              </View>
              <CircleIcon name="calendar" size={36} iconSize={16} fillColor={colors.surfaceDark} />
            </View>
          </Interactive3DCard>
        ))}
      </ScrollView>

      <OverlayModal visible={modalVisible} type="contact" onClose={() => setModalVisible(false)} />
    </View>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1, backgroundColor: colors.background },
  content: { padding: 24, paddingTop: 60, paddingBottom: 40 },
  card: { marginBottom: 16, padding: 14 },
  cardRow: { flexDirection: 'row', alignItems: 'center' },
  eventImage: { width: 60, height: 60, borderRadius: radii.md },
  eyebrow: { fontSize: 9, fontWeight: '700', color: colors.gold, letterSpacing: 0.8 },
  cardTitle: { fontSize: 15, fontWeight: '700', color: colors.textPrimary, marginTop: 2 },
  cardMeta: { fontSize: 12, color: colors.textSecondary, marginTop: 2 },
});
