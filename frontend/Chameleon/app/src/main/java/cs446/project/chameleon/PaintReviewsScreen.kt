package cs446.project.chameleon

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cs446.project.chameleon.composables.ReviewCard
import cs446.project.chameleon.composables.styling.CenteredColumn
import cs446.project.chameleon.composables.styling.ChameleonDivider
import cs446.project.chameleon.composables.styling.ColouredBox
import cs446.project.chameleon.composables.styling.PrimaryButton
import cs446.project.chameleon.composables.styling.Screen
import cs446.project.chameleon.data.model.Paint
import cs446.project.chameleon.data.model.Review
import cs446.project.chameleon.data.viewmodel.ErrorViewModel
import cs446.project.chameleon.data.viewmodel.ImageViewModel
import cs446.project.chameleon.data.viewmodel.PaintViewModel
import cs446.project.chameleon.data.viewmodel.UserViewModel
import cs446.project.chameleon.utils.getColour
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaintReviewsScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    paintViewModel: PaintViewModel,
    imageViewModel: ImageViewModel,
    errorViewModel: ErrorViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val paint = paintViewModel.getSelectedPaint() ?: return
    var favourites = userViewModel.getFavourites()

    var isLiked by remember { mutableStateOf(paint in favourites) }

    // TODO: hard-coded values for now
    val reviews = listOf(
        Review("PPG1065-2", "1", "Ayo this slaps", Instant.now(), listOf("")),
        Review("PPG1065-2", "2", "Yea I agree", Instant.now(), listOf("")),
        Review("PPG1065-2", "3", "Weird name but aight", Instant.now(), listOf(""))
    )

    Screen(navController, userViewModel, errorViewModel) { padding ->
        CenteredColumn(modifier = Modifier.padding(padding), centerVertically = false) {

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
                    Text(text = paint.id, fontSize = 12.sp)
                    Text(text = paint.brand, fontSize = 12.sp)
                }

                PrimaryButton(
                    text = "Try!",
                    onClick = {
                        paintViewModel.updateSelectedPaints(paint)
                        navController.navigate("camera_screen")
                    }
                )

                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = {
                        coroutineScope.launch {
                            if (!isLiked) {
                                userViewModel.addFavourite(paint)
                            } else {
                                userViewModel.deleteFavourite(paint)
                            }
                            favourites = userViewModel.getFavourites()
                            isLiked = paint in favourites
                        }
                    }
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
