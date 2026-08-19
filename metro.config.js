const { getDefaultConfig } = require('expo/metro-config');

const config = getDefaultConfig(__dirname);

// Keep Expo's AppEntry resolution rooted at the project when pnpm exposes
// dependencies through .pnpm symlinks.
config.resolver.unstable_enableSymlinks = true;

module.exports = config;
