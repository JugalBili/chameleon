package cs446.project.chameleon.composables.styling

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CenteredRow(
    modifier: Modifier = Modifier,
    fullWidth: Boolean = true,
    centerHorizontally: Boolean = true,
    centerVertically: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {

    Row (
        modifier = modifier.then(if (fullWidth) Modifier.fillMaxWidth() else Modifier),
        verticalAlignment = if (centerVertically) {
            Alignment.CenterVertically
        } else {
            Alignment.Top
        },
        horizontalArrangement = if (centerHorizontally) {
            Arrangement.Center
        } else {
            Arrangement.Start
        },
        content = content
    )
}
