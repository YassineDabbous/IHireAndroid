package net.ekhdemni.presentation.mchUI.adapters;


import net.ekhdemni.model.models.Idea;
import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.IdeaVH;

import java.util.List;

import tn.core.presentation.listeners.OnClickItemListener;


public class IdeasAdapter extends BaseAdapter<Idea, IdeaVH> {

    public IdeasAdapter(List<Idea> itemList, OnClickItemListener<Idea> mListener) {
        super(itemList, IdeaVH.class, mListener, false);
    }
}
