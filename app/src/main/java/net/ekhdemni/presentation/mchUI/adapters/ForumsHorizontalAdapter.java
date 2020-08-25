package net.ekhdemni.presentation.mchUI.adapters;

import java.util.List;
import net.ekhdemni.presentation.base.BaseAdapter;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.Forum;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.CircleVH;

public class ForumsHorizontalAdapter extends BaseAdapter<Forum, CircleVH> {
    public ForumsHorizontalAdapter(List<Forum> items, OnClickItemListener<Forum> listener) {
        super(items, CircleVH.class, listener, false);
    }
}
