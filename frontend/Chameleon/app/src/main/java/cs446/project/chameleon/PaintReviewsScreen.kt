package cs446.project.chameleon

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import cs446.project.chameleon.composables.ReviewCard
import cs446.project.chameleon.composables.styling.CenteredColumn
import cs446.project.chameleon.composables.styling.CenteredRow
import cs446.project.chameleon.composables.styling.ChameleonDivider
import cs446.project.chameleon.composables.styling.ChameleonText
import cs446.project.chameleon.composables.styling.ColouredBox
import cs446.project.chameleon.composables.styling.PrimaryButton
import cs446.project.chameleon.composables.styling.Screen
import cs446.project.chameleon.data.model.Paint
import cs446.project.chameleon.data.model.Review
import cs446.project.chameleon.data.viewmodel.ErrorViewModel
import cs446.project.chameleon.data.viewmodel.ImageViewModel
import cs446.project.chameleon.data.viewmodel.PaintViewModel
import cs446.project.chameleon.data.viewmodel.UserViewModel
import cs446.project.chameleon.utils.HEADER
import cs446.project.chameleon.utils.getColour
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.Instant

@SuppressLint("StateFlowValueCalledInComposition")
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
    var isWritingReview by remember { mutableStateOf(false) }
    var writingReviewText by remember { mutableStateOf("") }

    var reviews by remember { mutableStateOf(userViewModel.getReviews()) }
    var hasLeftReview by remember { mutableStateOf(reviews.any { it.uid == userViewModel.getUser()?.uid }) }

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
                ColouredBox(colour = getColour(paint.rgb), modifier = Modifier.weight(1f))

                CenteredColumn(fullWidth = false, centerHorizontally = false, modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                    Text(text = paint.name, fontSize = 18.sp)
                    Text(text = paint.id, fontSize = 14.sp)
                    Text(text = paint.brand, fontSize = 14.sp)
                }

                CenteredColumn(fullWidth = false, modifier = Modifier.weight(1f)) {
                    CenteredRow(fullWidth = false) {
                        PrimaryButton(
                            text = "Try!",
                            onClick = {
                                paintViewModel.updateSelectedPaints(paint)
                                navController.navigate("camera_screen")
                            }
                        )
                        IconButton(
                            modifier = Modifier
                                .size(48.dp)
                                .weight(1f),
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
                    PrimaryButton(
                        text = if (hasLeftReview) "Edit Review" else "Write Review",
                        onClick = {
                            isWritingReview = true
                        }
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

        if (isWritingReview) {
            Dialog(
                onDismissRequest = { isWritingReview = false }
            ) {
                Surface(shape = MaterialTheme.shapes.medium) {
                    CenteredColumn(modifier = Modifier.padding(12.dp)) {
                        ChameleonText(if (hasLeftReview) "Edit your Review!" else "Write a Review!", HEADER)
                        ChameleonDivider()

                        TextField(
                            modifier = Modifier.padding(bottom = 10.dp),
                            value = writingReviewText,
                            onValueChange = { writingReviewText = it },
                            label = { Text("Write your review here") }
                        )
                        PrimaryButton(
                            "Submit review",
                            onClick = {
                                coroutineScope.launch {
                                    isWritingReview = false
                                    userViewModel.createReview(paint.id, writingReviewText)
                                    userViewModel.fetchReviews(paint.id)
                                    reviews = userViewModel.getReviews()
                                    hasLeftReview = reviews.any { it.uid == userViewModel.getUser()?.uid }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
