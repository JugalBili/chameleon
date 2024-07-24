package cs446.project.chameleon.composables

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cs446.project.chameleon.R
import cs446.project.chameleon.data.viewmodel.ErrorViewModel
import cs446.project.chameleon.data.viewmodel.UserViewModel
import kotlinx.coroutines.launch


@Composable
fun NavBar(
    navController: NavHostController,
    userViewModel: UserViewModel,
    errorViewModel: ErrorViewModel
) {
    var selectedIndex by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    BottomNavigation(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color(84, 139, 227)
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_format_paint_24),
                    contentDescription = "Paint Icon"
                )
            },
            selected = selectedIndex == 0,
            onClick = { selectedIndex = 0
                navController.navigate("gallery_page")}
        )

        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_camera_alt_24),
                    contentDescription = "Camera Icon"
                )
            },
            selected = selectedIndex == 1,
            onClick = { selectedIndex = 1
                navController.navigate("camera_screen")}
        )

        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_person_24),
                    contentDescription = "Profile Icon"
                )
            },
            selected = selectedIndex == 2,
            onClick = {
                coroutineScope.launch {
                    selectedIndex = 2
                    val response = userViewModel.fetchNonCachedHistory()
                    if (response != null) {
                        errorViewModel.displayError(response)
                    } else {
                        navController.navigate("profile_screen")
                    }
                }
            }
        )
    }
}