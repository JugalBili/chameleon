package cs446.project.chameleon.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val HEADER = "Header"
const val SUBHEADER = "Subheader"
const val BODY = "Body"

val smallSpacing = Modifier.padding(16.dp)

fun fontSize(type: String): TextUnit {
    return when (type) {
        HEADER -> 32.sp
        SUBHEADER -> 22.sp
        BODY -> 14.sp
        else -> 14.sp
    }
}
