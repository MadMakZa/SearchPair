package makza.afonsky.searchpair


import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import makza.afonsky.searchpair.databinding.ActivityGameMainBinding


class GameMainActivity : AppCompatActivity() {


    private lateinit var bindingClass: ActivityGameMainBinding
    private var buttonLevelsArray = ArrayList<Button?>()

    //набор звуков с айдишниками
    private var soundPool: SoundPool? = null
    private var buttonClose = 1
    private var soundDrop = 2

    //сейвы уровней
    private lateinit var save: SharedPreferences
    private var levelProgress = 0
    private var cheatCounter = 0
    //сейв бонусного восстановления здоровья
    private lateinit var bonusHealth: SharedPreferences
    private var healthKitSmallAccumulated = 0
    private var healthKitMediumAccumulated = 0
    private var healthKitBigAccumulated = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityGameMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        addLvlButtonsToArray()
        //загрузка звуков
        soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        soundPool!!.load(baseContext, R.raw.close, 1)
        soundPool!!.load(baseContext, R.raw.stone_drop, 1)

        //коробка с сейвами
        save = getSharedPreferences("Save", MODE_PRIVATE)
        levelProgress = save.getInt("Level", 1)
        //коробка с аптечками
        bonusHealth = getSharedPreferences("bonusHealthSave", MODE_PRIVATE)
        healthKitSmallAccumulated = bonusHealth.getInt("HealthKitSmall", 0)
        healthKitMediumAccumulated = bonusHealth.getInt("HealthKitMedium", 0)
        healthKitBigAccumulated = bonusHealth.getInt("HealthKitBig", 0)



