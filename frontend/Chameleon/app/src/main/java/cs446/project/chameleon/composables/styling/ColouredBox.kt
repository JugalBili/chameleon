package cs446.project.chameleon.composables.styling

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColouredBox(
    colour: Color,
    modifier: Modifier = Modifier.height(100.dp).width(100.dp).padding(0.dp)
) {

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
            .background(Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)).background(colour))
    }
}
