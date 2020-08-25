package net.ekhdemni.presentation.mchUI.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.model.feeds.db.MyDataBase;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.feeds.Parser;
import net.ekhdemni.model.models.Article;
import net.ekhdemni.model.models.Resource;
import net.ekhdemni.presentation.mchUI.notificationHeads.ChatHeadService;
import net.ekhdemni.presentation.mchUI.notificationHeads.Utils;
import tn.core.util.LocaleManager;
import tn.core.util.PrefManager;
import net.ekhdemni.presentation.mchUI.widgets.CollectionAppWidgetProvider;

public class MyService extends Service {
    Context context;
    public static boolean force = false;

    @Override
    public void onDestroy() {
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        MyActivity.log("attachBaseContext");
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static ArrayList<Article> lista = new ArrayList<>();
    static int y = 0;
    static List<Resource> resourcesMuted;
    static List<Resource> resources;
    private PrefManager prefManager;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        prefManager = new PrefManager(this);
        resourcesMuted = new ArrayList<>();
        resources = new ArrayList<>();

        long UPDATE_FEED_AFTER = prefManager.getPreference().getLong(prefManager.UPDATE_FEED_AFTER,0);
        if(System.currentTimeMillis()>=UPDATE_FEED_AFTER || force){
            setAlert();
            MyDataBase db = MyDataBase.getInstance(context);
            db.openToRead();
            List<Resource> rs = db.takeResources(true);
            db.close();
            for (Resource resource: rs) {
                if(resource.enabledNotification){
                    resources.add(resource);
                }else {
                    resourcesMuted.add(resource);
                }
            }
            Log.w("resources", resources.size()+"");
            Log.w("resourcesMuted", resourcesMuted.size()+"");
            MyService.y=0;
            if(resources.size()>0){
                MyActivity.isUpdating = true;
                for (int i = 0; i < resources.size(); i++) {
                    Parser parser = new Parser(context);
                    parser.execute(resources.get(i));
                    parser.onFinish(new Parser.OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(ArrayList<Article> list) {
                            MyService.lista.addAll(list);
                            y++;
                            if(MyService.y >= resources.size()){
                                save(true);
                                runForMuted();
                            }else
                                MyActivity.isUpdating = false;
                        }
                        @Override
                        public void onError() {
                            //what to do in case of error
                            y++;
                            if(MyService.y >= resources.size()){
                                save(true);
                                runForMuted();
                            }else
                                MyActivity.isUpdating = false;
                        }
                    });
                }
            }else if(resourcesMuted.size()>0){
                MyActivity.isUpdating = true;
                runForMuted();
            }else
                MyActivity.isUpdating = false;
        }
        else{
            //Date date = new Date();
            //date.setTime(UPDATE_FEED_AFTER);
            //Toast.makeText(this, "Update set at " + date +", bye",Toast.LENGTH_LONG).show();
            //stopSelf(); never user this, it may stop updating when user close up before completing date saving
        }

        return START_STICKY;
    }



    void runForMuted(){
        MyService.y=0;
        for (int i = 0; i < resourcesMuted.size(); i++) {
            Parser parser = new Parser(context);
            parser.execute(resourcesMuted.get(i));
            parser.onFinish(new Parser.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(ArrayList<Article> list) {
                    MyService.lista.addAll(list);
                    y++;
                    if(MyService.y >= resourcesMuted.size()){
                        save(false);
                    }else
                        MyActivity.isUpdating = false;
                }
                @Override
                public void onError() {
                    //what to do in case of error
                    y++;
                    if(MyService.y >= resourcesMuted.size()){
                        save(false);
                    }else
                        MyActivity.isUpdating = false;
                }
            });
        }
    }










    @Override public void onTaskRemoved(Intent rootIntent){
        Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);
    }

    public void setAlert() {
        int oneMin = 60*1000; // in millis
        Intent intent = new Intent(this, RestartServiceBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long fromSettings = oneMin * Long.parseLong(prefManager.getPreference().getString(getString(R.string.key_notifications_delay),"60"));
        long after = System.currentTimeMillis() + fromSettings;
        prefManager.setLong(prefManager.UPDATE_FEED_AFTER, after);
        alarmManager.set(AlarmManager.RTC_WAKEUP, after, pendingIntent);
    }









    public void save(boolean notify){
        ArrayList<Article> articles= new ArrayList<>();
        for (Article a : lista) {
            Log.d("DB", "Searching DB for GUID: " + a.resource);
            MyDataBase dba = MyDataBase.getInstance(getApplicationContext());
            dba.openToRead();
            List<Article> dbArticles = dba.getPosts(a.getTitle());
            dba.close();
            if (dbArticles == null || dbArticles.size()==0) {
                dba = MyDataBase.getInstance(getApplicationContext());
                dba.openToWrite();
                a.setDbId(dba.insertPost(a));
                List<String> cats = a.getCategories();
                for (String cn : cats) {
                    dba.insertCategory(cn);
                    dba.addPostForCategory(cn, a.getDbId());
                }
                dba.close();
                articles.add(a);
            }
        }
        lista = articles;

        CollectionAppWidgetProvider.sendRefreshBroadcast(context);
//
        if(notify && lista.size()!=0){
            if((prefManager.getPreference().getBoolean(getString(R.string.key_notifications_enable),true))){
                String notifMessage;
                Article first = lista.get(0);
                if(lista.size()==1)
                    notifMessage = first.getTitle();
                else
                    notifMessage = lista.size()+" "+getResources().getString(R.string.new_opportunity);
                String imgSrc = first.getImg();
                String notifTitle = getResources().getString(R.string.app_name);
                Notify(notifTitle, notifMessage, imgSrc, first.getUrl(), lista.size());
            }
            lista.removeAll(lista);
            runForMuted();
        }
        else{
            lista.removeAll(lista);
        }
        MyActivity.isUpdating = false;
    }



    private void Notify(String notificationTitle, String notificationMessage, String imgSrc, String url, int nbr) {
        Intent notificationIntent = new Intent(getBaseContext(), MainActivity.class);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, 0);
        Notification.Builder builder = new Notification.Builder(getBaseContext());
        builder.setTicker(notificationMessage);
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationMessage);
        builder.setSmallIcon(R.drawable.ic_stat_onesignal_default);
        builder.setContentIntent(pendingIntent);
        builder.setVibrate(new long[]{1000, 0, 1000, 0, 1000});
        builder.setLights(Color.RED, 3000, 3000);
        //if(!MyActivity.isInForeground()){
            String sound = prefManager.getPreference().getString(getString(R.string.key_notifications_new_message_ringtone),"wrinkle");
            String soundUrl = "android.resource://" + getApplicationContext().getPackageName() + "/raw/" + sound;// + R.raw.sound
            Log.w("soundUrl", soundUrl);
            builder.setSound(Uri.parse(soundUrl));
        //}


        int MY_NOTIFICATION_ID = 1;

        //if(prefManager.getPreference().getBoolean(getString(R.string.key_vibrate),true)){
            Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);
        //}

        notificationManager.notify(MY_NOTIFICATION_ID, builder.build());



        if(Utils.canDrawOverlays(context))
            startChatHead(imgSrc, notificationMessage, url, nbr);
    }


    private void startChatHead(String img, String str, String url, int nbr) {
        Intent it = new Intent(MyService.this, ChatHeadService.class);
        it.putExtra("img", img);
        it.putExtra("url", url);
        it.putExtra("msg", str);
        it.putExtra("nbr", nbr);
        startService(it);
    }

}