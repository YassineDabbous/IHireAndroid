package net.ekhdemni.presentation.mchUI.adapters;

import java.util.List;

import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.ForumVH;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.Forum;

public class ForumsAdapter extends BaseAdapter<Forum, ForumVH> {


    public ForumsAdapter(List<Forum> items, OnClickItemListener<Forum> listener) {
        super(items, ForumVH.class, listener);
        this.mListener = listener;
        this.items = items;
    }


}
