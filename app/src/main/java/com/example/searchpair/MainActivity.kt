package com.example.searchpair

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    var arrayImageViewsButtons = ArrayList<ImageView?>() //лист с кнопками
    var arrayTags = ArrayList<String?>() //лист с тагами (за конкретным тагом закреплена конкретная картинка)
    private var imageView1: ImageView? = null
    private var imageView2: ImageView? = null
    private var imageView3: ImageView? = null
    private var imageView4: ImageView? = null
    private var imageView5: ImageView? = null
    private var imageView6: ImageView? = null
    private var imageView7: ImageView? = null
    private var imageView8: ImageView? = null
    private var imageView9: ImageView? = null
    private var imageView10: ImageView? = null
    private var imageView11: ImageView? = null
    private var imageView12: ImageView? = null
    private var imageView13: ImageView? = null
    private var imageView14: ImageView? = null
    private var imageView15: ImageView? = null
    private var imageView16: ImageView? = null
    private var imageViewFirstCard: ImageView? = null
    private var imageViewTwoCard: ImageView? = null
    private var logoImage: ImageView? = null
    var animation1: Animation? = null
    var animation2: Animation? = null
    private var counterOpenedImages = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        for (str in arrayTags) {
            println(str)
        }
    }

    //инициализации блок
    private fun init() {
        animation1 = AnimationUtils.loadAnimation(this, R.anim.flip_to_middle)
        animation2 = AnimationUtils.loadAnimation(this, R.anim.flip_from_middle)
        logoImage = findViewById(R.id.idImageLogo)
        imageView1 = findViewById(R.id.idImage1)
        imageView2 = findViewById(R.id.idImage2)
        imageView3 = findViewById(R.id.idImage3)
        imageView4 = findViewById(R.id.idImage4)
        imageView5 = findViewById(R.id.idImage5)
        imageView6 = findViewById(R.id.idImage6)
        imageView7 = findViewById(R.id.idImage7)
        imageView8 = findViewById(R.id.idImage8)
        imageView9 = findViewById(R.id.idImage9)
        imageView10 = findViewById(R.id.idImage10)
        imageView11 = findViewById(R.id.idImage11)
        imageView12 = findViewById(R.id.idImage12)
        imageView13 = findViewById(R.id.idImage13)
        imageView14 = findViewById(R.id.idImage14)
        imageView15 = findViewById(R.id.idImage15)
        imageView16 = findViewById(R.id.idImage16)
        imageViewFirstCard = findViewById(R.id.idImageFirstCard)
        imageViewTwoCard = findViewById(R.id.idImageTwoCard)
        addToArrayImageViews()
        onClickImageViews()
        newGame()
        startNewGame()
    }

    //генерация игрового поля (новая игра)
    private fun newGame() {
        addTagsToList()
        arrayTags.shuffle()
        createTagsForImageViews()
        for (img in arrayImageViewsButtons) {
            img!!.setImageResource(R.drawable.imageshirt)
            img.visibility = View.VISIBLE
        }
        closeAllImages()
    }

    //начать новую игру
    private fun startNewGame() {
        logoImage!!.setOnClickListener { newGame() }
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
        arrayImageViewsButtons.add(imageView1)
        arrayImageViewsButtons.add(imageView2)
        arrayImageViewsButtons.add(imageView3)
        arrayImageViewsButtons.add(imageView4)
        arrayImageViewsButtons.add(imageView5)
        arrayImageViewsButtons.add(imageView6)
        arrayImageViewsButtons.add(imageView7)
        arrayImageViewsButtons.add(imageView8)
        arrayImageViewsButtons.add(imageView9)
        arrayImageViewsButtons.add(imageView10)
        arrayImageViewsButtons.add(imageView11)
        arrayImageViewsButtons.add(imageView12)
        arrayImageViewsButtons.add(imageView13)
        arrayImageViewsButtons.add(imageView14)
        arrayImageViewsButtons.add(imageView15)
        arrayImageViewsButtons.add(imageView16)
    }

    //присвоить таги для ImageViews
    private fun createTagsForImageViews() {
        arrayImageViewsButtons[0]!!.tag = arrayTags[0]
        arrayImageViewsButtons[1]!!.tag = arrayTags[1]
        arrayImageViewsButtons[2]!!.tag = arrayTags[2]
        arrayImageViewsButtons[3]!!.tag = arrayTags[3]
        arrayImageViewsButtons[4]!!.tag = arrayTags[4]
        arrayImageViewsButtons[5]!!.tag = arrayTags[5]
        arrayImageViewsButtons[6]!!.tag = arrayTags[6]
        arrayImageViewsButtons[7]!!.tag = arrayTags[7]
        arrayImageViewsButtons[8]!!.tag = arrayTags[8]
        arrayImageViewsButtons[9]!!.tag = arrayTags[9]
        arrayImageViewsButtons[10]!!.tag = arrayTags[10]
        arrayImageViewsButtons[11]!!.tag = arrayTags[11]
        arrayImageViewsButtons[12]!!.tag = arrayTags[12]
        arrayImageViewsButtons[13]!!.tag = arrayTags[13]
        arrayImageViewsButtons[14]!!.tag = arrayTags[14]
        arrayImageViewsButtons[15]!!.tag = arrayTags[15]
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
            println("Cards equals")
            imageViewFirstCard!!.visibility = View.INVISIBLE
            imageViewTwoCard!!.visibility = View.INVISIBLE
            counterOpenedImages = 0
        } else {
            //закрыть все карты
            if (counterOpenedImages == 2) {
                closeAllImages()
                counterOpenedImages = 0
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
            img!!.setImageResource(R.drawable.imageshirt)
            img.isClickable = true
        }
        imageViewFirstCard!!.isClickable = true
        imageViewTwoCard!!.isClickable = true
        //присвоить ресы по умолчанию
        imageViewFirstCard = findViewById(R.id.idImageFirstCard)
        imageViewTwoCard = findViewById(R.id.idImageTwoCard)
    }
}