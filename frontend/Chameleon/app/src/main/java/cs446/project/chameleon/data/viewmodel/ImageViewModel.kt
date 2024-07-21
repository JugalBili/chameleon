package cs446.project.chameleon.data.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs446.project.chameleon.data.model.Color
import cs446.project.chameleon.data.model.Paint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ImageViewModel(): ViewModel() {
    // Base image
    private val _baseImage = MutableLiveData<Bitmap>()
    val baseImage: LiveData<Bitmap> get() = _baseImage

    fun updateImage(newBitmap: Bitmap) {
        _baseImage.value = newBitmap
    }

    // Renders
    private val _renders = MutableStateFlow<List<Bitmap>>(emptyList())
    val renders = _renders.asStateFlow()

    // Corresponding Colors for Renders
    // TODO Could be PAINT object
    private val _renderColors = MutableStateFlow<List<Color>>(emptyList())
    val renderColors = _renderColors.asStateFlow()
}