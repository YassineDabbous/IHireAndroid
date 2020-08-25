package net.ekhdemni.model.models;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.model.feeds.db.MyDataBase;
import tn.core.model.net.net.NetworkUtils;

/**
 * Created by X on 2/4/2018.
 */

public class Resource {
    public long dbId = 0;
    public int position = 0;
    public boolean enabled = true;
    public boolean verified = false;
    public boolean enabledNotification = true;

    public String serverId = "";
    public String country = "";
    public String title = "ekhdemni";
    public String logo = "";
    public String url = "";

    public long last_update = 0;
    public String data = "";

    public Resource() {
    }

    public Resource(JSONObject c) throws JSONException {
        this.serverId = c.optString("id");
        this.country = c.optString("country");
        this.title = c.optString("name");
        this.logo = c.optString("logo");
        this.url = NetworkUtils.correctUrl(c.optString("url"));
        this.verified = c.optInt("verified") == 1 ? true : false;
        this.enabled = c.optInt("enabled") == 1 ? true : false;
    }

    public void update(Context context){
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToWrite();
        db.updateResourceDate(dbId, last_update);
        db.close();
    }
    public void updatePosition(Context context, int key){
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToWrite();
        db.updateResourcePosition(dbId, key);
        db.close();
    }

    public void delete(Context context){
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToWrite();
        db.deleteResource(dbId);
        db.close();
    }
    public void enable(Context context){
        enabled = !enabled;
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToWrite();
        db.enableResource(dbId, enabled);
        db.close();
    }
    public void enableNotification(Context context){
        enabledNotification = !enabledNotification;
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToWrite();
        db.enableNotificationResource(dbId, enabledNotification);
        db.close();
    }

    public List<Article> unreadPosts(Context context){
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToRead();
        List<Article> articles = db.resourceUnreadPosts(dbId);
        db.close();
        return articles;
    }

    public int getArticlesCount(Context context) {
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToRead();
        int c = db.getResourcePostsCount(dbId);
        db.close();
        return c;
    }
    public List<Article> getArticles(Context context) {
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToRead();
        List<Article> list = db.getResourcePosts(dbId, 0);
        db.close();
        if(list==null) list = new ArrayList<Article>();
        return list;
    }

    public String getBaseUri(){
        URL the_url = null;
        try {
            the_url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String baseUri = "http://www.ekhdemni.com";
        if(the_url!=null)
            baseUri = the_url.getProtocol() + "://" + the_url.getHost();
        return baseUri;
    }






}
