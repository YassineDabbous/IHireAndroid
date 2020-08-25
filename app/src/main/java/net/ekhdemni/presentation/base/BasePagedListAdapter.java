package net.ekhdemni.presentation.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ekhdemni.R;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.AdsViewHolder;

import tn.core.presentation.base.adapters.BaseViewHolder;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.oldNet.Ekhdemni;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BasePagedListAdapter<YModel, YViewHolder extends BaseViewHolder<YModel>> extends PagedListAdapter<YModel, RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_MODEL = 1;
    public static final int VIEW_TYPE_ADS = 2;
    public final int ADS_AFTER = Ekhdemni.ADS_AFTER;

    public OnClickItemListener<YModel> mListener;
    public PagedList<YModel> items;


    private Class<YViewHolder> clazzOfT;


    public BasePagedListAdapter(PagedList<YModel> itemList, Class<YViewHolder> clazz, @NonNull DiffUtil.ItemCallback<YModel> diffCallback, OnClickItemListener<YModel> mListener) {
        super(diffCallback);
        this.items = itemList;
        clazzOfT = clazz;
        this.mListener = mListener;
        if (itemList!=null)  submitList(itemList);
    }



    public YViewHolder getNewHolder(View view) throws Exception {
        //return new T(); // won't compile
        //return T.newInstance(); // won't compile
        //return T.class.newInstance(); // won't compile
        Class[] cArg = new Class[1]; //Our constructor has 3 arguments
        cArg[0] = View.class; //First argument is of *object* type Long
        return clazzOfT.
                getDeclaredConstructor(cArg).
                newInstance(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position >= ADS_AFTER)
            position = position - (position/ADS_AFTER);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MODEL:
                YModel item = getItem(position);
                if(item!=null) ((YViewHolder) holder).bind(item, mListener);
                break;
            case VIEW_TYPE_ADS:
                break;
        }
    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MODEL) {
            YViewHolder holder = null;
            try {
                holder = getNewHolder(parent);
                view = LayoutInflater.from(parent.getContext()).inflate(holder.getLayoutId(), parent, false);
                return getNewHolder(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (viewType == VIEW_TYPE_ADS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads_banner, parent, false);
            return new AdsViewHolder(view);
        }

        return null;
    }


    @Override
    public int getItemCount() {
        int s = super.getItemCount();
        int t  = s + (s / ADS_AFTER);
        //if(s>0) t++;
        MyActivity.log("☺☺☺• getItemCount "+t+" but real are "+s);
        return t;
    }



    @Override
    public int getItemViewType(int position) {
        if (position >= super.getItemCount() || position >= ADS_AFTER && (position % ADS_AFTER) == 0) {
            return VIEW_TYPE_ADS;
        } else {
            return VIEW_TYPE_MODEL;
        }
    }
}
