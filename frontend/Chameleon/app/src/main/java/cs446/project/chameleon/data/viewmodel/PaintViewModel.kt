package cs446.project.chameleon.data.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cs446.project.chameleon.data.model.Paint
import cs446.project.chameleon.utils.getPaintList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaintViewModel: ViewModel() {

    // Database paints
    private val _paints = MutableStateFlow<List<Paint>>(emptyList())
    val paints: StateFlow<List<Paint>> get() = _paints.asStateFlow()

    // Selected paints (for recolours)
    private var _selectedPaints = mutableStateListOf<Paint>()
    val selectedPaints: List<Paint> get() = _selectedPaints
    fun updateSelectedPaints(paint: Paint) {
        if (paint in selectedPaints) {
            _selectedPaints.remove(paint)
        } else {
            _selectedPaints.add(paint)
        }
    }
    fun clearSelectedPaints() {
        _selectedPaints.clear()
    }

    // Selected paint (for reviews)
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
        _paints.value = getPaintList()
    }
}
