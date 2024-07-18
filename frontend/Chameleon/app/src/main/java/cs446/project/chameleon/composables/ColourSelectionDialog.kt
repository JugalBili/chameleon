package cs446.project.chameleon.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cs446.project.chameleon.composables.PaintCard
import cs446.project.chameleon.data.model.Paint
import cs446.project.chameleon.gallery.PaintGallery


@Composable
fun ColourSelectionDialog(
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    onClick: (Paint) -> Unit,
    paintViewModel: MainViewModel
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Title
                    Text(text = "Select up to 4 colours", fontSize = 32.sp)
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

                    // Favourite paints
                    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.3f)) {
                        LazyHorizontalGrid(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            rows = GridCells.Fixed(1),
                            contentPadding = PaddingValues(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(favouritedPaints) { paint ->
                                PaintCard(
                                    paint = paint,
                                    onClick = onClick,
                                    toggleBorder = true
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // All paints
                    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f)) {
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

                    Button(
                        onClick = { onSubmit() }
                    ) {
                        Text("Submit")
                    }
                }
            }
    }
}

//@Composable
//fun PaintItem(
//    paint: Paint,
//    onClick: (String) -> Unit
//) {
//    val isSelected = remember { mutableStateOf(false) }
//    val colour = Color(red = paint.rgb[0], green = paint.rgb[1], blue = paint.rgb[2])
//    val borderColour = if (isSelected.value) Color.Green else Color.Transparent
//
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Box(modifier = Modifier
//            .size(100.dp)
//            .background(colour)
//            .clickable {
//                //onClick(paint.id)
//                // TODO: change this to pass smth else, this is just for demo
//                onClick(paint.name)
//                isSelected.value = !isSelected.value
//            }
//            .border(width = 2.dp, color = borderColour, shape = RectangleShape)
//        )
//        Text(text = paint.brand + ": " + paint.name)
//    }
//}
