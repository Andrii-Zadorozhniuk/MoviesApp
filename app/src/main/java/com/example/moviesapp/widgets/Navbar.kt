package com.example.moviesapp.widgets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NavBarItem(
    val icon: ImageVector,
    val label: String
)

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FloatingNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    items: List<NavBarItem>,

    opacity: Float = 0.7f,
    backgroundColor: Color = Color(0xFF1A1A1A),
    unselectedColor: Color = Color(0xFF666666),

    height: Dp = 52.dp,
    borderRadius: Dp = 35.dp,

    primaryColor: Color = Color(0xFF00D4FF),
    isFloating: Boolean = true,
    widthFactor: Float = 0.85f,
    showText: Boolean = false,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        BoxWithConstraints {

            val navBarWidth = maxWidth * widthFactor
            val itemWidth = navBarWidth / items.size

            val indicatorOffset by animateDpAsState(
                targetValue = itemWidth * selectedIndex,
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )

            Box(
                modifier = Modifier
                    .padding(
                        bottom = if (isFloating) 24.dp else 0.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .width(navBarWidth)
                    .height(height)
                    .clip(RoundedCornerShape(borderRadius))
                    .background(backgroundColor.copy(alpha = opacity))
                    .border(
                        width = 1.2.dp,
                        color = unselectedColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(borderRadius)
                    )
            ) {

                // Indicator (как AnimatedPositioned во Flutter)
                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .fillMaxHeight()
                        .width(itemWidth)
                        .padding(vertical = 6.dp, horizontal = 5.dp)
                        .background(
                            primaryColor.copy(alpha = 0.15f),
                            RoundedCornerShape(35.dp)
                        )
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    items.forEachIndexed { index, item ->

                        NavBarButton(
                            item = item,
                            isSelected = selectedIndex == index,
                            selectedColor = primaryColor,
                            unselectedColor = unselectedColor,
                            onTap = { onItemSelected(index) },
                            showText = showText,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavBarButton(
    item: NavBarItem,
    isSelected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    onTap: () -> Unit,
    showText: Boolean,
    modifier: Modifier = Modifier
) {

    val color = if (isSelected) selectedColor else unselectedColor

    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onTap() }
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = color,
            modifier = Modifier.size(22.dp)
        )

        Spacer(Modifier.height(3.dp))
        if (showText) {
            Text(
                text = item.label,
                fontSize = 11.sp,
                color = color,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }

    }
}