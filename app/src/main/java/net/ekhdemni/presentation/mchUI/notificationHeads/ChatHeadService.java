package net.ekhdemni.presentation.mchUI.notificationHeads;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.view.ViewCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.ui.activities.ViewerActivity;
import net.ekhdemni.model.feeds.db.MyDataBase;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.listeners.EndlessListener;
import net.ekhdemni.model.models.Article;
import net.ekhdemni.model.models.Resource;
import tn.core.util.DateUtils;
import net.ekhdemni.utils.ImageHelper;
import tn.core.util.Urls;
import net.ekhdemni.model.oldNet.Ekhdemni;
import android.widget.TextView;

public class ChatHeadService extends Service {
    private WindowManager windowManager;
    LayoutInflater inflater;
    private RelativeLayout currentHeadView, headView1, overView, removeView;
    TextView counter1;
    private LinearLayout txtView, txt_linearlayout;
    private ImageView chatheadImg, removeImg;
    private TextView txt1;
    private int x_init_cord, y_init_cord, x_init_margin, y_init_margin;
    private Point szWindow = new Point();
    private boolean isLeft = true;
    private String sMsg = "";
    private String imgSrc = "";
    int nbr = 1;
    private String url = Urls.root, url1, url2, url3, url4;
    int windowWidth = 0;
    int currentXPosition = 0;
    int currentYPosition = 0;

