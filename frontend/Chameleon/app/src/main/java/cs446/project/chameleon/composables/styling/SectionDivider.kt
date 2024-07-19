package cs446.project.chameleon.composables.styling

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SectionDivider() {

    HorizontalDivider(
        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
        thickness = 3.dp,
        color = Color.Black
    )
}
