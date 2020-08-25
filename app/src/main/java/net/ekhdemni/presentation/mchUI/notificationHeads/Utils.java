package net.ekhdemni.presentation.mchUI.notificationHeads;


import android.content.Context;
import android.os.Build;
import android.provider.Settings;

public class Utils {


	public static String LogTag = "Ekhdemni";

	public static boolean canDrawOverlays(Context context){
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}else{
			return Settings.canDrawOverlays(context);
		}
	}

}
