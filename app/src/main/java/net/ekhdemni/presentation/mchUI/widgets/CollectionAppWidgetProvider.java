package net.ekhdemni.presentation.mchUI.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.core.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import net.ekhdemni.R;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.ui.activities.ViewerActivity;
import net.ekhdemni.utils.TextUtils;

/**
 * Created by Indian Dollar on 3/22/2017.
 */

public class CollectionAppWidgetProvider extends AppWidgetProvider {

    Context myContext;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        myContext =context;
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(

                    context.getPackageName(),
                    R.layout.collection_widget

            );

            // click event handler for the title, launches the app when the user clicks on title
            Intent titleIntent = new Intent(context, MainActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
            views.setOnClickPendingIntent(R.id.widgetTitleLabel, titlePendingIntent);

            views.setImageViewBitmap(R.id.widgetTitleLabel, textToBmp(context.getString(R.string.app_name)));

            views.setOnClickPendingIntent(R.id.refreshBtn, getPendingSelfIntent(context, MyOnClick));

            Intent intent = new Intent(context, MyWidgetRemoteViewsService.class);
            views.setRemoteAdapter(R.id.widgetListView, intent);

            // template to handle the click listener for each item
            Intent clickIntentTemplate = new Intent(context, ViewerActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widgetListView, clickPendingIntentTemplate);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }




    public Bitmap textToBmp(String text)
    {
        Bitmap myBitmap = Bitmap.createBitmap(500, 40, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface typeRegular = TextUtils.getFont(myContext);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(typeRegular);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(36);
        paint.setTextAlign(Paint.Align.CENTER);
        myCanvas.drawText(text, 250, 30, paint);
        return myBitmap;
    }



    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, CollectionAppWidgetProvider.class));
        context.sendBroadcast(intent);
    }
    private static final String MyOnClick = "myOnClickTag";
    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(MyOnClick)){
            Log.w(MyOnClick,MyOnClick);
            sendRefreshBroadcast(context);
        }else if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, CollectionAppWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widgetListView);
        }
        super.onReceive(context, intent);
    }
}
