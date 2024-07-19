package cs446.project.chameleon.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cs446.project.chameleon.MainViewModel
import cs446.project.chameleon.constants.SUBHEADER
import cs446.project.chameleon.composables.styling.CenteredColumn
import cs446.project.chameleon.composables.styling.ChameleonDivider
import cs446.project.chameleon.composables.styling.ChameleonText
import cs446.project.chameleon.data.model.Paint

@Composable
fun FavouritesBar(
    paints: List<Paint>,
    onClick: (Paint) -> Unit
    // TODO: maybe get the favourites list from UserViewModel
) {

    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.3f)) {
        CenteredColumn(padding = PaddingValues(16.dp), centerHorizontally = false) {

            ChameleonText("Favourites", SUBHEADER)
            ChameleonDivider()

            LazyHorizontalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                rows = GridCells.Fixed(1),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(paints) { paint ->
                    PaintCard(
                        paint = paint,
                        onClick = onClick,
                        toggleBorder = true
                    )
                }
            }
        }
    }
}