    Handler myHandler = new Handler();
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            if (txtView != null) {
                txtView.setVisibility(View.GONE);
            }
        }
    };

    @SuppressWarnings("deprecation")

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d(net.ekhdemni.presentation.mchUI.notificationHeads.Utils.LogTag, "ChatHeadService.onCreate()");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.d(net.ekhdemni.presentation.mchUI.notificationHeads.Utils.LogTag, "ChatHeadService.onStartCommand() -> startId=" + startId);

        if (intent != null) {
            Bundle bd = intent.getExtras();

            if (bd != null) {
                final String terminate = bd.getString("terminate");
                if (terminate != null) {
                    stopSelf();
                } else {
                    sMsg = bd.getString("msg");
                    imgSrc = bd.getString("img");
                    url = bd.getString("url");
                    nbr = bd.getInt("nbr");
                }
            }
        }

        if (startId == Service.START_STICKY) {
            handleStart();
            return super.onStartCommand(intent, flags, startId);
        } else {
            handleStart();
            return Service.START_NOT_STICKY;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if ((overView != null) && (overView.isShown())) {
            windowManager.removeView(overView);
        }
        if ((headView1 != null) && (headView1.isShown())) {
            windowManager.removeView(headView1);
        }

        if (txtView != null) {
            windowManager.removeView(txtView);
        }

        if (removeView != null) {
            windowManager.removeView(removeView);
        }

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d(net.ekhdemni.presentation.mchUI.notificationHeads.Utils.LogTag, "ChatHeadService.onBind()");
        return null;
    }


    private void handleStart() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            windowManager.getDefaultDisplay().getSize(szWindow);
        } else {
            int w = windowManager.getDefaultDisplay().getWidth();
            int h = windowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }

        removeView = (RelativeLayout) inflater.inflate(R.layout.floats_remover, null);
        WindowManager.LayoutParams paramRemove = getLayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        paramRemove.gravity = Gravity.TOP | Gravity.LEFT;

        removeView.setVisibility(View.GONE);
        removeImg = (ImageView) removeView.findViewById(R.id.remove_img);
        windowManager.addView(removeView, paramRemove);


        setHeads();


        txtView = (LinearLayout) inflater.inflate(R.layout.float_text, null);
        txt1 = (TextView) txtView.findViewById(R.id.txt1);
        txt_linearlayout = (LinearLayout) txtView.findViewById(R.id.txt_linearlayout);
        WindowManager.LayoutParams paramsTxt = getLayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        paramsTxt.gravity = Gravity.TOP | Gravity.LEFT;
        txtView.setVisibility(View.GONE);
        windowManager.addView(txtView, paramsTxt);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                windowManager.getDefaultDisplay().getSize(szWindow);
            } else {
                int w = windowManager.getDefaultDisplay().getWidth();
                int h = windowManager.getDefaultDisplay().getHeight();
                szWindow.set(w, h);
            }
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) headView1.getLayoutParams();

            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.d(net.ekhdemni.presentation.mchUI.notificationHeads.Utils.LogTag, "ChatHeadService.onConfigurationChanged -> landscap");

                if (txtView != null) {
                    txtView.setVisibility(View.GONE);
                }

                if (layoutParams.y + (headView1.getHeight() + getStatusBarHeight()) > szWindow.y) {
                    layoutParams.y = szWindow.y - (headView1.getHeight() + getStatusBarHeight());
                    try {
                        windowManager.updateViewLayout(headView1, layoutParams);
                    }catch (Exception e){}
                }

                if (layoutParams.x != 0 && layoutParams.x < szWindow.x) {
                    resetPosition(headView1, szWindow.x);
                }

            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.d(net.ekhdemni.presentation.mchUI.notificationHeads.Utils.LogTag, "ChatHeadService.onConfigurationChanged -> portrait");

                if (txtView != null) {
                    txtView.setVisibility(View.GONE);
                }

                if (layoutParams.x > szWindow.x) {
                    resetPosition(headView1, szWindow.x);
                }

            }

        }catch (NullPointerException e){
            MyActivity.log("☻ Catched NullPointerException at line: "+ e.getStackTrace()[0].getLineNumber());
            MyActivity.log("☻ Catched NullPointerException Message: "+ e.getMessage());
        }catch (IllegalArgumentException e){
            MyActivity.log("☻ Catched IllegalArgumentException at line: "+ e.getStackTrace()[0].getLineNumber());
            MyActivity.log("☻ Catched IllegalArgumentException Message: "+ e.getMessage());
        }catch (Exception e){
            MyActivity.log("☻ Catched Exception at line: "+ e.getStackTrace()[0].getLineNumber());
            MyActivity.log("☻ Catched Exception Message: "+ e.getMessage());
        }
    }

    private void resetPosition(RelativeLayout relativeLayout, int x_cord_now) {
        if (float_main==null || !float_main.isShown()) {
            if (x_cord_now <= szWindow.x / 2) {
                isLeft = true;
                moveToLeft(relativeLayout, x_cord_now);
            } else {
                isLeft = false;
                moveToRight(relativeLayout, x_cord_now);
            }
            //collectAll(currentXPosition, currentXPosition);
        }
    }

    private void moveToLeft(final RelativeLayout relativeLayout, final int x_cord_now) {
        final int x = szWindow.x - x_cord_now;

        new CountDownTimer(500, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) relativeLayout.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;
                mParams.x = 0 - (int) (double) bounceValue(step, x);
                try {
                    windowManager.updateViewLayout(relativeLayout, mParams);
                }catch (Exception e){}
            }

            public void onFinish() {
                mParams.x = 0;
                currentXPosition = 0;
                try {
                    windowManager.updateViewLayout(relativeLayout, mParams);
                }catch (Exception e){}
            }
        }.start();
    }

    private void moveToRight(final RelativeLayout relativeLayout, final int x_cord_now) {
        try {
            new CountDownTimer(500, 5) {
                WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) relativeLayout.getLayoutParams();

                public void onTick(long t) {
                    long step = (500 - t) / 5;
                    mParams.x = szWindow.x + (int) (double) bounceValue(step, x_cord_now) - relativeLayout.getWidth();
                    try {
                        windowManager.updateViewLayout(relativeLayout, mParams);
                    }catch (Exception e){}
                }

                public void onFinish() {
                    mParams.x = szWindow.x - relativeLayout.getWidth();
                    currentXPosition = mParams.x;
                    try {
                        windowManager.updateViewLayout(relativeLayout, mParams);
                    }catch (Exception e){}
                }
            }.start();
        }catch (Exception e){}
    }


    private double bounceValue(long step, long scale) {
        double value = scale * Math.exp(-0.055 * step) * Math.cos(0.08 * step);
        return value;
    }

    private int getStatusBarHeight() {
        int statusBarHeight = (int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

    public void moveToTop() {
        WindowManager.LayoutParams paramsView = getLayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        paramsView.gravity = Gravity.TOP | Gravity.LEFT;
        paramsView.y = 0;
        windowWidth = windowManager.getDefaultDisplay().getWidth();
        paramsView.x = 0;
        /*if (overView != null && overView.isShown()) {
            windowManager.updateViewLayout(overView, paramsView);
        } else {
            windowManager.addView(overView, paramsView);
        }*/
        try {
            if (headView1 != null && headView1.isShown()) {
                windowManager.updateViewLayout(headView1, paramsView);
            } else {
                windowManager.addView(headView1, paramsView);
            }
        }catch (Exception e){}
        /*if (headView1 != null && headView1.isShown()) {
            paramsView.x = (windowWidth / 5);
            windowManager.updateViewLayout(headView1, paramsView);
        }*/
    }


    private void overView_click() {
        removeAll();
        Intent it = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }

    private void remove(RelativeLayout headView) {
        if ((headView != null) && (headView.isShown())) {
            windowManager.removeView(headView);
        }
        if (txtView != null && (txtView.isShown())) {
            windowManager.removeView(txtView);
        }

        if (removeView != null && (removeView.isShown())) {
            windowManager.removeView(removeView);
        }
    }

    private void removeAll() {
        if ((overView != null) && (overView.isShown())) {
            windowManager.removeView(overView);
        }
        if ((headView1 != null) && (headView1.isShown())) {
            windowManager.removeView(headView1);
        }

        if (txtView != null && (txtView.isShown())) {
            windowManager.removeView(txtView);
        }

        if (removeView != null && (removeView.isShown())) {
            windowManager.removeView(removeView);
        }
    }


    private void collectAll(int x, int y) {
        if (float_main==null || !float_main.isShown()) {
            WindowManager.LayoutParams paramsView = getLayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            paramsView.gravity = Gravity.TOP | Gravity.LEFT;
            paramsView.x = x;
            paramsView.y = y;
            //Toast.makeText(getApplicationContext(), paramsView.x + " : " + paramsView.x, //Toast.LENGTH_LONG).show();
            remove(overView);
            if ((headView1 != null) && (headView1.isShown())) {
                try {
                    windowManager.updateViewLayout(headView1, paramsView);
                }catch (Exception e){}
            }
        }
    }




    private void onHeadViewClick(RelativeLayout relativeLayout) {
        counter1.setVisibility(View.GONE);
        if(float_main!=null && float_main.isShown())
            hideFloatContent();
        else if(float_viewer!=null && float_viewer.isShown()){
            if(float_main!=null) float_main.setVisibility(View.VISIBLE);
            hideFloatDetails();
        }else{
            showFloatContent();
        }
    }


    public String getUrlByHeadView(RelativeLayout relativeLayout) {
        String u = url;
        if (relativeLayout.equals(headView1)) u = url1;
        url = u;
        return u;
    }

    private void chathead_longclick() {
        Log.d(net.ekhdemni.presentation.mchUI.notificationHeads.Utils.LogTag, "Into ChatHeadService.chathead_longclick() ");

        WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
        int x_cord_remove = (szWindow.x - removeView.getWidth()) / 2;
        int y_cord_remove = szWindow.y - (removeView.getHeight() + getStatusBarHeight());

        param_remove.x = x_cord_remove;
        param_remove.y = y_cord_remove;

        if (removeView != null && (removeView.isShown()))
            try {
                windowManager.updateViewLayout(removeView, param_remove);
            }catch (Exception e){}
    }


    private void showMsg(String sMsg, RelativeLayout floatView) {
        if (txtView != null && floatView != null) {
            Log.d(net.ekhdemni.presentation.mchUI.notificationHeads.Utils.LogTag, "ChatHeadService.showMsg -> sMsg=" + sMsg);
            Log.e("windows size", "" + windowManager.getDefaultDisplay().getWidth());
            txt1.setText(sMsg);
            myHandler.removeCallbacks(myRunnable);

            WindowManager.LayoutParams param_chathead = (WindowManager.LayoutParams) floatView.getLayoutParams();
            WindowManager.LayoutParams param_txt = (WindowManager.LayoutParams) txtView.getLayoutParams();

            txt_linearlayout.getLayoutParams().height = floatView.getHeight();
            txt_linearlayout.getLayoutParams().width = szWindow.x / 2;

            if (isLeft) {
                param_txt.x = param_chathead.x + chatheadImg.getWidth();
                param_txt.y = param_chathead.y;

                txt_linearlayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            } else {
                param_txt.x = param_chathead.x - szWindow.x / 2;
                param_txt.y = param_chathead.y;

                txt_linearlayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }

            txtView.setVisibility(View.VISIBLE);
            try {
                windowManager.updateViewLayout(txtView, param_txt);
            }catch (Exception e){}

            myHandler.postDelayed(myRunnable, 4000);

        }

    }

   // WebView browser;
    RelativeLayout float_main, float_menu, float_viewer;
    public void hideFloatContent() {
        if(recyclerView!=null) recyclerView.clearOnScrollListeners();
        if ((float_main != null) && (float_main.isShown())) {
            windowManager.removeView(float_main);
        }
        hideFloatDetails();
    }
    public void hideFloatDetails() {
        if ((float_menu != null) && (float_menu.isShown())) {
            windowManager.removeView(float_menu);
        }
        if ((float_viewer != null) && (float_viewer.isShown())) {
            windowManager.removeView(float_viewer);
        }
    }
    EndlessListener endlessListener;
    RecyclerView recyclerView;
    long after = 0;
    public void showFloatContent() {
        moveToTop();
        hideFloatDetails();
        WindowManager.LayoutParams params = getLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        params.y = 0;
        params.x = 0;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        float_main = (RelativeLayout) inflater.inflate(R.layout.floats_shower, null);
        View topView = float_main.findViewById(R.id.transparentView);
        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFloatContent();
            }
        });

        recyclerView = (RecyclerView)  float_main.findViewById(R.id.itemsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        MyDataBase dba = MyDataBase.getInstance(getApplicationContext());
        dba.openToRead();
        l = dba.take("10",0);
        dba.close();
        final FloatRecyclerViewAdapter adapter = new FloatRecyclerViewAdapter(l);
        recyclerView.setAdapter(adapter);
        endlessListener = new EndlessListener(20, ChatHeadService.ADS_AFTER, new EndlessListener.Action() {
            @Override
            public void getOnScroll() {
                int last = l.size()-1;
                MyActivity.log( "last one "+last);
                if(last >= 0)
                    after = l.get(last).getDbId();
                MyDataBase dba = MyDataBase.getInstance(getApplicationContext());
                dba.openToRead();
                List<Article> lista = dba.take("10", after);
                dba.close();
                l.addAll(lista);
                if(lista.size()<10){
                    Log.w("data", "no more data in db");
                    endlessListener.total = l.size();
                }else{
                    endlessListener.total = l.size()+10;
                }
                endlessListener.isloading = false;
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.addOnScrollListener(endlessListener);



        windowManager.addView(float_main, params);
    }
    List<Article> l = new ArrayList<Article>();

    public void showFloatDetails() {
        moveToTop();

        WindowManager.LayoutParams params = getLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        params.y = 0;
        params.x = 0;
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        float_menu = (RelativeLayout) inflater.inflate(R.layout.float_menu, null);
        TextView textView = float_menu.findViewById(R.id.title);
        View area = float_menu.findViewById(R.id.area);
        textView.setText(article.getTitle());
        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHeadViewClick(null);
            }
        });
        textView.setVisibility(View.VISIBLE);
        area.setVisibility(View.VISIBLE);


        float_viewer = (RelativeLayout) inflater.inflate(R.layout.float_viewer, null);
        View topView = float_viewer.findViewById(R.id.top);
        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFloatContent();
            }
        });
        useNativeView(article, float_viewer);

        windowManager.addView(float_menu, params);
        params = getLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        params.y = 0;
        params.x = 0;
        params.height = windowManager.getDefaultDisplay().getHeight() - params.y;
        windowManager.addView(float_viewer, params);
    }






    public static boolean isRtl(String string) {
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

    AdView bannerInArticle;
    TextView titleView, messagePart1, messagePart2, dateView, normalDateview, resourceView;
    ImageView pictureView, resourceLogoView;
    ScrollView nativeView;
    void useNativeView(Article article, View view){
        CardView cardView = view.findViewById(R.id.articleCard);
        cardView.setCardBackgroundColor(Color.WHITE);
        nativeView = view.findViewById(R.id.nativeView);
        titleView = view.findViewById(R.id.title);
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
        }
        titleView.setText(article.getTitle());
        Document document = Jsoup.parse(article.getContent());
        document.select("img").remove();
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
            messagePart1.setText(Html.fromHtml(part1));
            messagePart2.setText(Html.fromHtml(part2));
        }
        //messagePart1.setMovementMethod(LinkMovementMethod.getInstance());
        //messagePart2.setMovementMethod(LinkMovementMethod.getInstance());
