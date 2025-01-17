package cs446.project.chameleon

import androidx.compose.foundation.Image
import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import cs446.project.chameleon.R
import cs446.project.chameleon.data.viewmodel.ErrorViewModel
import cs446.project.chameleon.data.viewmodel.ImageViewModel
import cs446.project.chameleon.data.viewmodel.PaintViewModel
import cs446.project.chameleon.data.viewmodel.UserViewModel
import kotlinx.coroutines.launch


@Composable
fun LoginPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    paintViewModel: PaintViewModel,
    imageViewModel: ImageViewModel,
    errorViewModel: ErrorViewModel
) {

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val clicked = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val annotatedText = buildAnnotatedString {
        append("Don't have an account? Sign up ")
        pushStringAnnotation(tag = "signup", annotation = "here!")
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append("here!")
        }
        pop()
    }

    val coroutineScope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(3.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showDialog.value) {
                    Dialog(onDismissRequest = { showDialog.value = false }) {
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colors.background,
                            elevation = 24.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Warning",
                                    style = MaterialTheme.typography.h6,
                                    color = Color.Red
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "The visualized image may have discrepancies compared to the final product and should be used as a reference only.",
                                    style = MaterialTheme.typography.body1
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { showDialog.value = false }) {
                                    Text("Dismiss")
                                }
                            }
                        }
                    }
                }
                // Move the logo up to be above the username field
                val image: Painter = painterResource(id = R.drawable.logo)
                Image(
                    painter = image,
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .height(300.dp)
                )

                OutlinedTextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text("Username") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp)) // Reduced space before Password field

                // Center the Password field
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            coroutineScope.launch {
                                val response = userViewModel.loginUser(username.value, password.value)
                                if (response != null) {
                                    errorViewModel.displayError(response)
                                } else {
                                    navController.navigate("camera_screen")
                                }
                            }
                            keyboardController?.hide()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(24.dp)) // Space between Password field and Sign Up link

                ClickableText(text = annotatedText, onClick = { offset ->
                    annotatedText.getStringAnnotations(
                        tag = "signup", start = offset, end = offset
                    ).firstOrNull()?.let { navController.navigate("signup_page") }
                })
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val response =
                                    userViewModel.loginUser(username.value, password.value)
                                if (response != null) {
                                    errorViewModel.displayError(response)
                                } else {
                                    navController.navigate("camera_screen")
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(84, 139, 227),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(1f)
                    ) {
                        Text(text = "Login")
                    }
                }
            }
        },

        )
}
