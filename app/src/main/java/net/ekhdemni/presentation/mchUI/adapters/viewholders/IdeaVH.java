package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.ekhdemni.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

import net.ekhdemni.model.models.Idea;

public class IdeaVH extends BaseViewHolder<Idea> {
    public final View mView;
    public final TextView title;
    public ImageView thumbnail;
    public ImageView like;

    public IdeaVH(View view) {
        super(view);
        mView = view;
        title = view.findViewById(R.id.name);
        thumbnail = view.findViewById(R.id.image);
        like = view.findViewById(R.id.like);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_idea;
    }

    @Override
    public void bind(Idea model) {
        title.setText(model.getName());
        //if (model.getImages()!=null && model.getImages().size()>0) ImageHelper.load(thumbnail,model.getImages().get(0), 200, 200);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(model);
                }
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener instanceof OnInteractListener)
                    ((OnInteractListener<Idea>)listener).onInteract(model, Action.LIKE);
            }
        });
    }


    @Override
    public String toString() {
        return super.toString() + " '" + title.getText() + "'";
    }
}