//        appBanner.setImageBitmap(ArticleFragment.getBitmapFromAssets("banner.png", view.getContext()));
        dateView.setText(DateUtils.getTimeAgo(article.getPublished()));
        normalDateview.setText(DateUtils.timeToPretty(article.getPublished()));
        Resource resource = article.getResource(view.getContext());
        resourceView.setText(resource.title);
        ImageHelper.load(pictureView,article.getImg(), 400,300);
        ImageHelper.load(resourceLogoView, resource.logo, 40,40);
        //------------- ads
        /*Log.wtf("bannerbannerbanner","bannerbannerbanner");
        bannerInArticle = view.findViewById(R.id.bannerInArticle);
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
        });*/
        //-------------
        shareBtn = view.findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shareScreen(shareBtn.getContext());
                goToApp();
            }
        });
    }
    ImageView shareBtn;


    void goToApp(){
        Intent i = new Intent(getApplicationContext(), ViewerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("ARTICLE", article);
        startActivity(i);
    }




    public class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }












    public WindowManager.LayoutParams getLayoutParams(int width, int height){
        WindowManager.LayoutParams p;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            p = new WindowManager.LayoutParams(
                    width,
                    height,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
        }else{
            p = new WindowManager.LayoutParams(
                    width,
                    height,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
        }
        return p;
    }





    public void setHeads() {
        WindowManager.LayoutParams params = getLayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.y = 0;
        params.x = 0;

        overView = (RelativeLayout) inflater.inflate(R.layout.float_head, null);
        chatheadImg = (ImageView) overView.findViewById(R.id.chathead_img);
        TextView t = (TextView) overView.findViewById(R.id.counter);
        t.setText("+");
        //t.setVisibility(View.GONE);
        chatheadImg.setImageResource(R.drawable.ic_stat_onesignal_default);
        overView.setOnTouchListener(new MyTouchListener(headView1));


        params.x = currentXPosition;
        params.y = currentYPosition;
        if (headView1 == null) {
            url1 = url;
            headView1 = (RelativeLayout) inflater.inflate(R.layout.float_head, null);
            chatheadImg = (ImageView) headView1.findViewById(R.id.chathead_img);
            counter1 = headView1.findViewById(R.id.counter);
            //chatheadImg.setImageURI(Uri.parse(imgSrc));
            if(nbr==0)
                counter1.setVisibility(View.GONE);
            else
                counter1.setText(""+nbr);
            windowManager.addView(headView1, params);
            headView1.setOnTouchListener(new MyTouchListener(headView1));
            showMsg(sMsg, headView1);
        }
    }













    public class MyTouchListener implements View.OnTouchListener {
        long time_start = 0, time_end = 0;
        boolean isLongclick = false, inBounded = false;
        int remove_img_width = 0, remove_img_height = 0;
        RelativeLayout headView;

        public MyTouchListener(RelativeLayout headView) {
            this.headView = headView;
        }

        Handler handler_longClick = new Handler();
        Runnable runnable_longClick = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Log.d(net.ekhdemni.presentation.mchUI.notificationHeads.Utils.LogTag, "Into runnable_longClick");

                isLongclick = true;
                removeView.setVisibility(View.VISIBLE);
                chathead_longclick();
            }
        };

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            currentHeadView = headView;
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) headView.getLayoutParams();
            int x_cord = (int) event.getRawX();
            int y_cord = (int) event.getRawY();
            int x_cord_Destination, y_cord_Destination;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    time_start = System.currentTimeMillis();
                    handler_longClick.postDelayed(runnable_longClick, 600);

                    remove_img_width = removeImg.getLayoutParams().width;
                    remove_img_height = removeImg.getLayoutParams().height;

                    x_init_cord = x_cord;
                    y_init_cord = y_cord;
                    x_init_margin = layoutParams.x;
                    y_init_margin = layoutParams.y;

                    if (txtView != null) {
                        txtView.setVisibility(View.GONE);
                        myHandler.removeCallbacks(myRunnable);
                    }
                }
                break;


                case MotionEvent.ACTION_UP: {
                    isLongclick = false;
                    removeView.setVisibility(View.GONE);
                    removeImg.getLayoutParams().height = remove_img_height;
                    removeImg.getLayoutParams().width = remove_img_width;
                    handler_longClick.removeCallbacks(runnable_longClick);
                    if (inBounded) {
                        remove(float_main);
                        remove(headView); //stopService(new Intent(ChatHeadService.this, ChatHeadService.class).putExtra("num", 1));
                        inBounded = false;
                        break;
                    }

                    int x_diff = x_cord - x_init_cord;
                    int y_diff = y_cord - y_init_cord;

                    if (Math.abs(x_diff) < 5 && Math.abs(y_diff) < 5) {
                        time_end = System.currentTimeMillis();
                        if ((time_end - time_start) < 300) {
                            onHeadViewClick(headView);
                        }
                    }

                    y_cord_Destination = y_init_margin + y_diff;

                    int BarHeight = getStatusBarHeight();
                    if (y_cord_Destination < 0) {
                        y_cord_Destination = 0;
                    } else if (y_cord_Destination + (headView.getHeight() + BarHeight) > szWindow.y) {
                        y_cord_Destination = szWindow.y - (headView.getHeight() + BarHeight);
                    }
                    layoutParams.y = y_cord_Destination;

                    inBounded = false;
                    resetPosition(headView, x_cord);
                }
                break;

                case MotionEvent.ACTION_MOVE: {
                    int x_diff_move = x_cord - x_init_cord;
                    int y_diff_move = y_cord - y_init_cord;

                    x_cord_Destination = x_init_margin + x_diff_move;
                    y_cord_Destination = y_init_margin + y_diff_move;

                    if (isLongclick) {
                        int x_bound_left = szWindow.x / 2 - (int) (remove_img_width * 1.5);
                        int x_bound_right = szWindow.x / 2 + (int) (remove_img_width * 1.5);
                        int y_bound_top = szWindow.y - (int) (remove_img_height * 1.5);

                        if ((x_cord >= x_bound_left && x_cord <= x_bound_right) && y_cord >= y_bound_top) {
                            inBounded = true;

                            int x_cord_remove = (int) ((szWindow.x - (remove_img_height * 1.2)) / 2);
                            int y_cord_remove = (int) (szWindow.y - ((remove_img_width * 1.2) + getStatusBarHeight()));

                            if (removeImg.getLayoutParams().height == remove_img_height) {
                                removeImg.getLayoutParams().height = (int) (remove_img_height * 1.2);
                                removeImg.getLayoutParams().width = (int) (remove_img_width * 1.2);

                                WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
                                param_remove.x = x_cord_remove;
                                param_remove.y = y_cord_remove;

                                if (removeView != null && (removeView.isShown()))
                                    try {
                                        windowManager.updateViewLayout(removeView, param_remove);
                                    }catch (Exception e){}
                            }

                            layoutParams.x = x_cord_remove + (Math.abs(removeView.getWidth() - headView.getWidth())) / 2;
                            layoutParams.y = y_cord_remove + (Math.abs(removeView.getHeight() - headView.getHeight())) / 2;
                            if (float_main==null || !float_main.isShown())
                                try {
                                    windowManager.updateViewLayout(headView, layoutParams);
                                }catch (Exception e){}
                            break;
                        } else {
                            inBounded = false;
                            removeImg.getLayoutParams().height = remove_img_height;
                            removeImg.getLayoutParams().width = remove_img_width;

                            WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
                            int x_cord_remove = (szWindow.x - removeView.getWidth()) / 2;
                            int y_cord_remove = szWindow.y - (removeView.getHeight() + getStatusBarHeight());

                            param_remove.x = x_cord_remove;
                            param_remove.y = y_cord_remove;

                            if (removeView != null && (removeView.isShown()))
                                try {
                                    windowManager.updateViewLayout(removeView, param_remove);
                                }catch (Exception e){}
                        }

                    }


                    layoutParams.x = x_cord_Destination;
                    layoutParams.y = y_cord_Destination;
                    currentYPosition = y_cord_Destination;
                    if (float_main==null || !float_main.isShown())
                        try {
                            windowManager.updateViewLayout(headView, layoutParams);
                        }catch (Exception e){}
                }
                break;


                default:
                    Log.d(net.ekhdemni.presentation.mchUI.notificationHeads.Utils.LogTag, "headView.setOnTouchListener  -> event.getAction() : default");
                    break;
            }
            return true;
        }
    }




























    public  static final int ADS_AFTER = Ekhdemni.ADS_AFTER;
    Article article;
    public class FloatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int VIEW_TYPE_MODEL = 1;
        private static final int VIEW_TYPE_ADS = 2;
        private final List<Article> items;
        Context context;

        public FloatRecyclerViewAdapter(List<Article> items) {
            this.items = items;
        }
        @Override
        public int getItemCount() {
            int s = items.size();
            return s + (s / ADS_AFTER);
        }

        @Override
        public int getItemViewType(int position) {
            if (position >= ADS_AFTER && (position % ADS_AFTER) == 0) {
                return VIEW_TYPE_ADS;
            } else {
                return VIEW_TYPE_MODEL;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            context = parent.getContext();
            if (viewType == VIEW_TYPE_MODEL) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.float_list_item, parent, false);
                return new FloatRecyclerViewAdapter.ViewHolder(view);
            } else if (viewType == VIEW_TYPE_ADS) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_ads_banner, parent, false);
                return new AdsViewHolder(view);
            }
            return null;
        }



        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int row = position;
            if(position >= ADS_AFTER)
                position = position - (position/ADS_AFTER);
            Article item = items.get(position);

            switch (holder.getItemViewType()) {
                case VIEW_TYPE_MODEL:
                    ((FloatRecyclerViewAdapter.ViewHolder) holder).setData(item);
                    break;
                case VIEW_TYPE_ADS:
                    break;
            }
        }


        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            if((holder instanceof FloatRecyclerViewAdapter.ViewHolder) && !((FloatRecyclerViewAdapter.ViewHolder) holder).model.isSeen())
                ((FloatRecyclerViewAdapter.ViewHolder) holder).model.setSeen(context, true);
            super.onViewAttachedToWindow(holder);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView titleView;
            public final ImageView imageView;
            public Article model;
            public ViewHolder(View view) {
                super(view);
                mView = view;
                titleView = view.findViewById(R.id.title);
                imageView = view.findViewById(R.id.logo);

                //LinearLayout cardView = view.findViewById(R.id.card_view);
                //cardView.setCardBackgroundColor(Color.WHITE);
            }


            //Resource resource;
            public void setData(final Article model){
                this.model = model;
                String d = "";
                try {
                    d = DateUtils.getTimeAgo(model.getPublished());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String t = model.getTitle() +" - ";
                d = t + d;
                SpannableString ss1=  new SpannableString(d);
                ss1.setSpan(new RelativeSizeSpan(0.8f), t.length(),d.length(), 0); // set size
                ss1.setSpan(new ForegroundColorSpan(Color.GRAY), t.length(), d.length(), 0);// set color

                titleView.setText(ss1);

                ImageHelper.load(imageView, model.getImg(), 100,100);
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent it = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //context.startActivity(it);
                        if(float_main.isShown())
                            float_main.setVisibility(View.GONE);
                        article = model;
                        showFloatDetails();
                    }
                });
            }


            @Override
            public String toString() {
                return super.toString() + " '" + titleView.getText() + "'";
            }
        }

        private class AdsViewHolder extends RecyclerView.ViewHolder {

            AdsViewHolder(View itemView) {
                super(itemView);
                RelativeLayout adContainer = itemView.findViewById(R.id.adsContainer);
                AdView adView = new AdView(context);
                adView.setAdSize(AdSize.SMART_BANNER);
                adView.setAdUnitId(context.getResources().getString(R.string.admob_banner));
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                adContainer.addView(adView, params);
            }
        }
    }



}
