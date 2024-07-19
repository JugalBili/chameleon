package cs446.project.chameleon.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cs446.project.chameleon.MainViewModel
import cs446.project.chameleon.composables.PaintGallery
import cs446.project.chameleon.constants.HEADER
import cs446.project.chameleon.composables.styling.CenteredColumn
import cs446.project.chameleon.composables.styling.CenteredRow
import cs446.project.chameleon.composables.styling.ChameleonDivider
import cs446.project.chameleon.composables.styling.ChameleonText
import cs446.project.chameleon.composables.styling.Dropdown
import cs446.project.chameleon.composables.styling.Screen
import cs446.project.chameleon.composables.styling.SearchBox
import cs446.project.chameleon.constants.BODY

@Composable
fun GalleryPage(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val paints by mainViewModel.paints.collectAsState()

    // Handle search filters
    val filters = listOf("Name", "Brand")
    var filter by remember { mutableStateOf(filters[0]) }
    var filterQuery by remember { mutableStateOf("") }

    val filteredPaints = if (filter == filters[0]) {
        paints.filter { it.name.contains(filterQuery, ignoreCase = true) }
    } else if (filter == filters[1]) {
        paints.filter { it.brand.contains(filterQuery, ignoreCase = true) }
    } else {
        paints
    }

    Screen(navController) { padding ->
        CenteredColumn(padding = padding) {

            // Title
            ChameleonText("Gallery Page", HEADER)
            ChameleonDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Search field
            CenteredRow() {
                Box(modifier = Modifier.fillMaxWidth(0.3f).padding(end = 8.dp)) {
                    Dropdown(filters, updateSelectedOption = { option -> filter = option })
                }
                SearchBox(filterQuery, onChange = { update -> filterQuery = update })
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Paint Gallery
            PaintGallery(
                paints = filteredPaints,
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
    }
}
