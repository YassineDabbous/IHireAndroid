package net.ekhdemni.presentation.mchUI.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.model.feeds.db.MyDataBase;
import net.ekhdemni.model.models.Article;


/**
 * Created by Indian Dollar on 3/22/2017.
 */

public class MyWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private MyDataBase db;
    List<Article> articleList = new ArrayList<>();

    public MyWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        if (db != null) {
            db.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        db = MyDataBase.getInstance(mContext);
        db.openToRead();
        articleList = db.take("30", 0);
        db.close();
        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {
        if (db != null) {
            db.close();
        }
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || articleList.isEmpty()) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        rv.setTextViewText(R.id.widgetItemTaskNameLabel, articleList.get(position).getTitle());
        //if(!articleList.get(position).getImg().equals(""))
        //    rv.setImageViewBitmap(R.id.widgetImageView, getImageBitmap(articleList.get(position).getImg()));

        Intent i = new Intent();
        i.putExtra("ARTICLE", articleList.get(position));
        rv.setOnClickFillInIntent(R.id.widgetItemContainer, i);

        return rv;
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bitmap = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("img in widgets", "img in widgets Error getting bitmap", e);
        }
        if(bitmap == null)
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_stat_onesignal_default);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.argb(100,230,81,0));
        p.setStrokeWidth(5);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        return output;
    }







    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return articleList.get(position).getDbId()>0 ? articleList.get(position).getDbId() : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
