package net.ekhdemni.utils;

import android.content.Context;
import android.graphics.Typeface;

import net.ekhdemni.R;


import androidx.core.content.res.ResourcesCompat;

/**
 * Created by X on 7/4/2018.
 */

public class TextUtils extends tn.core.util.TextUtils {

    public static Typeface getFont(Context context){
        return getFont(context, "ar");
    }
    public static Typeface getFont(Context context, String local){
        switch (local){
            case "en": return ResourcesCompat.getFont(context, R.font.cairo); //tanseek
            default: return ResourcesCompat.getFont(context, R.font.cairo); //lateef
        }
    }

}