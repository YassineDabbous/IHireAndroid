package net.ekhdemni.presentation.ui.fragments;


import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ekhdemni.MyApplication;
import net.ekhdemni.R;
import net.ekhdemni.model.ModelType;
import net.ekhdemni.presentation.ui.activities.CommentsActivity;
import net.ekhdemni.presentation.ui.activities.ViewerActivity;
import tn.core.presentation.base.MyFragment;
import tn.core.presentation.extensions.NestedWebView;
import net.ekhdemni.model.models.Article;
import net.ekhdemni.model.models.Resource;
import tn.core.util.DateUtils;
import net.ekhdemni.utils.ImageHelper;
import tn.core.util.PrefManager;
import tn.core.model.net.net.NetworkUtils;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends MyFragment {

    //ViewerActivity parentActivity;
    public Resource resource = new Resource();
    public Article article;
    FloatingActionButton fab,fabComments;
    NestedWebView browser;
    TextView titleView, messagePart1, messagePart2, dateView, normalDateview, resourceView;
    ImageView pictureView, resourceLogoView;
    ImageView verified;
    public List<String> urls = new ArrayList<>();
    //AdView //bannerInArticle;

    NestedScrollView nativeView, smartView;
    //View view;
    private PrefManager prefManager;

    @Override
    public void clean() {
        super.clean();
        //parentActivity = null;
        resource = null;
        prefManager = null;
        fab = fabComments = null;
        browser = null;
        titleView = messagePart1 = messagePart2  = dateView = normalDateview = resourceView = null;
        pictureView = resourceLogoView = null;
        verified = null;
        urls = null;
        nativeView = smartView = null;
    }


    @Override
    public void init() {
        super.init();
        View view = getView();
        if (view!=null){
            prefManager = new PrefManager(getContext());
            initWebDialog();

            smartView = view.findViewById(R.id.smartView);
            nativeView = view.findViewById(R.id.nativeView);


            browser = view.findViewById(R.id.webView);
            //bannerInArticle = view.findViewById(R.id.bannerInArticle);


            //parentActivity = (ViewerActivity) getActivity();
            //parentActivity.browser = browser;

            fab = view.findViewById(R.id.fab);
            fabComments = view.findViewById(R.id.fabComments);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareScreen();
                }
            });
            fabComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (NetworkUtils.isOnline(getContext())){
                        CommentsActivity.title = article.getTitle();
                        CommentsActivity.link = article.getUrl();
                        CommentsActivity.type = ModelType.LINK;
                        CommentsActivity.item_id = 0;
                        startActivity(new Intent(getActivity(), CommentsActivity.class));
                    }
                    else
                        Toast.makeText(getContext(), getString(R.string.check_network), Toast.LENGTH_SHORT).show();
                }
            });



            if (article!=null){
                article.setRead(getContext(), true);
                resource = article.getResource(getContext());
                useNativeView();
            }else{
                //Toast.makeText(getContext(), "plz go back to jobs list", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        }
    }

    public ArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_article, container, false);
    }


    String setHtml(){
        String fontFamily = prefManager.getPreference().getString(prefManager.FONT, "Jazeera");
        fontFamily = fontFamily+" ,'Changa', 'Jazeera', sans-serif";
        String right = "right";
        String left = "left";
        String dir = "";
        if(isRtl(article.getTitle())){
            dir = "dir='rtl'";
            right = "left";
            left = "right";
        }
        String verifiedBadge = "";
        if(resource.verified)
            verifiedBadge = "<img src='file:///android_asset/verified.png' style='width: 30px !important; height: 30px;' />";
        String fontResource = "<style> @font-face { src: url(\"file:///android_asset/fonts/jazeera-regular.ttf\"); format('truetype'); font-family: 'Jazeera'; font-style: normal; } @font-face { src: url(\"file:///android_asset/fonts/Changa.woff2\"); format('woff2'); font-family: 'Changa'; font-style: normal; }</style> ";//"<link href=\"https://fonts.googleapis.com/css?family=Changa\" rel=\"stylesheet\"/>";
        String logoDom =  "<img style=\"width: 35px !important;height: 35px !important;float: "+left+";\" src=\"" + resource.logo + "\">";
        String bottomLogo = "<div style=\"width:100%; height:80px;position: relative;\"><div style=\"text-align: center;width: 100%;position: absolute;top: 50%;transform: translateY(-50%);\"><img src=\"file:///android_asset/banner.png\" style=\" height: 100%; \"><br></div></div>";
        String prefix = "<html "+dir+"><head> <meta name=\"viewport\" content=\"width=device-width\"> <style> body{ margin: 0 !important; } img{max-height:200px; width: 100% !important;} *{ overflow-wrap: break-word; word-wrap: break-word; -ms-word-break: break-all; word-break: break-all; word-break: break-word; -ms-hyphens: auto; -moz-hyphens: auto; -webkit-hyphens: auto; hyphens: auto; } </style> "+fontResource+" </head> <body style=\"font-family: "+fontFamily+";\"> <div style=\"width: 100%;display: flex\">  <div style='width: 65%;background: #212121;'>"+logoDom+"<strong style='color: #fff;float: "+left+";height: 35px;line-height: 35px;text-align: center;margin-right: 6px;margin-left: 6px;'>"+resource.title+"</strong> "+verifiedBadge+" </div> <div style=\"color: #fff;flex: 1;background: #212121;height: 35px;line-height: 35px;float: "+right+";\"><b style=\"margin-left: 5px;margin-right: 5px;font-size: 13px;width: 100%; text-align: "+right+"; float: "+right+";\">"+DateUtils.getTimeAgo(article.getPublished())+"</b></div> </div> <div style=\"width: 98%;color: #212121; margin:5px;\"><strong style=\" min-height: 30px; line-height: 30px; padding: 2px; \">"+article.getTitle()+"</strong> <br> <span style=\" color: #90949c; font-size: 12px; margin: 0px 5px 10px 5px; width: 100%;\">"+DateUtils.timeToPretty(article.getPublished())+"</span> </div> <div style='width:100%'><center>";
        String suffix = "</center></div><br/><div style='height:30px'></div></body></html>";
        String html = prefix+"<div style='width:97%;padding: 5px;'>"+article.getContent()+"</div>"+bottomLogo+"<br/>"+suffix;
        Log.w("html", html);
        return html;
    }

    DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

            request.setMimeType(mimeType);
            //------------------------COOKIE!!------------------------
            String cookies = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("cookie", cookies);
            //------------------------COOKIE!!------------------------
            request.addRequestHeader("User-Agent", userAgent);
            request.setDescription("Downloading file...");
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
            DownloadManager dm = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(getContext(), "Downloading File", Toast.LENGTH_LONG).show();
        }
    };

    private void injectSize(String fontSize) {
        browser.loadUrl("javascript:(function() {" +
                "var all = document.getElementsByTagName(\"*\"); for (var i=0, max=all.length; i < max; i++) { all[i].style.fontSize = \""+fontSize+"px\"; }"+
                "})()");
    }
    private void injectFont(String font) {
        browser.loadUrl("javascript:(function() {" +
                "var all = document.getElementsByTagName(\"*\"); for (var i=0, max=all.length; i < max; i++) { all[i].style.fontFamily = \""+font+"px\"; }"+
                "})()");
    }

    private void blackLinks() {
        browser.loadUrl("javascript:(function() {" +
                "var all = document.getElementsByTagName(\"a\"); for (var i=0, max=all.length; i < max; i++) { all[i].style.color = \"#000000\"; }"+
                "})()");
    }



    private void reloadImages() {
        browser.loadUrl("javascript:(function() {" +
                "var iframes = document.getElementsByTagName(\"img\"); for (var i=0, max=iframes.length; i < max; i++) { iframes[i].src = iframes[i].src; }"+
                "})()");
    }


