import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, Dimensions } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { StatusBar } from 'expo-status-bar';
import MavunoMark from '../components/MavunoMark';
import MandalaBackdrop from '../components/MandalaBackdrop';
import RingProgress from '../components/RingProgress';
import CircleIcon from '../components/CircleIcon';
import Pressy from '../components/Pressy';
import { colors } from '../theme/theme';

const { width } = Dimensions.get('window');

export default function SplashScreen({ navigation }) {
  const [progress, setProgress] = useState(0.08);

  useEffect(() => {
    // simulated "loading" ring, like the 91% ring in photo 4
    const id = setInterval(() => {
      setProgress((p) => {
        const next = p + 0.09;
        if (next >= 1) {
          clearInterval(id);
          setTimeout(() => navigation?.replace?.('Main'), 500);
          return 1;
        }
        return next;
      });
    }, 260);
    return () => clearInterval(id);
  }, []);

  const now = new Date();
  const dateLabel = now
    .toLocaleDateString('en-GB', { weekday: 'long', day: '2-digit', month: 'long' })
    .toUpperCase();

  return (
    <LinearGradient colors={[colors.orange, colors.orangeDeep]} style={styles.flex}>
      <StatusBar style="light" />
      <MandalaBackdrop color={colors.white} opacity={0.08} size={width * 1.3} style={{ top: -width * 0.35, left: -width * 0.15 }} />

      <View style={styles.top}>
        <View>
          <MavunoMark size={104} color={colors.white} delay={100} />
        </View>

        <Text style={styles.wordmark}>
          MAVUNO
        </Text>
        <Text style={styles.wordmarkSub}>
          CHURCH
        </Text>

        <View style={styles.taglineRow}>
          <Text style={styles.tagline}>A people. A purpose. A place to belong.</Text>
        </View>

        <View style={styles.progressRow}>
          <RingProgress
            size={64}
            strokeWidth={4}
            progress={progress}
            trackColor="rgba(255,255,255,0.25)"
            fillColor={colors.white}
            label={`${Math.round(progress * 100)}%`}
            labelColor={colors.white}
          />
          <View style={{ marginLeft: 14 }}>
            <Text style={styles.dateLabel}>{dateLabel}</Text>
            <Text style={styles.loadingLabel}>Preparing your space…</Text>
          </View>
        </View>
      </View>

      {/* bottom curved bar, echoing photo 4's white footer */}
      <View style={styles.footer}>
        <View style={styles.footerRow}>
          <CircleIcon name="chatbubble-outline" borderColor="rgba(0,0,0,0.15)" color={colors.black} size={40} iconSize={16} />
          <CircleIcon name="call-outline" borderColor="rgba(0,0,0,0.15)" color={colors.black} size={40} iconSize={16} />
          <Pressy onPress={() => navigation?.replace?.('Main')} rippleColor="rgba(255,255,255,0.4)">
            <View style={styles.playBtn}>
              <View style={styles.playTriangle} />
            </View>
          </Pressy>
        </View>
        <Text style={styles.skip} onPress={() => navigation?.replace?.('Main')}>
          Tap play to enter
        </Text>
      </View>
    </LinearGradient>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1 },
  top: { flex: 1, alignItems: 'center', justifyContent: 'center', paddingHorizontal: 28 },
  wordmark: {
    color: colors.white,
    fontSize: 34,
    fontWeight: '800',
    letterSpacing: 3,
    marginTop: 22,
  },
  wordmarkSub: {
    color: colors.white,
    fontSize: 15,
    fontWeight: '600',
    letterSpacing: 6,
    opacity: 0.9,
    marginTop: 2,
  },
  taglineRow: { marginTop: 18 },
  tagline: {
    color: 'rgba(255,255,255,0.85)',
    fontSize: 14,
    textAlign: 'center',
    lineHeight: 20,
  },
  progressRow: { flexDirection: 'row', alignItems: 'center', marginTop: 34 },
  dateLabel: { color: colors.white, fontWeight: '700', fontSize: 12, letterSpacing: 1 },
  loadingLabel: { color: 'rgba(255,255,255,0.75)', fontSize: 12, marginTop: 4 },
  footer: {
    backgroundColor: colors.white,
    borderTopLeftRadius: 40,
    borderTopRightRadius: 40,
    paddingTop: 22,
    paddingBottom: 34,
    alignItems: 'center',
  },
  footerRow: { flexDirection: 'row', alignItems: 'center', gap: 22 },
  playBtn: {
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: colors.orange,
    alignItems: 'center',
    justifyContent: 'center',
  },
  playTriangle: {
    width: 0,
    height: 0,
    marginLeft: 4,
    borderTopWidth: 9,
    borderBottomWidth: 9,
    borderLeftWidth: 14,
    borderTopColor: 'transparent',
    borderBottomColor: 'transparent',
    borderLeftColor: colors.white,
  },
  skip: { color: colors.black, opacity: 0.4, fontSize: 11, marginTop: 14, letterSpacing: 0.5 },
});
