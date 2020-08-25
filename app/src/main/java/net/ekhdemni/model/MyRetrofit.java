package net.ekhdemni.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tn.core.model.net.custom.CacheableInterceptor;
import tn.core.model.net.custom.OfflineCacheInterceptor;
import tn.core.model.net.custom.OnlineCacheInterceptor;
import tn.core.model.net.net.NetworkUtils;
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager;
import net.ekhdemni.model.oldNet.Action;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class MyRetrofit {

    private static final String BASE_URL = "https://ihire.qarya.app/v1/";


    Context ctx;
    public RestAPI getApi(Context context){
        ctx = context;
        return restAPI(retrofit(okHttpClient(cache(file(context)), httpLoggingInterceptor()), gsonConverterFactory(gson())));
    }

    public RestAPI restAPI(Retrofit retrofit){
        return retrofit.create(RestAPI.class);
    }

    public Retrofit retrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }




    public HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Timber.e(message);
                //MyActivity.log("Interceptor => "+message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public OkHttpClient okHttpClient(Cache cache, HttpLoggingInterceptor httpLoggingInterceptor ){
        Action action =  new Action(ctx){
            @Override
            public void doFunction(String s) throws JSONException {

            }
        };
        Interceptor interceptor = chain -> {
            Request originalRequest = chain.request();
            String tkn = "";
            if (YDUserManager.auth() != null)
                tkn = YDUserManager.auth().getToken();
            String key = action.getAPIKey();
            Request.Builder builder = originalRequest.newBuilder().
                    addHeader("api-key", key==null? "":key).
                    addHeader("api-token", tkn);

            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        };
        return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(interceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new CacheableInterceptor())
                .addNetworkInterceptor(new OnlineCacheInterceptor())
                .addInterceptor(new OfflineCacheInterceptor(NetworkUtils.isOnline(ctx)))
                .build();
    }




    public Cache cache(File file){
        return new Cache(file, 10 * 1024  * 1024 ); //10 MB
    }

    public File file(Context context){
        File cacheFile = new File(context.getCacheDir(), "HttpCache");
        cacheFile.mkdirs();
        return cacheFile;
    }




    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }

    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }
}
