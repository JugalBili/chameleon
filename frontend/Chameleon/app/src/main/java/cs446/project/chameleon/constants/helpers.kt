package cs446.project.chameleon.constants

import androidx.compose.ui.graphics.Color
import cs446.project.chameleon.data.model.RGB

fun getColour(rgb: RGB): Color {
    return Color(red = rgb.r, green = rgb.g, blue = rgb.b)
}
