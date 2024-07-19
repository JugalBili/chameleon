package cs446.project.chameleon.repository

import cs446.project.chameleon.data.model.Color
import cs446.project.chameleon.data.model.RGB
import cs446.project.chameleon.data.repository.ImageRepository
import cs446.project.chameleon.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ImageRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var imageRepository: ImageRepository

    @Before
    fun setup() {
        userRepository = UserRepository()
        imageRepository = ImageRepository()
    }

    // tests image flow (but no bitmaps since that's only on the phone!)
    @Test
    fun testImages() = runBlocking {
        val email = "ImageTest@gmail.com"
        val password = "password"
        val firstname = "image"
        val lastname = "test"
        val token = userRepository.register(email, password, firstname, lastname)
        assertNotNull(token)

        // upload image
        val filePath = "FILL_PATH_HERE"

        val colors = listOf(
            Color("paint_id_1", RGB(255, 0, 0)),
            Color("paint_id_2", RGB(0, 255, 0)),
            Color("paint_id_3", RGB(0, 0, 255))
        )

        val response = imageRepository.postImage(token.token, filePath, colors)
        println(response)

        // get list of images
        val images = imageRepository.getImageList(token.token, response.originalImage)
        assertEquals(response, images)

        // for each image get hash
        for (img in images.processedImages) {
            println(img.processedImageHash)
            // NOTE: cannot test this since bitmaps arent a part of JVM (they are android only i think??)
            //  so be sure to test this with the app itself
//            val bitmap = imageRepository.getImageBitmap(token.token, img.processedImageHash)
        }

    }
}
