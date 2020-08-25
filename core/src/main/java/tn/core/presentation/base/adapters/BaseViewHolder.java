package tn.core.presentation.base.adapters;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import tn.core.presentation.listeners.OnClickItemListener;

public abstract class BaseViewHolder<Y> extends RecyclerView.ViewHolder{

    public OnClickItemListener<Y> listener;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void bind(Y model);

    public void bind(Y model, OnClickItemListener<Y> listener){
        this.listener = listener;
        setIsRecyclable(false);
        bind(model);
    }

    public  int getRealPosition(){
        return getAdapterPosition();
    }

}
