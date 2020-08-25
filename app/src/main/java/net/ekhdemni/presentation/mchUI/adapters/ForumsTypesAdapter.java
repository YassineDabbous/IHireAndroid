package net.ekhdemni.presentation.mchUI.adapters;

import java.util.List;

import net.ekhdemni.presentation.base.BaseAdapter;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.ForumTypeVH;
import net.ekhdemni.model.models.Forum;

public class ForumsTypesAdapter extends BaseAdapter<Forum, ForumTypeVH> {

    public ForumsTypesAdapter(List<Forum> items, OnClickItemListener<Forum> listener) {
        super(items, ForumTypeVH.class, listener);
    }



}
