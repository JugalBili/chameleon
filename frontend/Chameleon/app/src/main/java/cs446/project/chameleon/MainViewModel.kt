package cs446.project.chameleon

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import cs446.project.chameleon.data.model.Paint


class MainViewModel: ViewModel() {

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value += bitmap
    }

    // PAINTS
    private val _paints = MutableStateFlow<List<Paint>>(emptyList())
    val paints: StateFlow<List<Paint>> get() = _paints.asStateFlow()
    init {
        val defaultPaints = listOf(
            Paint("Id1", "PPG", "Viva La Bleu", listOf(151, 190, 226)),
            Paint("Id2", "PPG", "Calypso Berry", listOf(197, 58, 75)),
            Paint("Id3", "Benjamin Moore", "Blue Pearl", listOf(147, 160, 189)),
            Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)),
            Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)),
            Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)),
            Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)),
            Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)),
            Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)),
            Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)),
            Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)),
            Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)),
            Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105)),
            Paint("Id4", "Benjamin Moore", "Grape Green", listOf(213, 216, 105))
        )
        _paints.value = defaultPaints
    }

    private var _selectedPaint: Paint? by mutableStateOf(null)

    fun getSelectedPaint(): Paint? {
        return _selectedPaint
    }

    fun updateSelectedPaint(paint: Paint) {
        _selectedPaint = paint
        Log.d("MainViewModel", "Paint updated: $_selectedPaint")
    }
}
