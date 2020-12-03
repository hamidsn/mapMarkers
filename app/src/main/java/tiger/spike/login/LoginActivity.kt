package tiger.spike.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import tiger.spike.maps.MapsActivity

private const val RC_SIGN_IN = 1

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Define providers to sign in
        // We only use Google and Email sign in methods for this sample
        // https://console.firebase.google.com/u/0/project/map-markers-70001/authentication/providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        // We are using default UI for this project
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    //todo, We can add log out functionality in the future

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                //todo, Sign in failed.
            }
        }
    }
}