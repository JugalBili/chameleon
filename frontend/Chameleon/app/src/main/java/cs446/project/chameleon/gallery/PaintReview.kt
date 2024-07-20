package cs446.project.chameleon.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cs446.project.chameleon.MainViewModel
import cs446.project.chameleon.PaintViewModel
import cs446.project.chameleon.composables.NavBar
import cs446.project.chameleon.composables.ReviewCard
import cs446.project.chameleon.composables.styling.CenteredColumn
import cs446.project.chameleon.composables.styling.ChameleonDivider
import cs446.project.chameleon.composables.styling.ColouredBox
import cs446.project.chameleon.composables.styling.PrimaryButton
import cs446.project.chameleon.composables.styling.Screen
import cs446.project.chameleon.constants.getColour
import cs446.project.chameleon.data.model.Review
import cs446.project.chameleon.data.model.ReviewOLD

@Composable
fun PaintReview(
    navController: NavHostController,
    mainViewModel: PaintViewModel
) {
    val paint = mainViewModel.getSelectedPaint() ?: return

    // TODO: get the default value of isLiked from user data
    var isLiked by remember { mutableStateOf(false) }

    // TODO: hard-coded values for now
    val reviews = listOf(
        ReviewOLD("John", "Doe", "2024-07-17", "ayo this slaps"),
        ReviewOLD("John", "Smith", "2024-07-17", "ayo this also slaps"),
        ReviewOLD("John", "UninspiredLastName", "2024-07-17", "uwu this colour looks soo good! Can’t" +
                " wait till I buy more of it and color my " +
                "entire house ")
    )

    Screen(navController) { padding ->
        CenteredColumn(padding = padding, centerVertically = false) {

            // Paint Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColouredBox(colour = getColour(paint.rgb))

                CenteredColumn(fullWidth = false, centerHorizontally = false) {
                    Text(text = paint.name, fontSize = 14.sp)
                    Text(text = paint.id, fontSize = 12.sp) // TODO: change this to paint code
                    Text(text = paint.brand, fontSize = 12.sp)
                }

                PrimaryButton(
                    text = "Try!",
                    onClick = { navController.navigate("camera_screen") }
                )

                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = { isLiked = !isLiked }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isLiked) Color.Red else Color.Black
                    )
                }
            }
            ChameleonDivider()

            // Paint reviews
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(reviews) { review ->
                    ReviewCard(review = review)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
