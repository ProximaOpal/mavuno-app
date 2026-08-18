import React, { useState } from 'react';
import { View, Text, ScrollView, StyleSheet } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import { LinearGradient } from 'expo-linear-gradient';
import RingProgress from '../components/RingProgress';
import ScreenHeader from '../components/ScreenHeader';
import Interactive3DCard from '../components/Interactive3DCard';
import OverlayModal from '../components/OverlayModal';
import { colors, radii } from '../theme/theme';
import CircleIcon from '../components/CircleIcon';

const OPTIONS = [
  { id: 'tithe', title: 'Tithe', meta: 'Give your tithe via M-Pesa Paybill 508000', eyebrow: 'Regular Tithe' },
  { id: 'offering', title: 'Sunday Offering', meta: 'A thanksgiving offering for the week', eyebrow: 'One-time Gift' },
  { id: 'building', title: 'Building & Campus Fund', meta: 'Support the new Hill City campus project', eyebrow: 'Development' },
  { id: 'missions', title: 'Missions & Outreach', meta: 'Fuel community outreach in Nairobi', eyebrow: 'Community' },
];

export default function GiveScreen() {
  const [modalVisible, setModalVisible] = useState(false);

  return (
    <View style={styles.root}>
      <StatusBar style="light" />

      {/* Hero Header with Orange Gradient */}
      <LinearGradient colors={[colors.orange, colors.orangeDeep]} style={styles.hero}>
        <RingProgress
          size={120}
          strokeWidth={8}
          progress={0.82}
          trackColor="rgba(255,255,255,0.28)"
          fillColor={colors.white}
          label="82%"
          sublabel="OF AUGUST GOAL"
          labelColor={colors.white}
        />
        <Text style={styles.heroCaption}>KES 1,230,000 of KES 1,500,000 raised this month</Text>
      </LinearGradient>

      {/* Content — Deep White Surface */}
      <ScrollView contentContainerStyle={styles.content}>
        <ScreenHeader title="Give & Partner" subtitle="Every seed sown here builds lives and expands community." />

        {OPTIONS.map((o) => (
          <Interactive3DCard
            key={o.id}
            style={styles.card}
            tiltAmount={6}
            scaleHover={1.02}
            onPress={() => setModalVisible(true)}
          >
            <View style={styles.cardRow}>
              <CircleIcon name="heart" size={40} iconSize={18} fillColor={colors.orange} />
              <View style={{ flex: 1, marginLeft: 14 }}>
                <Text style={styles.eyebrow}>{o.eyebrow.toUpperCase()}</Text>
                <Text style={styles.cardTitle}>{o.title}</Text>
                <Text style={styles.cardMeta}>{o.meta}</Text>
              </View>
            </View>
          </Interactive3DCard>
        ))}
      </ScrollView>

      <OverlayModal visible={modalVisible} type="give" onClose={() => setModalVisible(false)} />
    </View>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1, backgroundColor: colors.background },
  hero: {
    alignItems: 'center',
    paddingTop: 64,
    paddingBottom: 28,
    borderBottomLeftRadius: radii.xl,
    borderBottomRightRadius: radii.xl,
  },
  heroCaption: { color: 'rgba(255,255,255,0.9)', fontSize: 13, marginTop: 14, fontWeight: '600' },
  content: { padding: 24, paddingBottom: 40 },
  card: { marginBottom: 14, padding: 16 },
  cardRow: { flexDirection: 'row', alignItems: 'center' },
  eyebrow: { fontSize: 9, fontWeight: '700', color: colors.orange, letterSpacing: 0.8 },
  cardTitle: { fontSize: 16, fontWeight: '700', color: colors.textPrimary, marginTop: 2 },
  cardMeta: { fontSize: 12, color: colors.textSecondary, marginTop: 2 },
});
