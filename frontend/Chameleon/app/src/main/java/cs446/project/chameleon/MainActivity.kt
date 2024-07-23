package cs446.project.chameleon

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cs446.project.chameleon.data.viewmodel.ErrorViewModel
import cs446.project.chameleon.data.viewmodel.ImageViewModel
import cs446.project.chameleon.data.viewmodel.PaintViewModel
import cs446.project.chameleon.data.viewmodel.UserViewModel
import cs446.project.chameleon.ui.theme.ChameleonTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O) // TODO: remove these RequiresAPI annotations if the Timestamp type changes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check camera permissions
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0
            )
        }

        val context: Context = this

        // FOR DEMO PURPOSES, final processed image TODO remove after
        val demoBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.demo_before)
        val demoBitmap1 = BitmapFactory.decodeResource(this.resources, R.drawable.demo_after_1)
        val demoBitmap2 = BitmapFactory.decodeResource(this.resources, R.drawable.demo_after_2)

        enableEdgeToEdge()
        setContent {
            ChameleonTheme {
                val navController = rememberNavController()
                val userViewModel: UserViewModel = UserViewModel(listOf(demoBitmap, demoBitmap1, demoBitmap2))
                val paintViewModel: PaintViewModel = viewModel()
                val imageViewModel: ImageViewModel = ImageViewModel(context)
                val errorViewModel: ErrorViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "login_page"
                ) {
                    composable("login_page") {
                        LoginPage(navController, userViewModel, paintViewModel, imageViewModel, errorViewModel)
                    }
                    composable("signup_page") {
                        SignupPage(navController, userViewModel, paintViewModel, imageViewModel, errorViewModel)
                    }
                    composable("camera_screen") {
                        CameraScreen(navController, userViewModel, paintViewModel, imageViewModel, errorViewModel)
                    }
                    composable("image_preview_screen") {
                        ImagePreviewScreen(navController, userViewModel, paintViewModel, imageViewModel, errorViewModel)
                    }
                    composable("image_result_screen") {
                        ImageResultScreen(navController, userViewModel, paintViewModel, imageViewModel, errorViewModel)
                    }
                    composable("profile_screen") {
                        ProfileScreen(navController, userViewModel, paintViewModel, imageViewModel, errorViewModel)
                    }
                    composable("gallery_page") {
                        PaintGalleryScreen(navController, userViewModel, paintViewModel, imageViewModel, errorViewModel)
                    }
                    composable("paint_review") {
                        PaintReviewsScreen(navController, userViewModel, paintViewModel, imageViewModel, errorViewModel)
                    }
                }

                if (errorViewModel.showErrorDialog.value) {
                    AlertDialog(
                        onDismissRequest = { errorViewModel.dismissError() },
                        title = { Text("Error") },
                        text = { Text(errorViewModel.errorMsg.value) },
                        confirmButton = {
                            TextButton(onClick = { errorViewModel.dismissError() }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA
        )
    }
}