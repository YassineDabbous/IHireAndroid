package net.ekhdemni.model.feeds;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.oldNet.Action;

/**
 * Created by X on 5/12/2018.
 */
public class JsonHandler {

    private static final String TAG = JsonHandler.class.getSimpleName();

    public JsonHandler() {
    }

    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            Action.responseCode = conn.getResponseCode();
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            MyActivity.log( "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            MyActivity.log( "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            MyActivity.log( "IOException: " + e.getMessage());
        } catch (Exception e) {
            MyActivity.log( "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}