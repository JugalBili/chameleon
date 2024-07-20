package cs446.project.chameleon

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cs446.project.chameleon.data.model.HSL
import cs446.project.chameleon.data.model.Paint
import cs446.project.chameleon.data.model.RGB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaintViewModel: ViewModel() {

    // Database paints
    private val _paints = MutableStateFlow<List<Paint>>(emptyList())
    val paints: StateFlow<List<Paint>> get() = _paints.asStateFlow()

    // User-selected paints
    private var _selectedPaint: Paint? by mutableStateOf(null)
    fun getSelectedPaint(): Paint? {
        return _selectedPaint
    }
    fun updateSelectedPaint(paint: Paint) {
        _selectedPaint = paint
        Log.d("MainViewModel", "Paint updated: $_selectedPaint")
    }

    // Default value initializations
    init {
        val defaultPaints = listOf(
            Paint("Benjamin Moore", "https://www.benjaminmoore.com/en-ca/paint-colours/colour/2000-70/voile-pink", "Voile Pink", "2000-70", RGB(252, 226, 230), HSL(351f, 81.3f, 93.7f), "white", "red"),
            Paint("PPG", "https://www.ppgpaints.com/color/color-families/greens/grass-daisy", "Grass Daisy", "PPG1215-6", RGB(206, 176, 42), HSL(49f, 66.1f, 48.6f), "yellow", "yellow"),
            Paint("PPG", "https://www.ppgpaints.com/color/color-families/oranges/fiesta", "Fiesta", "PPG1065-2", RGB(237, 216, 210), HSL(13f, 42.9f, 87.6f), "white", "red"),
            Paint("Dunn Edwards", "https://www.dunnedwards.com/colors/browser/de5921", "Your Shadow", "DE5921", RGB(120, 126, 147), HSL(227f, 11.1f, 52.4f), "gray", "blue")
        )
        _paints.value = defaultPaints
    }
}
