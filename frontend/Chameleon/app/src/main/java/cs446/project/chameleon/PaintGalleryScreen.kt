package cs446.project.chameleon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cs446.project.chameleon.composables.PaintGrid
import cs446.project.chameleon.utils.HEADER
import cs446.project.chameleon.composables.styling.CenteredColumn
import cs446.project.chameleon.composables.styling.CenteredRow
import cs446.project.chameleon.composables.styling.ChameleonDivider
import cs446.project.chameleon.composables.styling.ChameleonText
import cs446.project.chameleon.composables.styling.Dropdown
import cs446.project.chameleon.composables.styling.Screen
import cs446.project.chameleon.composables.styling.SearchBox
import cs446.project.chameleon.utils.BRAND_FILTER
import cs446.project.chameleon.utils.COLOUR_FILTER
import cs446.project.chameleon.utils.NAME_FILTER

@Composable
fun PaintGalleryScreen(
    navController: NavHostController,
    paintViewModel: PaintViewModel
) {
    val paints by paintViewModel.paints.collectAsState()

    // Handle search filters
    val filters = listOf(NAME_FILTER, BRAND_FILTER, COLOUR_FILTER)
    var filter by remember { mutableStateOf(filters[0]) }
    var filterQuery by remember { mutableStateOf("") }
    var placeholderText by remember { mutableStateOf("Search by name (e.g. Light Sky)") }

    val filteredPaints = if (filter == NAME_FILTER) {
        paints.filter { it.name.contains(filterQuery, ignoreCase = true) }
    } else if (filter == BRAND_FILTER) {
        paints.filter { it.brand.contains(filterQuery, ignoreCase = true) }
    } else if (filter == COLOUR_FILTER) {
        paints.filter { it.labelHSL.contains(filterQuery, ignoreCase = true) }
    } else {
        paints
    }

    Screen(navController) { padding ->
        CenteredColumn(modifier = Modifier.padding(padding)) {

            // Title
            ChameleonText("Gallery Page", HEADER)
            ChameleonDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Search field
            CenteredRow() {
                Box(modifier = Modifier.fillMaxWidth(0.3f).padding(end = 8.dp)) {
                    Dropdown(
                        filters,
                        updateSelectedOption = { option ->
                            filter = option
                            filterQuery = ""
                            placeholderText = when (filter) {
                                NAME_FILTER -> "Search by name (e.g. Light Sky)"
                                BRAND_FILTER -> "Search by brand (e.g. PPG)"
                                COLOUR_FILTER -> "Search by colour (e.g. blue)"
                                else -> "Search"
                            }
                        }
                    )
                }
                SearchBox(
                    filterQuery,
                    placeholderText,
                    onChange = { update -> filterQuery = update }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            PaintGrid(
                paints = filteredPaints,
                onPaintClick = { paint ->
                    paintViewModel.updateSelectedPaint(paint)
                    navController.navigate("paint_review")
                }
            )
        }
    }
}
