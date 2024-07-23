package cs446.project.chameleon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cs446.project.chameleon.R
import cs446.project.chameleon.data.viewmodel.ErrorViewModel
import cs446.project.chameleon.data.viewmodel.UserViewModel

@Composable
fun LoginPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    errorViewModel: ErrorViewModel,
) {

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val clicked = remember { mutableStateOf(false) }

    val colours = listOf(
        Color(0xFFD32F2F),
        Color(0xFFFF5722),
        Color(0xFFF57C00),
        Color(0xFFFDD835),
        Color(0xFF43A047),
        Color(0xFF29B6F6),
        Color(0xFF1E88E5),
        Color(0xFF8E24AA),
        Color(0xFF5E35B1)
    )


    val annotatedText = buildAnnotatedString {
        append("Don't have an account? Sign up ")
        pushStringAnnotation(tag = "signup", annotation = "here!")
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append("here!")
        }
        pop()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Replace the Text composable with the Image composable
                val image: Painter = painterResource(id = R.drawable.logo)
                Image(
                    painter = image,
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .height(300.dp)
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(24.dp))

                ClickableText(
                    text = annotatedText,
                    onClick = { offset ->
                        annotatedText.getStringAnnotations(
                            tag = "signup", start = offset, end = offset
                        ).firstOrNull()?.let { navController.navigate("signup_page") }
                    }
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
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f)
                ) {
                    Text(text = "Login")
                }
            }
        }
    )
}
