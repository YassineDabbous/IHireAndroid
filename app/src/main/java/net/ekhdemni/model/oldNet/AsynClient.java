package net.ekhdemni.model.oldNet;

import android.content.Context;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.ekhdemni.BuildConfig;
import net.ekhdemni.presentation.base.MyActivity;

;import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Created by X on 5/12/2018.
 */

public class AsynClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static Request.Builder getRequestBuilder(Action action){
        Request.Builder b = new Request.Builder()
                .addHeader("api-key", action.getAPIKey())
                .addHeader("api-token", action.getAPIToken())
                .addHeader("lang", action.lang())
                .addHeader("os", "android")
                .addHeader("version", String.valueOf(BuildConfig.VERSION_CODE));
        return b;
    };
    String correctUrl(String url) {
        if(!url.startsWith("http"))
            url = "http://"+url;
        return url;
    }
    Action action;
    public Call execute(Action action) {
        this.action = action;
        action.localization();
        //action.auth();
        String finalUrl = correctUrl(action.url);//+"?api_key="+Action.key+"&api_token="+ YDUserManager.get(context, YDUserManager.TOKEN_KEY);
        MyActivity.log(finalUrl);
        Request.Builder b = getRequestBuilder(action).url(finalUrl);
        if(action.method.equals(Action.GET)){
            b = b.get();
        }else if(action.method.equals(Action.DELETE)){
            b = b.delete();
        }else{
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            MyActivity.log("POST DATA => "+ action.params.toString());
            Map<String, Object> params = action.params;
            for (Map.Entry<String, Object> param : params.entrySet()) {
                builder = builder.addFormDataPart(param.getKey(), String.valueOf(param.getValue()));
            }
            if(action.isMultiPart && action.filePath!=null){
                MyActivity.log("action.filePath: "+action.filePath);
                File file = new File(action.filePath);
                final MediaType MEDIA_TYPE = action.filePath.endsWith("png") ? MediaType.parse("image/png") : MediaType.parse("image/jpeg");
                builder = builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE, file));
            }
            else if (action.isJson){
                RequestBody.create(JSON, (new JSONObject(action.getParams())).toString());
            }
            RequestBody requestBody = builder.build();
            if(action.method.equals(Action.PUT)){
                b = b.put(requestBody);
            }else {
                b = b.post(requestBody);
            }
        }
        if (!action.enableCache) // || action.method != Action.GET
            b = b.cacheControl(CacheControl.FORCE_NETWORK);
        Request request = b.build();
        //OkHttpClient client = getClient(action.ctx);
        MyActivity.log("execute request "+ request.toString());
        Call call = getClient(action.ctx).newCall(request);
        call.enqueue(action);
        return call;
    }

    public static OkHttpClient client;
     OkHttpClient getClient(Context context){
         if (client==null) client = okHttpClient(getCache(context), httpLoggingInterceptor());
         return client;
    }





    public HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> {
            Timber.e(message);
            MyActivity.log("Interceptor => "+message);
            if (action!=null && action.log!=null) Action.log = Action.log+"\nInterceptor => "+message;
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public OkHttpClient okHttpClient(Cache cache, HttpLoggingInterceptor httpLoggingInterceptor ){
        return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(new CacheInterceptor())
                .build();
    }


    Cache getCache(Context context){
        //File httpCacheDirectory = new File(context.getCacheDir(), "http-cache");
        return new Cache(context.getCacheDir(), (Action.CACHE_SIZE * 1024 * 1024));
    }



    public class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(Action.CACHE_TIME, TimeUnit.MINUTES)
                    .build();

            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", cacheControl.toString())
                    .build();
        }
    }
}