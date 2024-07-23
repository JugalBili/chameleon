package cs446.project.chameleon.data.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ErrorViewModel: ViewModel() {
    val showErrorDialog = mutableStateOf(false)
    val errorMsg = mutableStateOf("")

    fun displayError(msg: String) {
        errorMsg.value = msg
        showErrorDialog.value = true
    }

    fun dismissError() {
        errorMsg.value = ""
        showErrorDialog.value = false
    }
}
