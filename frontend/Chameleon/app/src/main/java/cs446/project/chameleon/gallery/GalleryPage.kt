package cs446.project.chameleon.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cs446.project.chameleon.MainViewModel
import cs446.project.chameleon.composables.NavBar
import cs446.project.chameleon.composables.PaintGallery

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Title
                Text(text = "Paint Gallery", fontSize = 32.sp)
                HorizontalDivider(
                    modifier = Modifier.padding(top = 12.dp, bottom = 26.dp),
                    thickness = 3.dp,
                    color = Color.Black
                )

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Paint Gallery
                PaintGallery(
                    paints = filteredPaints,
                    navController = navController,
                    mainViewModel = mainViewModel
                )
            }
        },
        bottomBar = {
            NavBar(navController = navController)
        }
    )
}
