package tn.core.model.net.net;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.webkit.URLUtil;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;
import tn.core.presentation.base.BaseActivity2;

/**
 * Created by YaSsIn on 25/07/2016.
 */
public class NetworkUtils  {

    public static String correctUrl(String url){
        if (url.contains("\\/"))    {
            url = url.replace("\\","");
        }
        else if (!url.startsWith("http://") && !url.startsWith("https://"))    {
            url = "http://"+url;
        }
        //MyActivity.log("☻☻☻ UserLink ☻☻☻: "+ url);
        return url;
    }

    public static boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static HttpURLConnection setupConnection(URL url) throws IOException {
        HttpURLConnection connection = new OkUrlFactory(new OkHttpClient()).open(url);

        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.setRequestProperty("User-agent", "Mozilla/5.0 (compatible) AppleWebKit Chrome Safari"); // some feeds need this to work properly
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("accept", "*/*");

        COOKIE_MANAGER.getCookieStore().removeAll(); // Cookie is important for some sites, but we clean them each times
        connection.connect();

        return connection;
    }
    public static HttpURLConnection setupConnection(String url) throws IOException {
        return setupConnection(new URL(url));
    }
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];

        int n;
        while ((n = inputStream.read(buffer)) > 0) {
            output.write(buffer, 0, n);
        }

        byte[] result = output.toByteArray();

        output.close();
        inputStream.close();
        return result;
    }

    private static final CookieManager COOKIE_MANAGER = new CookieManager() {{
        CookieHandler.setDefault(this);
    }};



    public static void openWebPage(Context context, String url) {
        try {
            if (!URLUtil.isValidUrl(url)) {
                BaseActivity2.log("This is not a valid link: "+url);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "عذرا، ليس لديك متصفح واب لفتح الرابط !", Toast.LENGTH_LONG).show();
        }
    }

}