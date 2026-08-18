import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ScrollView, Image, TouchableOpacity } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import Svg, { Line } from 'react-native-svg';
import MandalaBackdrop from '../components/MandalaBackdrop';
import AccentStat from '../components/AccentStat';
import IconRail from '../components/IconRail';
import MavunoMark from '../components/MavunoMark';
import CircleIcon from '../components/CircleIcon';
import Interactive3DCard from '../components/Interactive3DCard';
import OverlayModal from '../components/OverlayModal';
import { colors, radii } from '../theme/theme';

// Radial tick ring matching the photo reference ("59" circular clock face)
function RadialClockTickWidget({ size = 110, count = 44, timeStr = '21:56', secNum = '59' }) {
  const c = size / 2;
  return (
    <View style={{ width: size, height: size, alignItems: 'center', justifyContent: 'center' }}>
      <Svg width={size} height={size} style={StyleSheet.absoluteFill}>
        {Array.from({ length: count }).map((_, i) => {
          const angle = (i * (360 / count) * Math.PI) / 180;
          const rOuter = c - 2;
          const rInner = c - 8;
          const x1 = c + Math.cos(angle) * rOuter;
          const y1 = c + Math.sin(angle) * rOuter;
          const x2 = c + Math.cos(angle) * rInner;
          const y2 = c + Math.sin(angle) * rInner;
          return <Line key={i} x1={x1} y1={y1} x2={x2} y2={y2} stroke="rgba(15, 23, 42, 0.25)" strokeWidth={1.5} />;
        })}
      </Svg>
      <View style={styles.clockCenter}>
        <View style={styles.secBadge}>
          <Text style={styles.secBadgeText}>{secNum}</Text>
        </View>
        <Text style={styles.clockTimeText}>{timeStr}</Text>
      </View>
    </View>
  );
}

