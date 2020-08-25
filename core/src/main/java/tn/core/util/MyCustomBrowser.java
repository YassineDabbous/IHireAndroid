package tn.core.util;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by YaSsIn on 28/09/2016.
 */
public class MyCustomBrowser extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}