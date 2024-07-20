package cs446.project.chameleon.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import cs446.project.chameleon.data.model.RGB
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getColour(rgb: RGB): Color {
    return Color(red = rgb.r, green = rgb.g, blue = rgb.b)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimestamp(timestamp: Instant): String {
    return DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT).withZone(ZoneId.systemDefault()).format(timestamp)
}