        startNewGame()
        chooseLevel()
        activateCheat()


    }

    /**
     * Чит открыть все уровни
     */
    private fun activateCheat(){
        bindingClass.idTextSelectLevel.setOnClickListener {
            cheatCounter++
            if (cheatCounter >= 30){
                val save = getSharedPreferences("Save", MODE_PRIVATE) //получить доступ к коробке
                val editor = save.edit()
                editor.putInt("Level", 25) //положить в коробку результат
                editor.apply()          //сохранить

                soundPlay(buttonClose)
                val intentStart = Intent(this, GameMainActivity::class.java)
                startActivity(intentStart)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()

                cheatCounter = 0
            }

        }
    }

    //воспроизведение звука
    private fun soundPlay(id: Int){
        soundPool?.play(id, 1f, 1f, 0,0,1f)
    }
    //выйти из игры
    override fun onBackPressed() {
        soundPlay(soundDrop)
        val dialogExit = Dialog(this)
        dialogExit.setContentView(R.layout.dialog_reset_progress)
        dialogExit.setTitle("Exit Game")
        dialogExit.show()

        val textViewExitGame = dialogExit.findViewById<TextView>(R.id.questionTextView)
        val buttonYes = dialogExit.findViewById<Button>(R.id.button_yes)
        val buttonNo = dialogExit.findViewById<Button>(R.id.button_no)
        textViewExitGame.text = "Exit Game?"


        buttonYes.setOnClickListener {
            soundPlay(soundDrop)
            dialogExit.dismiss()
            super.onBackPressed()
        }
        buttonNo.setOnClickListener {
            soundPlay(soundDrop)
            dialogExit.dismiss()
        }

    }
    //показать диалог
    private fun startDialogResetProgress(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_reset_progress)
        dialog.setTitle("New Game")
        dialog.show()

        val buttonYes = dialog.findViewById<Button>(R.id.button_yes)
        val buttonNo = dialog.findViewById<Button>(R.id.button_no)

        buttonYes.setOnClickListener {
            //обнулить прогресс
            val save = getSharedPreferences("Save", MODE_PRIVATE) //получить доступ к коробке
            val editor = save.edit()
            editor.putInt("Level", 1) //положить в коробку результат
            editor.apply()          //сохранить
            dialog.dismiss()
            //запуск первого уровня
            startFirstLevel()
        }
        buttonNo.setOnClickListener {
            soundPlay(soundDrop)
            dialog.dismiss()
        }
    }

    //начать новую игру по нажатию на кнопку
    private fun startNewGame() {
        bindingClass.btnNewGame.setOnClickListener {

            if (levelProgress >= 2) {
                soundPlay(soundDrop)
                startDialogResetProgress()
            }else{
                //запуск первого уровня
                startFirstLevel()
            }
        }
    }
    //старт первого уровня
    private fun startFirstLevel(){
        soundPlay(soundDrop)
        val intentStart = Intent(this, Level1::class.java)
        startActivity(intentStart)
        overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
        finish()
    }
    //добавление кнопок в аррай лист
    private fun addLvlButtonsToArray(){
        buttonLevelsArray.add(bindingClass.btnLevel1)
        buttonLevelsArray.add(bindingClass.btnLevel2)
        buttonLevelsArray.add(bindingClass.btnLevel3)
        buttonLevelsArray.add(bindingClass.btnLevel4)
        buttonLevelsArray.add(bindingClass.btnLevel5)
        buttonLevelsArray.add(bindingClass.btnLevel6)
        buttonLevelsArray.add(bindingClass.btnLevel7)
        buttonLevelsArray.add(bindingClass.btnLevel8)
        buttonLevelsArray.add(bindingClass.btnLevel9)
        buttonLevelsArray.add(bindingClass.btnLevel10)
        buttonLevelsArray.add(bindingClass.btnLevel11)
        buttonLevelsArray.add(bindingClass.btnLevel12)
        buttonLevelsArray.add(bindingClass.btnLevel13)
        buttonLevelsArray.add(bindingClass.btnLevel14)
        buttonLevelsArray.add(bindingClass.btnLevel15)
        buttonLevelsArray.add(bindingClass.btnLevel16)
        buttonLevelsArray.add(bindingClass.btnLevel17)
        buttonLevelsArray.add(bindingClass.btnLevel18)
        buttonLevelsArray.add(bindingClass.btnLevel19)
        buttonLevelsArray.add(bindingClass.btnLevel20)
        buttonLevelsArray.add(bindingClass.btnLevel21)
        buttonLevelsArray.add(bindingClass.btnLevel22)
        buttonLevelsArray.add(bindingClass.btnLevel23)
        buttonLevelsArray.add(bindingClass.btnLevel24)
        buttonLevelsArray.add(bindingClass.btnLevel25)
    }
    private fun buttonsOnOff(){
        for (btn in buttonLevelsArray){
            btn?.isClickable = false
        }
    }
    //выбор уровня
    private fun chooseLevel() {

        if(levelProgress >= 1) {
            bindingClass.btnLevel1.isClickable = false
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
            bindingClass.btnLevel2.isClickable = false
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
            bindingClass.btnLevel3.isClickable = false
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
            bindingClass.btnLevel4.isClickable = false
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
            bindingClass.btnLevel5.isClickable = false
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
            bindingClass.btnLevel6.isClickable = false
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
            bindingClass.btnLevel7.isClickable = false
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
            bindingClass.btnLevel8.isClickable = false
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
            bindingClass.btnLevel9.isClickable = false
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
            bindingClass.btnLevel10.isClickable = false
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
            bindingClass.btnLevel11.isClickable = false
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
            bindingClass.btnLevel12.isClickable = false
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
            bindingClass.btnLevel13.isClickable = false
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
            bindingClass.btnLevel14.isClickable = false
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
            bindingClass.btnLevel15.isClickable = false
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
            bindingClass.btnLevel16.isClickable = false
            bindingClass.btnLevel16.text = "16"
            bindingClass.btnLevel16.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level16::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }

        }
        if(levelProgress >= 17){
            bindingClass.btnLevel17.isClickable
            bindingClass.btnLevel17.isClickable = false
            bindingClass.btnLevel17.text = "17"
            bindingClass.btnLevel17.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level17::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }

        }
        if(levelProgress >= 18){
            bindingClass.btnLevel18.isClickable
            bindingClass.btnLevel18.isClickable = false
            bindingClass.btnLevel18.text = "18"
            bindingClass.btnLevel18.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level18::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }

        }
        if(levelProgress >= 19){
            bindingClass.btnLevel19.isClickable
            bindingClass.btnLevel19.isClickable = false
            bindingClass.btnLevel19.text = "19"
            bindingClass.btnLevel19.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level19::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }

        }
        if(levelProgress >= 20){
            bindingClass.btnLevel20.isClickable
            bindingClass.btnLevel20.isClickable = false
            bindingClass.btnLevel20.text = "20"
            bindingClass.btnLevel20.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level20::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }

        }
        if(levelProgress >= 21){
            bindingClass.btnLevel21.isClickable
            bindingClass.btnLevel21.isClickable = false
            bindingClass.btnLevel21.text = "21"
            bindingClass.btnLevel21.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level21::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }

        }
        if(levelProgress >= 22){
            bindingClass.btnLevel22.isClickable
            bindingClass.btnLevel22.isClickable = false
            bindingClass.btnLevel22.text = "22"
            bindingClass.btnLevel22.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level22::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }

        }
        if(levelProgress >= 23){
            bindingClass.btnLevel23.isClickable
            bindingClass.btnLevel23.isClickable = false
            bindingClass.btnLevel23.text = "23"
            bindingClass.btnLevel23.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level23::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }

        }
        if(levelProgress >= 24){
            bindingClass.btnLevel24.isClickable
            bindingClass.btnLevel24.isClickable = false
            bindingClass.btnLevel24.text = "24"
            bindingClass.btnLevel24.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level24::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }

        }
        if(levelProgress >= 25){
            bindingClass.btnLevel25.isClickable
            bindingClass.btnLevel25.isClickable = false
            bindingClass.btnLevel25.text = "25"
            bindingClass.btnLevel25.setOnClickListener {
                soundPlay(soundDrop)
                val intent = Intent(this, Level25::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
                finish()
            }

        }
    }

}