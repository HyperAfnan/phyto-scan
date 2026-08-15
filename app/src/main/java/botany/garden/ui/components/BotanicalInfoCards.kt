package botany.garden.ui.components

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import botany.garden.ui.theme.CardBg
import botany.garden.ui.theme.Moss
import botany.garden.ui.theme.SubText

@Composable
fun BotanicalInfoCards(botanicalName: String, family: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        lateinit var textToSpeech: TextToSpeech
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val localeIn = java.util.Locale("en", "IN")
                val result = textToSpeech.setLanguage(localeIn)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    textToSpeech.setLanguage(java.util.Locale.ENGLISH)
                }
            }
        }
        tts = textToSpeech
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedCard(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = CardBg),
            modifier = Modifier
                .weight(1f)
                .clickable(role = Role.Button) {
                    tts?.speak(botanicalName, TextToSpeech.QUEUE_FLUSH, null, "botanical-name")
                },
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = botanicalName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Scientific Name",
                        style = MaterialTheme.typography.labelSmall,
                        color = SubText
                    )
                    Icon(
                        imageVector = Icons.Outlined.VolumeUp,
                        contentDescription = "Speak scientific name",
                        tint = Moss,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }

        OutlinedCard(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = CardBg),
            modifier = Modifier
                .weight(1f)
                .clickable(role = Role.Button) {
                    tts?.speak(family, TextToSpeech.QUEUE_FLUSH, null, "botanical-family")
                },
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = family,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Botanical Family",
                        style = MaterialTheme.typography.labelSmall,
                        color = SubText
                    )
                    Icon(
                        imageVector = Icons.Outlined.VolumeUp,
                        contentDescription = "Speak botanical family",
                        tint = Moss,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
    }
}
