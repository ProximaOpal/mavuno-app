package com.mavuno.church.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mavuno.church.audio.AudioEffectsManager
import com.mavuno.church.guard.EllaOverlayManager
import com.mavuno.church.ui.components.EllaAssistantCard
import com.mavuno.church.ui.components.GlassmorphicShieldOverlay
import com.mavuno.church.ui.components.HomeScreenSimulationView
import com.mavuno.church.ui.components.PulsingScanBorder
import com.mavuno.church.ui.screens.EventsScreen
import com.mavuno.church.ui.screens.GiveScreen
import com.mavuno.church.ui.screens.GuardScreen
import com.mavuno.church.ui.screens.HomeScreen
import com.mavuno.church.ui.screens.HybridWebScreen
import com.mavuno.church.ui.screens.SermonsScreen
import com.mavuno.church.ui.screens.SplashScreen
import com.mavuno.church.ui.screens.TodayScreen
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.MavunoTheme

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
}

sealed class BottomTab(
    val route: String,
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
) {
    object Home : BottomTab("tab_home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Today : BottomTab("tab_today", "Today", Icons.Filled.GridView, Icons.Outlined.GridView)
    object Sermons : BottomTab("tab_sermons", "Sermons", Icons.Filled.MenuBook, Icons.Outlined.MenuBook)
    object Guard : BottomTab("tab_guard", "Guard", Icons.Filled.Shield, Icons.Outlined.Shield)
    object Web : BottomTab("tab_web", "Web", Icons.Filled.Language, Icons.Outlined.Language)
    object Events : BottomTab("tab_events", "Events", Icons.Filled.Event, Icons.Outlined.Event)
    object Give : BottomTab("tab_give", "Give", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
}

val bottomTabs = listOf(
    BottomTab.Home,
    BottomTab.Today,
    BottomTab.Sermons,
    BottomTab.Guard,
    BottomTab.Web,
    BottomTab.Give
)

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            MainContainer()
        }
    }
}

@Composable
fun MainContainer() {
    var selectedTab by remember { mutableStateOf<BottomTab>(BottomTab.Home) }

    val isScanning by EllaOverlayManager.isScanning.collectAsState()
    val isShieldActive by EllaOverlayManager.isShieldActive.collectAsState()
    val activeShieldScanResult by EllaOverlayManager.activeShieldScanResult.collectAsState()
    val currentAgeTier by EllaOverlayManager.currentAgeTier.collectAsState()
    val isEllaVisible by EllaOverlayManager.isEllaVisible.collectAsState()
    val ellaResponse by EllaOverlayManager.ellaResponse.collectAsState()
    val isEllaThinking by EllaOverlayManager.isEllaThinking.collectAsState()
    val isHomeScreenSimulation by EllaOverlayManager.isHomeScreenSimulation.collectAsState()

    Scaffold(
        containerColor = MavunoTheme.colors.background,
        bottomBar = {
            MavunoBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { newTab ->
                    if (newTab != selectedTab) {
                        AudioEffectsManager.playTabSwitch()
                        selectedTab = newTab
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            // Main Screen Content with Smooth Animated Transition
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) +
                            slideInHorizontally(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            ) { width -> width / 12 } togetherWith
                            fadeOut(animationSpec = tween(180)) +
                            slideOutHorizontally(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            ) { width -> -width / 12 }
                },
                label = "tab_screen_transition"
            ) { targetTab ->
                when (targetTab) {
                    BottomTab.Home -> HomeScreen(
                        onNavigateToTab = { tabName ->
                            AudioEffectsManager.playTabSwitch()
                            when (tabName) {
                                "Today" -> selectedTab = BottomTab.Today
                                "Sermons" -> selectedTab = BottomTab.Sermons
                                "Events" -> selectedTab = BottomTab.Events
                                "Give" -> selectedTab = BottomTab.Give
                                "Guard" -> selectedTab = BottomTab.Guard
                                "Web" -> selectedTab = BottomTab.Web
                                else -> selectedTab = BottomTab.Home
                            }
                        }
                    )
                    BottomTab.Today -> TodayScreen()
                    BottomTab.Sermons -> SermonsScreen()
                    BottomTab.Guard -> GuardScreen()
                    BottomTab.Web -> HybridWebScreen()
                    BottomTab.Events -> EventsScreen()
                    BottomTab.Give -> GiveScreen()
                }
            }

            // Quick Floating Ella Trigger FAB
            if (!isEllaVisible && !isShieldActive && !isHomeScreenSimulation) {
                FloatingActionButton(
                    onClick = {
                        AudioEffectsManager.playEllaChime()
                        EllaOverlayManager.triggerEllaAssistant()
                    },
                    containerColor = BrandOrange,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp, end = 16.dp)
                        .testTag("floating_ella_trigger_fab")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = "Ask Ella",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text("Ella ✨", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            // Live Pulsing Gradient Border Overlay during real-time scan
            PulsingScanBorder(isScanning = isScanning)

            // Glassmorphic Shield Overlay on inappropriate content
            GlassmorphicShieldOverlay(
                isShieldActive = isShieldActive,
                scanResult = activeShieldScanResult,
                ageTier = currentAgeTier,
                onDismiss = { EllaOverlayManager.dismissShield() }
            )

            // Floating Ella Assistant Card & Pill Stack
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
            ) {
                EllaAssistantCard(
                    isVisible = isEllaVisible && !isHomeScreenSimulation,
                    ageTier = currentAgeTier,
                    response = ellaResponse,
                    isThinking = isEllaThinking,
                    onAsk = { EllaOverlayManager.askElla(it) },
                    onDismiss = { EllaOverlayManager.dismissElla() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            // Full Home Screen & Apps Overlay Simulation Mode
            HomeScreenSimulationView(
                isVisible = isHomeScreenSimulation,
                ageTier = currentAgeTier,
                isEllaVisible = isEllaVisible,
                ellaResponse = ellaResponse,
                isEllaThinking = isEllaThinking,
                onCloseSimulation = {
                    AudioEffectsManager.playTap()
                    EllaOverlayManager.toggleHomeScreenSimulation(false)
                }
            )
        }
    }
}

@Composable
fun MavunoBottomNavigation(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp, spotColor = Color(0x140F172A))
            .background(MavunoTheme.colors.surface)
            .navigationBarsPadding()
            .height(68.dp)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomTabs.forEach { tab ->
                val isSelected = tab == selectedTab

                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()

                val tabScale by animateFloatAsState(
                    targetValue = if (isPressed) 0.85f else if (isSelected) 1.05f else 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "tab_scale"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .scale(tabScale)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = ripple(bounded = false, radius = 24.dp),
                            onClick = { onTabSelected(tab) }
                        )
                        .testTag("tab_${tab.route}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MavunoTheme.colors.primarySoft else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) tab.filledIcon else tab.outlinedIcon,
                            contentDescription = tab.title,
                            tint = if (isSelected) BrandOrange else MavunoTheme.colors.textMuted,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Text(
                        text = tab.title,
                        fontSize = 9.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) BrandOrange else MavunoTheme.colors.textMuted,
                        letterSpacing = 0.3.sp,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
            }
        }
    }
}
