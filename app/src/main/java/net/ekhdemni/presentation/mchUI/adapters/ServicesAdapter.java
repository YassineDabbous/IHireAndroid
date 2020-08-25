package net.ekhdemni.presentation.mchUI.adapters;

import java.util.List;

import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.ServicesVH;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.Service;

public class ServicesAdapter extends BaseAdapter<Service, ServicesVH> {
    public ServicesAdapter(List<Service> items, OnClickItemListener<Service> listener) {
        super(items, ServicesVH.class, listener, false);
    }


}