//browser.scrollBy(0,1);
    void autoScroll(){
        canScroll = true;
        startAutoScroll();
        prefManager.setBoolean(prefManager.AUTO_SCROLL, true);
    }
    int to = 1;
    public boolean canScroll = true;
    public boolean scrollMe = false;
    public void startAutoScroll(){
        browser.scrollTo(0, to);
        to=to+4;
        if(canScroll && scrollMe && to<browser.getBottom()){
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startAutoScroll();
                }
            }, 100);
        }else if(canScroll && to >= browser.getBottom()){ //
            //canScroll = false;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ((ViewerActivity) getActivity()).moveNext();
                }
            }, 1000);
        }
    }
    void stopAutoScroll(){
        canScroll = false;
        browser.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        prefManager.setBoolean(prefManager.AUTO_SCROLL, false);
    }

    void setNightMode(){
        /*int bg  = getResources().getColor(R.color.bg_night);
        Log.w("bg color", "background color is "+bg);
        browser.loadUrl("javascript:(function() {" +
                " var all = document.getElementsByTagName(\"*\"); for (var i=0, max=all.length; i < max; i++) { all[i].style.backgroundColor = \"#151518\"; if( all[i].style.color == \"\" || all[i].style.color == \"rgb(21, 21, 24)\" || all[i].style.color == \"rgb(0, 0, 0)\" ){ all[i].style.color = \"#ffffff\"; } }"+
                "})()");*/
    }

    public class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            url = overrideUrl(url);//url.replace("file://","https://");
            urls.add(url);
            return false;
        }


        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

            try {
                return new WebResourceResponse("", "", new URL(url).openStream());
            } catch(Exception exception) {
                Log.e( "shouldIntrceptRqst---->", exception.toString() );
            }

            return super.shouldInterceptRequest(view, url);

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (MyApplication.nightMode) {
                setNightMode();
            }

            view.getSettings().setLoadsImagesAutomatically(true);
            reloadImages();

            boolean autoScroll = prefManager.getPreference().getBoolean(prefManager.AUTO_SCROLL, false);
            if (autoScroll) startAutoScroll();

            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            //Toast.makeText(getApplicationContext(), getString(R.string.check_network) +": "+ description , Toast.LENGTH_LONG).show();
            //browser.loadUrl("http://www.ekhdemni.tn/404");
            alertWindow(getString(R.string.check_network));
            return;

        }



    }


    public void alertWindow(String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle(getString(R.string.network_error))
                .setMessage(message)
                .setPositiveButton(R.string.refresh, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        browser.reload();
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





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // First clear current all the menu items
        menu.clear();
        // Add the new menu items
        inflater.inflate(R.menu.main, menu);

        super.onCreateOptionsMenu(menu, inflater);
        //super.onCreateOptionsMenu(menu);
    }

    boolean isSmartView = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeViewer:
                if(isSmartView){
                    useNativeView();
                    item.setTitle(R.string.smartView);
                }else{
                    useSmartView();
                    item.setTitle(R.string.nativeView);
                }
                return true;
            case R.id.article_links:
                getLinks();
                return true;
            case R.id.autoscroll:
                autoScroll();
                return true;
            case R.id.stopAutoScroll:
                stopAutoScroll();
                return true;
            case R.id.font_size:
                fontSettings(1);
                return true;
            case R.id.font:
                fontSettings(0);
                return true;
            case R.id.copy:
                copyUrl();
                return true;
            case R.id.shareUrl:
                shareUrl();
                return true;
            case R.id.shareScreen:
                shareScreen();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    File imagePAth;
    private void saveBitmap(Bitmap bitmap) {
        this.imagePAth = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        try {
            FileOutputStream fos = new FileOutputStream(this.imagePAth);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("saveBitmap", e.toString());
        } catch (IOException e2) {
            Log.d("saveBitmap", e2.toString());
        }
    }


    //create bitmap from the ScrollView
    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }
    public Bitmap takeScreenshot() {
        if(!isSmartView)
            return getBitmapFromView(nativeView, nativeView.getChildAt(0).getHeight(), nativeView.getChildAt(0).getWidth());
        else{
            return getBitmapFromView(smartView, smartView.getChildAt(0).getHeight(), smartView.getChildAt(0).getWidth());
        }

        /*view = browser;
        view.measure(View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int iHeight = bitmap.getHeight();
        canvas.drawBitmap(bitmap, 0, iHeight, paint);
        view.draw(canvas);
        return bitmap;*/
    }
    private void shareIt() {
        String description = article.getTitle()  + "\n-" + getString(R.string.via_application);
        Uri uri = Uri.fromFile(imagePAth);
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        sharingIntent.setType("image/*");
        sharingIntent.putExtra("android.intent.extra.TEXT", description);
        sharingIntent.putExtra("android.intent.extra.STREAM", uri);
        Intent sharingfinalintent = Intent.createChooser(sharingIntent, "Share via");
        sharingfinalintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(sharingfinalintent);
    }
    public void shareAction() {
        if(!isSmartView)
            //bannerInArticle.setVisibility(View.GONE);
        fab.setVisibility(View.INVISIBLE);
        fabComments.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                saveBitmap(takeScreenshot());
                shareIt();
                fab.setVisibility(View.VISIBLE);
                fabComments.setVisibility(View.VISIBLE);
                //if(!isSmartView)
                    //bannerInArticle.setVisibility(View.VISIBLE);

            }
        }, 500);

    }


    public void shareScreen() {
        if (ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(getContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            if(isSmartView) blackLinks();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    shareAction();
                }
            }, 500);
            return;
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 101);
        return;
    }



    private void shareUrl() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, article.getUrl()+" \n "+ getResources().getString(R.string.shared_via)+" "+getResources().getString(R.string.app_name)+" https://play.google.com/store/apps/details?id="+getContext().getPackageName());
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_via)));
    }

    private void copyUrl(){
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.app_name), article.getUrl());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), getResources().getString(R.string.url_copied), Toast.LENGTH_SHORT).show();
    }








    List<String> entriesArrayList;
    List<String> valuesArrayList;
    void fontSettings(final int what){
        if(what==0){
            entriesArrayList = Arrays.asList(getResources().getStringArray(R.array.fonts_entries));
            valuesArrayList = Arrays.asList(getResources().getStringArray(R.array.fonts_values));
        }
        else{
            entriesArrayList = Arrays.asList(getResources().getStringArray(R.array.font_sizes_entries));
            valuesArrayList = Arrays.asList(getResources().getStringArray(R.array.font_sizes_values));
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,entriesArrayList);


        builder.setTitle(getString(R.string.pick_one));
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = valuesArrayList.get(which);
                if(what==0){
                    prefManager.setString(PrefManager.FONT,      value);
                    injectFont(value);
                }
                else {
                    prefManager.setString(PrefManager.FONT_SIZE, value);
                    injectSize(value);
                }
            }
        });

