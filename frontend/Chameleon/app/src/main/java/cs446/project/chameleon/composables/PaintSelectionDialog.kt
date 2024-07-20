package cs446.project.chameleon.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cs446.project.chameleon.PaintViewModel
import cs446.project.chameleon.data.model.Paint
import cs446.project.chameleon.utils.HEADER
import cs446.project.chameleon.composables.styling.CenteredColumn
import cs446.project.chameleon.composables.styling.ChameleonDivider
import cs446.project.chameleon.composables.styling.ChameleonText
import cs446.project.chameleon.composables.styling.PrimaryButton
import cs446.project.chameleon.composables.styling.SearchBox

@Composable
fun PaintSelectionDialog(
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    onClick: (Paint) -> Unit,
    paintViewModel: PaintViewModel
) {
    val paints by paintViewModel.paints.collectAsState()
    val favouritedPaints = paints // TODO: implement actual favouriting system

    // Handle search filters
    var searchQuery by remember { mutableStateOf("") }
    val filteredPaints = if (searchQuery.isEmpty()) {
        paints
    } else {
        paints.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Dialog(
        properties = DialogProperties( usePlatformDefaultWidth = false ),
        onDismissRequest = { onClose() }
    ) {
        Card(modifier = Modifier.fillMaxWidth(0.9f)) {
            CenteredColumn(padding = PaddingValues(vertical = 12.dp), centerVertically = false) {

                // Title
                ChameleonText("Select up to 4 colours", HEADER)
                ChameleonDivider()

                // Search field
                SearchBox(searchQuery, onChange = { update -> searchQuery = update })
                Spacer(modifier = Modifier.height(32.dp))

                // Favourite paints
                FavouritesBar(favouritedPaints, onClick)
                Spacer(modifier = Modifier.height(16.dp))

                // All paints
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)) {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredPaints) { paint ->
                            PaintCard(
                                paint = paint,
                                onClick = onClick,
                                toggleBorder = true
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton("Submit", onSubmit)
            }
        }
    }
}
