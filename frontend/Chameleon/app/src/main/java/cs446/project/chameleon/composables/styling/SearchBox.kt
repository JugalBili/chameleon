package cs446.project.chameleon.composables.styling

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SearchBox(
    searchQuery: String,
    placeholderText: String = "Search",
    onChange: (String) -> Unit
) {

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onChange,
        placeholder = { Text(placeholderText) },
        singleLine = true
    )
}
