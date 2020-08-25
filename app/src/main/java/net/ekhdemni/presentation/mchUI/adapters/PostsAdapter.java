package net.ekhdemni.presentation.mchUI.adapters;

import java.util.List;

import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.PostsVH;
import net.ekhdemni.model.models.Post;
import net.ekhdemni.model.oldNet.Ekhdemni;

import tn.core.presentation.listeners.OnInteractListener;

public class PostsAdapter extends BaseAdapter<Post, PostsVH> {
    public static final int ADS_AFTER = Ekhdemni.ADS_AFTER;

    public PostsAdapter(List<Post> itemList, OnInteractListener<Post> mListener) {
        super(itemList, PostsVH.class, mListener);
        this.mListener = mListener;
        this.items = itemList;
    }
}
