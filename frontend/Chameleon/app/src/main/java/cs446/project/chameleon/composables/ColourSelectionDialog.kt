package cs446.project.chameleon.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cs446.project.chameleon.data.model.Paint


@Composable
fun ColourSelectionDialog(
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    onClick: (String) -> Unit
) {

    // TODO: hard-coded paints for now
    val paints = mutableListOf<Paint>()
    paints.add(Paint("Id1", "PPG", "Viva La Bleu", listOf(151, 190, 226)))
    paints.add(Paint("Id2", "PPG", "Calypso Berry", listOf(197, 58, 75)))
    paints.add(Paint("Id3", "Benjamin Moore", "Blue Pearl", listOf(147, 160, 189)))
    paints.add(Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)))

    Dialog(onDismissRequest = { onClose() }) {
        Card(modifier = Modifier
            .padding(16.dp)
            .width(300.dp)) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                LazyColumn(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    items(items = paints.chunked(1)) { paintsPerRow ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            for (paint in paintsPerRow) {
                                PaintItem(paint = paint, onClick = onClick)
                            }
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

@Composable
fun PaintItem(
    paint: Paint,
    onClick: (String) -> Unit
) {
    val isSelected = remember { mutableStateOf(false) }
    val colour = Color(red = paint.rgb[0], green = paint.rgb[1], blue = paint.rgb[2])
    val borderColour = if (isSelected.value) Color.Green else Color.Transparent

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .size(100.dp)
            .background(colour)
            .clickable {
                //onClick(paint.id)
                // TODO: change this to pass smth else, this is just for demo
                onClick(paint.name)
                isSelected.value = !isSelected.value
            }
            .border(width = 2.dp, color = borderColour, shape = RectangleShape)
        )
        Text(text = paint.brand + ": " + paint.name)
    }
}
