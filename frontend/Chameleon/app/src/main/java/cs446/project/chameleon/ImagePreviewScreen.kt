package cs446.project.chameleon

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.NavHostController
import cs446.project.chameleon.composables.NavBar
import cs446.project.chameleon.composables.PaintSelectionDialog
import cs446.project.chameleon.composables.SelectionBar
import cs446.project.chameleon.data.viewmodel.ErrorViewModel
import cs446.project.chameleon.data.viewmodel.ImageViewModel
import cs446.project.chameleon.data.viewmodel.PaintViewModel
import cs446.project.chameleon.data.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ImagePreviewScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    paintViewModel: PaintViewModel,
    imageViewModel: ImageViewModel,
    errorViewModel: ErrorViewModel
) {
    var isProcessing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // FOR DEMO PURPOSES, final processed image TODO remove after
    val demoBitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.demo_after_1)
    val demoBitmap2 = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.demo_after_2)

    // Colour Selection Modal
    val showModal = remember { mutableStateOf(false) }

    // ImageViewModel setup
    val bitmapState = imageViewModel.baseImage.observeAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {
            bitmapState.value?.let { bitmap ->
                Log.d("ImagePreviewScreen", "Bitmap received")
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured Photo",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (isProcessing) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x80000000)) // Optional: Add a semi-transparent background
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            } ?: Log.d("ImagePreviewScreen", "Bitmap is null")},
        bottomBar = {
            Column {
                // Selection bar
                SelectionBar(
                    onAddClick = { showModal.value = true },
                    onProcessClick = {
                        // process the image, then display result
                        isProcessing = true
                        coroutineScope.launch {
                            val response = imageViewModel.postImage(userViewModel.token.token, paintViewModel.selectedPaints)
                            if (response != null) {
                                errorViewModel.displayError(response)
                            } else {
                                isProcessing = false
                                paintViewModel.clearSelectedPaints()
                                navController.navigate("image_result_screen")
                            }
                        }
                    }
                )


                // Nav bar
                NavBar(navController = navController, userViewModel, errorViewModel)
            }
        }
    )

    if (showModal.value) {
        PaintSelectionDialog(
            onClose = { showModal.value = false },
            onSubmit = { showModal.value = false },
            paintViewModel = paintViewModel,
            userViewModel = userViewModel,
            errorViewModel = errorViewModel
        )
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
