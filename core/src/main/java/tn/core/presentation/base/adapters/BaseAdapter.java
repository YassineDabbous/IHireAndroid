package tn.core.presentation.base.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tn.core.presentation.base.BaseActivity;
import tn.core.presentation.listeners.OnClickItemListener;

public class BaseAdapter<YModel, YViewHolder extends BaseViewHolder<YModel>, YViewHolder2 extends BaseViewHolder> extends RecyclerView.Adapter {
    public static final int VIEW_TYPE_MODEL = 1;
    public static final int VIEW_TYPE_ADS = 2;
    public static boolean ADS_ENABLED = true;
    public int ADS_AFTER = 7;


    public OnClickItemListener<YModel> mListener;
    public List<YModel> items;

    public BaseAdapter(List<YModel> itemList, Class<YViewHolder> clazz, Class<YViewHolder2> clazz2) {
        this.items = itemList;
        clazzOfT = clazz;
        clazzOfT2 = clazz2;
    }
    public BaseAdapter(List<YModel> itemList, Class<YViewHolder> clazz, Class<YViewHolder2> clazz2, OnClickItemListener<YModel> mListener) {
        this.items = itemList;
        clazzOfT = clazz;
        clazzOfT2 = clazz2;
        this.mListener = mListener;
    }

    public BaseAdapter(List<YModel> itemList, Class<YViewHolder> clazz, Class<YViewHolder2> clazz2, OnClickItemListener<YModel> mListener, int adsAfter) {
        this.items = itemList;
        clazzOfT = clazz;
        clazzOfT2 = clazz2;
        this.mListener = mListener;
        ADS_AFTER = adsAfter>0 ? adsAfter : 999999;
    }

    private Class<YViewHolder> clazzOfT;
    private Class<YViewHolder2> clazzOfT2;
    public YViewHolder getNewHolder(View view) throws Exception {
        //return new T(); // won't compile
        //return T.newInstance(); // won't compile
        //return T.class.newInstance(); // won't compile
        Class[] cArg = new Class[1]; //Our constructor has 1 arguments
        cArg[0] = View.class; //First argument is of *object* type View
        return clazzOfT.
                getDeclaredConstructor(cArg).
                newInstance(view); // constructor must be PUBLIC or it will return NUlL !!!!!!!!!!!!!!!!!!!
    }
    public YViewHolder2 getNewHolder2(View view) throws Exception {
        //return new T(); // won't compile
        //return T.newInstance(); // won't compile
        //return T.class.newInstance(); // won't compile
        Class[] cArg = new Class[1]; //Our constructor has 1 arguments
        cArg[0] = View.class; //First argument is of *object* type View
        return clazzOfT2.
                getDeclaredConstructor(cArg).
                newInstance(view); // constructor must be PUBLIC or it will return NUlL !!!!!!!!!!!!!!!!!!!
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (ADS_ENABLED)
            if(position >= ADS_AFTER)
                position = position - (position/ADS_AFTER);
        YModel item = items.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MODEL:
                ((YViewHolder) holder).bind(item, mListener);
                break;
            case VIEW_TYPE_ADS:
                ((YViewHolder2) holder).bind(null);
                break;
        }
    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        BaseViewHolder holder = null;
        if (viewType == VIEW_TYPE_MODEL) {
            try {
                holder = getNewHolder(parent);
                view = LayoutInflater.from(parent.getContext()).inflate(holder.getLayoutId(), parent, false);
                return getNewHolder(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (viewType == VIEW_TYPE_ADS) {
            try {
                holder = getNewHolder2(parent);
                view = LayoutInflater.from(parent.getContext()).inflate(holder.getLayoutId(), parent, false);
                return getNewHolder2(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    @Override
    public int getItemCount() {
        if(!ADS_ENABLED || ADS_AFTER==0)
            return items.size();
        int s = items.size();
        return s + (s / ADS_AFTER);
    }

    public void refresh(YModel yModel){
        refresh(-1, yModel);
    }
    public void refresh(int i, YModel yModel){
        if (items.indexOf(yModel)>=0) items.set(items.indexOf(yModel), yModel);
        if (i==-1) i = items.indexOf(yModel);
        BaseActivity.log("initial index, "+i);

        /*
        if (i==-1){
            for (int k = 0; k <items.size(); k++) {
                if (items.get(k).getId().equals(yModel.getId())){
                    MyActivity.log("Item found!");
                    i = k;
                    break;
                }
            }
        }*/
        if (ADS_ENABLED && i>ADS_AFTER)
                 i = i + (i / ADS_AFTER);
        BaseActivity.log("indeeeeeeeex after 2 years, "+i);
        if (i<getItemCount())
            notifyDataSetChanged();
            //notifyItemChanged(i, yModel);
        else
            BaseActivity.log("indeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeex, "+i+"but size is "+getItemCount());
        /*for (YModel y: items) {
            if (y.equals(yModel)){
                MyActivity.log("==> NotifyItemChanged("+items.indexOf(yModel)+", "+yModel+")");
                notifyItemChanged(items.indexOf(yModel), yModel);
            }
        }*/
    }


    @Override
    public int getItemViewType(int position) {
        if (ADS_ENABLED && position >= ADS_AFTER && (position % ADS_AFTER) == 0) {
            return VIEW_TYPE_ADS;
        } else {
            return VIEW_TYPE_MODEL;
        }
    }
}
