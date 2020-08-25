package net.ekhdemni.model.oldNet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.presentation.ui.activities.auth.LoginActivity;
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager;
import tn.core.presentation.base.BaseActivity;

import net.ekhdemni.model.models.Resource;

import net.ekhdemni.utils.AlertUtils;
import tn.core.util.LocaleManager;
import tn.core.util.Utilities;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by X on 5/12/2018.
 */

public abstract class Action implements Callback {
    public String url = "";
    public Map<String, Object> params = new HashMap<String, Object>();
    public Context ctx;
    public boolean needAuth= true;
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public String method = GET;
    public boolean isJson = false;
    public boolean isMultiPart = false;
    public boolean enableCache = false;
    public String filePath = "";
    public static int responseCode = 200;
    public int errorCode = 0;
    public static int CACHE_SIZE = 10 ; // 10 MiB
    public static int CACHE_TIME = 720; // 1440 minutes = 1 day

    public Map<String, String> getParams() {
        Map<String,String> newMap =new HashMap<String,String>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if(entry.getValue() instanceof String){
                newMap.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return newMap;
    }

    public void run(){
        new AsynClient().execute(this);
    }

    public Action(Context ctx) {
        this.ctx = ctx;
    }

    public String lang(){
        String lang = LocaleManager.getLanguage(ctx);
        MyActivity.log("lang "+ lang);
        if (lang == null) {
            lang = "en";
        }
        return lang;
    }

    public void localization(){
        if (!url.contains("?")) this.url += "?";
        else this.url += "&";
        this.url += "lang="+lang();
    }


    //<native>
    //static {
    //    System.loadLibrary("native-lib");
    //}
    //public native String getAPIKey();
    //</native>
    public String getAPIKey(){ return "";}

    public String getAPIToken(){
        String k = null;
        if (YDUserManager.auth() != null)
             k = YDUserManager.auth().getToken();
        return  k!=null ? k: "";
    }
    public void auth(){
        key = getAPIKey();
        //YDUserManager.save(ctx,YDUserManager.API_KEY, key);
        this.url += "&api_key="+key;
        String k = null;
        if (YDUserManager.auth() != null)
            k = YDUserManager.auth().getToken();
        MyActivity.log("Ekhdemni: auth token "+ k);
        if (k != null) {
            this.url += "&api_token="+k;
        }
        else{
            MyActivity.log( "auth: no  token");
            /* startActivity replaced in onPostExecute method.
            Intent i = new Intent(ctx, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i); */
        }

    }
    public static String key = "";
/*    public static String auth(Context ctx, String url){
        url += "&api_key="+key;
        String t = YDUserManager.get(ctx,YDUserManager.TOKEN_KEY);
        MyActivity.log("Ekhdemni: auth token "+ t);
        if (t != null) {
            url += "&api_token="+t;
        }
        return url;
    }*/



    public  void onFailure(Call call, IOException e){
        MyActivity.log("okhttp onFailure getCause : "+e.getCause());
        MyActivity.log("okhttp onFailure getClass : "+e.getClass());
        MyActivity.log("okhttp onFailure getMessage : "+e.getMessage());
        MyActivity.log("okhttp onFailure call.isExecuted: "+call.isExecuted());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(ctx!=null)
                if(ctx instanceof MyActivity && ((MyActivity)ctx).isInForeground()){
                    AlertUtils.Action action = new AlertUtils.Action() {
                        public void doFunction(Object o) {
                            Intent intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            ctx.startActivity(intent);
                        }
                    };
                    action.message = ctx.getText(R.string.check_network).toString();
                    action.positive = ctx.getText(R.string.network_settings).toString();
                    AlertUtils.alert(ctx, action);
                }else{
                    Toast.makeText(ctx, ctx.getText(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onResponse(Call call, final Response response) throws IOException{
        final int code = response.code();
        MyActivity.log("okhttp => code : "+response.code());
                if (response.isSuccessful()) {
                    try {
                        final String responseStr = response.body().string();
                        MyActivity.log("okhttp => response body : "+responseStr);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                try { checkErrors(responseStr);}
                                catch (Exception e) {
                                    onUnsuccess(code, e.getMessage());
                                }
                            }
                        });

                    } catch (IOException e) {
                        onUnsuccess(code, e.getMessage());
                    }
                } else {
                    onUnsuccess(code, response.toString());
                }
    }



    public class EkhdemniAPIException extends Exception  {
        public EkhdemniAPIException(String errorMessage) {
            super(errorMessage);
            MyActivity.log("log sent to EkhdemniAPIException :D");
        }
    }

    public static String log = "";
    public void setLog(String log) {
        this.log = log;
    }
    public String getAndEmptyLog() {
        String v = log;
        log="";
        return v;
    }


    void checkLog(){
        try {
            if (log!=null && !log.isEmpty())
                throw new EkhdemniAPIException(getAndEmptyLog());
            else
                MyActivity.log("log is empty :/");
        } catch (EkhdemniAPIException e) {
            Crashlytics.logException(e);
        }
        /*Crashlytics.log(Log.WARN, "Network Error "+code, response);
        String userid = YDUserManager.get(ctx, YDUserManager.ID_KEY);
        if (userid==null) userid= "guest";
        Answers.getInstance().logCustom(
                new CustomEvent("Error").putCustomAttribute("Network Error, code +"+code, userid)
        );*/
    }

    void onUnsuccess(final int code, String response){
        checkLog();
        MyActivity.log("okhttp response.isNotSuccessful : "+response);
        //if(ctx!=null)
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (code==401){
                    YDUserManager.logout();
                    Intent i = new Intent(ctx, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(i);
                    Toast.makeText(ctx, ctx.getText(R.string.need_auth), Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(ctx instanceof MyActivity && ((MyActivity)ctx).isInForeground()){
                        final AlertUtils.Action action = new AlertUtils.Action() {
                            public void doFunction(Object o) {
                                Action.this.run();
                            }
                        };
                        action.message = ctx.getText(R.string.error_request_failed).toString();
                        AlertUtils.alert(ctx, action);
                    }else{
                        Toast.makeText(ctx, ctx.getText(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    public abstract void doFunction(String s) throws JSONException;
    //public abstract void doFunction(String s, String[] vKeys, String[] vValues) throws JSONException;

    public static long time = 0;
    public static void setTime(long time) {
        MyActivity.log("server time: "+time+" | client time: "+Action.time);
        if(time > 0)
            Action.time = time*1000;
    }

    public String getResponseMessage(JSONObject o){
        String message = "";
        JSONArray JA = o.optJSONArray("messages");
        if (JA!=null && JA.length()>0){
            message = JA.optString(0);
            if (message!=null)
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
        }
        return message;
    }


    public List<String> vKeys = new ArrayList<>(), vValues = new ArrayList<>();

    public void checkErrors(String json) {
        try {
            JSONObject reader = new JSONObject(json);
            setTime(reader.optLong("time"));
            JSONObject validator = reader.optJSONObject("validation");
            if (validator!=null){
                Iterator iteratorObj = validator.keys();
                MyActivity.log( "VALIDATION ERRORS !!");
                while (iteratorObj.hasNext())
                {
                    String key = (String) iteratorObj.next();
                    String value = validator.optString(key);
                    MyActivity.log( key + " => "+ value);
                    value = value.replace("[","").replace("]","").replace("\"","");
                    vKeys.add(key); vValues.add(value);
                    if(ctx instanceof Activity)
                        AlertUtils.toast((Activity) ctx,  value);
                    else
                        //key+" ||=> "+
                        Toast.makeText(ctx, value, Toast.LENGTH_LONG).show();

                }
            }else {
            }
        }
        catch (JSONException e){

        }catch (Exception e){
            MyActivity.log( e.getMessage());
        }
        //ctx=null;
        try {
            doFunction(json);
        }
        catch (JSONException e){

        }
        catch (NullPointerException e){
            //Toast.makeText(ctx, "☻ Catched NullPointerException at line: "+ e.getStackTrace()[0].getLineNumber(), Toast.LENGTH_LONG).show();
            MyActivity.log("☻ Catched NullPointerException at line: "+ e.getStackTrace()[0].getLineNumber());
            MyActivity.log("☻ Catched NullPointerException Message: "+ e.getMessage());
            //e.getCause();
        }
    }

    public List<Resource> parseResources(String jsonStr){
        List<Resource> lista  = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray models = jsonObj.getJSONArray("data");

            for (int i = 0; i < models.length(); i++) {
                JSONObject c = models.getJSONObject(i);
                Resource model = new Resource(c);
                lista.add(model);
            }
        } catch (final JSONException e) {
            Log.e("Ekhdemni: Action.class", "Json parsing error: " + e.getMessage());
        }
        return lista;
    }




    public boolean parse(String jsonStr){
        try {
            JSONObject json = new JSONObject(jsonStr);
            int code = json.getInt("code");
            if(code==200){
                return true;
            }
        } catch (final Exception e) {
            Log.e("Ekhdemni: Action.class", "Json parsing error: " + e.getMessage());
            return false;
        }
        return false;
    }


    public static void deleteSubscriptionTags(){
        OneSignal.getTags(new OneSignal.GetTagsHandler() {
            @Override
            public void tagsAvailable(JSONObject tags) {
                OneSignal.deleteTags(Utilities.keys(tags));
                MyActivity.log( "OneSignal.deleteTags("+tags+")");
            }
        });
    }

}
