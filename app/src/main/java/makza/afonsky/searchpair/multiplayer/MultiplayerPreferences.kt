package makza.afonsky.searchpair.multiplayer

import android.content.Context
import kotlin.random.Random

class MultiplayerPreferences(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var userId: Long
        get() = prefs.getLong(KEY_USER_ID, 0L)
        set(value) = prefs.edit().putLong(KEY_USER_ID, value).apply()

    var nickname: String
        get() = prefs.getString(KEY_NICKNAME, null) ?: generateNickname().also { nickname = it }
        set(value) = prefs.edit().putString(KEY_NICKNAME, value).apply()

    fun generateNickname(): String {
        return Random.nextInt(10_000_000, 100_000_000).toString()
    }

    companion object {
        private const val PREFS_NAME = "multiplayer_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_NICKNAME = "nickname"
    }
}
