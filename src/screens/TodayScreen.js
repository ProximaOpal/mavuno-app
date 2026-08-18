import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ScrollView } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import Svg, { Line } from 'react-native-svg';
import RingProgress from '../components/RingProgress';
import CircleIcon from '../components/CircleIcon';
import Interactive3DCard from '../components/Interactive3DCard';
import OverlayModal from '../components/OverlayModal';
import { colors } from '../theme/theme';

function TickRing({ size = 96, count = 40, color = 'rgba(15, 23, 42, 0.25)' }) {
  const c = size / 2;
  return (
    <Svg width={size} height={size}>
      {Array.from({ length: count }).map((_, i) => {
        const angle = (i * (360 / count) * Math.PI) / 180;
        const rOuter = c - 1;
        const rInner = c - 6;
        const x1 = c + Math.cos(angle) * rOuter;
        const y1 = c + Math.sin(angle) * rOuter;
        const x2 = c + Math.cos(angle) * rInner;
        const y2 = c + Math.sin(angle) * rInner;
        return <Line key={i} x1={x1} y1={y1} x2={x2} y2={y2} stroke={color} strokeWidth={1.4} />;
      })}
    </Svg>
  );
}

export default function TodayScreen({ navigation }) {
  const [now, setNow] = useState(new Date());
  const [modalState, setModalState] = useState({ visible: false, type: null });

  useEffect(() => {
    const id = setInterval(() => setNow(new Date()), 1000 * 30);
    return () => clearInterval(id);
  }, []);

  const time = now.toLocaleTimeString('en-GB', { hour: '2-digit', minute: '2-digit' });
  const daysToSunday = (7 - now.getDay()) % 7 || 7;

  const openModal = (type) => setModalState({ visible: true, type });
  const closeModal = () => setModalState({ visible: false, type: null });

  return (
    <View style={styles.root}>
      <StatusBar style="dark" />

      <ScrollView contentContainerStyle={styles.content}>
        <Text style={styles.headerTitle}>TODAY AT MAVUNO</Text>
        <Text style={styles.headerSub}>Live service countdown & giving goals</Text>

        <Interactive3DCard style={styles.widgetCard} tiltAmount={6}>
          <View style={styles.row}>
            {/* Countdown tick ring widget */}
            <View style={styles.countdownWrap}>
              <TickRing size={90} />
              <View style={styles.countdownBadge}>
                <Text style={styles.countdownNum}>{daysToSunday}</Text>
              </View>
              <Text style={styles.timeText}>{time}</Text>
            </View>

            {/* Giving goal progress ring */}
            <RingProgress size={100} strokeWidth={7} progress={0.82} fillColor={colors.orange} label="82%" sublabel="August goal" labelColor={colors.textPrimary} />

            <View style={styles.liveCol}>
              <Text style={styles.liveLabel}>LIVE</Text>
              <View style={styles.bars}>
                <View style={[styles.bar, { height: 14 }]} />
                <View style={[styles.bar, { height: 22 }]} />
                <View style={[styles.bar, { height: 10 }]} />
              </View>
            </View>
          </View>
        </Interactive3DCard>

        {/* Circular Dark Button Widgets (Call, Chat, Give) */}
        <View style={styles.trioWrap}>
          <Text style={styles.sectionTitle}>QUICK ACTIONS & CONTACT</Text>
          <View style={styles.iconRow}>
            <CircleIcon name="call" size={48} iconSize={20} onPress={() => openModal('contact')} />
            <CircleIcon name="chatbubbles" size={48} iconSize={20} onPress={() => openModal('chat')} />
            <CircleIcon name="heart" size={48} iconSize={20} fillColor={colors.orange} onPress={() => openModal('give')} />
          </View>
        </View>

        <Text style={styles.captionText}>Countdown to Sunday service · Tap the phone, chat, or heart button to connect.</Text>
      </ScrollView>

      <OverlayModal visible={modalState.visible} type={modalState.type} onClose={closeModal} />
    </View>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1, backgroundColor: colors.background },
  content: { padding: 24, paddingTop: 64 },
  headerTitle: { fontSize: 13, fontWeight: '800', color: colors.orange, letterSpacing: 1.5 },
  headerSub: { fontSize: 20, fontWeight: '800', color: colors.textPrimary, marginTop: 4, marginBottom: 24 },
  widgetCard: { padding: 20, marginBottom: 28 },
  row: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' },
  countdownWrap: { width: 90, height: 90, alignItems: 'center', justifyContent: 'center' },
  countdownBadge: {
    position: 'absolute',
    top: 6,
    left: 0,
    width: 24,
    height: 24,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: colors.textPrimary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  countdownNum: { color: colors.textPrimary, fontSize: 11, fontWeight: '700' },
  timeText: { color: colors.textPrimary, fontSize: 15, fontWeight: '700', marginTop: 6 },
  liveCol: { alignItems: 'center' },
  liveLabel: { color: colors.orange, fontSize: 11, fontWeight: '800', letterSpacing: 2, marginBottom: 8 },
  bars: { flexDirection: 'row', alignItems: 'flex-end', gap: 4 },
  bar: { width: 6, backgroundColor: colors.orange, borderRadius: 2 },
  trioWrap: { marginTop: 12, marginBottom: 28 },
  sectionTitle: { fontSize: 11, fontWeight: '700', color: colors.textMuted, letterSpacing: 1, marginBottom: 16 },
  iconRow: { flexDirection: 'row', gap: 20, justifyContent: 'flex-start' },
  captionText: { color: colors.textSecondary, fontSize: 13, lineHeight: 20 },
});
