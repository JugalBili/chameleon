package cs446.project.chameleon.repository

import cs446.project.chameleon.data.model.Favorite
import cs446.project.chameleon.data.model.RGB
import cs446.project.chameleon.data.repository.FavoriteRepository
import cs446.project.chameleon.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class FavoriteRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setup() {
        userRepository = UserRepository()
        favoriteRepository = FavoriteRepository()
    }

    @Test
    fun testFavorites() = runBlocking {
        val email = "FavoriteTest@gmail.com"
        val password = "password"
        val firstname = "favorite"
        val lastname = "test"
        val token = userRepository.register(email, password, firstname, lastname)
        assertNotNull(token)

        // adding some favorites
        val fav1 = Favorite("test_paint1", RGB(10, 20, 30))
        val fav2 = Favorite("test_paint2", RGB(30, 20, 10))

        val fetch0 = favoriteRepository.getFavorites(token.token)
        assertEquals(emptyList<Favorite>(), fetch0)
        println(fetch0)

        favoriteRepository.postFavorite(token.token, fav1)

        val fetch1 = favoriteRepository.getFavorites(token.token)
        assertEquals(listOf<Favorite>(fav1), fetch1)
        println(fetch1)

        favoriteRepository.postFavorite(token.token, fav2)

        val fetch2 = favoriteRepository.getFavorites(token.token)
        assertEquals(listOf<Favorite>(fav1, fav2), fetch2)
        println(fetch2)

        favoriteRepository.deleteFavorite(token.token, fav1.paintId)

        val fetch3 = favoriteRepository.getFavorites(token.token)
        assertEquals(listOf<Favorite>(fav2), fetch3)
        println(fetch3)

    }

}