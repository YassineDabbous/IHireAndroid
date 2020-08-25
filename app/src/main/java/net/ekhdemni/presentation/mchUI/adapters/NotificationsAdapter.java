package net.ekhdemni.presentation.mchUI.adapters;


import java.util.List;

import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.NotificationsVH;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.Notification;
/**
 * Created by X on 5/23/2018.
 */

public class NotificationsAdapter extends BaseAdapter<Notification, NotificationsVH> {

    public NotificationsAdapter(List<Notification> itemList, OnClickItemListener<Notification> mListener) {
        super(itemList, NotificationsVH.class, mListener);
        this.mListener = mListener;
        this.items = itemList;
    }

}