package net.ekhdemni.model.feeds;

/**
 * Created by X on 1/21/2018.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import net.ekhdemni.model.models.Article;
import net.ekhdemni.model.models.Resource;
import net.ekhdemni.model.oldNet.Ekhdemni;

public class Parser extends AsyncTask<Resource, Void, String> implements Observer {

    private XMLParser xmlParser;
    private static ArrayList<Article> articles = new ArrayList<>();
    Context context;
    Resource resource;
    private OnTaskCompleted onComplete;

    public Parser(Context context) {
        this.context = context;
        xmlParser = new XMLParser();
        xmlParser.addObserver(this);
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(ArrayList<Article> list);

        void onError();
    }

    public void onFinish(OnTaskCompleted onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    protected String doInBackground(Resource... resources) {
        resource = resources[0];
        Response response = null;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(resource.url)
                .build();

        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful())
                return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            onComplete.onError();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {


        resource.data = result;
        if (result != null) {
            try {
                xmlParser.parseXML(context, resource);
                Log.i(Ekhdemni.TAG, "RSS parsed correctly!");
            } catch (Exception e) {
                e.printStackTrace();
                onComplete.onError();
            }
        } else
            onComplete.onError();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable observable, Object data) {
        articles = (ArrayList<Article>) data;
        onComplete.onTaskCompleted(articles);
    }

}