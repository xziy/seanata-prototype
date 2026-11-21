package seanata.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import seanata.data.*
import seanata.res.*
import seanata.theme.Sea
import seanata.ui.components.*

import org.jetbrains.compose.resources.DrawableResource

/** The photo behind the top of a screen: navy at the top for the bar, fading into the page below. */
@Composable
fun Backdrop(
    photo: DrawableResource,
    height: Int,
    fade: Boolean = true,
    alignment: Alignment = Alignment.Center,
) {
    Box(Modifier.fillMaxWidth().height(height.dp)) {
        Photo(photo, Modifier.matchParentSize(), alignment)
        if (fade) {
            Scrim(0f to Sea.Navy.copy(alpha = 0.92f), 0.45f to Sea.Navy.copy(alpha = 0.6f), 0.8f to Sea.Bg.copy(alpha = 0.85f), 1f to Sea.Bg, modifier = Modifier.matchParentSize())
        } else {
            Scrim(0f to Sea.Navy.copy(alpha = 0.9f), 0.3f to Sea.Navy.copy(alpha = 0.15f), 0.48f to Sea.Navy.copy(alpha = 0.3f), 0.75f to Sea.NavyDeep.copy(alpha = 0.78f), 1f to Sea.NavyDeep.copy(alpha = 0.92f), modifier = Modifier.matchParentSize())
        }
    }
}

/** A plain titled block of text on a card. */
@Composable
fun TextCard(
    title: String,
    text: String,
) {
    Column(Modifier.fillMaxWidth().card().padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Txt(title, size = 19, weight = FontWeight.Bold, tracking = -0.3)
        Txt(text, size = 14, lineHeight = 21)
    }
}
