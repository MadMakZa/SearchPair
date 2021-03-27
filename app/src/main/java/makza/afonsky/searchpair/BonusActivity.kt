package makza.afonsky.searchpair

import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class BonusActivity : AppCompatActivity() {

    private var relativeLayout: RelativeLayout? = null
    var animationCoin: Animation? = null
    //набор звуков с айдишниками
    private var soundPool: SoundPool? = null
    private var soundDrop = 2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bonus)
        init()
        justClick()
    }

    private fun init() {
        relativeLayout = findViewById(R.id.layout)
        animationCoin = AnimationUtils.loadAnimation(this, R.anim.anim_bonus)
        //загрузка звуков
        soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        soundPool!!.load(baseContext, R.raw.close, 1)
        soundPool!!.load(baseContext, R.raw.stone_drop, 1)


    }
    //вернуться в меню
    override fun onBackPressed() {
        soundPlay(soundDrop)
        val intent = Intent(this, GameMainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
        finish()
    }
    //воспроизведение звука
    private fun soundPlay(id: Int){
        soundPool?.play(id, 1f, 1f, 0,0,1f)
    }

    //Нажатие на картинку
    private fun justClick() {
        for (num in 0..5) {
            GlobalScope.launch(Dispatchers.Main) {
                for (num in 0 until 200) {
                    delay(150L)
                    generateCoin()
                }
            }
        }
    }


    //Генерация падающих картинок
    private fun generateCoin() {
        val img = ImageView(this@BonusActivity)
        val randomTag = Random().nextInt(12)
        img.setTag(randomTag).toString()
        openCard(img)
        relativeLayout!!.addView(img)
        val params = img.layoutParams as RelativeLayout.LayoutParams
        val randomX = Random().nextInt((relativeLayout!!.width)-100)
        params.width = 120
        params.height = 120
        img.layoutParams = params
        img.x = randomX.toFloat()
        img.y = 0f

        val anim = AnimationUtils.loadAnimation(this, R.anim.anim_bonus)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                img.visibility = View.GONE
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        img.startAnimation(anim)
    }

    private fun openCard(img: ImageView?) {
        //установить ресурс для этой вью из списка в зависимости от индекса
        when (img!!.tag.toString()) {
            "1" -> img.setImageResource(R.drawable.image1)
            "2" -> img.setImageResource(R.drawable.image2)
            "3" -> img.setImageResource(R.drawable.image3)
            "4" -> img.setImageResource(R.drawable.image4)
            "5" -> img.setImageResource(R.drawable.image5)
            "6" -> img.setImageResource(R.drawable.image6)
            "7" -> img.setImageResource(R.drawable.image7)
            "8" -> img.setImageResource(R.drawable.image8)
            "9" -> img.setImageResource(R.drawable.image9)
            "10" -> img.setImageResource(R.drawable.image10)
            "11" -> img.setImageResource(R.drawable.image11)
            "12" -> img.setImageResource(R.drawable.image12)
        }
    }


}