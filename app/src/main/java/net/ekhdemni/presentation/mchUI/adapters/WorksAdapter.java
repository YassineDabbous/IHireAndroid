package net.ekhdemni.presentation.mchUI.adapters;

/**
 * Created by X on 6/4/2018.
 */

import java.util.List;

import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.WorkVH;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.Work;

public class WorksAdapter extends BaseAdapter<Work, WorkVH> {

    public WorksAdapter(List<Work> items, OnClickItemListener<Work> listener) {
        super(items, WorkVH.class, listener);
    }

/*
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.getRecycledViewPool().clear();
    }
    @Override
    public void onViewDetachedFromWindow(@NonNull WorkVH holder) {
        super.onViewDetachedFromWindow(holder);
        holder.elPhoto.invalidate();//destroyDrawingCache();//Fresco.getImagePipeline().evictFromCache(holder.elPhoto);
        //holder.elPhoto.setImageURI(Uri.EMPTY);
    }
*/
}