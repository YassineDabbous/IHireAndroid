package net.ekhdemni.presentation.ui.activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.InputStream;
import java.util.Random;

import net.ekhdemni.MyApplication;
import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Service;
import tn.core.util.Urls;
import tn.core.model.net.net.NetworkUtils;


public class BrowserActivity extends MyActivity {
    WebView browser;
    AVLoadingIndicatorView loader;
    String[] INDICATORS = new String[]{ "BallPulseIndicator", "BallGridPulseIndicator", "BallClipRotateIndicator", "BallClipRotatePulseIndicator", "SquareSpinIndicator", "BallClipRotateMultipleIndicator", "BallRotateIndicator",  "BallZigZagDeflectIndicator", "BallTrianglePathIndicator", "BallScaleIndicator",  "BallScaleMultipleIndicator", "LineScalePulseOutIndicator", "LineScalePulseOutRapidIndicator", "BallSpinFadeLoaderIndicator", "LineSpinFadeLoaderIndicator", "PacmanIndicator", "BallGridBeatIndicator", "com.wang.avi.sample.MyCustomIndicator" }; //"BallPulseRiseIndicator", "TriangleSkewSpinIndicator",  "SemiCircleSpinIndicator", "CubeTransitionIndicator", "BallZigZagIndicator", "BallPulseSyncIndicator", "BallBeatIndicator",  "BallScaleRippleIndicator", "BallScaleRippleMultipleIndicator", "LineScaleIndicator", "LineScalePartyIndicator",
    String url = Urls.root;
    String order = "";
    public static String EL_URL = "EL_URL";
    public static String _ORDER_ = "_ORDER_";
    public static Service service;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(net.ekhdemni.R.layout.activity_browser);


        url = getIntent().getStringExtra(EL_URL);
        order = getIntent().getStringExtra(_ORDER_);
        if(url!=null && !url.equals("")){
            url = getIntent().getStringExtra(EL_URL);
        }
        else if(service!= null){
            url = service.getUrl();
        }
        else{
            //Toast.makeText(this, "no order", Toast.LENGTH_SHORT).show();
        }

        browser = (WebView) findViewById(R.id.shower);
        browser.setVisibility(View.INVISIBLE);

        loader = (AVLoadingIndicatorView) findViewById(R.id.browserLoadingView);
        loader.setIndicator(INDICATORS[new Random().nextInt(INDICATORS.length)]);
        loader.show();