export default function HomeScreen({ navigation }) {
  const [now, setNow] = useState(new Date());
  const [modalState, setModalState] = useState({ visible: false, type: null });

  useEffect(() => {
    const id = setInterval(() => setNow(new Date()), 1000 * 10);
    return () => clearInterval(id);
  }, []);

  const timeStr = now.toLocaleTimeString('en-GB', { hour: '2-digit', minute: '2-digit' });
  const secNum = String(now.getSeconds()).padStart(2, '0');
  const dateStr = now.toLocaleDateString('en-GB', { weekday: 'long', day: 'numeric', month: 'short' });

  const openModal = (type) => setModalState({ visible: true, type });
  const closeModal = () => setModalState({ visible: false, type: null });

  const handleRailSelect = (key) => {
    switch (key) {
      case 'live':
        navigation?.navigate?.('Today');
        break;
      case 'sermons':
        navigation?.navigate?.('Sermons');
        break;
      case 'events':
        navigation?.navigate?.('Events');
        break;
      case 'give':
        openModal('give');
        break;
      case 'prayer':
        openModal('contact');
        break;
      case 'community':
        navigation?.navigate?.('Events');
        break;
      case 'notifications':
        openModal('chat');
        break;
      case 'chat':
        openModal('chat');
        break;
      case 'contact':
        openModal('contact');
        break;
      default:
        break;
    }
  };

  return (
    <View style={styles.root}>
      <StatusBar style="dark" />

      {/* Main Panel — Deep White Light Theme */}
      <ScrollView style={styles.panel} contentContainerStyle={styles.panelContent}>
        <MandalaBackdrop color={colors.orange} opacity={0.06} size={460} style={{ top: -140, right: -160 }} />

        {/* Header Bar */}
        <View style={styles.header}>
          <View style={styles.brandRow}>
            <MavunoMark size={32} color={colors.orange} animate={false} />
            <Text style={styles.headerLabel}>MAVUNO CHURCH</Text>
          </View>
          <View style={styles.campusBadge}>
            <Text style={styles.campusBadgeText}>NAIROBI CAMPUS</Text>
          </View>
        </View>

        {/* 3D Radial Clock & Circular Dark Button Trio (Widget Row from Photo) */}
        <Interactive3DCard style={styles.widgetRowCard} tiltAmount={6} scaleHover={1.01}>
          <View style={styles.widgetRow}>
            {/* Clock Tick Ring Widget */}
            <RadialClockTickWidget size={96} count={40} timeStr={timeStr} secNum={secNum} />

            {/* Dark Circular Button Widgets Trio (Phone, Chat, Give/G+) */}
            <View style={styles.iconTrio}>
              <CircleIcon name="call" size={44} iconSize={18} onPress={() => openModal('contact')} />
              <CircleIcon name="chatbubbles" size={44} iconSize={18} onPress={() => openModal('chat')} />
              <CircleIcon name="heart" size={44} iconSize={18} fillColor={colors.orange} onPress={() => openModal('give')} />
            </View>
          </View>
        </Interactive3DCard>

        {/* 3D Sermon Media Card with Photo & Video Trigger */}
        <Interactive3DCard style={styles.mediaCard} tiltAmount={8} scaleHover={1.02} onPress={() => openModal('video')}>
          <View style={styles.imageWrap}>
            <Image source={{ uri: '/sermon_hero_3d.png' }} style={styles.heroImage} resizeMode="cover" />
            <View style={styles.imageOverlay} />
            <TouchableOpacity style={styles.playBadge} onPress={() => openModal('video')}>
              <CircleIcon name="play" size={42} iconSize={20} fillColor={colors.orange} />
            </TouchableOpacity>
          </View>
          <View style={styles.mediaDetails}>
            <Text style={styles.mediaCategory}>LATEST SERMON · WATCH & LISTEN</Text>
            <Text style={styles.mediaTitle}>Rooted: Living from Purpose</Text>
            <Text style={styles.mediaCaption}>Pastor James · Sunday Main Service</Text>
          </View>
        </Interactive3DCard>

        {/* Stats & Word for Today */}
        <View style={styles.stats}>
          <AccentStat
            eyebrow={dateStr}
            value={timeStr}
            caption="You're right on time for prayer and fellowship."
            accentColor={colors.orange}
            delay={100}
          />
          <AccentStat
            eyebrow="Word for Today"
            value={'“Blessed are the\npeacemakers.”'}
            caption="Matthew 5:9 — reflect on this before Sunday service."
            accentColor={colors.gold}
            delay={220}
          />
          <AccentStat
            eyebrow="Next Service"
            value="Sun · 9:00 AM"
            caption="Second Service · Main Auditorium · Doors open 8:30"
            accentColor={colors.orange}
            delay={340}
          />
        </View>

        <View style={styles.footer}>
          <MavunoMark size={20} color={colors.textMuted} animate={false} />
          <Text style={styles.footerText}>Mavuno Church · Nairobi Campus</Text>
        </View>
      </ScrollView>

      {/* Right Sidebar Rail */}
      <IconRail onSelect={handleRailSelect} />

      {/* Interactive Overlay Modal */}
      <OverlayModal
        visible={modalState.visible}
        type={modalState.type}
        onClose={closeModal}
        onAction={(action) => {
          if (action === 'call') openModal('contact');
          else if (action === 'prayer') openModal('chat');
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1, flexDirection: 'row', backgroundColor: colors.background },
  panel: { flex: 1, backgroundColor: colors.background },
  panelContent: { padding: 24, paddingTop: 56, minHeight: '100%' },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 28,
  },
  brandRow: { flexDirection: 'row', alignItems: 'center' },
  headerLabel: {
    color: colors.textPrimary,
    fontSize: 14,
    fontWeight: '800',
    letterSpacing: 1.5,
    marginLeft: 10,
  },
  campusBadge: {
    backgroundColor: colors.orangeSoft,
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: radii.pill,
  },
  campusBadgeText: { color: colors.orangeDeep, fontSize: 10, fontWeight: '700', letterSpacing: 0.8 },
  widgetRowCard: {
    marginBottom: 24,
    backgroundColor: colors.surface,
  },
  widgetRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  clockCenter: { alignItems: 'center' },
  secBadge: {
    width: 22,
    height: 22,
    borderRadius: 11,
    borderWidth: 1,
    borderColor: colors.textPrimary,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 2,
  },
  secBadgeText: { fontSize: 10, fontWeight: '700', color: colors.textPrimary },
  clockTimeText: { fontSize: 15, fontWeight: '700', color: colors.textPrimary },
  iconTrio: { flexDirection: 'row', gap: 12 },
  mediaCard: {
    marginBottom: 28,
    padding: 0,
  },
  imageWrap: {
    height: 170,
    width: '100%',
    position: 'relative',
    borderTopLeftRadius: radii.lg,
    borderTopRightRadius: radii.lg,
    overflow: 'hidden',
  },
  heroImage: { width: '100%', height: '100%' },
  imageOverlay: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: 'rgba(15, 23, 42, 0.25)',
  },
  playBadge: {
    position: 'absolute',
    bottom: 14,
    right: 14,
  },
  mediaDetails: { padding: 18 },
  mediaCategory: { fontSize: 10, fontWeight: '700', color: colors.orange, letterSpacing: 1 },
  mediaTitle: { fontSize: 18, fontWeight: '800', color: colors.textPrimary, marginTop: 4 },
  mediaCaption: { fontSize: 12, color: colors.textSecondary, marginTop: 2 },
  stats: { marginTop: 4 },
  footer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 'auto',
    paddingTop: 28,
  },
  footerText: { color: colors.textMuted, fontSize: 11, marginLeft: 8, letterSpacing: 0.5 },
});
