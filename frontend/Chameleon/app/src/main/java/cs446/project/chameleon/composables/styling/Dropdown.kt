package cs446.project.chameleon.composables.styling

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cs446.project.chameleon.constants.BODY

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Dropdown(
    options: List<String>,
    updateSelectedOption: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    Box() {
        ExposedDropdownMenuBox(
            expanded = expanded, 
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedOption, 
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { ChameleonText(option, BODY) },
                        onClick = {
                            selectedOption = option
                            updateSelectedOption(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
