package cs446.project.chameleon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cs446.project.chameleon.composables.ColourSelectionDialog


// TODO: delete this
// This is a demo page to show how to integrate the ColourSelectionDialog
@Composable
fun TestingPage(navController: NavHostController) {

    val showModal = remember { mutableStateOf(true) }
    val selectedPaintIds = remember { mutableStateListOf<String>() }

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

                if (showModal.value) {
                    ColourSelectionDialog(
                        onClose = {
                            showModal.value = false
                        },
                        onSubmit = { showModal.value = false },
                        onClick = { paintId ->
                            if (selectedPaintIds.contains(paintId)) {
                                selectedPaintIds.remove(paintId)
                            } else {
                                selectedPaintIds.add(paintId)
                            }
                        }
                    )
                }

                if (!showModal.value) {
                    Text(text = selectedPaintIds.joinToString(separator = ", "))
                }
            }
        }
    )
}
