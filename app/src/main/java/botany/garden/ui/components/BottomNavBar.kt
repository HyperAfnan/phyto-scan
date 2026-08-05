package botany.garden.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.scale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.ui.theme.MossDark
import botany.garden.ui.theme.NavInactive
import botany.garden.ui.theme.Oleander
import botany.garden.ui.theme.Paper94Alpha

@Composable
fun BottomNavBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier.clip(RoundedCornerShape(26.dp)),
        containerColor = Paper94Alpha,
        contentColor = Color.Transparent,
    ) {
        NavItem(selectedIndex == 2, { onTabSelected(2) }, Icons.Outlined.QrCodeScanner, "Scan")
        NavItem(selectedIndex == 1, { onTabSelected(1) }, Icons.Outlined.LocalFlorist, "Garden")
        NavItem(selectedIndex == 0, { onTabSelected(0) }, Icons.Outlined.Explore, "Explore")
    }
}

@Composable
private fun RowScope.NavItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (selected) 1.02f else 1f,
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.9f),
        label = "navScale",
    )

    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Oleander else NavInactive,
                modifier = Modifier.size(20.dp).scale(animatedScale),
            )
        },
        label = {
            Text(
                text = label,
                fontFamily = FontFamily.Monospace,
                fontSize = 8.5.sp,
                letterSpacing = (0.04 * 8.5).sp,
                color = if (selected) MossDark else NavInactive,
            )
        },
        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
    )
}
