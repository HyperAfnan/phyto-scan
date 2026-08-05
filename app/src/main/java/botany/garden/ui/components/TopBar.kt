package botany.garden.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import botany.garden.ui.theme.Ink
import botany.garden.ui.theme.Paper92Alpha

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        IconButton(
            onClick = { },
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Paper92Alpha),
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Ink,
                modifier = Modifier.size(17.dp),
            )
        }
    }
}
