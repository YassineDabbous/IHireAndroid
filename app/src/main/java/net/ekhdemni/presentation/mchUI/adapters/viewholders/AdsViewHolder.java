package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import net.ekhdemni.R;
import tn.core.presentation.base.adapters.BaseViewHolder;

public class AdsViewHolder extends BaseViewHolder<Object> {
    AdView adView;
    RelativeLayout adContainer;
    //View itemView;
    public AdsViewHolder(View itemView) {
        super(itemView);
        adContainer = itemView.findViewById(R.id.adsContainer);
        adView = new AdView(itemView.getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_ads_banner;
    }

    @Override
    public void bind(Object model) {
        adView.setAdSize(AdSize.LARGE_BANNER);
        adView.setAdUnitId(itemView.getContext().getResources().getString(R.string.admob_banner));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        adContainer.addView(adView, params);
    }
}