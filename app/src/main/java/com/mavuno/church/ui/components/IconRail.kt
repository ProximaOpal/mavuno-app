package com.mavuno.church.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FrontHand
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.BrandOrangeDeep

data class RailItem(
    val key: String,
    val icon: ImageVector,
    val description: String
)

val defaultRailItems = listOf(
    RailItem("live", Icons.Outlined.PlayCircleOutline, "Live Service"),
    RailItem("guard", Icons.Outlined.Shield, "Kids Guard"),
    RailItem("web", Icons.Outlined.Language, "Hybrid Web"),
    RailItem("sermons", Icons.Outlined.MenuBook, "Sermons"),
    RailItem("events", Icons.Outlined.Event, "Events"),
    RailItem("give", Icons.Outlined.FavoriteBorder, "Give"),
    RailItem("prayer", Icons.Outlined.FrontHand, "Prayer"),
    RailItem("chat", Icons.Outlined.ChatBubbleOutline, "Chat"),
    RailItem("contact", Icons.Outlined.Call, "Contact")
)

@Composable
fun IconRail(
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    items: List<RailItem> = defaultRailItems
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(BrandOrange, BrandOrangeDeep)
    )

    Box(
        modifier = modifier
            .width(56.dp)
            .fillMaxHeight()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp),
                spotColor = BrandOrangeDeep
            )
            .background(
                brush = gradient,
                shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)
            )
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items.forEach { item ->
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x24FFFFFF))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = Color.White),
                            onClick = { onSelect(item.key) }
                        )
                        .testTag("rail_item_${item.key}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.description,
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}
