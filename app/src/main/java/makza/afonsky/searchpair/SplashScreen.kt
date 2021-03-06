package makza.afonsky.searchpair

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

/**
 * Загрузочный экран
 */
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        Handler().postDelayed({
        val intent = Intent(this, GameMainActivity::class.java)
        startActivity(intent)
        finish()
        }, 500)
    }
}