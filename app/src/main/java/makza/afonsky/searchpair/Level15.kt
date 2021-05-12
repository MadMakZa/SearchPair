package makza.afonsky.searchpair

import android.animation.ObjectAnimator
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import makza.afonsky.searchpair.databinding.ActivityGameFieldBinding
import java.util.*

/**
 *  Средняя сложность - найти трио
 */

class Level15 : AppCompatActivity() {

    private lateinit var bindingClass: ActivityGameFieldBinding

    private var linearLayout: LinearLayout? = null

    var arrayImageViewsButtons = ArrayList<ImageView?>() //лист с кнопками
    var arrayTags = ArrayList<String?>() //лист с тагами (за конкретным тагом закреплена конкретная картинка)

    private var imageViewFirstCard: ImageView? = null
    private var imageViewTwoCard: ImageView? = null
    private var imageViewThreeCard: ImageView? = null
    private lateinit var btnNewGame: Button
    var animation1: Animation? = null
    var animation2: Animation? = null
    var animation3: Animation? = null
    var animation4: Animation? = null
    var animation5: Animation? = null
    private var counterOpenedImages = 0
    private var counterPairs = 0
    private var health = 0
    private var healthMax = 221
    private var cheatCounter = 0
    private var healthKitRegen = 20
    //набор звуков с айдишниками
    private var soundPool: SoundPool? = null
    private var buttonClose = 1
    private var soundDrop = 2
    private var soundCrash = 3
    private var soundClose = 4
    private var soundOpen = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityGameFieldBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        //реклама
        MobileAds.initialize(this)
        val adRequest: AdRequest = AdRequest.Builder().build()
        bindingClass.adView.loadAd(adRequest)

