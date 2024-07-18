package cs446.project.chameleon

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

data class Item(
    val bitmap: Bitmap,
    val colors: List<Color>
)

@Composable
fun LazyColumnExample(items: List<Item>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {  }
                    .border(2.dp, Color.Black)
                    .padding(8.dp)
            ) {
                Image(
                    bitmap = item.bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(end = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    item.colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(color)
                        )
                    }
                }
            }
        }
    }
}