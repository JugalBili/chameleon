package cs446.project.chameleon

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import cs446.project.chameleon.composables.CameraPreview
import cs446.project.chameleon.composables.styling.Screen
import cs446.project.chameleon.data.viewmodel.ErrorViewModel
import cs446.project.chameleon.data.viewmodel.ImageViewModel
import cs446.project.chameleon.data.viewmodel.PaintViewModel
import cs446.project.chameleon.data.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    paintViewModel: PaintViewModel,
    imageViewModel: ImageViewModel,
    errorViewModel: ErrorViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    Screen(navController, userViewModel, errorViewModel) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CameraPreview(
                controller = controller,
                modifier = Modifier
                    .fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 75.dp)
                    .background(Color.Transparent)
            ) {
                IconButton(
                    onClick = {
                        takePhoto(
                            controller = controller,
                            onPhotoTaken = imageViewModel::updateImage,
                            context = context,
                            navController = navController
                        )
                    },
                    modifier = Modifier.size(100.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.take_photo),
                        contentDescription = "Take photo",
                        tint = Color.Black,
                        modifier = Modifier.size(150.dp).alpha(0.6f)
                    )
                }
            }
        }
    }
}


private fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    context: android.content.Context,
    navController: NavHostController
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )

                onPhotoTaken(rotatedBitmap)
                navController.navigate("image_preview_screen")
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take photo: ", exception)
            }
        }
    )
}
