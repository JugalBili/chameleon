package cs446.project.chameleon.composables.styling

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun SectionTitle(title: String) {

    Text(text = title, fontSize = 32.sp)
}
