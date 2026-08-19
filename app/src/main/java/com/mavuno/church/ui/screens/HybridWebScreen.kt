package com.mavuno.church.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.guard.EllaOverlayManager
import com.mavuno.church.ui.components.ScreenHeader
import com.mavuno.church.ui.components.WebContainerView
import com.mavuno.church.ui.theme.BrandGold
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.MavunoTheme

@Composable
fun HybridWebScreen(
    modifier: Modifier = Modifier
) {
    var webTitle by remember { mutableStateOf("Mavuno Hybrid Web Host") }
    val isGuardActive by EllaOverlayManager.isGuardActive.collectAsState()
    val ageTier by EllaOverlayManager.currentAgeTier.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MavunoTheme.colors.background)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
            ScreenHeader(
                title = "Hybrid Web Portal",
                subtitle = "Hosting mavuno-app with two-way @JavascriptInterface bridge",
                actionIcon = Icons.Filled.AutoAwesome,
                onActionClick = { EllaOverlayManager.triggerEllaAssistant("🛡️ Checking web page safety...") }
            )

            // Bridge Indicator Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MavunoTheme.colors.surfaceElevated)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isGuardActive) Color(0xFF10B981) else BrandGold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isGuardActive) "Bridge Online · ${ageTier.title}" else "Guard Paused",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MavunoTheme.colors.textPrimary
                    )
                }

                Text(
                    text = "MavunoBridge.js Active",
                    fontSize = 10.sp,
                    color = BrandOrange,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Web Container View
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            WebContainerView(
                onPageTitleChanged = { title ->
                    webTitle = title
                }
            )
        }
    }
}
