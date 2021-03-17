package com.example.searchpair

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.searchpair.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var bindingClass: ActivityMainBinding

    var arrayImageViewsButtons = ArrayList<ImageView?>() //лист с кнопками
    var arrayTags = ArrayList<String?>() //лист с тагами (за конкретным тагом закреплена конкретная картинка)

    private var imageViewFirstCard: ImageView? = null
    private var imageViewTwoCard: ImageView? = null
    private var logoImage: ImageView? = null
    var animation1: Animation? = null
    var animation2: Animation? = null
    var animation3: Animation? = null
    var animation4: Animation? = null
    var animation5: Animation? = null
    private var counterOpenedImages = 0
    private lateinit var soundOpen: MediaPlayer
    private lateinit var soundClose: MediaPlayer
    private lateinit var soundDrop: MediaPlayer
    private lateinit var soundCrash: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
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
        logoImage = findViewById(R.id.idImageLogo)
        imageViewFirstCard = findViewById(R.id.idImageFirstCard)
        imageViewTwoCard = findViewById(R.id.idImageTwoCard)
        soundOpen = MediaPlayer.create(this, R.raw.stone_open)
        soundClose = MediaPlayer.create(this, R.raw.stone_close)
        soundDrop = MediaPlayer.create(this, R.raw.stone_drop)
        soundCrash = MediaPlayer.create(this, R.raw.stone_crash)
        //заполнение массива + слушатели нажатий
        addToArrayImageViews()
        onClickImageViews()
        startNewGame()

        newGame()

    }
    //воспроизведение звука
    private fun soundPlay(sound: MediaPlayer){
        sound.start()
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
    }

    //начать новую игру
    private fun startNewGame() {
        logoImage!!.setOnClickListener {
            soundPlay(soundDrop)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
            finish()
        }
    }

    //заполнить лист тагов
    private fun addTagsToList() {
        for (i in 1..8) {
            arrayTags.add(i.toString())
        }
        for (i in 1..8) {
            arrayTags.add(i.toString())
        }
    }

    //Заполнение массива с кнопками (для присвоения слушателей нажатий через цикл for-each)
    private fun addToArrayImageViews() {
        arrayImageViewsButtons.add(bindingClass.idImage1)
        arrayImageViewsButtons.add(bindingClass.idImage2)
        arrayImageViewsButtons.add(bindingClass.idImage3)
        arrayImageViewsButtons.add(bindingClass.idImage4)
        arrayImageViewsButtons.add(bindingClass.idImage5)
        arrayImageViewsButtons.add(bindingClass.idImage6)
        arrayImageViewsButtons.add(bindingClass.idImage7)
        arrayImageViewsButtons.add(bindingClass.idImage8)
        arrayImageViewsButtons.add(bindingClass.idImage9)
        arrayImageViewsButtons.add(bindingClass.idImage10)
        arrayImageViewsButtons.add(bindingClass.idImage11)
        arrayImageViewsButtons.add(bindingClass.idImage12)
        arrayImageViewsButtons.add(bindingClass.idImage13)
        arrayImageViewsButtons.add(bindingClass.idImage14)
        arrayImageViewsButtons.add(bindingClass.idImage15)
        arrayImageViewsButtons.add(bindingClass.idImage16)
    }

    //присвоить таги для ImageViews
    private fun createTagsForImageViews() {
        for (i in 0..15){
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

                println("Clicked ImageView Tag:  " + img.tag)
                println("first card:  " + imageViewFirstCard!!.tag)
                println("two card:  " + imageViewTwoCard!!.tag)
            }
        }
    }

    //сравнить открытые картинки
    private fun checkCards() {
        if (imageViewFirstCard!!.tag == imageViewTwoCard!!.tag) {
            soundPlay(soundCrash)
            imageViewFirstCard!!.startAnimation(animation5)
            imageViewTwoCard!!.startAnimation(animation5)
            println("Cards equals")
            imageViewFirstCard!!.visibility = View.INVISIBLE
            imageViewTwoCard!!.visibility = View.INVISIBLE
            counterOpenedImages = 0
        } else {
            //закрыть все карты
            if (counterOpenedImages == 2) {
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