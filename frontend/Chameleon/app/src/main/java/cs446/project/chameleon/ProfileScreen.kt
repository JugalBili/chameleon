package cs446.project.chameleon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cs446.project.chameleon.composables.NavBar
import cs446.project.chameleon.composables.styling.Screen

@Composable
fun ProfileScreen(navController: NavHostController) {
    Screen(navController) { padding ->
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "This is the Profile Screen",
                modifier = Modifier.padding(48.dp)
            )
        }
    }
}
