package cs446.project.chameleon.composables.styling

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cs446.project.chameleon.composables.NavBar

@Composable
fun Screen(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = content,
        bottomBar = {
            NavBar(navController = navController)
        }
    )
}
