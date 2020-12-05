package tiger.spike.maps

import android.app.Activity
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import tiger.spike.MapApp
import tiger.spike.login.LoginActivity
import tiger.spike.login.RC_SIGN_IN

class LoginActivityTest : BaseActivityTest<LoginActivity>(LoginActivity::class.java) {

    @Before
    override fun setup() {
        mockkStatic(AuthUI::class)
        AuthUI.setApplicationContext(MapApp.appContext)
        val mockedAuthUI: AuthUI = mockk()
        every { AuthUI.getInstance() } returns mockedAuthUI

        val mockedIntent: Intent = mockk(relaxed = true)
        every {
            mockedAuthUI.createSignInIntentBuilder().setAvailableProviders(any()).build()
        } returns mockedIntent

        super.setup()
    }

    @Test
    fun testOnCreate() {
        assertNotNull(activity)
    }

    @Test
    fun `onActivityResult with result code  RESULT_LOGIN_SUCCESS`() {
        activity.onActivityResult(RC_SIGN_IN, Activity.RESULT_OK, null)
        assertTrue(activity.isFinishing)
    }
}