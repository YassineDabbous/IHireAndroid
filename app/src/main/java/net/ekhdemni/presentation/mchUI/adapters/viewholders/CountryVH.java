package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.view.View;

import android.widget.ImageView;

import net.ekhdemni.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Country;
import android.widget.TextView;

public class CountryVH extends BaseViewHolder<Country> {
    public final TextView usernameTV,  commentTV, dateTV, like;
    public ImageView user_photo;
    View mView;


    public CountryVH(View view) {
        super(view);
        mView = view;
        usernameTV = view.findViewById(R.id.tv_username);
        commentTV = view.findViewById(R.id.tv_comment);
        dateTV = view.findViewById(R.id.tv_time);
        user_photo =  view.findViewById(R.id.user_photo);
        like = view.findViewById(R.id.like);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_comment;
    }

    @Override
    public void bind(final Country item) {
        like.setVisibility(View.GONE);
        commentTV.setVisibility(View.GONE);
        dateTV.setVisibility(View.GONE);
        user_photo.setVisibility(View.GONE);

        usernameTV.setText(item.getName());
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(item);
                }
            }
        });
    }
}