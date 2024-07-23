package cs446.project.chameleon.data.viewmodel;

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoadingViewModel: ViewModel() {
    val showLoad = mutableStateOf(false)

    fun displayLoad() {
        showLoad.value = true
    }

    fun dismissLoad() {
        showLoad.value = false
    }
}
