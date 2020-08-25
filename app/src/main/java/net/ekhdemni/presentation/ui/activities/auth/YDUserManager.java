package net.ekhdemni.presentation.ui.activities.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonSyntaxException;

import net.ekhdemni.MyApplication;
import net.ekhdemni.model.configs.Showcases;
import net.ekhdemni.model.models.responses.AuthResponse;
import net.ekhdemni.model.configs.ConfigsResponse;
import net.ekhdemni.presentation.base.MyActivity;

import timber.log.Timber;
import tn.core.util.Utilities;

/**
 * Created by Yassine on 5/10/2018.
 */

public class YDUserManager {
    public static final String PREFERENCES_NAME = "yassine";
    public final static String KEY_AUTH = "Y_KEY_AUTH";
    public final static String KEY_CONFIG = "KEY_CONFIG";
    public final static String KEY_SHOWCASES = "KEY_SHOWCASES";

    public final static String COUNTRY_KEY = "Y_COUNTRY";
    public final static String CATEGORY_KEY = "Y_CATEGORY";
    public final static String LAST_NOTIF_TIME_KEY = "Y_LAST_NOTIF_TIME";

    public final static String SHOWCASE_COFFEE= "Y_SHOWCASE_COFFEE";

    public static AuthResponse authResponse;
    public static ConfigsResponse configsResponse;
    public static Showcases showcases;




    public static void save(Showcases showcases) {
        YDUserManager.showcases = showcases;
        YDUserManager.save(MyApplication.getInstance(), YDUserManager.KEY_SHOWCASES,    Utilities.getGsonParser().toJson(showcases));
    }
    public static Showcases showcases(){
        if (YDUserManager.showcases != null) return YDUserManager.showcases;
        String json = get(MyApplication.getInstance(), YDUserManager.KEY_SHOWCASES);
        if (json==null || json.isEmpty() || "null".equals(json)) return new Showcases();
        MyActivity.log("=>>>>>>>>> ☺☺☻• "+json);
        try {
            Showcases showcases = Utilities.getGsonParser().fromJson(json, Showcases.class);
            Timber.d("Returned active user from memory: %s", showcases.toString());
            YDUserManager.showcases = showcases;
            return showcases;
        }catch (JsonSyntaxException e){
            return new Showcases();
        }
    }





    public static void save(AuthResponse authResponse) {
        YDUserManager.authResponse = authResponse;
        YDUserManager.save(MyApplication.getInstance(), YDUserManager.KEY_AUTH,    Utilities.getGsonParser().toJson(authResponse));
    }
    public static AuthResponse auth(){
        if (YDUserManager.authResponse != null) return YDUserManager.authResponse;
        String auth = get(MyApplication.getInstance(), YDUserManager.KEY_AUTH);
        if (auth==null || auth.isEmpty() || "null".equals(auth)) return null;
        MyActivity.log("=>>>>>>>>> ☺☺☻• "+auth);
        try {
            AuthResponse authResponse = Utilities.getGsonParser().fromJson(auth, AuthResponse.class);
            Timber.d("Returned active user from memory: %s", authResponse.toString());
            YDUserManager.authResponse = authResponse;
            return authResponse;
        }catch (JsonSyntaxException e){
            return null;
        }
    }
    public static void save(ConfigsResponse configsResponse) {
        YDUserManager.configsResponse = configsResponse;
        YDUserManager.save(MyApplication.getInstance(), YDUserManager.KEY_CONFIG,    Utilities.getGsonParser().toJson(configsResponse));
    }
    public static ConfigsResponse configs(){
        if (YDUserManager.configsResponse != null) return YDUserManager.configsResponse;
        String json = get(MyApplication.getInstance(), YDUserManager.KEY_CONFIG);
        if (json==null || json.isEmpty() || "null".equals(json)) return new ConfigsResponse();
        MyActivity.log("=>>>>>>>>> ☺☺☻• "+json);
        try {
            ConfigsResponse configsResponse = Utilities.getGsonParser().fromJson(json, ConfigsResponse.class);
            Timber.d("Returned active user from memory: %s", configsResponse.toString());
            YDUserManager.configsResponse = configsResponse;
            return configsResponse;
        }catch (JsonSyntaxException e){
            return new ConfigsResponse();
        }
    }

    public static void save(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit().putString(key, value)
                .apply();
    }
    public static void saveLong(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value)
                .apply();
    }
    public static void saveInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value)
                .apply();
    }

    public static String get(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        return sp.getString(key, null);
    }

    public static long getLong(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }
    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }



    public static boolean check() {
        if(auth()==null) return false;
        return auth().getToken() == null ? false : true ;
    }

    public static void logout() {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit().putString(KEY_AUTH, null)
                .putString(KEY_CONFIG, null)

                .putString(COUNTRY_KEY, null)
                .putString(CATEGORY_KEY, null)
                .putString(LAST_NOTIF_TIME_KEY, null)
                .apply();
    }










    public static void saveBool(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit().putBoolean(key, value)
                .apply();
    }

    public static boolean getBool(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        return sp.getBoolean(key, false);
    }
}
