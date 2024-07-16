package cs446.project.chameleon.gallery

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cs446.project.chameleon.Paint

@Composable
fun PaintCard(
    paint: Paint,
    onClick: (String) -> Unit
) {
    val isSelected = remember { mutableStateOf(false) }
    val colour = Color(red = paint.rgb[0], green = paint.rgb[1], blue = paint.rgb[2])
    val borderColour = if (isSelected.value) Color.Green else Color.Transparent

    Box(
        modifier = Modifier
            .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .height(175.dp).width(150.dp)
    ) {

        Column(Modifier.fillMaxSize()) {

            // Colour
            Box(modifier = Modifier.fillMaxWidth().weight(1f).background(colour))

            // Text
            Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp)) {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = paint.name, fontSize = 14.sp)
                    Text(text = paint.id, fontSize = 12.sp) // TODO: change this to paint code
                    Text(text = paint.brand, fontSize = 12.sp)
                }
            }
        }
    }


//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Box(modifier = Modifier
//            .size(100.dp)
//            .background(colour)
//            .clickable {
//                //onClick(paint.id)
//                // TODO: change this to pass smth else, this is just for demo
//                onClick(paint.name)
//                isSelected.value = !isSelected.value
//            }
//            .border(width = 2.dp, color = borderColour, shape = RectangleShape)
//        )
//        Text(text = paint.brand + ": " + paint.name)
//    }
}