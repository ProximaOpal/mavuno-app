import React from 'react';
import { NavigationContainer, DefaultTheme } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';
import { View } from 'react-native';

import SplashScreen from '../screens/SplashScreen';
import HomeScreen from '../screens/HomeScreen';
import TodayScreen from '../screens/TodayScreen';
import SermonsScreen from '../screens/SermonsScreen';
import EventsScreen from '../screens/EventsScreen';
import GiveScreen from '../screens/GiveScreen';
import { colors } from '../theme/theme';

const Stack = createNativeStackNavigator();
const Tab = createBottomTabNavigator();

const ICONS = {
  Home: 'home-outline',
  Today: 'grid-outline',
  Sermons: 'book-outline',
  Events: 'calendar-outline',
  Give: 'heart-outline',
};

function Tabs() {
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        headerShown: false,
        tabBarActiveTintColor: colors.orange,
        tabBarInactiveTintColor: colors.textMuted,
        tabBarStyle: {
          backgroundColor: colors.surface,
          borderTopColor: 'rgba(15, 23, 42, 0.08)',
          height: 74,
          paddingTop: 8,
          paddingBottom: 16,
          shadowColor: '#000',
          shadowOffset: { width: 0, height: -4 },
          shadowOpacity: 0.05,
          shadowRadius: 12,
          elevation: 10,
        },
        tabBarLabelStyle: { fontSize: 10, fontWeight: '700', letterSpacing: 0.4 },
        tabBarIcon: ({ color, size, focused }) => (
          <View
            style={{
              padding: 6,
              borderRadius: 999,
              backgroundColor: focused ? 'rgba(245, 130, 31, 0.12)' : 'transparent',
            }}
          >
            <Ionicons name={ICONS[route.name]} size={20} color={color} />
          </View>
        ),
      })}
    >
      <Tab.Screen name="Home" component={HomeScreen} />
      <Tab.Screen name="Today" component={TodayScreen} />
      <Tab.Screen name="Sermons" component={SermonsScreen} />
      <Tab.Screen name="Events" component={EventsScreen} />
      <Tab.Screen name="Give" component={GiveScreen} />
    </Tab.Navigator>
  );
}

const navTheme = {
  ...DefaultTheme,
  colors: {
    ...DefaultTheme.colors,
    background: colors.background,
    card: colors.surface,
    border: 'rgba(15, 23, 42, 0.08)',
    primary: colors.orange,
    text: colors.textPrimary,
  },
};

export default function RootNavigator() {
  return (
    <NavigationContainer theme={navTheme}>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        <Stack.Screen name="Splash" component={SplashScreen} />
        <Stack.Screen name="Main" component={Tabs} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
