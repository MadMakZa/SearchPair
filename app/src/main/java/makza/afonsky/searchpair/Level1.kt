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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import makza.afonsky.searchpair.databinding.ActivityGameFieldBinding
import java.util.*

class Level1 : AppCompatActivity() {

    private lateinit var bindingClass: ActivityGameFieldBinding

    private var linearLayout: LinearLayout? = null


    //набор звуков с айдишниками
    private var soundPool: SoundPool? = null
    private var buttonClose = 1
    private var soundDrop = 2
    private var soundCrash = 3
    private var soundClose = 4
    private var soundOpen = 5

    var arrayImageViewsButtons = ArrayList<ImageView?>() //лист с кнопками
    var arrayTags = ArrayList<String?>() //лист с тагами (за конкретным тагом закреплена конкретная картинка)

    private var imageViewFirstCard: ImageView? = null
    private var imageViewTwoCard: ImageView? = null
    var animation1: Animation? = null
    var animation2: Animation? = null
    var animation3: Animation? = null
    var animation4: Animation? = null
    var animation5: Animation? = null
    var animation777: Animation? = null
    private var counterOpenedImages = 0
    private var counterPairs = 0
    private var health = 0
    private var healthMax = 65
    private var cheatCounter = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityGameFieldBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        init()

    }

    //инициализации блок
    private fun init() {
        animation1 = AnimationUtils.loadAnimation(this, R.anim.flip_to_middle)
        animation2 = AnimationUtils.loadAnimation(this, R.anim.flip_from_middle)
        animation3 = AnimationUtils.loadAnimation(this, R.anim.flip_to_middle)
        animation4 = AnimationUtils.loadAnimation(this, R.anim.flip_from_middle)
        animation5 = AnimationUtils.loadAnimation(this, R.anim.anim_scale)
        animation777 = AnimationUtils.loadAnimation(this, R.anim.anim_bonus)

        imageViewFirstCard = findViewById(R.id.idImageFirstCard)
        imageViewTwoCard = findViewById(R.id.idImageTwoCard)
        //загрузка звуков
        soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        soundPool!!.load(baseContext, R.raw.close, 1)       //buttonClose
        soundPool!!.load(baseContext, R.raw.stone_drop, 1)  //soundDrop
        soundPool!!.load(baseContext, R.raw.stone_crash, 1) //soundCrash
        soundPool!!.load(baseContext, R.raw.stone_close, 1) //soundClose
        soundPool!!.load(baseContext, R.raw.stone_open, 1)  //soundOpen

        bindingClass.idSetTextLevel.setText(R.string.name_level_1)
        //шкала здоровья
        bindingClass.progressBar.max = healthMax


        linearLayout = findViewById(R.id.layout_restore_health)


        //заполнение массива + слушатели нажатий
        addToArrayImageViews()
        onClickImageViews()
        startNewGame()

        newGame()

        activateCheatHp()
        addHealthKitToBar()

    }
    //показать аптечки на экране
    private fun addHealthKitToBar(){
        //собрано аптечек
        var bonusesAccumulated = getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
            .getInt("HealthKit",0)
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
        img.setImageResource(R.drawable.restorehealth)
        img.layoutParams = params
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

        if (currentSave < 2) {
            getSharedPreferences("Save", MODE_PRIVATE)
                    .edit()
                    .putInt("Level", 2)
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
            val intent = Intent(this, Level1::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
            finish()
        }

    }
    //восстановить здоровье
    private fun healthRestore(){
        if(health <= 10) health = 0
        if (health >= 10) health -=10

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
        bindingClass.btnNewGame!!.visibility = View.INVISIBLE
    }

    //начать новую игру по нажатию на кнопку
    private fun startNewGame() {
        bindingClass.btnNewGame!!.setOnClickListener {
            soundPlay(soundDrop)
            val intent = Intent(this, Level2::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
            finish()
        }
    }

    //заполнить лист тагов
    private fun addTagsToList() {
        for (i in 1..6) {
            arrayTags.add(i.toString())
        }
        for (i in 1..6) {
            arrayTags.add(i.toString())
        }
    }

    //Заполнение массива с кнопками (для присвоения слушателей нажатий через цикл for-each)
    private fun addToArrayImageViews() {
        //колонка 1
        arrayImageViewsButtons.add(bindingClass.idColumn1Image1)
        arrayImageViewsButtons.add(bindingClass.idColumn1Image2)
        arrayImageViewsButtons.add(bindingClass.idColumn1Image3)
        //колонка 2
        arrayImageViewsButtons.add(bindingClass.idColumn2Image1)
        arrayImageViewsButtons.add(bindingClass.idColumn2Image2)
        arrayImageViewsButtons.add(bindingClass.idColumn2Image3)
        //колонка 3
        arrayImageViewsButtons.add(bindingClass.idColumn3Image1)
        arrayImageViewsButtons.add(bindingClass.idColumn3Image2)
        arrayImageViewsButtons.add(bindingClass.idColumn3Image3)
        //колонка 4
        arrayImageViewsButtons.add(bindingClass.idColumn4Image1)
        arrayImageViewsButtons.add(bindingClass.idColumn4Image2)
        arrayImageViewsButtons.add(bindingClass.idColumn4Image3)

    }

    //присвоить таги для ImageViews из листа с тагами
    private fun createTagsForImageViews() {
        for (i in 0..11){
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
                                    }
                                    if (counterOpenedImages == 1) {
                                        imageViewTwoCard = img
                                        blockAllButtons(false)
                                        imageViewTwoCard!!.isClickable = false
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
        if (imageViewFirstCard!!.tag == imageViewTwoCard!!.tag) {
            healthRestore()
            soundPlay(soundCrash)
            imageViewFirstCard!!.startAnimation(animation5)
            imageViewTwoCard!!.startAnimation(animation5)
            println("Cards equals")
            imageViewFirstCard!!.visibility = View.INVISIBLE
            imageViewTwoCard!!.visibility = View.INVISIBLE
            counterOpenedImages = 0
            counterPairs++
            println("counter pairs = $counterPairs")
            //если поле пустое
            if (counterPairs == 6){
                //сохранить прогресс
                saveProgress()
                //показать кнопку новой игры
                bindingClass.btnNewGame!!.visibility = View.VISIBLE
                bindingClass.idLevelComplete.visibility = View.VISIBLE

            }
        } else {
            //закрыть все карты
            if (counterOpenedImages == 2) {
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
                                imageViewFirstCard!!.setImageResource(R.drawable.imageshirt)
                                imageViewTwoCard!!.setImageResource(R.drawable.imageshirt)
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
            img.setImageResource(R.drawable.imageshirt)
            img.isClickable = true
        }
        imageViewFirstCard!!.isClickable = true
        imageViewTwoCard!!.isClickable = true
        //присвоить ресы по умолчанию
        imageViewFirstCard = findViewById(R.id.idImageFirstCard)
        imageViewTwoCard = findViewById(R.id.idImageTwoCard)
    }
}