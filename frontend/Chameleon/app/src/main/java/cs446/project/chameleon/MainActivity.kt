package cs446.project.chameleon

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

        // FOR DEMO PURPOSES, final processed image TODO remove after
        val demoBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.demo_before)
        val demoBitmap1 = BitmapFactory.decodeResource(this.resources, R.drawable.demo_after_1)
        val demoBitmap2 = BitmapFactory.decodeResource(this.resources, R.drawable.demo_after_2)

        enableEdgeToEdge()
        setContent {
            ChameleonTheme {
                val userViewModel: UserViewModel = UserViewModel(listOf(demoBitmap, demoBitmap1, demoBitmap2))
                val paintViewModel: PaintViewModel = viewModel()
                val imageViewModel: ImageViewModel = viewModel()
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login_page"
                ) {
                    composable("login_page") {
                        LoginPage(navController)
                    }
                    composable("camera_screen") {
                        CameraScreen(navController, imageViewModel)
                    }
                    composable("image_preview_screen") {
                        ImagePreviewScreen(navController, paintViewModel, imageViewModel)
                    }
                    composable("image_result_screen") {
                        ImageResultScreen(navController, imageViewModel)
                    }
                    composable("profile_screen") {
                        ProfileScreen(navController, paintViewModel, userViewModel, imageViewModel)
                    }
                    composable("gallery_page") {
                        PaintGalleryScreen(navController, paintViewModel)
                    }
                    composable("paint_review") {
                        PaintReviewsScreen(navController, paintViewModel)
                    }
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