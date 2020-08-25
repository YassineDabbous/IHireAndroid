package net.ekhdemni.utils;

import android.app.ProgressDialog;
import android.content.Context;

import net.ekhdemni.R;


/**
 * Created by X on 8/6/2018.
 */

public class ProgressUtils {
    public static ProgressDialog getProgressDialog(Context context){
        ProgressDialog pd = new ProgressDialog(context);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.setMessage(context.getString(R.string.wait));
        pd.setCancelable(true);
        pd.setIndeterminate(true);
        return pd;
    }
}