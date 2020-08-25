package net.ekhdemni.presentation.mchUI.adapters;

import java.util.List;

import net.ekhdemni.presentation.base.BaseAdapter;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.JobVH;
import net.ekhdemni.model.models.Job;

public class JobsAdapter extends BaseAdapter<Job, JobVH> {

    public JobsAdapter(List<Job> itemList, OnClickItemListener<Job> mListener) {
        super(itemList, JobVH.class, mListener);
    }
/*
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MODEL) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
            return new JobVH(view);
        } else if (viewType == VIEW_TYPE_ADS) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ads_banner, parent, false);
            return new AdsViewHolder(view);
        }

        return null;
    }

*/

}
