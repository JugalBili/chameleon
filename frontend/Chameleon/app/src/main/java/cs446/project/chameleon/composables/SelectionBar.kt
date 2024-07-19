package cs446.project.chameleon.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import cs446.project.chameleon.R
import cs446.project.chameleon.processImage
import kotlinx.coroutines.launch

@Composable
fun SelectionBar(
    onAddClick: () -> Unit,
    onProcessClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(84, 139, 227))
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onAddClick
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_box_24),
                    contentDescription = "Plus Icon",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(7f))
            IconButton(
                onClick = onProcessClick
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_check_box_24),
                    contentDescription = "Checkmark Icon",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}