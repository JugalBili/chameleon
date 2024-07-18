package cs446.project.chameleon

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel()
) {

    val bitmaps by mainViewModel.bitmaps.collectAsState()

    val paints = mutableListOf<Paint>()
    paints.add(Paint("Id1", "PPG", "Viva La Bleu", listOf(151, 190, 226)))
    paints.add(Paint("Id2", "PPG", "Calypso Berry", listOf(197, 58, 75)))
    paints.add(Paint("Id3", "Benjamin Moore", "Blue Pearl", listOf(147, 160, 189)))
    paints.add(Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)))

    val items = mutableListOf<Item>()
    for (bitmap in bitmaps) {
        items.add(Item(bitmap, listOf(Color(0xFFD0BCFF))))
    }


    Scaffold(
        content = { padding ->
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "Firstname Lastname",
                    modifier = Modifier.padding(48.dp)

                )

                Text(
                    text = "Favourites",
                    modifier = Modifier.padding(48.dp)

                )

                Card(modifier = Modifier
                    .padding(16.dp)
                    .width(400.dp)) {
                    Column(modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        LazyRow(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            items(items = paints.chunked(1)) { paintsPerRow ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    for (paint in paintsPerRow) {
                                        PaintItem1(paint = paint)
                                        Spacer(modifier = Modifier.width(16.dp))
                                    }
                                }
                            }
                        }

                    }
                }

                Text(
                    text = "History",
                    modifier = Modifier.padding(48.dp)

                )

//                PhotoBottomSheetContent(
//                    bitmaps = bitmaps,
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    onPhotoClick = { bitmap ->
//
//                    }
//                )
                LazyColumnExample(items)
            }
        },
        bottomBar = {
            NavBar(navController)
        }
    )
}

@Composable
fun PaintItem1(
    paint: Paint
) {
    val isSelected = remember { mutableStateOf(false) }
    val colour = Color(red = paint.rgb[0], green = paint.rgb[1], blue = paint.rgb[2])
    val borderColour = if (isSelected.value) Color.Green else Color.Transparent

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .size(200.dp)
            .background(colour)
            .clickable {
            }
            .border(width = 2.dp, color = borderColour, shape = RectangleShape)
        )
        Text(text = paint.brand + ": " + paint.name)
    }
}
