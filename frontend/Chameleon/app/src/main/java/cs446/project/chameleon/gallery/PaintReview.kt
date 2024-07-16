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
import cs446.project.chameleon.Paint

@Composable
fun PaintReview(
    navController: NavHostController, // TODO: do we need this?
    paint: Paint
) {

    // TODO: hard-coded paints for now
    val paints = mutableListOf<Paint>()
    paints.add(Paint("Id1", "PPG", "Viva La Bleu", listOf(151, 190, 226)))
    paints.add(Paint("Id2", "PPG", "Calypso Berry", listOf(197, 58, 75)))
    paints.add(Paint("Id3", "Benjamin Moore", "Blue Pearl", listOf(147, 160, 189)))
    paints.add(Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)))
    for (i in 1..100) {
        paints.add(Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)))
    }

    // Handle search filters
    var searchQuery by remember { mutableStateOf("") }
    val filteredPaints = if (searchQuery.isEmpty()) {
        paints
    } else {
        paints.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        // Page Content
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
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                    thickness = 3.dp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

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
                    paints = filteredPaints
                )
            }
        },

        // BOTTOMBAR: navigation between sections
        bottomBar = {
            // TODO: add reusable composable here for the buttons to navigate between sections
        }
    )
}