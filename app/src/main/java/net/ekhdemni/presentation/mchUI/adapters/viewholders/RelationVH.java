package net.ekhdemni.presentation.mchUI.adapters.viewholders;


import android.view.View;
import android.widget.Button;

import android.widget.ImageView;

import net.ekhdemni.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Relation;

import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;
import net.ekhdemni.utils.ImageHelper;
import android.widget.TextView;

public class RelationVH extends BaseViewHolder<Relation> {
    public final TextView usernameTV;
    public final Button accept, reject;
    public ImageView user_photo;
    View mView, btns;
    Relation model;

    public RelationVH(View view) {
        super(view);
        mView = view;
        usernameTV = view.findViewById(R.id.tv_username);
        btns = view.findViewById(R.id.btns);
        accept = view.findViewById(R.id.accept);
        reject = view.findViewById(R.id.reject);
        user_photo =  view.findViewById(R.id.user_photo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_relation;
    }

    @Override
    public void bind(final Relation model) {
        this.model = model;
        usernameTV.setText(model.getUname());
        ImageHelper.load(user_photo,model.getUpicture(), 100,100);
        if(!model.getRelation().equals(1))
            btns.setVisibility(View.GONE);
        accept.setOnClickListener(view -> ((OnInteractListener<Relation>)listener).onInteract(model, Action.ACCEPT)); //setRelation(2)
        reject.setOnClickListener(view -> ((OnInteractListener<Relation>)listener).onInteract(model, Action.REFUSE)); //setRelation(0));
        mView.setOnClickListener(view -> {
            if (null != listener)
                listener.onClick(model);
        });
    }


}