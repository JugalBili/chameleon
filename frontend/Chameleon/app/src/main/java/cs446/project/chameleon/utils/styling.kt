package cs446.project.chameleon.utils

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

const val HEADER = "Header"
const val SUBHEADER = "Subheader"
const val BODY = "Body"

fun fontSize(type: String): TextUnit {
    return when (type) {
        HEADER -> 32.sp
        SUBHEADER -> 22.sp
        BODY -> 14.sp
        else -> 14.sp
    }
}
