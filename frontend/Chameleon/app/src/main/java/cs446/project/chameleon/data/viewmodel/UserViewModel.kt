package cs446.project.chameleon.data.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import cs446.project.chameleon.data.model.Color
import androidx.lifecycle.ViewModel
import cs446.project.chameleon.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.*
import cs446.project.chameleon.data.model.Favorite
import cs446.project.chameleon.data.model.HSL
import cs446.project.chameleon.data.model.Paint
import cs446.project.chameleon.data.model.RGB
import cs446.project.chameleon.data.model.Token
import cs446.project.chameleon.data.repository.FavoriteRepository
import cs446.project.chameleon.data.repository.HistoryRepository
import cs446.project.chameleon.data.repository.ImageRepository
import cs446.project.chameleon.data.repository.UserRepository
import cs446.project.chameleon.utils.getPaintById
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait

data class UIHistory (
    var baseImage: Bitmap,
    val images: List<Bitmap>,
    val colors: List<Color>
)

class UserViewModel(test: List<Bitmap>): ViewModel() {

    // Setup repo
    private var userRepository: UserRepository = UserRepository()
    private var imageRepository: ImageRepository = ImageRepository()
    private var favoriteRepository: FavoriteRepository = FavoriteRepository()
    private var historyRepository: HistoryRepository = HistoryRepository()

    // User object
    private var _user: User? by mutableStateOf(null)
    fun getUser(): User? {
        return _user
    }
    fun updateUser(user: User) {
        _user = user
    }

    // User token
    lateinit var token: Token

    private val _favourites = MutableStateFlow<List<Paint>>(emptyList())
    val favourites = _favourites.asStateFlow()
    fun addPaint(paint: Paint) {
        _favourites.value += paint
    }
    fun deletePaint(paint: Paint) {
        _favourites.value -= paint
    }

    // User history
    private val _historyList = MutableStateFlow<List<UIHistory>>(emptyList())
    val historyList = _historyList.asStateFlow()
    fun addHistory(history: UIHistory) {
        _historyList.value += history
    }
    fun deleteHistory(history: UIHistory) {
        _historyList.value -= history
    }


    suspend fun loginUser(email: String, password: String) {
        val response = userRepository.login(email, password)
        token = response.token
        _user = response.user

        runBlocking {
            fetchFavourites()
        }
        runBlocking {
            fetchHistory()
        }
    }

    suspend fun registerUser(email: String, password: String, firstname: String, lastname: String) {
        userRepository.register(email, password, firstname, lastname)

        runBlocking {
            loginUser(email, password)
        }
    }

    // favourites
    suspend fun fetchFavourites() {
        _favourites.value = emptyList()
        val response = favoriteRepository.getFavorites(token.token)
        for (fav in response) {
            addPaint(getPaintById(fav.paintId))
        }
    }

    suspend fun addFavourite(paint: Paint) {
        println(token)
        val fav = Favorite(paint.id, paint.rgb)
        favoriteRepository.postFavorite(token.token, fav)

        runBlocking {
            fetchFavourites()
        }
    }

    suspend fun deleteFavourite(paint: Paint) {
        favoriteRepository.deleteFavorite(token.token, paint.id)

        runBlocking {
            fetchFavourites()
        }
    }

    // history
    suspend fun fetchHistory() {
        _historyList.value = emptyList()

        val response = historyRepository.getHistory(token.token)

        for (history in response.history) {
            val imgRes = imageRepository.getImageList(token.token, history.baseImage)

            // get original image
            val baseImage = imageRepository.getImageBitmap(token.token, imgRes.originalImage)

            // get renders
            val images = mutableListOf<Bitmap>()

            for (imgHash in imgRes.processedImages) {
                images.add(imageRepository.getImageBitmap(token.token, imgHash.processedImageHash))     // TODO maybe sus
            }

            addHistory(UIHistory(baseImage, images, history.colors))
        }
    }


    // INIT TO TEST PAGE
//    init {
//        val defaultUser = User("asdf", "John", "Doe", "asdf")
//        updateUser(defaultUser)
//        val defaultPaints = listOf(
//            Paint("Benjamin Moore", "https://www.benjaminmoore.com/en-ca/paint-colours/colour/2000-70/voile-pink", "Voile Pink", "2000-70", RGB(252, 226, 230), HSL(351f, 81.3f, 93.7f), "white", "red"),
//            Paint("PPG", "https://www.ppgpaints.com/color/color-families/greens/grass-daisy", "Grass Daisy", "PPG1215-6", RGB(206, 176, 42), HSL(49f, 66.1f, 48.6f), "yellow", "yellow"),
//            Paint("PPG", "https://www.ppgpaints.com/color/color-families/oranges/fiesta", "Fiesta", "PPG1065-2", RGB(237, 216, 210), HSL(13f, 42.9f, 87.6f), "white", "red"),
//            Paint("Dunn Edwards", "https://www.dunnedwards.com/colors/browser/de5921", "Your Shadow", "DE5921", RGB(120, 126, 147), HSL(227f, 11.1f, 52.4f), "gray", "blue")
//        )
//        for (i in defaultPaints) {
//            addPaint(i)
//        }
//
//        val colors = listOf(
//            Color("paint_id_1", RGB(255, 0, 0)),
//            Color("paint_id_2", RGB(0, 255, 0)),
//            Color("paint_id_3", RGB(0, 0, 255))
//        )
//        _historyList.value += UIHistory(test[0],test, colors)
//        _historyList.value += UIHistory(test[0],test, colors)
//        _historyList.value += UIHistory(test[0],test, colors)
//        _historyList.value += UIHistory(test[0],test, colors)
//        _historyList.value += UIHistory(test[0],test, colors)
//        _historyList.value += UIHistory(test[0],test, colors)
//        _historyList.value += UIHistory(test[0],test, colors)
//    }
}
