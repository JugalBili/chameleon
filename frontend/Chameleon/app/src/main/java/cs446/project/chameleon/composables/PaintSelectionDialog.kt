package cs446.project.chameleon.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import cs446.project.chameleon.composables.styling.CenteredRow
import cs446.project.chameleon.composables.styling.ChameleonDivider
import cs446.project.chameleon.composables.styling.ChameleonText
import cs446.project.chameleon.composables.styling.ColouredBox
import cs446.project.chameleon.composables.styling.Dropdown
import cs446.project.chameleon.composables.styling.ExpandableSection
import cs446.project.chameleon.composables.styling.PrimaryButton
import cs446.project.chameleon.composables.styling.SearchBox
import cs446.project.chameleon.utils.PAINT_SELECTION_OPTIONS
import cs446.project.chameleon.utils.getColour
import cs446.project.chameleon.utils.smallSpacing

@Composable
fun PaintSelectionDialog(
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    onClick: (Paint) -> Unit,
    paintViewModel: PaintViewModel
) {
    val paints by paintViewModel.paints.collectAsState()
    val favouritePaints = paints // TODO: implement actual favourite system

    var paintGroup by remember { mutableStateOf(PAINT_SELECTION_OPTIONS[0]) }


    Dialog(
        properties = DialogProperties( usePlatformDefaultWidth = false ),
        onDismissRequest = { onClose() }
    ) {
        Card(modifier = Modifier.fillMaxWidth(0.9f)) {
            CenteredColumn(padding = PaddingValues(vertical = 12.dp), centerVertically = false) {

                // Title
                ChameleonText("Select up to 4 colours", HEADER)
                ChameleonDivider()

                // Row of selected colours
                CenteredRow(padding = PaddingValues(8.dp), centerHorizontally = false) {
                    Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                        ChameleonText("Selected\nPaints: ")
                    }
                    paintViewModel.selectedPaints.forEach { paint ->
                        ColouredBox(
                            getColour(paint.rgb),
                            Modifier
                                .height(75.dp)
                                .width(75.dp)
                                .padding(horizontal = 8.dp)
                        )
                    }
                }

                // Paint choices
                Dropdown(
                    options = PAINT_SELECTION_OPTIONS,
                    updateSelectedOption = { group ->
                        paintGroup = group
                    }
                )
                Spacer(modifier = smallSpacing)
                PaintGrid(
                    paints = if (paintGroup == "Favourites") {
                        favouritePaints
                    } else {
                        paints.filter { it.labelHSL.contains(paintGroup, ignoreCase = true) }
                    },
                    onPaintClick = { paint ->
                        paintViewModel.updateSelectedPaints(paint)
                    }
                )
                Spacer(modifier = smallSpacing)

                PrimaryButton("Submit", onSubmit)
            }
        }
    }
}
