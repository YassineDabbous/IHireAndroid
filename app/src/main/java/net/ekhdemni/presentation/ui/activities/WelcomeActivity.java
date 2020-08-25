package net.ekhdemni.presentation.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.Random;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.presentation.mchUI.services.MyService;
import tn.core.util.LocaleManager;
import tn.core.util.PrefManager;
import tn.core.model.net.net.NetworkUtils;

public class WelcomeActivity extends MyActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        String l = LocaleManager.getLanguage(this);
        if (l==null)
            LocaleManager.setNewLocale(this, "ar", false, WelcomeActivity.class);

        AVLoadingIndicatorView loader;
        String[] INDICATORS = new String[]{ "BallClipRotateMultipleIndicator", "BallClipRotatePulseIndicator"  }; //,"BallPulseIndicator", "BallGridPulseIndicator", "BallClipRotateIndicator", "BallClipRotatePulseIndicator", "SquareSpinIndicator", "BallClipRotateMultipleIndicator", "BallRotateIndicator",  "BallZigZagDeflectIndicator", "BallTrianglePathIndicator", "BallScaleIndicator",  "BallScaleMultipleIndicator", "LineScalePulseOutIndicator", "LineScalePulseOutRapidIndicator", "BallSpinFadeLoaderIndicator", "LineSpinFadeLoaderIndicator", "PacmanIndicator", "BallGridBeatIndicator", "com.wang.avi.sample.MyCustomIndicator" //"BallPulseRiseIndicator", "TriangleSkewSpinIndicator",  "SemiCircleSpinIndicator", "CubeTransitionIndicator", "BallZigZagIndicator", "BallPulseSyncIndicator", "BallBeatIndicator",  "BallScaleRippleIndicator", "BallScaleRippleMultipleIndicator", "LineScaleIndicator", "LineScalePartyIndicator",
        loader = (AVLoadingIndicatorView) findViewById(R.id.AVLoadingIndicatorView);
        loader.setIndicator(INDICATORS[new Random().nextInt(INDICATORS.length)]);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);
        AnimationSet animationFade = new AnimationSet(false); //change to false
        animationFade.addAnimation(fadeIn);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setAnimation(animationFade);

        prepare();
    }

    private PrefManager prefManager;
    boolean toastFirstTime = true;
    protected void prepare(){
        if (!NetworkUtils.isOnline(getApplicationContext()) && toastFirstTime){
            if(toastFirstTime){
                Toast.makeText(getApplicationContext(),getResources().getString(net.ekhdemni.R.string.check_network),Toast.LENGTH_LONG).show();
                toastFirstTime = false;
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    prepare();
                }
            }, 4000);
        }
        else{
            startService(new Intent(this, MyService.class));
            prefManager = new PrefManager(this);
            if(!prefManager.isFirstTimeLaunch()){
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        launch();
                    }
                }, 3000);
            }else{
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        launch();
                    }
                }, 2000);
            }
        }
    }
    protected void launch(){
        if (prefManager.isFirstTimeLaunch()) {
            startActivity(new Intent(getBaseContext(), WelcomeViewerActivity.class));
        }else
            startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }

}
