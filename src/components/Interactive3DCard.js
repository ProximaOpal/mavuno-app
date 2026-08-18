import React, { useRef, useState } from 'react';
import { View, StyleSheet, Animated, Platform } from 'react-native';
import { colors, radii } from '../theme/theme';

export default function Interactive3DCard({
  children,
  style,
  tiltAmount = 10,
  scaleHover = 1.02,
  onPress,
}) {
  const rotateX = useRef(new Animated.Value(0)).current;
  const rotateY = useRef(new Animated.Value(0)).current;
  const scale = useRef(new Animated.Value(1)).current;
  const [isHovered, setIsHovered] = useState(false);

  const handleMouseMove = (e) => {
    if (Platform.OS !== 'web') return;
    const { nativeEvent } = e;
    const target = e.currentTarget || e.target;
    if (!target) return;
    const rect = target.getBoundingClientRect ? target.getBoundingClientRect() : null;
    if (!rect) return;

    const x = nativeEvent.clientX - rect.left;
    const y = nativeEvent.clientY - rect.top;
    const centerX = rect.width / 2;
    const centerY = rect.height / 2;

    const rotX = ((y - centerY) / centerY) * -tiltAmount;
    const rotY = ((x - centerX) / centerX) * tiltAmount;

    Animated.spring(rotateX, { toValue: rotX, friction: 7, tension: 40, useNativeDriver: false }).start();
    Animated.spring(rotateY, { toValue: rotY, friction: 7, tension: 40, useNativeDriver: false }).start();
  };

  const handleMouseEnter = () => {
    setIsHovered(true);
    Animated.spring(scale, { toValue: scaleHover, friction: 6, tension: 50, useNativeDriver: false }).start();
  };

  const handleMouseLeave = () => {
    setIsHovered(false);
    Animated.spring(rotateX, { toValue: 0, friction: 6, tension: 40, useNativeDriver: false }).start();
    Animated.spring(rotateY, { toValue: 0, friction: 6, tension: 40, useNativeDriver: false }).start();
    Animated.spring(scale, { toValue: 1, friction: 6, tension: 40, useNativeDriver: false }).start();
  };

  const interpolateRotateX = rotateX.interpolate({
    inputRange: [-20, 20],
    outputRange: ['-20deg', '20deg'],
  });

  const interpolateRotateY = rotateY.interpolate({
    inputRange: [-20, 20],
    outputRange: ['-20deg', '20deg'],
  });

  const webProps = Platform.OS === 'web' ? {
    onMouseMove: handleMouseMove,
    onMouseEnter: handleMouseEnter,
    onMouseLeave: handleMouseLeave,
    onClick: onPress,
  } : {};

  return (
    <Animated.View
      {...webProps}
      style={[
        styles.card,
        isHovered && styles.cardHovered,
        {
          transform: [
            { perspective: 1000 },
            { rotateX: interpolateRotateX },
            { rotateY: interpolateRotateY },
            { scale },
          ],
        },
        style,
      ]}
    >
      {children}
      {isHovered && <View style={styles.glareHighlight} pointerEvents="none" />}
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: colors.surface,
    borderRadius: radii.lg,
    padding: 20,
    borderWidth: 1,
    borderColor: 'rgba(15, 23, 42, 0.06)',
    shadowColor: '#0F172A',
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.06,
    shadowRadius: 20,
    elevation: 4,
    overflow: 'hidden',
    position: 'relative',
  },
  cardHovered: {
    borderColor: 'rgba(245, 130, 31, 0.3)',
    shadowOpacity: 0.12,
    shadowRadius: 30,
    shadowOffset: { width: 0, height: 16 },
  },
  glareHighlight: {
    position: 'absolute',
    top: -50,
    left: -50,
    right: -50,
    height: 120,
    backgroundColor: 'rgba(255, 255, 255, 0.25)',
    transform: [{ rotate: '-25deg' }],
  },
});
