package net.ekhdemni.presentation.base;

import net.ekhdemni.model.oldNet.Ekhdemni;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.AdsViewHolder;
import tn.core.presentation.base.adapters.BaseViewHolder;
import tn.core.presentation.listeners.OnClickItemListener;

import java.util.List;

public class BaseAdapter<YModel, YViewHolder extends BaseViewHolder<YModel>> extends tn.core.presentation.base.adapters.BaseAdapter<YModel,YViewHolder,AdsViewHolder> {
    public BaseAdapter(List<YModel> itemList, Class<YViewHolder> clazz) {
        super(itemList, clazz, AdsViewHolder.class, null, Ekhdemni.ADS_AFTER);
    }

    public BaseAdapter(List<YModel> itemList, Class<YViewHolder> clazz, OnClickItemListener<YModel> mListener) {
        super(itemList, clazz, AdsViewHolder.class, mListener, Ekhdemni.ADS_AFTER);
    }

    public BaseAdapter(List<YModel> itemList, Class<YViewHolder> clazz, OnClickItemListener<YModel> mListener, boolean ads) {
        super(itemList, clazz, AdsViewHolder.class, mListener, ads ? Ekhdemni.ADS_AFTER : 0);
    }
}
