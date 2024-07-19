package cs446.project.chameleon.repository

import cs446.project.chameleon.data.repository.GalleryRepository
import cs446.project.chameleon.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GalleryRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var galleryRepository: GalleryRepository

    @Before
    fun setup() {
        userRepository = UserRepository()
        galleryRepository = GalleryRepository()
    }

    // tests gallery and review flow (but no bitmaps since that's only on the phone!)
    @Test
    fun testGallery() = runBlocking {
        val email = "ReviewTest@gmail.com"
        val password = "password"
        val firstname = "review"
        val lastname = "test"
        val token = userRepository.register(email, password, firstname, lastname)
        assertNotNull(token)

        val email1 = "ReviewTest1@gmail.com"
        val password1 = "password"
        val firstname1 = "review1"
        val lastname1 = "test"
        val token1 = userRepository.register(email1, password1, firstname1, lastname1)
        assertNotNull(token1)

        // upload image
        val filePath =
            "filepath here"

        val paintId = "moonbase_red"
        val review = "AAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHH"
        val review1 = "makes me feel like primordial soup!"

        galleryRepository.createReview(token.token, filePath, paintId, review)

        val userReview = galleryRepository.getUserReview(token.token, paintId)
        assertEquals(paintId, userReview.paintId)
        assertEquals(review, userReview.review)
        println(userReview)

        galleryRepository.createReview(token.token, filePath, paintId, review1)
        val userReview1 = galleryRepository.getUserReview(token.token, paintId)
        assertEquals(paintId, userReview1.paintId)
        assertEquals(review1, userReview1.review)
        println(userReview1)

        // testing get all reviews
        val reviewList = galleryRepository.getAllReviews(token.token, paintId)
        assertEquals(listOf(userReview1), reviewList)
        println(reviewList)

        val review_new_usr = "awawawawawawa"
        galleryRepository.createReview(token1.token, filePath, paintId, review_new_usr)
        val userReview_new = galleryRepository.getUserReview(token1.token, paintId)

        val reviewList1 = galleryRepository.getAllReviews(token.token, paintId)
        println(reviewList1)

        val reviewList2 = galleryRepository.getAllReviews(token1.token, paintId)
        assertEquals(reviewList1, reviewList2)
    }

}
