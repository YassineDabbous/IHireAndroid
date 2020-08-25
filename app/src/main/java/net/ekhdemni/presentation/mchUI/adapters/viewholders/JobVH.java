package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.view.View;

import android.widget.ImageView;

import net.ekhdemni.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Job;
import net.ekhdemni.utils.ImageHelper;
import android.widget.TextView;

public class JobVH extends BaseViewHolder<Job> {
    public final View mView;
    public final TextView titleView, descView, usernameView;
    ImageView photo;

    public JobVH(View view) {
        super(view);
        mView = view;
        titleView = view.findViewById(R.id.title);
        descView = view.findViewById(R.id.report);
        usernameView = view.findViewById(R.id.user);
        photo = view.findViewById(R.id.logo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_job;
    }


    @Override
    public void bind(final Job model){
        titleView.setText(model.getTitle());
        usernameView.setText(model.getUname());
        descView.setText(model.getCountry());
        ImageHelper.load(photo,model.getUpicture(), 50, 50);
        mView.setOnClickListener(v -> {
            if (listener!=null) listener.onClick(model);
        });
    }
}
