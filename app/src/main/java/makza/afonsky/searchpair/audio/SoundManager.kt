package makza.afonsky.searchpair.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import makza.afonsky.searchpair.R

enum class GameSound {
    CLOSE,
    DROP,
    CRASH,
    CARD_CLOSE,
    CARD_OPEN,
    BUM,
    CHEST,
}

class SoundManager(context: Context) {

    private val soundPool: SoundPool
    private val soundIds = mutableMapOf<GameSound, Int>()

    init {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(6)
            .setAudioAttributes(attributes)
            .build()

        val appContext = context.applicationContext
        soundIds[GameSound.CLOSE] = soundPool.load(appContext, R.raw.close, 1)
        soundIds[GameSound.DROP] = soundPool.load(appContext, R.raw.stone_drop, 1)
        soundIds[GameSound.CRASH] = soundPool.load(appContext, R.raw.stone_crash, 1)
        soundIds[GameSound.CARD_CLOSE] = soundPool.load(appContext, R.raw.stone_close, 1)
        soundIds[GameSound.CARD_OPEN] = soundPool.load(appContext, R.raw.stone_open, 1)
        soundIds[GameSound.BUM] = soundPool.load(appContext, R.raw.bum, 1)
        soundIds[GameSound.CHEST] = soundPool.load(appContext, R.raw.chestopen, 1)
    }

    fun play(sound: GameSound) {
        soundIds[sound]?.let { id ->
            soundPool.play(id, 1f, 1f, 0, 0, 1f)
        }
    }

    fun release() {
        soundPool.release()
    }
}
