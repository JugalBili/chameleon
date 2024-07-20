package cs446.project.chameleon.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cs446.project.chameleon.utils.getColour
import cs446.project.chameleon.data.model.Paint

@Composable
fun PaintCard(
    paint: Paint,
    onClick: (Paint) -> Unit,
    selected: Boolean = false
) {
    val borderColour = if (selected) {
        Color.Green
    } else {
        Color.Black
    }

    Box(
        modifier = Modifier
            .border(2.dp, borderColour, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .height(175.dp)
            .width(175.dp)
            .clickable(onClick = {
                onClick(paint)
            })
    ) {
        Column(Modifier.fillMaxSize()) {

            // Colour
            Box(modifier = Modifier.fillMaxWidth().weight(1f).background(getColour(paint.rgb)))

            // Paint details
            Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = paint.name, fontSize = 14.sp)
                    Text(text = paint.id, fontSize = 12.sp) // TODO: change this to paint code
                    Text(text = paint.brand, fontSize = 12.sp)
                }
            }
        }
    }
}
