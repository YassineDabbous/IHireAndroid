package net.ekhdemni.presentation.mchUI.adapters;


import java.util.List;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.VHApps;
import net.ekhdemni.presentation.base.BaseAdapter;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.App;


public class AppsAdapter extends BaseAdapter<App, VHApps> {

    public AppsAdapter(List<App> itemList, OnClickItemListener<App> mListener) {
        super(itemList, VHApps.class, mListener, false);
    }
}
