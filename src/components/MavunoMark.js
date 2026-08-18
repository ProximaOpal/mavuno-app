import React, { useEffect } from 'react';
import Svg, { Circle, Path } from 'react-native-svg';
import Animated, {
  useSharedValue,
  useAnimatedProps,
  withTiming,
  withDelay,
  Easing,
} from 'react-native-reanimated';
import { colors } from '../theme/theme';

const AnimatedCircle = Animated.createAnimatedComponent(Circle);
const AnimatedPath = Animated.createAnimatedComponent(Path);

const RING_LEN = 340; // approx circumference for the two arcs at r=54
const M_LEN = 260; // approx path length of the M stroke

export default function MavunoMark({ size = 140, color = colors.white, animate = true, delay = 0 }) {
  const ringProgress = useSharedValue(animate ? 0 : 1);
  const mProgress = useSharedValue(animate ? 0 : 1);
  const scale = useSharedValue(animate ? 0.85 : 1);
  const opacity = useSharedValue(animate ? 0 : 1);

  useEffect(() => {
    if (!animate) return;
    opacity.value = withDelay(delay, withTiming(1, { duration: 300 }));
    scale.value = withDelay(delay, withTiming(1, { duration: 700, easing: Easing.out(Easing.back(1.2)) }));
    ringProgress.value = withDelay(delay, withTiming(1, { duration: 900, easing: Easing.out(Easing.cubic) }));
    mProgress.value = withDelay(delay + 250, withTiming(1, { duration: 700, easing: Easing.out(Easing.cubic) }));
  }, [animate]);

  const ringProps = useAnimatedProps(() => ({
    strokeDashoffset: RING_LEN * (1 - ringProgress.value),
  }));
  const mProps = useAnimatedProps(() => ({
    strokeDashoffset: M_LEN * (1 - mProgress.value),
  }));

  return (
    <Animated.View style={{ width: size, height: size, opacity, transform: [{ scale }] }}>
      <Svg width={size} height={size} viewBox="0 0 140 140">
        {/* Two offset ring arcs, echoing the logo's broken double circle */}
        <AnimatedCircle
          cx="70"
          cy="70"
          r="54"
          stroke={color}
          strokeWidth="4"
          fill="none"
          strokeDasharray={`${RING_LEN * 0.78} ${RING_LEN}`}
          strokeLinecap="round"
          rotation="-100"
          origin="70,70"
          animatedProps={ringProps}
        />
        <AnimatedCircle
          cx="70"
          cy="70"
          r="46"
          stroke={color}
          strokeWidth="3"
          fill="none"
          strokeDasharray={`${RING_LEN * 0.7} ${RING_LEN}`}
          strokeLinecap="round"
          rotation="80"
          origin="70,70"
          animatedProps={ringProps}
        />
        {/* Brush-style M */}
        <AnimatedPath
          d="M46 92 L46 48 L70 78 L94 48 L94 92"
          stroke={color}
          strokeWidth="9"
          strokeLinecap="round"
          strokeLinejoin="round"
          fill="none"
          strokeDasharray={`${M_LEN} ${M_LEN}`}
          animatedProps={mProps}
        />
      </Svg>
    </Animated.View>
  );
}
