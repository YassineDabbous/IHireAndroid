package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;

import net.ekhdemni.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Forum;
import net.ekhdemni.utils.ImageHelper;
import android.widget.TextView;

public class ForumVH extends BaseViewHolder<Forum> {
    public final View mView;
    public final TextView title,desc, count;
    public ImageView thumbnail;
    public ImageView overflow;

    public ForumVH(View view) {
        super(view);
        mView = view;
        title = view.findViewById(R.id.title);
        count = view.findViewById(R.id.counter);
        desc = view.findViewById(R.id.description);
        thumbnail = view.findViewById(R.id.thumbnail);
        overflow = view.findViewById(R.id.overflow);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_service;
    }


    public void bind(final Forum model) {

        overflow.setVisibility(View.GONE);
        desc.setVisibility(View.GONE);

        if(model.getPostsCount()!=null && model.getPostsCount()>0){
            String countxt = model.getPostsCount()+"";
            if (model.getPostsCount()>=100) countxt = "+99";
            count.setText(countxt);
            count.setVisibility(View.VISIBLE);
        }
        title.setText(model.getName());
        ImageHelper.load(thumbnail,model.getImage(), 100, 100);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(model);
                }
            }
        });
    }
}