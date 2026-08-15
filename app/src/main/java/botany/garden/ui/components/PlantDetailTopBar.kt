package botany.garden.ui.components

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import botany.garden.ui.theme.Ink
import botany.garden.ui.theme.Moss
import botany.garden.ui.theme.Paper
import botany.garden.ui.theme.Paper92Alpha

@Composable
fun PlantDetailTopBar(
    plantName: String,
    speakText: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
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

    Surface(
        color = Paper,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Paper92Alpha)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Ink
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = plantName,
                style = MaterialTheme.typography.headlineMedium,
                color = Ink
            )
            
            if (speakText.isNotBlank()) {
                IconButton(
                    onClick = { tts?.speak(speakText, TextToSpeech.QUEUE_FLUSH, null, null) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = "Pronounce",
                        tint = Moss
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp)) // To balance the back button
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}
