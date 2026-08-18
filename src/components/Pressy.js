import React, { useState } from 'react';
import { Pressable, StyleSheet, Platform } from 'react-native';
import Animated, {
  useSharedValue,
  useAnimatedStyle,
  withTiming,
  withSpring,
} from 'react-native-reanimated';

/**
 * Pressy — the app's universal touch target.
 * - On press: scales down slightly + a translucent ripple ring expands from
 *   the touch point (approximated centrally; RN has no true ripple-at-point
 *   API without native modules, so we fake it with a centered pulse — reads
 *   very close to Material ripple at this scale).
 * - On web (Expo web / RNW): also lights up on hover, since a mouse cursor
 *   is present there — this is the "cursor motion" effect requested.
 */
export default function Pressy({ children, onPress, style, rippleColor = 'rgba(255,255,255,0.25)', disabled }) {
  const scale = useSharedValue(1);
  const rippleScale = useSharedValue(0);
  const rippleOpacity = useSharedValue(0);
  const [hovered, setHovered] = useState(false);

  const handlePressIn = () => {
    scale.value = withTiming(0.94, { duration: 90 });
    rippleScale.value = 0;
    rippleOpacity.value = 0.5;
    rippleScale.value = withTiming(1, { duration: 420 });
    rippleOpacity.value = withTiming(0, { duration: 420 });
  };
  const handlePressOut = () => {
    scale.value = withSpring(1, { damping: 12, stiffness: 180 });
  };

  const boxStyle = useAnimatedStyle(() => ({
    transform: [{ scale: scale.value }, { scale: withTiming(hovered ? 1.02 : 1, { duration: 150 }) }],
  }));
  const rippleStyle = useAnimatedStyle(() => ({
    opacity: rippleOpacity.value,
    transform: [{ scale: 0.3 + rippleScale.value * 1.2 }],
  }));

  const webHoverProps =
    Platform.OS === 'web'
      ? {
          onHoverIn: () => setHovered(true),
          onHoverOut: () => setHovered(false),
        }
      : {};

  return (
    <Pressable
      onPress={onPress}
      onPressIn={handlePressIn}
      onPressOut={handlePressOut}
      disabled={disabled}
      {...webHoverProps}
      style={({ pressed }) => [style, disabled && { opacity: 0.4 }]}
    >
      <Animated.View style={[styles.fill, boxStyle]}>
        {children}
        <Animated.View pointerEvents="none" style={[styles.ripple, rippleStyle, { backgroundColor: rippleColor }]} />
      </Animated.View>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  fill: { position: 'relative' },
  ripple: {
    ...StyleSheet.absoluteFillObject,
    borderRadius: 999,
  },
});
