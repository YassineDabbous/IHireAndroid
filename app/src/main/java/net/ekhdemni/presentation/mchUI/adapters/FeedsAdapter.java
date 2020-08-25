package net.ekhdemni.presentation.mchUI.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.daimajia.swipe.SwipeLayout;
import android.widget.ImageView;

import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Article;
import net.ekhdemni.model.models.Resource;

import tn.core.presentation.listeners.OnInteractListener;
import tn.core.util.DateUtils;
import net.ekhdemni.utils.ImageHelper;
import android.widget.TextView;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {

    private final List<Article> mValues;
    private final OnInteractListener<Article> mListener;
    Context context;

    public FeedsAdapter(List<Article> items, OnInteractListener<Article> listener) {
        mValues = items;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_large, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(position < mValues.size()){
            holder.article = mValues.get(position);
            MyActivity.log("|||||| article ID: "+holder.article.getDbId());
            holder.setData();
            holder.article.position = position;
            holder.itemDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener && !holder.waitSipe) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onClick(holder.article);
                    }
                }
            });




            SwipeLayout swipeLayout = holder.swipeLayout;
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    holder.waitSipe = false;
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {
                    holder.waitSipe = true;
                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                    holder.waitSipe = true;
                }

                @Override
                public void onStartClose(SwipeLayout layout) {
                    //holder.waitSipe = false;

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public void removeItem(int position) {
        //mValues.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Article item, int position) {
        mValues.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }


    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        if(!holder.article.isSeen())
            holder.article.setSeen(context, true);
        super.onViewAttachedToWindow(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleView, resourceTitle, dateView;
        public final ImageView imageView, resourceLogo;
        public RelativeLayout viewBackground,viewForeground, itemDetails;
        public Article article;
        public boolean waitSipe = false;
        public SwipeLayout swipeLayout;
        public Button readedBtn, markBtn, saveBtn, reminderBtn;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            swipeLayout= view.findViewById(R.id.swipeLayout);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            itemDetails = view.findViewById(R.id.itemDetails);
            titleView = view.findViewById(R.id.title);
            resourceTitle = view.findViewById(R.id.resourceTitle);
            dateView  = view.findViewById(R.id.date);
            imageView = view.findViewById(R.id.photo);
            resourceLogo = view.findViewById(R.id.resourceLogo);
            readedBtn = view.findViewById(R.id.readedBtn);
            markBtn = view.findViewById(R.id.markBtn);
            saveBtn = view.findViewById(R.id.saveBtn);
            reminderBtn = view.findViewById(R.id.reminderBtn);


            readedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    article.setRead(context, !article.isRead());
                }
            });
            markBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    article.setAsMarked(context, !article.isMarked());
                }
            });
            reminderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("android.intent.action.EDIT");
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("title", article.getTitle());
                    long afterHour = System.currentTimeMillis() + (3600*1000);
                    intent.putExtra("beginTime", afterHour);
                    intent.putExtra("endTime", afterHour+ + (3600*1000));
                    intent.putExtra("allDay", false);
                    intent.putExtra("description", "");
                    context.startActivity(intent);
                }
            });
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, "Soon", Toast.LENGTH_SHORT).show();
                    shareUrl();
                }
            });
        }


        private void shareUrl() {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());
            sharingIntent.putExtra(Intent.EXTRA_TEXT, article.getUrl()+" \n "+ context.getResources().getString(R.string.shared_via)+" "+context.getResources().getString(R.string.app_name)+" https://play.google.com/store/apps/details?id="+context.getPackageName());
            context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_via)));
        }


        Resource resource;
        public void setData(){
            titleView.setText(article.getTitle());
            try {
                dateView.setText(DateUtils.getTimeAgo(article.getPublished()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String imgUrl = article.getImg();

            resource = article.getResource(context);
            if(resource!=null){
                resourceTitle.setText(resource.title);
                if(resource.logo!=null && !resource.logo.isEmpty()){
                        ImageHelper.load(resourceLogo,resource.logo, 60,60);
                        /*if(imgUrl.isEmpty()){
                            if(resource.logo!=null && !resource.logo.isEmpty())
                                imgUrl = resource.logo;
                        }*/
                }
            }
            ImageHelper.load(imageView,imgUrl);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + titleView.getText() + "'";
        }
    }
}
