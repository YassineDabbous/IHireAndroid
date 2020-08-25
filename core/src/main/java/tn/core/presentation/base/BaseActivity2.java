package tn.core.presentation.base;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import tn.core.util.LocaleManager;
import tn.core.util.PrefManager;

public abstract class BaseActivity2 extends AppCompatActivity implements ComponentCallbacks2 {
    public boolean isInForegroundMode;
    public static boolean isUpdating = false;
    public BaseFragmentInterface currentFragment = null;
    public PrefManager prefManager;
    public int tag = 0;

    public abstract void showPD();
    public abstract void dismissPD();

    public abstract void setFragment(BaseFragment fragment);
    public abstract void setFragment(BaseDialogFragment fragment);
    public abstract void setFirstFragment(BaseFragment fragment);

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        BaseActivity2.log("attachBaseContext");
    }

    public static void log(String... logs){
        for (String log: logs) {
            Log.wtf("tn.core", log);
        }
    }


    public BaseFragmentInterface getLiveCurrentFragment() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            currentFragment = (BaseFragmentInterface) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        }
        return currentFragment;
    }

    public boolean isInForeground() {
        return isInForegroundMode;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isInForegroundMode = true;
    }

    @Override
    protected void onPause() {
        isInForegroundMode = false;
        //if (mSensorManager != null)
        //    mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        isInForegroundMode = true;
        super.onResume();
        //if(mSensorManager!=null)
        //    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        setupToolbar();
        init();
    }

   
    

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        prefManager = new PrefManager(getApplicationContext());
        super.onCreate(savedInstanceState);
    }

    

    public <T extends View> T getView(@IdRes int id) {
        return findViewById(id);
    }



    public void setupToolbar() {
        log("");
    }


    



    


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //clean();
        //currentFragment = null;
    }

    /*  MEMORY MANAGEMENT */
    public void onUIHidden(){
        //clean();
    }
    public void clean(){
        BaseActivity2.log("♣♣♣ BaseActivity clean "+ getClass().getName());
        //if(currentFragment!=null)
        //currentFragment.clean();
        System.gc();
    }
    public void init(){
        BaseActivity2.log("♣♣♣ BaseActivity init "+ getClass().getName());
        //if(currentFragment!=null)
        //currentFragment.init();
    }



    public void onTrimMemory(int level) {
        // Determine which lifecycle or system event was raised.
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                //Release any UI objects that currently hold memory. The user interface has moved to the background.
                onUIHidden();
                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:

                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */
                //clean();
                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:

                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */
                //clean();
                break;

            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }








}