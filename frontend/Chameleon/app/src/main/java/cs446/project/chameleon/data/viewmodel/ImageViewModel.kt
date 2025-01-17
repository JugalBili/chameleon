package cs446.project.chameleon.data.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs446.project.chameleon.data.model.Color
import cs446.project.chameleon.data.model.ImageResponse
import cs446.project.chameleon.data.model.Paint
import cs446.project.chameleon.data.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ImageViewModel(@field:SuppressLint("StaticFieldLeak") private val context: Context): ViewModel() {
    private var imageRepository: ImageRepository = ImageRepository()

    // Base image
    private val _baseImage = MutableLiveData<Bitmap>()
    val baseImage: LiveData<Bitmap> get() = _baseImage

    fun updateImage(newBitmap: Bitmap) {
        _baseImage.value = newBitmap
    }

    // Renders
    private val _renders = MutableStateFlow<List<Bitmap>>(emptyList())
    val renders = _renders.asStateFlow()
    fun addRender(bitmap: Bitmap) {
        _renders.value += bitmap
    }

    fun updateRenders(newRenders: List<Bitmap>) {
        for (i in newRenders) {
            addRender(i)
        }
    }

    // Corresponding Colors for Renders
    private val _renderColors = MutableStateFlow<List<Color>>(emptyList())
    val renderColors = _renderColors.asStateFlow()
    fun addRenderColor(color: Color) {
        _renderColors.value += color
    }

    fun updateRenderColors(newColors: List<Color>) {
        _renderColors.value = newColors
    }

    fun onHistoryRowClick(uiHistory: UIHistory, bitmaps: List<Bitmap>) {
        updateImage(uiHistory.baseImage)
        _renders.value = emptyList()
        baseImage.value?.let { addRender(it) }
        updateRenders(bitmaps)
        updateRenderColors(uiHistory.colors)
    }

    suspend fun postImage(authToken: String, paints: List<Paint>): String? {
        try {
            _renders.value = emptyList()
            baseImage.value?.let { addRender(it) }
            _renderColors.value = emptyList()
            val colorsList = mutableListOf<Color>()
            for (paint in paints) {
                colorsList.add(Color(paint.id, paint.rgb))
            }

            val tempFile = withContext(Dispatchers.IO) {
                File.createTempFile("temp_image", ".jpg", context.cacheDir)
            }

            ByteArrayOutputStream().use { byteArrayOutputStream ->
                _baseImage.value?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()

                // Write the ByteArray to the temporary file
                FileOutputStream(tempFile).use { fileOutputStream ->
                    fileOutputStream.write(byteArray)
                }
            }

            runBlocking {
                val response = imageRepository.postImage(authToken, tempFile, colorsList)

                for (image in response.processedImages) {
                    addRenderColor(image.color)
                    addRender(imageRepository.getImageBitmap(authToken, image.processedImageHash))
                }
            }

            return null
        } catch (e: Exception) {
            return "Connection to server failed"
        }

    }
}