Log.w("fontSettings","fontSettings");
        builder.show();
    }


    void getLinks(){
            if(urls.size()!=0){
                String prefix = "<html> <head> </head> <body style=\"background-color:#eee; \"><center>";
                String suffix = "</center></body></html>";
                String html = "<iframe style='height:100%;width:100%' src='"+urls.get(0)+"'  frameborder='0'></iframe>";
                wv.loadData(prefix+html+suffix,"text/html", "utf-8");
                alert.show();
            }else{
                Toast.makeText(getContext(), "this article hasn't external links", Toast.LENGTH_SHORT).show();
            }
    }
    WebView wv;
    AlertDialog.Builder alert;
    void initWebDialog(){
        alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Title here");
        wv = new WebView(getContext());
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("غلق", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }




/*
    void initInArticleBanner(){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        bannerInArticle.loadAd(adRequest);
        bannerInArticle.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                bannerInArticle.setVisibility(View.VISIBLE);
            }


            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                bannerInArticle.setVisibility(View.GONE);
            }
        });
    }
*/

    void showViewer(){
        if(isSmartView){
            smartView.setVisibility(View.VISIBLE);
        }else{
            nativeView.setVisibility(View.VISIBLE);
        }
    }
    void hideViewer(){
        if(isSmartView){
            smartView.setVisibility(View.GONE);
        }else{
            nativeView.setVisibility(View.GONE);
        }
    }



    public boolean isRtl(String string) {
        if (string == null) {
            return false;
        }

        for (int i = 0, n = string.length(); i < n; ++i) {
            byte d = Character.getDirectionality(string.charAt(i));

            switch (d) {
                case Character.DIRECTIONALITY_RIGHT_TO_LEFT:
                case Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC:
                case Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING:
                case Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE:
                    return true;

                case Character.DIRECTIONALITY_LEFT_TO_RIGHT:
                case Character.DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING:
                case Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE:
                    return false;
            }
        }

        return false;
    }


    ImageView appBanner;
    void useNativeView(){
        ((ViewerActivity) getActivity()).setToolbarTitle(R.string.nativeView);
        View view = getView();
        titleView = view.findViewById(R.id.title);
        verified = view.findViewById(R.id.verified);
        messagePart1 = view.findViewById(R.id.messagePart1);
        messagePart2 = view.findViewById(R.id.messagePart2);
        dateView = view.findViewById(R.id.date);
        normalDateview = view.findViewById(R.id.normalDate);
        resourceView = view.findViewById(R.id.resourceTitle);
        pictureView = view.findViewById(R.id.photo);
        resourceLogoView = view.findViewById(R.id.resourceLogo);
        appBanner = view.findViewById(R.id.appBanner);
        LinearLayout viewHeader = view.findViewById(R.id.viewHeader);
        if(isRtl(article.getTitle())){
            ViewCompat.setLayoutDirection (viewHeader, ViewCompat.LAYOUT_DIRECTION_RTL);
            dateView.setGravity(Gravity.LEFT);
            resourceView.setGravity(Gravity.RIGHT);
            titleView.setGravity(Gravity.RIGHT);
            normalDateview.setGravity(Gravity.RIGHT);
            messagePart1.setGravity(Gravity.RIGHT);
            messagePart2.setGravity(Gravity.RIGHT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                dateView.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
                resourceView.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
                titleView.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
                normalDateview.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
                messagePart1.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
                messagePart2.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
            }
        }
        titleView.setText(article.getTitle());
        Document document = Jsoup.parse(article.getContent());
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        document.select("img").remove();
        document.select("br").remove();
        Elements elements = document.getAllElements();
        for (Element element: elements) {
            Element style = document.select("div").first();
            if(style != null){
                String sty = style.attr("style");
                style.attr("style", sty+"; font-size:14px");
            }
        }

        String[] lines = document.html().split("</div>");
        Log.wtf("lines lines",lines.length+"");
        if(lines.length<4){
            messagePart1.setText(Html.fromHtml(document.html()));
            messagePart2.setText("");
        }
        else{
            String part1 = "";
            String part2 = "";
            int middle = lines.length / 2;
            for (int i = 0; i < middle; i++) {
                part1 += lines[i]+"</div>";
            }
            for (int i = middle; i < lines.length; i++) {
                part2 += lines[i]+"</div>";
            }
            Spanned sp = Html.fromHtml(part1);
            Spanned sp2 = Html.fromHtml(part2);
            SpannableString span1=  new SpannableString(sp);
            span1.setSpan(new RelativeSizeSpan(1f), 0, sp.length(), 0);
            messagePart1.setText(span1);
            SpannableString span2=  new SpannableString(sp2);
            span2.setSpan(new RelativeSizeSpan(1f), 0, sp2.length(), 0);
            messagePart2.setText(span2);
        }
        messagePart1.setMovementMethod(LinkMovementMethod.getInstance());
        messagePart2.setMovementMethod(LinkMovementMethod.getInstance());
        //appBanner.setImageBitmap(getBitmapFromAssets("banner.png", getContext()));
        Log.wtf("bannerbannerbanner","bannerbannerbanner");
        //initInArticleBanner();
        dateView.setText(DateUtils.getTimeAgo(article.getPublished()));
        normalDateview.setText(DateUtils.timeToPretty(article.getPublished()));
        resourceView.setText(resource.title);
        if(resource.verified){
            //verified.setImageBitmap(getBitmapFromAssets("verified.png", getContext()));
            //verified.setVisibility(View.VISIBLE);
        }
        ImageHelper.load(pictureView,article.getImg(), 400,300);
        ImageHelper.load(resourceLogoView, resource.logo, 40,40);
        nativeView.setVisibility(View.VISIBLE);
        smartView.setVisibility(View.GONE);
        isSmartView = false;
    }

/*    public Bitmap getBitmapFromAssets(String fileName, Context context){
        AssetManager am = getContext().getAssets();
        InputStream is = null;
        try{
            is = am.open(fileName);
        }catch(IOException e){
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }*/


    void useSmartView(){
        //browser.setFragment(this);
        ((ViewerActivity) getActivity()).setToolbarTitle(R.string.smartView);
        browser.setWebViewClient(new MyBrowser());
        WebSettings settings = browser.getSettings();
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDomStorageEnabled(true);
        browser.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.setScrollbarFadingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            browser.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            browser.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        browser.setMinimumHeight(1000);



        browser.setDownloadListener(downloadListener);
        browser.setOnScrollChangedCallback(new NestedWebView.OnScrollChangedCallback() {
            @Override
            public void onScrollChange(WebView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY && scrollY > 0) {
                    fab.hide();
                    fabComments.hide();
                }
                else if (scrollY < oldScrollY) {
                    fab.show();
                    fabComments.show();
                }
            }
        });
        ViewTreeObserver viewTreeObserver  = browser.getViewTreeObserver();

        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                browserHeight = browser.getMeasuredHeight();
                if( browserHeight != 0 ){
                    //Toast.makeText(getActivity(), "height:"+height,Toast.LENGTH_SHORT).show();
                    browser.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return false;
            }
        });

        String html = setHtml();
        browser.loadDataWithBaseURL(resource.getBaseUri(), html, "text/html", "utf-8", null);
        nativeView.setVisibility(View.GONE);
        smartView.setVisibility(View.VISIBLE);
        isSmartView = true;
    }
    int browserHeight = 0;
}