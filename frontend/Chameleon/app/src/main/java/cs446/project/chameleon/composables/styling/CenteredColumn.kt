package cs446.project.chameleon.composables.styling

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CenteredColumn(
    modifier: Modifier = Modifier,
    fullWidth: Boolean = true,
    centerHorizontally: Boolean = true,
    centerVertically: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {

    Column(
        modifier = modifier.then(if (fullWidth) Modifier.fillMaxWidth() else Modifier),
        horizontalAlignment = if (centerHorizontally) {
            Alignment.CenterHorizontally
        } else {
            Alignment.Start
        },
        verticalArrangement = if (centerVertically) {
            Arrangement.Center
        } else {
            Arrangement.Top
        },
        content = content
    )
}
