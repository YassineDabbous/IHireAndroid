package net.ekhdemni.presentation.ui.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.ekhdemni.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AdsFragment extends Fragment {

    public AdsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_ads, container, false);

        for(int i=0; i<4;i++){
            AdView adView = new AdView(getContext());
            adView.setAdSize(AdSize.LARGE_BANNER);
            adView.setAdUnitId(getContext().getResources().getString(R.string.admob_banner));
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            view.addView(adView, params);
        }


        return view;
    }



}
