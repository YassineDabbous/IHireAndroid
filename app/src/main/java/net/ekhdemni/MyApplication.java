package net.ekhdemni;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import androidx.multidex.MultiDexApplication;
import androidx.appcompat.app.AppCompatDelegate;
import timber.log.Timber;

import android.util.DisplayMetrics;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.onesignal.OneSignal;

import net.ekhdemni.presentation.ui.activities.auth.YDUserManager;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.MyRetrofit;
import net.ekhdemni.model.RestAPI;
import net.ekhdemni.presentation.mchUI.services.PushHandler;
import tn.core.util.LocaleManager;
import tn.core.util.PrefManager;
import net.ekhdemni.model.oldNet.Ekhdemni;


/**
 * Created by X on 1/19/2018.
 */

public class MyApplication extends MultiDexApplication {

    private PrefManager prefManager;
    public static  boolean nightMode = false;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        /*if (BuildConfig.DEBUG) { //for sqlite leak crash
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }*/
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        MyActivity.log("attachBaseContext");
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
        MyActivity.log( "onConfigurationChanged: " + newConfig.locale.getLanguage());
    }
    

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Timber.plant(new Timber.DebugTree());
        MobileAds.initialize(this, getResources().getString(R.string.admob_initialize));
        prefManager = new PrefManager(this);
        nightMode =prefManager.getPreference().getBoolean(PrefManager.NIGHT_MODE,false);
        // OneSignal Initialization
          OneSignal.startInit(this)
            .setNotificationOpenedHandler(new PushHandler(this))
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            //return;
        }
        LeakCanary.install(this);*/
        if(YDUserManager.auth()!=null){
            Crashlytics.setUserIdentifier("user "+YDUserManager.auth().getId());
        }
        if(YDUserManager.configs()!=null)
            Ekhdemni.ADS_AFTER = YDUserManager.configs().getAdsAfter();


        MyRetrofit myRetrofit = new MyRetrofit();
        restAPI = myRetrofit.getApi(this);
    }


    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = getInstance().getResources().getDisplayMetrics(); // null exception
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }


    public RestAPI getRestAPI(){
        return restAPI;
    }

    RestAPI restAPI;
    public static MyApplication get(Context activity){ return (MyApplication) activity.getApplicationContext(); }
    public static MyApplication get(Activity activity){ return (MyApplication) activity.getApplication(); }


    private static MyApplication mInstance;
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
    public static void goTo(Class<?> to) {
        Intent intent = new Intent(mInstance, to).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        mInstance.startActivity(intent);
    }
}