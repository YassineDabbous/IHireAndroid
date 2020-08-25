package net.ekhdemni.presentation.mchUI.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.ui.activities.CategoriesActivity;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.CategoryViewHolder;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Broadcast;
import net.ekhdemni.model.models.Category;
import net.ekhdemni.utils.BroadUtils;

import tn.core.presentation.listeners.OnInteractListener;
import tn.core.util.Completion;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    public enum Type{
        JOBS,
        WORKS
    }
    Type type = Type.JOBS;
    List<Broadcast> broadcasts = new ArrayList<>();
    private final List<Category> mValues;
    public OnInteractListener<Category> mListener;
    public CategoriesActivity.CategoriesListener categoriesListener;
    Context context;

    public void setBroadcasts(List<Broadcast> broadcasts) {
        MyActivity.log("broadcasts => "+broadcasts.size());
        this.broadcasts = broadcasts;
    }

    public CategoryAdapter(List<Category> items, OnInteractListener<Category> listener) {
        mValues = items;
        mListener = listener;
    }
    public CategoryAdapter(List<Category> items, CategoriesActivity.CategoriesListener listener, Type type) {
        this.type = type;
        mValues = items;
        categoriesListener = listener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        context = parent.getContext();
        return new CategoryViewHolder(view);
    }
    public boolean isSubscribed(final String broadcast){
        MyActivity.log("broadcasts are => "+broadcasts);
        for (Broadcast b: broadcasts) {
            MyActivity.log(b.getBroadkey()+" <=> "+broadcast );
            if (b.getBroadkey().equals(broadcast))
                return true;
        }
        return false;
    }

    void subscribe(String broad, boolean subscribed, ImageView follow){
        Completion completion = new Completion() {
            @Override
            public void finish(Object s) {
                if(!subscribed){
                    OneSignal.sendTag(broad,broad);
                    follow.setImageResource(R.drawable.ic_notifications_active_white_24dp);
                    ImageViewCompat.setImageTintList(follow, ColorStateList.valueOf(follow.getContext().getResources().getColor(R.color.colorPrimary)));
                }else {
                    OneSignal.deleteTag(broad);
                    follow.setImageResource(R.drawable.ic_os_notification_fallback_white_24dp);
                    ImageViewCompat.setImageTintList(follow, ColorStateList.valueOf(follow.getContext().getResources().getColor(R.color.grey)));
                }
            }
        };
        BroadUtils.follow(follow.getContext(), !subscribed, broad,broad, completion);
    }


    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Category c = mValues.get(position);
        holder.descView.setVisibility(View.GONE);
        holder.titleView.setText(c.getTitle());
        int count = type == Type.JOBS ? c.getJobsCount() : c.getWorksCount() ;//c.getPostsNumber(context);
        if(count > 0){
            String countxt = count+"";
            if (count>=100) countxt = "+99";
            holder.counter.setText(countxt);
            holder.counter.setVisibility(View.VISIBLE);
        }

        if(isSubscribed("cat"+c.getId())){
            MyActivity.log(" subscribed to cat"+c.getId());
            holder.follow.setImageResource(R.drawable.ic_notifications_active_white_24dp);
            ImageViewCompat.setImageTintList(holder.follow, ColorStateList.valueOf(holder.follow.getContext().getResources().getColor(R.color.colorPrimary)));
        }else
            MyActivity.log("isn'tsubscribed to cat"+c.getId());
        holder.follow.setOnClickListener(view -> {
                subscribe("cat"+c.getId(), isSubscribed("cat"+c.getId()), holder.follow);

        });
        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onClick(holder.mItem);
            }else if (null != categoriesListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                categoriesListener.onClick(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
