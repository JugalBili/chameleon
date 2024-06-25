package cs446.project.chameleon

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cs446.project.chameleon.ui.theme.ChameleonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check camera permissions
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0
            )
        }

        enableEdgeToEdge()
        setContent {
            ChameleonTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login_page"
                ) {
                    composable("login_page") {
                        LoginPage(navController)
                    }
                    composable("camera_screen") {
                        CameraScreen(navController)
                    }
                    composable("profile_screen") {
                        ProfileScreen(navController)
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