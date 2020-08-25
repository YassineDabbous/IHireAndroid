package tn.core.model.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Constant {
    public final static String LOADING = "Loading";
    public final static String LOADED = "Loaded";
    public final static String CHECK_NETWORK_ERROR = "Check your network connection.";

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}