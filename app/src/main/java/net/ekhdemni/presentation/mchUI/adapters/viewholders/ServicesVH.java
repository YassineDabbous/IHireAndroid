package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;

import net.ekhdemni.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Service;
import net.ekhdemni.utils.ImageHelper;
import android.widget.TextView;

public class ServicesVH  extends BaseViewHolder<Service> {
    public final View mView;
    public final TextView title, description;
    public ImageView thumbnail;
    public ImageView overflow;

    public ServicesVH(View view) {
        super(view);
        mView = view;
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        thumbnail = view.findViewById(R.id.thumbnail);
        overflow = (ImageView) view.findViewById(R.id.overflow);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_service;
    }

    @Override
    public void bind(Service model) {
        overflow.setVisibility(View.GONE);
        description.setVisibility(View.GONE);

        title.setText(model.getTitle());
        ImageHelper.load(thumbnail, model.getLogo(), 200, 200);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(model);
                }
            }
        });
    }


    @Override
    public String toString() {
        return super.toString() + " '" + title.getText() + "'";
    }
}