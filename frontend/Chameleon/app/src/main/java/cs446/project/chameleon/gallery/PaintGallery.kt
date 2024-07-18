package cs446.project.chameleon.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cs446.project.chameleon.MainViewModel
import cs446.project.chameleon.composables.PaintCard
import cs446.project.chameleon.data.model.Paint

@Composable
fun PaintGallery(
    paints: List<Paint>,
    navController: NavHostController,
    mainViewModel: MainViewModel
) {

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(paints) { paint ->
            PaintCard(
                paint = paint,
                onClick = { paint ->
                    mainViewModel.updateSelectedPaint(paint)
                    navController.navigate("paint_review")
                }
            )
        }
    }
}
