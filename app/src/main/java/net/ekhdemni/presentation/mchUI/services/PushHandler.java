package net.ekhdemni.presentation.mchUI.services;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import net.ekhdemni.presentation.ui.activities.ConversationsActivity;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.oldNet.Ekhdemni;

/**
 * Created by X on 7/1/2018.
 */

public class PushHandler implements OneSignal.NotificationOpenedHandler {

    private Application application;

    public PushHandler(Application application) {
        this.application = application;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {

        // Get custom datas from notification
        JSONObject data = result.notification.payload.additionalData;
        Class<?> to = MainActivity.class;
        int type = 0;
        if (data != null) {
            type = data.optInt("type", 0);
            Log.i(Ekhdemni.TAG, "push notification type ====> " + type);
            if(type == 1){
                to = ConversationsActivity.class;
            }
        }

        // React to button pressed
        OSNotificationAction.ActionType actionType = result.action.type;
        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i(Ekhdemni.TAG, "Button pressed with id: " + result.action.actionID);

        // Launch new activity using Application object
        startApp(to, type);
    }

    private void startApp(Class<?> to, int type) {
        MyActivity.log("push => "+type);
        Intent intent = new Intent(application, to).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("push", type);
        application.startActivity(intent);
    }
}
