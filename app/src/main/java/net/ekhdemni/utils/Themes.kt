package net.ekhdemni.utils

import android.content.Context
import net.ekhdemni.R
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager

public class Themes{
    companion object {
        val DEFAULT = "day"
        val DARK = "night"

        public fun saveTheme(value: String, context:Context) {
            val editor = context.getSharedPreferences(YDUserManager.PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
            //val editor = getPreferences(Activity.MODE_PRIVATE).edit()
            editor.putString("theme", value)
            editor.commit()
        }

        public fun getSavedTheme(context:Context): Int {
            val theme = context.getSharedPreferences(YDUserManager.PREFERENCES_NAME, Context.MODE_PRIVATE).getString("theme", DEFAULT)
            when (theme) {
                DEFAULT -> return R.style.AppTheme_Day
                DARK -> return R.style.AppTheme_Transparent
                else -> return R.style.AppTheme
            }
        }
    }
}