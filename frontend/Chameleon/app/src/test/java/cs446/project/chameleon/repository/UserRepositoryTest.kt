package cs446.project.chameleon.repository

import cs446.project.chameleon.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

// note: this way of testing sucks, but im using it to test that the connection to the backend works and the endpoints work
//  prob have to manually clean up in firebase when done running this
class UserRepositoryTest {
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        userRepository = UserRepository()
    }

    // tests the user management flow (more of a sanity check tbh)
    @Test
    fun testUserWorkflow() = runBlocking {
        val email = "usertest@gmail.com"
        val password = "password"
        val firstname = "unit"
        val lastname = "test"
        val token = userRepository.register(email, password, firstname, lastname)
        assertNotNull(token)
        println(token)

        // try to re-register, should give 409
        try {
            val fail = userRepository.register(email, password, firstname, lastname)
            assertTrue("Expected an HttpException to be thrown", false)
        } catch (e: HttpException) {
            assertEquals(409, e.code())
        } catch (e: Exception) {
            assertTrue("Expected an HttpException, but got ${e::class}", false)
        }

        // then try to login, once with incorrect pwd then with correct one
        try {
            val fail = userRepository.login(email, "junk")
            assertTrue("Expected an HttpException to be thrown", false)
        } catch (e: HttpException) {
            assertEquals(401, e.code())
        } catch (e: Exception) {
            assertTrue("Expected an HttpException, but got ${e::class}", false)
        }

        val loginResponse = userRepository.login(email, password)
        println(loginResponse)
        assertNotNull(loginResponse.token)
        assertNotNull(loginResponse.user)
        assertEquals(email , loginResponse.user.email)
        assertEquals(firstname , loginResponse.user.firstname)
        assertEquals(lastname , loginResponse.user.lastname)

        // get user by token
        val usr = userRepository.getUser(loginResponse.token.token)
        println(usr)
        assertNotNull(usr)
        assertEquals(loginResponse.user, usr)
    }
}