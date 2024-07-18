package cs446.project.chameleon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun ImagePreviewScreen(
    navController: NavHostController,
    paintViewModel: MainViewModel
) {
    val capturedImage = navController.previousBackStackEntry?.savedStateHandle?.get<Bitmap>("capturedImage")
    var isProcessing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // FOR DEMO PURPOSES, final processed image TODO remove after
    val demoBitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.demo_after_1)
    val demoBitmap2 = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.demo_after_2)

    // Colour Selection Modal
    val showModal = remember { mutableStateOf(false) }
    val selectedPaintIds = remember { mutableStateListOf<String>() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        capturedImage?.let { image ->
            Image(
                bitmap = image.asImageBitmap(),
                contentDescription = "Captured Photo",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Retake Button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Retake Photo",
                tint = Color.White
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)) {

            Column() {
                if (selectedPaintIds.isNotEmpty() && !showModal.value) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text("Selected paints: " + selectedPaintIds.joinToString(separator = ", "))
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Empty button for now",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        showModal.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Color Picker",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        // process the image, then display result
                        isProcessing = true
                        coroutineScope.launch {
                            processImage(listOf(demoBitmap, demoBitmap2)) { processedImages -> // TODO change demoBitmap to actual result
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "processedImages",
                                    processedImages
                                )
                                isProcessing = false
                                navController.navigate("image_result_screen")
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Generate Image",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        if (isProcessing) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        if (showModal.value) {
            ColourSelectionDialog(
                onClose = { showModal.value = false },
                onSubmit = { showModal.value = false },
                onClick = { paint ->
                    if (selectedPaintIds.contains(paint.id)) {
                        selectedPaintIds.remove(paint.id)
                    } else {
                        selectedPaintIds.add(paint.id)
                    }
                },
                paintViewModel = paintViewModel
            )
        }
    }
}

// TODO implement
suspend fun processImage(bitmap: List<Bitmap>?, onProcessingComplete: (List<Bitmap>) -> Unit) {
    if (bitmap == null) return
    // make backend call here; for the demo we are simulating the delay
    delay(10000)

    // return processed image
    onProcessingComplete(bitmap)
}

