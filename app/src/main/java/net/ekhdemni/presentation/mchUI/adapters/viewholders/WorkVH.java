package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;


import net.ekhdemni.R;
import tn.core.domain.Failure;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Work;
import net.ekhdemni.model.models.responses.LikeResponse;
import net.ekhdemni.domain.usecase.UCLike;
import tn.core.domain.base.Closure;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

import net.ekhdemni.utils.ImageHelper;

public class WorkVH  extends BaseViewHolder<Work> {

    //public TextView countryName;
    public ImageView elPhoto;
    public ImageView like;
    View view;

    public WorkVH(View itemView) {
        super(itemView);
        view = itemView;
        //countryName = (TextView) itemView.findViewById(R.id.country_name);
        elPhoto = itemView.findViewById(R.id.country_photo);
        like = itemView.findViewById(R.id.like);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_work;
    }

    @Override
    public void bind(Work model) {
        ImageHelper.loadFit(elPhoto, model.getPhoto());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(model);
            }
        });

        if(model.getLiked())
            like.setImageResource(R.drawable.heart);//thumb_up
        else
            like.setImageResource(R.drawable.heart_outline);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener instanceof OnInteractListener)
                    ((OnInteractListener<Work>)listener).onInteract(model, Action.LIKE);
                else like(model);
            }
        });
    }

    public void like(final Work model){
        new UCLike().likeWork(model.getId(), new Closure<LikeResponse>() {
            @Override
            public void onSuccess(LikeResponse response) {
                model.setLiked(response.getLiked());
                model.setLikesCount(response.getLikesCount());
                bind(model);
            }

            @Override
            public void onError(Failure failure) {

            }
        });
    }



}