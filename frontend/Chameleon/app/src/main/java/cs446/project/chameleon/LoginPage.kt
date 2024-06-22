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


@Composable
fun LoginPage(navController: NavHostController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),

        content = { padding ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(64.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text (
                    text = "This is the login page",
                    modifier = Modifier.padding(48.dp)
                )
            }


        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.navigate("camera_screen") },
                    modifier = Modifier.padding(10.dp).weight(1f)
                ) {
                    Text(text = "Go to Camera Screen")
                }
            }
        }

    )
}