        init()

    }

    //инициализации блок
    private fun init() {
        animation1 = AnimationUtils.loadAnimation(this, R.anim.flip_to_middle)
        animation2 = AnimationUtils.loadAnimation(this, R.anim.flip_from_middle)
        animation3 = AnimationUtils.loadAnimation(this, R.anim.flip_to_middle)
        animation4 = AnimationUtils.loadAnimation(this, R.anim.flip_from_middle)
        animation5 = AnimationUtils.loadAnimation(this, R.anim.anim_scale)
        btnNewGame = findViewById(R.id.btn_new_game)
        imageViewFirstCard = findViewById(R.id.idImageFirstCard)
        imageViewTwoCard = findViewById(R.id.idImageTwoCard)
        imageViewThreeCard = findViewById(R.id.idImageThreeCard)
        //загрузка звуков
        soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        soundPool!!.load(baseContext, R.raw.close, 1)       //buttonClose
        soundPool!!.load(baseContext, R.raw.stone_drop, 1)  //soundDrop
        soundPool!!.load(baseContext, R.raw.stone_crash, 1) //soundCrash
        soundPool!!.load(baseContext, R.raw.stone_close, 1) //soundClose
        soundPool!!.load(baseContext, R.raw.stone_open, 1)  //soundOpen

        bindingClass.idSetTextLevel.setText(R.string.name_level_15)
        //шкала здоровья
        bindingClass.progressBar.max = healthMax
        //хранилище монеток
        linearLayout = findViewById(R.id.layout_restore_health)
        //заполнение массива + слушатели нажатий
        addToArrayImageViews()
        onClickImageViews()
        startNewGame()

        newGame()

        activateCheatHp()
        exchangeCoins()
        addHealthKitToBar()

    }
    /**
     * Аптечки
     */
    //обменник валют
    private fun exchangeCoins(){
        var exchanged = 0
        var exchangedDelete = 0
        val smallKit = getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
            .getInt("HealthKitSmall",0)
        val mediumKit = getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
            .getInt("HealthKitMedium",0)
        if(smallKit != 0) {
            if (smallKit in 3..5 && mediumKit < 6){
                exchanged = 1
                exchangedDelete = 3
            }
            if (smallKit == 6 && mediumKit < 5){
                exchanged = 2
                exchangedDelete = 6
            }
            //сохранить пересчитанные монеты в копилку medium
            val countHealthKitMedium = getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
                .getInt("HealthKitMedium",0)
            getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
                .edit()
                .putInt("HealthKitMedium", countHealthKitMedium + exchanged)
                .apply()
            //удалить пересчитанные монеты из копилки small
            val countHealthKitSmall = getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
                .getInt("HealthKitSmall",0)
            getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
                .edit()
                .putInt("HealthKitSmall", countHealthKitSmall - exchangedDelete)
                .apply()

        }

    }
    //показать аптечки на экране
    private fun addHealthKitToBar(){
        //собрано аптечек
        val bonusesAccumulated = getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
            .getInt("HealthKitMedium",0)
        //если есть бонусные аптчеки, добавить их на экран
        if (bonusesAccumulated > 0) {
            for (count in 1..bonusesAccumulated) {
                generateHealthKit()
            }
        }
    }
    //Генерация картинок аптечек
    private fun generateHealthKit() {
        val img = ImageView(this)
        linearLayout!!.addView(img)
        val params = img.layoutParams as LinearLayout.LayoutParams
        params.width = 125
        params.height = 125
        img.setImageResource(R.drawable.restorehealth2)
        img.layoutParams = params
        img.startAnimation(animation5)
        //при нажатии на аптечку
        img.setOnClickListener {
            soundPlay(buttonClose)
            health -= healthKitRegen
            img.visibility = View.GONE
            ObjectAnimator.ofInt(bindingClass.progressBar, "progress", health)
                .setDuration(1000)
                .start()
            //удалить одну аптечку
            var countHealthKit = getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
                .getInt("HealthKitMedium",0)
            countHealthKit--
            getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
                .edit()
                .putInt("HealthKitMedium", countHealthKit)
                .apply()
        }
    }
    /**
     * Чит восполнить здоровье
     */
    private fun activateCheatHp(){
        bindingClass.progressBar.setOnClickListener {
            cheatCounter++
            if (cheatCounter >= 20){
                soundPlay(buttonClose)
                health = 0
                ObjectAnimator.ofInt(bindingClass.progressBar, "progress", health)
                        .setDuration(1000)
                        .start()

                cheatCounter = 0
            }

        }
    }
    //вернуться в меню
    override fun onBackPressed() {
        soundPlay(soundDrop)
        val intent = Intent(this, GameMainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
        finish()
    }
    //сохранение прогресса
    private fun saveProgress(){
        val currentSave = getSharedPreferences("Save", MODE_PRIVATE)
                .getInt("Level",1)

        if (currentSave < 16) {
            getSharedPreferences("Save", MODE_PRIVATE)
                    .edit()
                    .putInt("Level", 16)
                    .apply()
        }
        //добавить аптечку на следующий уровень
        var countHealthKit = getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
            .getInt("HealthKitMedium",0)
        if (countHealthKit in 0..5) {
            countHealthKit++
            getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
                .edit()
                .putInt("HealthKitMedium", countHealthKit)
                .apply()
        }
    }
    //получить урон
    private fun healthDamaged(){
        health +=10
        ObjectAnimator.ofInt(bindingClass.progressBar, "progress", health)
                .setDuration(1000)
                .start()
        //если шкала заполнилась запустить по-новой уровень
        if (health > healthMax){
            soundPlay(soundDrop)
            val intent = Intent(this, Level15::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
            finish()
        }

    }
    //восстановить здоровье
    private fun healthRestore(){
        if(health <= 20) health = 0
        if (health >= 20) health -=20

        ObjectAnimator.ofInt(bindingClass.progressBar, "progress", health)
                .setDuration(1000)
                .start()
    }
    //воспроизведение звука
    private fun soundPlay(id: Int){
        soundPool?.play(id, 1f, 1f, 0,0,1f)
    }

    //генерация игрового поля (новая игра)
    private fun newGame() {
        addTagsToList()
        arrayTags.shuffle()
        createTagsForImageViews()
        for (img in arrayImageViewsButtons) {
            img!!.visibility = View.VISIBLE
        }
        closeAllImages()
        counterPairs = 0
        btnNewGame!!.visibility = View.INVISIBLE
    }

    //начать новую игру по нажатию на лого
    private fun startNewGame() {
        btnNewGame!!.setOnClickListener {
            soundPlay(soundDrop)
            val intent = Intent(this, Level16::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
            finish()
        }
    }

    //заполнить лист тагов
    private fun addTagsToList() {
        for (i in 1..11) {
            arrayTags.add(i.toString())
        }
        for (i in 1..11) {
            arrayTags.add(i.toString())
        }
        for (i in 1..11) {
            arrayTags.add(i.toString())
        }
    }

    //Заполнение массива с кнопками (для присвоения слушателей нажатий через цикл for-each)
    private fun addToArrayImageViews() {
        //колонка 1
        arrayImageViewsButtons.add(bindingClass.idColumn1Image1)
        arrayImageViewsButtons.add(bindingClass.idColumn1Image2)
        arrayImageViewsButtons.add(bindingClass.idColumn1Image3)
        arrayImageViewsButtons.add(bindingClass.idColumn1Image4)
        arrayImageViewsButtons.add(bindingClass.idColumn1Image5)
        arrayImageViewsButtons.add(bindingClass.idColumn1Image6)
        //колонка 2
        arrayImageViewsButtons.add(bindingClass.idColumn2Image1)
        arrayImageViewsButtons.add(bindingClass.idColumn2Image2)
        arrayImageViewsButtons.add(bindingClass.idColumn2Image3)
        arrayImageViewsButtons.add(bindingClass.idColumn2Image4)
        arrayImageViewsButtons.add(bindingClass.idColumn2Image5)
        arrayImageViewsButtons.add(bindingClass.idColumn2Image6)
        arrayImageViewsButtons.add(bindingClass.idColumn2Image7)
        //колонка 3
        arrayImageViewsButtons.add(bindingClass.idColumn3Image1)
        arrayImageViewsButtons.add(bindingClass.idColumn3Image2)
        arrayImageViewsButtons.add(bindingClass.idColumn3Image3)
        arrayImageViewsButtons.add(bindingClass.idColumn3Image4)
        arrayImageViewsButtons.add(bindingClass.idColumn3Image5)
        arrayImageViewsButtons.add(bindingClass.idColumn3Image6)
        arrayImageViewsButtons.add(bindingClass.idColumn3Image7)
        //колонка 4
        arrayImageViewsButtons.add(bindingClass.idColumn4Image1)
        arrayImageViewsButtons.add(bindingClass.idColumn4Image2)
        arrayImageViewsButtons.add(bindingClass.idColumn4Image3)
        arrayImageViewsButtons.add(bindingClass.idColumn4Image4)
        arrayImageViewsButtons.add(bindingClass.idColumn4Image5)
        arrayImageViewsButtons.add(bindingClass.idColumn4Image6)
        arrayImageViewsButtons.add(bindingClass.idColumn4Image7)
        //колонка 5
        arrayImageViewsButtons.add(bindingClass.idColumn5Image1)
        arrayImageViewsButtons.add(bindingClass.idColumn5Image2)
        arrayImageViewsButtons.add(bindingClass.idColumn5Image3)
        arrayImageViewsButtons.add(bindingClass.idColumn5Image4)
        arrayImageViewsButtons.add(bindingClass.idColumn5Image5)
        arrayImageViewsButtons.add(bindingClass.idColumn5Image6)

    }

    //присвоить таги для ImageViews из листа с тагами
    private fun createTagsForImageViews() {
        for (i in 0..32){
            arrayImageViewsButtons[i]!!.tag = arrayTags[i]
        }
    }

    //открыть карту
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

    //слушатель нажатий для картинок
    private fun onClickImageViews() {
        for (img in arrayImageViewsButtons) {
            img!!.setOnClickListener {
                    //запуск первой половины анимации
                    img.startAnimation(animation1)
                    animation1!!.setAnimationListener(object : AnimationListener {
                        override fun onAnimationStart(animation: Animation) {
                            soundPlay(soundOpen)
                            blockAllButtons(true)
                        }
                        override fun onAnimationEnd(animation: Animation) {
                            //запуск второй половины анимации
                            img.startAnimation(animation2)
                            animation2!!.setAnimationListener(object : AnimationListener {
                                override fun onAnimationStart(animation: Animation) {
                                    openCard(img)
                                }
                                override fun onAnimationEnd(animation: Animation) {
                                    //присваивание
                                    if (counterOpenedImages == 0) {
                                        imageViewFirstCard = img
                                        blockAllButtons(false)
                                        imageViewFirstCard!!.isClickable = false
                                        println("Clicked first card, TAG = ${imageViewFirstCard!!.getTag()}")
                                    }
                                    if (counterOpenedImages == 1) {
                                        imageViewTwoCard = img
                                        blockAllButtons(false)
                                        imageViewFirstCard!!.isClickable = false
                                        imageViewTwoCard!!.isClickable = false
                                        println("Clicked two card, TAG = ${imageViewTwoCard!!.getTag()}")
                                    }
                                    if (counterOpenedImages == 2) {
                                        imageViewThreeCard = img
                                        blockAllButtons(false)
                                        imageViewFirstCard!!.isClickable = false
                                        imageViewTwoCard!!.isClickable = false
                                        imageViewThreeCard!!.isClickable = false
                                        println("Clicked three card, TAG = ${imageViewThreeCard!!.getTag()}")
                                    }
                                    counterOpenedImages++
                                    checkCards()

                                }

                                override fun onAnimationRepeat(animation: Animation) {}
                            })
                        }
                        override fun onAnimationRepeat(animation: Animation) {}
                    })

            }
        }
    }

    //сравнить открытые картинки
    private fun checkCards() {

        if (imageViewFirstCard!!.tag == imageViewTwoCard!!.tag
                && imageViewThreeCard!!.tag == imageViewFirstCard!!.tag) {
            healthRestore()
            //уничтожить 3 совпадающие
            soundPlay(soundCrash)
            imageViewFirstCard!!.startAnimation(animation5)
            imageViewTwoCard!!.startAnimation(animation5)
            imageViewThreeCard!!.startAnimation(animation5)
            println("Cards equals")
            imageViewFirstCard!!.visibility = View.INVISIBLE
            imageViewTwoCard!!.visibility = View.INVISIBLE
            imageViewThreeCard!!.visibility = View.INVISIBLE
            counterOpenedImages = 0
            counterPairs++
            println("counter pairs = $counterPairs")
            //если поле пустое
            if (counterPairs == 11){
                saveProgress()
                //показать кнопку новой игры
                btnNewGame!!.visibility = View.VISIBLE
                bindingClass.idLevelComplete.visibility = View.VISIBLE

            }

        } else {
            //закрыть все карты если 2 открыты
            if (counterOpenedImages == 2
                    && imageViewFirstCard!!.tag != imageViewTwoCard!!.tag) {
                healthDamaged()
                imageViewFirstCard!!.startAnimation(animation3)
                imageViewTwoCard!!.startAnimation(animation3)
                animation3!!.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        soundPlay(soundClose)
                        blockAllButtons(true)
                    }
                    override fun onAnimationEnd(animation: Animation) {
                        //запуск второй половины анимации
                        imageViewFirstCard!!.startAnimation(animation4)
                        imageViewTwoCard!!.startAnimation(animation4)
                        animation4!!.setAnimationListener(object : AnimationListener {
                            override fun onAnimationStart(animation: Animation) {
                                imageViewFirstCard!!.setImageResource(R.drawable.shirtpurple)
                                imageViewTwoCard!!.setImageResource(R.drawable.shirtred)

                            }
                            override fun onAnimationEnd(animation: Animation) {
                                blockAllButtons(false)
                                //присвоить ресы по умолчанию
                                imageViewFirstCard = findViewById(R.id.idImageFirstCard)
                                imageViewTwoCard = findViewById(R.id.idImageTwoCard)
                                imageViewFirstCard!!.visibility = View.GONE
                                imageViewTwoCard!!.visibility = View.GONE

                            }

                            override fun onAnimationRepeat(animation: Animation) {}
                        })
                    }
                    override fun onAnimationRepeat(animation: Animation) {}
                })

                counterOpenedImages = 0
                imageViewFirstCard!!.isClickable = true
                imageViewTwoCard!!.isClickable = true
                imageViewThreeCard!!.isClickable = true

            }
            //закрыть все карты
            if (counterOpenedImages == 3) {
                healthDamaged()
                imageViewFirstCard!!.startAnimation(animation3)
                imageViewTwoCard!!.startAnimation(animation3)
                imageViewThreeCard!!.startAnimation(animation3)
                animation3!!.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        soundPlay(soundClose)
                        blockAllButtons(true)
                    }
                    override fun onAnimationEnd(animation: Animation) {
                        //запуск второй половины анимации
                        imageViewFirstCard!!.startAnimation(animation4)
                        imageViewTwoCard!!.startAnimation(animation4)
                        imageViewThreeCard!!.startAnimation(animation4)
                        animation4!!.setAnimationListener(object : AnimationListener {
                            override fun onAnimationStart(animation: Animation) {
                                imageViewFirstCard!!.setImageResource(R.drawable.shirtblue)
                                imageViewTwoCard!!.setImageResource(R.drawable.shirtpurple)
                                imageViewThreeCard!!.setImageResource(R.drawable.shirtred)
                            }
                            override fun onAnimationEnd(animation: Animation) {
                                blockAllButtons(false)
                                //присвоить ресы по умолчанию
                                imageViewFirstCard = findViewById(R.id.idImageFirstCard)
                                imageViewTwoCard = findViewById(R.id.idImageTwoCard)
                                imageViewThreeCard = findViewById(R.id.idImageThreeCard)
                                imageViewFirstCard!!.visibility = View.GONE
                                imageViewTwoCard!!.visibility = View.GONE
                                imageViewThreeCard!!.visibility = View.GONE
                            }

                            override fun onAnimationRepeat(animation: Animation) {}
                        })
                    }
                    override fun onAnimationRepeat(animation: Animation) {}
                })

                counterOpenedImages = 0
                imageViewFirstCard!!.isClickable = true
                imageViewTwoCard!!.isClickable = true
                imageViewThreeCard!!.isClickable = true

            }
        }
    }
    //блокировка и разблокировка кнопок
    private fun blockAllButtons(bol: Boolean) {
        when (bol) {
            false -> for (img in arrayImageViewsButtons) {
                img!!.isClickable = true
            }
            true -> for (img in arrayImageViewsButtons) {
                img!!.isClickable = false
            }
        }
    }
    //закрыть все карты
    private fun closeAllImages() {
        for (img in arrayImageViewsButtons) {
            img!!.startAnimation(animation3)
            img.setImageResource(R.drawable.shirtpurple)
            img.isClickable = true
        }
        imageViewFirstCard!!.isClickable = true
        imageViewTwoCard!!.isClickable = true
        imageViewThreeCard!!.isClickable = true
        //присвоить ресы по умолчанию
        imageViewFirstCard = findViewById(R.id.idImageFirstCard)
        imageViewTwoCard = findViewById(R.id.idImageTwoCard)
        imageViewThreeCard = findViewById(R.id.idImageThreeCard)
    }
}