package cs446.project.chameleon.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cs446.project.chameleon.Paint

@Composable
fun PaintGallery(
    paints: List<Paint>
) {
    var selectedPaintId by remember { mutableStateOf("") }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(paints) { paint ->
            PaintCard(
                paint = paint,
                onClick = { paintId ->
                    selectedPaintId = paintId
                    //if (selectedPaintIds.contains(paintId)) {
                    //    selectedPaintIds.remove(paintId)
                    //} else {
                    //    selectedPaintIds.add(paintId)
                    //}
                }
            )
        }
    }
}