package net.ekhdemni.presentation.mchUI.adapters;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import tn.core.presentation.listeners.OnInteractListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.github.florent37.tutoshowcase.TutoShowcase;

import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.model.configs.Showcases;
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager;
import net.ekhdemni.model.models.Resource;
import net.ekhdemni.utils.AlertUtils;
import net.ekhdemni.utils.ImageHelper;
import android.widget.TextView;


public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesAdapter.ViewHolder> {

    public static boolean showcased = false;

    private final List<Resource> mValues;
    private final OnInteractListener<Resource> mListener;
    Activity context;

    public ResourcesAdapter(Activity ctx, List<Resource> items, OnInteractListener<Resource> listener) {
        mValues = items;
        mListener = listener;
        context = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_resources_item, parent, false);
        //context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Resource c = mValues.get(position);
        if (c.verified) holder.verified.setVisibility(View.VISIBLE);
        if(c.enabled)
            holder.mView.setAlpha(1f);
        else
            holder.mView.setAlpha(0.5f);
        holder.titleView.setText(c.title);
        holder.descView.setText(context.getText(R.string.contains)+" " + c.getArticlesCount(context)+" "+context.getText(R.string.jobs_offer));
        ImageHelper.load(holder.logo,c.logo,200,200);
        int unreads = c.unreadPosts(context).size();
        if (unreads>0){
            holder.unreadPostsNbr.setText(unreads+"");
        }else{
            holder.unreadText.setVisibility(View.INVISIBLE);
            holder.unreadPostsNbr.setVisibility(View.INVISIBLE);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    if (!holder.waitSipe)
                        mListener.onClick(holder.mItem);
                }
            }
        });
        final Resource r = c;
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertUtils.Action action = new AlertUtils.Action() {
                     public void doFunction(Object o) {
                        r.delete(context);
                    }
                };
                AlertUtils.alert(context, action);
            }
        });
        holder.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r.enableNotification(context);
                if (r.enabledNotification){
                    Toast.makeText(context, context.getString(R.string.enable_notification_resource)+": "+r.title, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, context.getString(R.string.disable_notification_resource)+": "+r.title, Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r.enable(context);
                if (r.enabled){
                    holder.mView.setAlpha(1f);
                    Toast.makeText(context, context.getString(R.string.enable_resource)+": "+r.title, Toast.LENGTH_SHORT).show();
                }else{
                    holder.mView.setAlpha(0.5f);
                    Toast.makeText(context, context.getString(R.string.disable_resource)+": "+r.title, Toast.LENGTH_SHORT).show();
                }
            }
        });


        SwipeLayout swipeLayout =  (SwipeLayout)holder.mView;

//set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        //swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));

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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleView, descView, unreadPostsNbr, unreadText;
        public Resource mItem;
        ImageView verified;
        Button enable,notification,delete;
        public final ImageView logo;
        public boolean waitSipe = false;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            verified= view.findViewById(R.id.verified);
            titleView = view.findViewById(R.id.title);
            descView = view.findViewById(R.id.report);
            unreadPostsNbr = view.findViewById(R.id.unreadPostsNbr);
            unreadText = view.findViewById(R.id.unreadText);
            enable =  view.findViewById(R.id.enable);
            notification =  view.findViewById(R.id.notification);
            delete =  view.findViewById(R.id.delete);
            logo =  view.findViewById(R.id.logo);

            if(!showcased){
                showcased = true;
                if(YDUserManager.showcases().resources == 0){
                    Showcases sc = YDUserManager.showcases();
                    sc.resources = 1;
                    YDUserManager.save(sc);
                    TutoShowcase.from(context)
                            .setContentView(R.layout.showcase_resources)
                            .on(logo)
                            .displaySwipableLeft()
                            .show();
                }
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleView.getText() + "'";
        }

    }







}