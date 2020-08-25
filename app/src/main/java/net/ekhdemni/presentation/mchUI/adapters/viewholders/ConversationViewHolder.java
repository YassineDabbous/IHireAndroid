package net.ekhdemni.presentation.mchUI.adapters.viewholders;


import android.view.View;

import android.widget.ImageView;

import net.ekhdemni.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Conversation;
import net.ekhdemni.utils.ImageHelper;
import android.widget.TextView;

public class ConversationViewHolder extends BaseViewHolder<Conversation> {
    public final TextView usernameTV,  commentTV, dateTV, like;
    public ImageView user_photo;
    View mView;
    Conversation model;

    public ConversationViewHolder(View view) {
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
    public void bind(Conversation model) {
        this.model = model;
        like.setVisibility(View.GONE);
        commentTV.setText(model.getLastMessage());
        dateTV.setText(model.getTimeAgo());
        usernameTV.setText(model.getUname());
        ImageHelper.load(user_photo,model.getUpicture(), 150,150);
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