package com.example.searchpair


import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.example.searchpair.databinding.ActivityGameMainBinding


class GameMainActivity : AppCompatActivity() {


    private lateinit var bindingClass: ActivityGameMainBinding

    private lateinit var buttonClose: MediaPlayer
    private lateinit var soundDrop: MediaPlayer

    private lateinit var save: SharedPreferences
    private var levelProgress = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityGameMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        buttonClose = MediaPlayer.create(this, R.raw.close)
        soundDrop = MediaPlayer.create(this, R.raw.stone_drop)


        save = getSharedPreferences("Save", MODE_PRIVATE) //коробка с сейвами
        levelProgress = save.getInt("Level", 1)


        startNewGame()
        chooseLevel()

    }

    //воспроизведение звука
    private fun soundPlay(sound: MediaPlayer){
        sound.start()
    }

    //начать новую игру по нажатию на кнопку
    private fun startNewGame() {
        bindingClass.btnNewGame.setOnClickListener {
            //обнулить прогресс
            val save = getSharedPreferences("Save", MODE_PRIVATE) //получить доступ к коробке
            val editor = save.edit()
            editor.putInt("Level", 1) //положить в коробку результат
            editor.apply()          //сохранить

            soundPlay(soundDrop)
            val intentStart = Intent(this, Level1::class.java)
            startActivity(intentStart)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
    }

    //выбор уровня
    private fun chooseLevel() {

        if(levelProgress >= 1) {
            bindingClass.btnLevel1.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level1::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
        }
        }
        if(levelProgress >= 2) {
            bindingClass.btnLevel2.isClickable
            bindingClass.btnLevel2.text = "2"
            bindingClass.btnLevel2.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level2::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 3){
            bindingClass.btnLevel3.isClickable
            bindingClass.btnLevel3.text = "3"
            bindingClass.btnLevel3.setOnClickListener{
                soundPlay(soundDrop)
                val intent = Intent(this, Level3::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 4){
            bindingClass.btnLevel4.isClickable
            bindingClass.btnLevel4.text = "4"
            bindingClass.btnLevel4.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level4::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 5){
            bindingClass.btnLevel5.isClickable
            bindingClass.btnLevel5.isClickable
            bindingClass.btnLevel5.text = "5"
            bindingClass.btnLevel5.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level5::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 6){
            bindingClass.btnLevel6.isClickable
            bindingClass.btnLevel6.text = "6"
            bindingClass.btnLevel6.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level6::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 7){
            bindingClass.btnLevel7.isClickable
            bindingClass.btnLevel7.text = "7"
            bindingClass.btnLevel7.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level7::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 8){
            bindingClass.btnLevel8.isClickable
            bindingClass.btnLevel8.text = "8"
            bindingClass.btnLevel8.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level8::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 9){
            bindingClass.btnLevel9.isClickable
            bindingClass.btnLevel9.text = "9"
            bindingClass.btnLevel9.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level9::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 10){
            bindingClass.btnLevel10.isClickable
            bindingClass.btnLevel10.text = "10"
            bindingClass.btnLevel10.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level10::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 11){
            bindingClass.btnLevel11.isClickable
            bindingClass.btnLevel11.text = "11"
            bindingClass.btnLevel11.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level11::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 12){
            bindingClass.btnLevel12.isClickable
            bindingClass.btnLevel12.text = "12"
            bindingClass.btnLevel12.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level12::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 13){
            bindingClass.btnLevel13.isClickable
            bindingClass.btnLevel13.text = "13"
            bindingClass.btnLevel13.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level13::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 14){
            bindingClass.btnLevel14.isClickable
            bindingClass.btnLevel14.text = "14"
            bindingClass.btnLevel14.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level14::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 15){
            bindingClass.btnLevel15.isClickable
            bindingClass.btnLevel15.text = "15"
            bindingClass.btnLevel15.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level15::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }
        }
        if(levelProgress >= 16){
            bindingClass.btnLevel16.isClickable
            bindingClass.btnLevel16.text = "16"
            bindingClass.btnLevel16.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level16::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }

        }
    }

}