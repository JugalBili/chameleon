package cs446.project.chameleon.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cs446.project.chameleon.composables.styling.CenteredColumn
import cs446.project.chameleon.composables.styling.ChameleonDivider
import cs446.project.chameleon.composables.styling.ChameleonText
import cs446.project.chameleon.data.model.History
import cs446.project.chameleon.utils.SUBHEADER
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavHostController
import cs446.project.chameleon.composables.styling.ColouredBox
import cs446.project.chameleon.data.viewmodel.ImageViewModel
import cs446.project.chameleon.data.viewmodel.UIHistory
import cs446.project.chameleon.utils.getColour

@Composable
fun HistoryRows(
    navController: NavHostController,
    history: List<UIHistory>,
    imageViewModel: ImageViewModel
) {
    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        CenteredColumn(modifier = Modifier.padding(16.dp), centerHorizontally = false) {

            ChameleonText("History", SUBHEADER)
            ChameleonDivider()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(history) { item ->
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                            .padding(8.dp)
                            .clickable {
                                imageViewModel.onHistoryRowClick(item)
                                navController.navigate("image_result_screen")},
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item {
                            Image(
                                bitmap = item.baseImage.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Start)
                            )
                        }

                        items(item.colors) { color ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(4.dp)
                                    .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                                    .background(Color.Transparent)
                            ) {
                                Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)).background(Color(color.rgb.r, color.rgb.g, color.rgb.b)))
                            }
                        }
                    }
                }
            }
        }
    }
}