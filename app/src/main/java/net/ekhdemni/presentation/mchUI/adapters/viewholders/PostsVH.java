package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;


import net.ekhdemni.R;
import tn.core.domain.Failure;

import net.ekhdemni.model.ModelType;
import net.ekhdemni.model.models.responses.LikeResponse;
import net.ekhdemni.domain.usecase.UCLike;
import tn.core.domain.base.Closure;
import net.ekhdemni.presentation.ui.activities.CommentsActivity;
import tn.core.presentation.base.adapters.BaseViewHolder;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

import net.ekhdemni.model.models.Post;
import net.ekhdemni.utils.ImageHelper;
import net.ekhdemni.utils.TextUtils;
import android.widget.TextView;

import java.util.List;

public class PostsVH  extends BaseViewHolder<Post> {
    public final View mView, postBody;
    public final TextView uname, timestamp, title, statusMsg , url, likesCount , commentsCount;
    public ImageView profilePic, feedImageView;
    public ImageView like, comment, share;

    public PostsVH(View view) {
        super(view);
        mView = view;
        postBody = mView.findViewById(R.id.postBody);
        uname = (TextView) mView.findViewById(R.id.name);
        title = (TextView) mView.findViewById(R.id.txtStatusTitle);
        timestamp = (TextView) mView.findViewById(R.id.timestamp);
        statusMsg = (TextView) mView.findViewById(R.id.txtStatusMsg);
        url = (TextView) mView.findViewById(R.id.txtUrl);
        profilePic = mView.findViewById(R.id.profilePic);
        feedImageView = mView.findViewById(R.id.feedImage1);
        comment = mView.findViewById(R.id.comment);
        share = mView.findViewById(R.id.share);
        like = mView.findViewById(R.id.like);
        likesCount = (TextView) mView.findViewById(R.id.likesCount);
        commentsCount = (TextView) mView.findViewById(R.id.commentsCount);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_post;
    }


    public void bind(final Post item) {
        if (item.getUname()!=null)
            uname.setText(item.getUname());
        timestamp.setText(item.getTimeAgo());
        //statusMsg.setText(item.description);
        if (item.getDescription().endsWith("...")){
            int start = item.getDescription().lastIndexOf("...");
            StringBuilder builder = new StringBuilder();
            builder.append(item.getDescription().substring(0, start));
            builder.append("<font color='#A2A2A2'>   ..."+mView.getContext().getText(R.string.more).toString().toLowerCase()+"</font>");
            item.setDescription(builder.toString());
        }
        title.setText(item.getTitle());
        TextUtils.htmlToViewNonClickable(statusMsg, item.getDescription());
        ImageHelper.load(profilePic, item.getUpicture(), 150,150);

        if(item.getPhoto() != null && !item.getPhoto().equals("")){
            feedImageView.setVisibility(View.VISIBLE);
            ImageHelper.load(feedImageView,item.getPhoto());
        }

        List<String> urls = TextUtils.extractUrls(item.getDescription());
        if (urls.size()>0) {
            url.setText(Html.fromHtml("<a href=\"" + urls.get(0) + "\">"
                    + urls.get(0) + "</a> "));
            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            url.setVisibility(View.GONE);
        }




        if(item.getCanComment()==0){
            comment.setVisibility(View.GONE);
        }

        if (item.getLikesCount()>0){
            likesCount.setText(String.valueOf(item.getLikesCount()));
        }
        /*if (item.commentsCount>0){
            commentsCount.setText(String.valueOf(item.commentsCount));
        }*/

        if (item.getLiked()!=null)
        if(item.getLiked())
            like.setImageResource(R.drawable.thumb_up);
        else
            like.setImageResource(R.drawable.thumb_up_outline);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener instanceof OnInteractListener)
                    ((OnInteractListener<Post>)listener).onInteract(item, Action.LIKE);
                else
                    like(item);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener instanceof OnInteractListener)
                    ((OnInteractListener<Post>)listener).onInteract(item, Action.COMMENT);
                else {
                    CommentsActivity.title = item.getTitle();
                    CommentsActivity.type = ModelType.POST;
                    CommentsActivity.item_id = item.getId();
                    mView.getContext().startActivity(new Intent(mView.getContext(), CommentsActivity.class));
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener instanceof OnInteractListener)
                    ((OnInteractListener<Post>)listener).onInteract(item, Action.SHARE);
                else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "ekhdemni.net");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, item.getTitle());
                    mView.getContext().startActivity(Intent.createChooser(sharingIntent, mView.getContext().getResources().getString(R.string.share_via)));
                }
            }
        });
        postBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(item);
                }
            }
        });
    }


    public void like(Post post){
        new UCLike().likePost(post.getId(), new Closure<LikeResponse>() {
            @Override
            public void onSuccess(LikeResponse response) {
                post.setLiked(response.getLiked());
                post.setLikesCount(response.getLikesCount());
                bind(post);
            }

            @Override
            public void onError(Failure failure) {

            }
        });
    }

}