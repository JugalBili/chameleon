package cs446.project.chameleon.gallery

import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import cs446.project.chameleon.MainViewModel
import cs446.project.chameleon.Paint

@Composable
fun PaintReview(
    navController: NavHostController, // TODO: do we need this?
    mainViewModel: MainViewModel
) {
    val paint = mainViewModel.getSelectedPaint() ?: return
    Log.d("MainViewModel", "Paint queried: $paint")

    val colour = Color(red = paint.rgb[0], green = paint.rgb[1], blue = paint.rgb[2])

    var isLiked by remember { mutableStateOf(false) }

    // TODO: hard-coded values for now
    val reviews = listOf(
        Review("John", "Doe", "2024-07-17", "ayo this slaps"),
        Review("John", "Smith", "2024-07-17", "ayo this also slaps"),
        Review("John", "UninspiredLastName", "2024-07-17", "uwu this colour looks soo good! Can’t" +
                " wait till I buy more of it and color my " +
                "entire house ")
    )

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

                // Paint Info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp, top = 40.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Paint Colour
                    Box(
                        modifier = Modifier
                            .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .height(100.dp)
                            .width(100.dp)
                            .background(colour)
                    )

                    // Paint Details
                    Column() {
                        Text(text = paint.name, fontSize = 14.sp)
                        Text(text = paint.id, fontSize = 12.sp) // TODO: change this to paint code
                        Text(text = paint.brand, fontSize = 12.sp)
                    }

                    // "Try" Button
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(16.dp)
                        //modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = "Try", color = Color.White)
                    }

                    // "Like" Button
                    IconButton(
                        onClick = { isLiked = !isLiked },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isLiked) Color.Red else Color.Black
                        )
                    }
                }

                // DIVIDER
                HorizontalDivider(
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    thickness = 3.dp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                // REVIEWS
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(reviews) { review ->
                        ReviewCard(review = review)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

            }
        },

        // BOTTOMBAR: navigation between sections
        bottomBar = {
            // TODO: add reusable composable here for the buttons to navigate between sections
        }
    )
}