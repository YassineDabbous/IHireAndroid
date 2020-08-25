package net.ekhdemni.presentation.mchUI.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by YaSsIn on 19/10/2016.
 */
public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MyService.class);
        context.startService(myIntent);
    }

}