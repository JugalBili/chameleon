package cs446.project.chameleon.composables.styling

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cs446.project.chameleon.composables.constants.BODY
import cs446.project.chameleon.composables.constants.fontSize

@Composable
fun ChameleonText(text: String, type: String = BODY) {

    Text(text = text, fontSize = fontSize(type))
}
