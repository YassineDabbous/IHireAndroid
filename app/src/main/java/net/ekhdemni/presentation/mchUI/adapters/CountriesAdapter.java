package net.ekhdemni.presentation.mchUI.adapters;

import java.util.List;


import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.CountryVH;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.Country;

/**
 * Created by X on 5/23/2018.
 */

public class CountriesAdapter extends BaseAdapter<Country, CountryVH> {
    public CountriesAdapter(List<Country> itemList, OnClickItemListener<Country> mListener) {
        super(itemList, CountryVH.class, mListener, false);
    }

}