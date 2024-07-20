package cs446.project.chameleon.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cs446.project.chameleon.PaintViewModel
import cs446.project.chameleon.data.model.Paint

@Composable
fun PaintGrid(
    paints: List<Paint>,
    maxHeight: Float = 0.5f,
    onPaintClick: (Paint) -> Unit,
    paintViewModel: PaintViewModel? = null
) {

    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(maxHeight)) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(paints) { paint ->
                PaintCard(
                    paint = paint,
                    onClick = onPaintClick,
                    selected = if (paintViewModel != null) {
                        paint in paintViewModel.selectedPaints
                    } else {
                        false
                    }
                )
            }
        }
    }
}
