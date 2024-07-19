package cs446.project.chameleon.repository

import cs446.project.chameleon.data.model.Color
import cs446.project.chameleon.data.model.RGB
import cs446.project.chameleon.data.repository.HistoryRepository
import cs446.project.chameleon.data.repository.ImageRepository
import cs446.project.chameleon.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class HistoryRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var imageRepository: ImageRepository
    private lateinit var historyRepository: HistoryRepository

    @Before
    fun setup() {
        userRepository = UserRepository()
        imageRepository = ImageRepository()
        historyRepository = HistoryRepository()
    }

    @Test
    fun testHistory() = runBlocking {
        val email = "HistoryTest@gmail.com"
        val password = "password"
        val firstname = "history"
        val lastname = "test"
        val token = userRepository.register(email, password, firstname, lastname)
        assertNotNull(token)

        // upload image
        val filePath = "F:\\Myles Esteban\\Documents\\UWaterloo\\4A\\CS446\\cs446-project\\frontend\\Chameleon\\app\\src\\test\\java\\cs446\\project\\chameleon\\repository\\img.jpg"

        val colors = listOf(
            Color("paint_id_1", RGB(255, 0, 0)),
            Color("paint_id_2", RGB(0, 255, 0)),
            Color("paint_id_3", RGB(0, 0, 255))
        )

        val response = imageRepository.postImage(token.token, filePath, colors)
        assertNotNull(response)
        println(response)

        // get list of images
        val images = imageRepository.getImageList(token.token, response.originalImage)
        assertEquals(response, images)


        val historyResponse = historyRepository.getHistory(token.token)
        assertNotNull(historyResponse)
        println(historyResponse)
        assertEquals(response.originalImage, historyResponse.history[0].baseImage)
        assertEquals(colors, historyResponse.history[0].colors)

    }
}
