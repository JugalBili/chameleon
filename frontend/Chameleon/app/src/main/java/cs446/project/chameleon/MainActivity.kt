package cs446.project.chameleon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cs446.project.chameleon.ui.theme.ChameleonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChameleonTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "landing_page"
                ) {
                    composable("landing_page" ) {
                        LandingPage(navController)
                    }
                    composable(
                        "first_screen/{name}",
                        arguments = listOf(navArgument("name") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: "DEFAULT_NAME" // this doesnt do anything?
                        FirstScreen(navController, name)
                    }
                    composable("second_screen") { SecondScreen(navController) }
                }
            }
        }
    }
}

@Composable
fun LandingPage(navController: NavHostController) {
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
                    text = "This is the landing page",
                    modifier = Modifier.padding(48.dp)
                )
            }


        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.navigate("first_screen/TESTBOBLINE84") },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Go to First Screen")
                }
                Button(
                    onClick = { navController.navigate("second_screen") },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Go to Second Screen")
                }
            }
        }

    )
}

@Composable
fun FirstScreen(navController: NavHostController, name: String) {
    Scaffold(
        content = { padding ->
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text (
                    text = "This is the First Screen for $name",
                    modifier = Modifier.padding(48.dp)
                )
            }
        },
        bottomBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { navController.navigate("landing_page") },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Go to MAIN SCREEN")
                }
            }
        }
    )
}

@Composable
fun SecondScreen(navController: NavHostController) {
    Scaffold(
        content = { padding ->
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "This is the Second Screen",
                    modifier = Modifier.padding(48.dp)
                )
            }
        },
        bottomBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { navController.navigate("landing_page") },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Go to MAIN SCREEN")
                }
            }
        }
    )
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChameleonTheme {
        Greeting("Android")
    }
}