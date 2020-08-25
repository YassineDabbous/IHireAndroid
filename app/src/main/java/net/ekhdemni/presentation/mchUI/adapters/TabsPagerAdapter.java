package net.ekhdemni.presentation.mchUI.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import net.ekhdemni.presentation.ui.fragments.AdsFragment;
import net.ekhdemni.presentation.ui.fragments.ArticleFragment;
import net.ekhdemni.model.oldNet.Ekhdemni;


/**
 * Created by X on 1/21/2018.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public static int ADS_AFTER = Ekhdemni.ADS_AFTER;
    //public static int pos = 0;

    private List<ArticleFragment> myFragments;

    public TabsPagerAdapter(FragmentManager fragmentManager, List<ArticleFragment> myFrags) {
        super(fragmentManager);
        myFragments = myFrags;
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= ADS_AFTER && (position % ADS_AFTER) == 0) {
            return new AdsFragment();
        } else {
            return myFragments.get(position);
        }
    }




    @Override
    public int getCount() {
        return myFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //setPos(position);
        if (position >= ADS_AFTER && (position % ADS_AFTER) == 0) {
            return myFragments.get(position).article.getTitle();
        }else{
            return  "ADS $$";
        }
    }


    public void add(ArticleFragment articleFragment) {
        myFragments.add(articleFragment);
    }

}