        browser.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                setTitle( getResources().getString(net.ekhdemni.R.string.refresh));
                setProgress(progress * 100);
                if(progress == 100)
                    setTitle(net.ekhdemni.R.string.app_name);
            }
        });

        browser.setWebViewClient(new MyBrowser());
        //browser.setWebChromeClient(new MyWebChromeClient());
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.getSettings().setBuiltInZoomControls(true);
        browser.getSettings().setDisplayZoomControls(false);

        if(url== null) url = "http://www.ekhdemni.tn";

        if (!NetworkUtils.isOnline(getApplicationContext())){
            alertWindow("");
            return;
        }
        browser.loadUrl(url);
    }

    private void shareIt() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, url);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_via)));
    }

    private void copyUrl(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("yassine", url);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.url_copied), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){
        if(browser.canGoBack())
            browser.goBack();
        else
            super.onBackPressed();
    }


    void setNightMode(){
        /*int bg  = getResources().getColor(R.color.bg_night);
        Log.w("bg color", "background color is "+bg);*/
        browser.loadUrl("javascript:(function() {" +
                " var all = document.getElementsByTagName(\"*\"); for (var i=0, max=all.length; i < max; i++) { all[i].style.backgroundColor = \"#151518\"; if( all[i].style.color == \"\" || all[i].style.color == \"rgb(21, 21, 24)\" || all[i].style.color == \"rgb(0, 0, 0)\" ){ all[i].style.color = \"#ffffff\"; } }"+
                "})()");
    }
    void injectFont(){
        browser.loadUrl("javascript:(function() {" +
                " var link = document.createElement('link');"+
                " link.href = \"https://fonts.googleapis.com/css?family=Cairo\";" +
                " link.rel = \"stylesheet\";" +
                " document.head.appendChild(link);" +
                " var all = document.getElementsByTagName(\"*\"); for (var i=0, max=all.length; i < max; i++) { all[i].style.fontFamily = \"Cairo\"; }"+
                "})()");
    }







    private void injectStyle() {
        try {
            InputStream inputStream = getAssets().open("style.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            browser.loadUrl("javascript:(function() {" +
                    " var parent = document.head;" +
                    " var style = document.createElement('style');" +
                    " style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    " style.innerHTML = window.atob('" + encoded + "');" +
                    " parent.appendChild(style);" +
                    " var fieldset = document.getElementsByTagName(\"fieldset\").item(0);"+
                    " fieldset.style.border = \"0px\";"+
                    " var ul = document.getElementsByTagName('ul').item(0);" +
                    " ul.style.width = \"100%\";" +
                    " var li = document.getElementsByTagName('li').item(0);" +
                    " li.style.width = \"49%\";" +
                    " var li2 = document.getElementsByTagName('li').item(1);" +
                    " li2.style.width = \"49%\";" +
                    " var container = document.getElementsByClassName('container').item(0);" +
                    " container.style.width = \"100%\";" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void autoScroll(){
        browser.scrollBy(0,1);
        browser.scrollTo(0, browser .getBottom());
    }






    void injectJs(String js){
        browser.loadUrl(js);
    }
    private void injectCss(String css) {
        try {
            browser.loadUrl("javascript:(function() {" +
                    " var parent = document.head;" +
                    " var style = document.createElement('style');" +
                    " style.type = 'text/css';" +
                    " style.innerHTML = '"+css+"';" +
                    " parent.appendChild(style);" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    int can = 0;
    public class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.contains("?"))
                url=url+"&imprimer=1";
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            //if(!order.equals("maps") && !url.contains("emploi.nat.tn")){
                injectStyle();
                injectFont();

            if(service!= null){
                if(!service.getJs().isEmpty()) injectJs(service.getJs());
                if(!service.getCss().isEmpty()) injectCss(service.getCss());
            }


            if (MyApplication.nightMode) {
                setNightMode();
            }


            new Handler().postDelayed(new Runnable() {
                public void run() {
                    loader.setVisibility(View.GONE);
                    browser.setVisibility(View.VISIBLE);
                }
            }, 500);

            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //loader.setIndicator(INDICATORS[new Random().nextInt(INDICATORS.length)]);
            //loader.show();
            loader.setVisibility(View.VISIBLE);
            browser.setVisibility(View.INVISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            //Toast.makeText(getApplicationContext(), getString(R.string.check_network) +": "+ description , Toast.LENGTH_LONG).show();
            //browser.loadUrl("http://www.ekhdemni.tn/404");
            alertWindow(getString(R.string.check_network));
            return;

        }
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
            //Your code to do
            Toast.makeText(getApplicationContext(), getString(R.string.check_network) +": "+ error , Toast.LENGTH_LONG).show();
        }



    }


    public void alertWindow(String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getApplicationContext());
        }
        if(isInForeground()){
            builder.setTitle(getString(R.string.network_error))
                    .setMessage(message)
                    .setPositiveButton(R.string.refresh, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            browser.loadUrl(url);
                        }
                    })
                    .setNegativeButton(R.string.network_settings, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                            startActivity(intent);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }









    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {
            browser.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.borwser_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {


            case R.id.share:
                shareIt();
                return true;


            case R.id.copy:
                copyUrl();
                return true;


            case R.id.next:
                browser.goForward();
                return true;

            case net.ekhdemni.R.id.refresh:
                browser.reload();
                return true;



            case R.id.autoscroll:
                autoScroll();
                return true;

            case net.ekhdemni.R.id.exit:
                Intent exit = new Intent(Intent.ACTION_MAIN);
                exit.addCategory(Intent.CATEGORY_HOME);
                exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(exit);
                return true;

            default: // R.id.exit
                System.exit(0);
                return true;
        }

    }



//
}

