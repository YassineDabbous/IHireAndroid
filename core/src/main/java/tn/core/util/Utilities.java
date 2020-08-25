package tn.core.util;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tn.core.presentation.base.BaseActivity;

/**
 * Created by X on 6/7/2018.
 */

public class Utilities {


    private static Gson gson;
    /**
     * Add specific parsing to gson
     *
     * @return new instance of {@link Gson}
     */
    public static Gson getGsonParser() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            //gsonBuilder.registerTypeAdapter(Filters.class, new DeserializerFilters());
            gson = gsonBuilder.create();
        }
        return gson;
    }

    public static  Map<String, List<String>> checkErrors(JSONObject validator) {
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        try {
            Iterator iteratorObj = validator.keys();
            BaseActivity.log( "VALIDATION ERRORS !!");
            while (iteratorObj.hasNext())
            {
                String key = (String) iteratorObj.next();
                String value = validator.optString(key);
                BaseActivity.log( key + " => "+ value);
                value = value.replace("[","").replace("]","").replace("\"","");
                List<String> list = new ArrayList<>();
                list.add(value);
                params.put(key, list);
            }
        }catch (Exception e){
            BaseActivity.log( e.getMessage());
        }
        return params;
    }

    public static String getPath(Uri uri, Context context) {
        return FilePath.getPath(context, uri);
        /*
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
        */
    }




    public static List<String> keys(JSONObject jsonObject){
        List<String> strings = new ArrayList<>();
        if(jsonObject!=null){
            Iterator iteratorObj = jsonObject.keys();
            BaseActivity.log( "VALIDATION ERRORS !!");
            while (iteratorObj.hasNext()){
                String key = (String) iteratorObj.next();
                strings.add(key);
            }
        }
        return strings;
    }

}
