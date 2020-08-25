package tn.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



/**
 * Created by X on 2/4/2018.
 */

public class PrefManager {
    public static SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "ekhdemni-preferences";

    public static final String UPDATE_FEED_AFTER = "UPDATE_FEED_AFTER";

    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public static final String NIGHT_MODE = "NIGHT_MODE";
    public static final String FULLSCREEN = "FULLSCREEN";
    public static final String AUTO_SCROLL = "AUTO_SCROLL";
    public static final String FONT = "FONT";
    public static final String FONT_SIZE = "FONT_SIZE";


    public PrefManager(Context context) {
        this._context = context;
        //pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
    }



    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }
    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }
    public void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public static SharedPreferences getPreference() {
        return pref;
    }
}