package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Forum;
import android.widget.TextView;
import net.ekhdemni.utils.ImageHelper;

public class CircleVH extends BaseViewHolder<Forum> {
    public final View mView, dot;
    public ImageView thumbnail;
    public TextView name, count;
    public Forum mItem;

    public CircleVH(View view) {
        super(view);
        mView = view;
        thumbnail = view.findViewById(R.id.photo);
        name = view.findViewById(R.id.name);
        count = view.findViewById(R.id.counter);
        dot = view.findViewById(R.id.dot);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_circle;
    }

    @Override
    public void bind(Forum model) {
        name.setText(model.getName());
        if(model.getActive()==1){
            dot.setVisibility(View.VISIBLE);
        }
        /*
        if(model.postsCount>0){
            String countxt = model.postsCount+"";
            if (model.postsCount>=100) countxt = "+99";
            count.setText(countxt);
            count.setVisibility(View.VISIBLE);
        }*/
        ImageHelper.load(thumbnail, model.getImage(), 150,150);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivity.log("clicked", model.getName());
                if (null != listener) {
                    listener.onClick(model);
                }
            }
        });
    }

}