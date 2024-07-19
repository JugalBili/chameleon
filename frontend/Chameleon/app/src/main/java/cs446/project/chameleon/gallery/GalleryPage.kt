package cs446.project.chameleon.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cs446.project.chameleon.MainViewModel
import cs446.project.chameleon.composables.NavBar
import cs446.project.chameleon.composables.PaintGallery
import cs446.project.chameleon.composables.styling.CenteredColumn
import cs446.project.chameleon.composables.styling.Screen
import cs446.project.chameleon.composables.styling.SearchBox
import cs446.project.chameleon.composables.styling.SectionDivider
import cs446.project.chameleon.composables.styling.SectionTitle

@Composable
fun GalleryPage(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val paints by mainViewModel.paints.collectAsState()

    // Handle search filters
    var searchQuery by remember { mutableStateOf("") }
    val filteredPaints = if (searchQuery.isEmpty()) {
        paints
    } else {
        paints.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Screen(navController) { padding ->
        CenteredColumn(padding = padding) {

            // Title
            SectionTitle("Paint Gallery")
            SectionDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Search field
            SearchBox(searchQuery, onChange = { update -> searchQuery = update })
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
