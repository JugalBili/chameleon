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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cs446.project.chameleon.data.model.Paint

@Composable
fun PaintCard(
    paint: Paint,
    onClick: (Paint) -> Unit,
    toggleBorder: Boolean = false
) {
    val colour = Color(red = paint.rgb[0], green = paint.rgb[1], blue = paint.rgb[2])
    var isSelected by remember { mutableStateOf(false) }
    val borderColour = if (isSelected && toggleBorder) {
        Color.Green
    } else {
        Color.Black
    }

//    val configuration = LocalConfiguration.current
//    val screenWidth = configuration.screenWidthDp.dp
//    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .border(2.dp, borderColour, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
//            .size(width = screenWidth / 5, height = screenHeight / 10)
            .height(175.dp)
            .width(175.dp)
            .clickable(onClick = {
                if (toggleBorder) {
                    isSelected = !isSelected
                }
                onClick(paint)
            })
    ) {
        Column(Modifier.fillMaxSize()) {

            // Colour
            Box(modifier = Modifier.fillMaxWidth().weight(1f).background(colour))

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
