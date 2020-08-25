package net.ekhdemni.presentation.mchUI.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.OnMenuItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import net.ekhdemni.R;
import tn.core.domain.Failure;

import net.ekhdemni.model.ModelType;
import net.ekhdemni.model.models.responses.LikeResponse;
import net.ekhdemni.domain.usecase.UCLike;
import tn.core.domain.base.Closure;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.AdsViewHolder;
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.presentation.ui.fragments.TopicFragment.OnListFragmentInteractionListener;
import net.ekhdemni.model.models.Comment;
import net.ekhdemni.utils.AlertUtils;
import net.ekhdemni.utils.ImageHelper;
import tn.core.util.TextDrawable;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;
import net.ekhdemni.utils.powerMenu.IconPowerMenuItem;
import net.ekhdemni.utils.powerMenu.MenuUtils;
import android.widget.TextView;

public class CommentsAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MODEL = 1;
    private static final int VIEW_TYPE_ADS = 2;
    public static final int ADS_AFTER = Ekhdemni.ADS_AFTER;
    public boolean areSolutions = false;
    public String solutionId = "";

    private Context context;
    private List<Comment> items;
    public OnListFragmentInteractionListener mListener;

    public CommentsAdapter(List<Comment> itemList, OnListFragmentInteractionListener mListener) {
        this.mListener = mListener;
        this.items = itemList;
    }

    @Override
    public int getItemCount() {
        if(ADS_AFTER==0) return items.size();
        int s = items.size();
        return s + (s / ADS_AFTER);
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= ADS_AFTER && (position % ADS_AFTER) == 0) {
            return VIEW_TYPE_ADS;
        } else {
            return VIEW_TYPE_MODEL;
        }
    }

    Integer me;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        if(me==null) me = YDUserManager.auth().getId();
        if (viewType == VIEW_TYPE_MODEL) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comment, parent, false);
            return new ModelViewHolder(view);
        } else if (viewType == VIEW_TYPE_ADS) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ads_banner, parent, false);
            return new AdsViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int row = position;
        if(position >= ADS_AFTER)
            position = position - (position/ADS_AFTER);
        Comment item = items.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MODEL:
                ((ModelViewHolder) holder).bind(item, row);
                break;
            case VIEW_TYPE_ADS:
                break;
        }
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView usernameTV,  commentTV, dateTV, likeTV;
        public ImageView user_photo;
        public ImageView more;
        public Comment model;
        CustomPowerMenu powerMenu;
        CustomPowerMenu.Builder b;

        public ModelViewHolder(View view) {
            super(view);
            mView = view;
            usernameTV = view.findViewById(R.id.tv_username);
            commentTV = view.findViewById(R.id.tv_comment);
            dateTV = view.findViewById(R.id.tv_time);
            likeTV = view.findViewById(R.id.like);
            user_photo =  view.findViewById(R.id.user_photo);
            more =  view.findViewById(R.id.more);
            more.setVisibility(View.VISIBLE);
        }
        private OnMenuItemClickListener<IconPowerMenuItem> onIconMenuItemClickListener = new OnMenuItemClickListener<IconPowerMenuItem>() {
            @Override
            public void onItemClick(int position, IconPowerMenuItem item) {
                if (!areSolutions) position++;
                switch (position) {
                    case 0: reportAlert(); break;
                    case 1: if (areSolutions) setAsSolution();
                            else deleteAlert();
                            break;
                    case 2: deleteAlert();   break;
                }
                powerMenu.setSelectedPosition(position); // change selected item
                powerMenu.dismiss();
            }
        };

        void setMenu(Comment model){
            MyActivity.log("model.uid "+model.getUid() + "&& me is "+me);
            b = MenuUtils.builder(context, onIconMenuItemClickListener)
                    .addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.flag), context.getText(R.string.report).toString()));
            if (areSolutions)  b  = b.addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.thumb_up), context.getText(R.string.solution).toString()));
            if(model.getUid().equals(me))  b  = b.addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.ic_delete_white_24dp), context.getText(R.string.delete).toString()));
            powerMenu = b.build();
        }

        public int position, row;
        public void bind(final Comment model,final int p) {
            setMenu(model);
            if(areSolutions && model.getId().equals(solutionId)){
                mView.findViewById(R.id.solution).setVisibility(View.VISIBLE);
            }
            this.model = model;
            row = p;
            if(p >= ADS_AFTER)
                position = p - (p/ADS_AFTER);
            usernameTV.setText(model.getUname());
            commentTV.setText(model.getComment());
            dateTV.setText(model.getTimeAgo());
            String h = "♥ ";
            String nbr = ""+model.getLikesCount();
            if(model.getLikesCount()==0){
                h="";
                nbr = "";
            }
            if (model.getLiked()){
                likeTV.setText(context.getResources().getString(R.string.liked)+"   "+h+nbr);
                likeTV.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
            else{
                likeTV.setText(context.getResources().getString(R.string.like)+"   "+h+nbr);
                likeTV.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
            if(model.getUpicture().equals("")){
                TextDrawable drawable = TextDrawable.builder().buildRound(model.getUname().charAt(0)+"");
                user_photo.setImageDrawable(drawable);
            }else{
                ImageHelper.load(user_photo,model.getUpicture(), 100,100);
            }
            likeTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like(position, row, model);
                }
            });
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    powerMenu.showAsDropDown(more);
                }
            });
            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    powerMenu.showAsDropDown(mView);
                    return false;
                }
            });
        }

        public void reportAlert(){
            AlertUtils.Action action = new AlertUtils.Action() {
                public void doFunction(Object o) {
                        reportMe(model.getId()+"", ModelType.COMMENT+"", o.toString());
                }
            };
            action.message = context.getText(R.string.report).toString();
            AlertUtils.report(context, action);
        }
        public void reportMe(String id, String type, String text){
            Log.wtf("ekhdemni.net","report item "+id+" type "+type+" because of: "+text);
            Action action = new Action(context) {
                public void doFunction(String s) throws JSONException {
                    JSONObject jsonObject = new JSONObject(s);
                    //int code = jsonObject.getInt("code");
                    String message = getResponseMessage(jsonObject);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            };
            action.method = action.POST;
            action.url = Ekhdemni.reports;
            action.params.put("id", id);
            action.params.put("type", type);
            action.params.put("description",text);
            action.run();
        }



        public void deleteAlert(){
            AlertUtils.Action action = new AlertUtils.Action() {
                public void doFunction(Object o) {
                    deleteMe();
                }
            };
            action.message = context.getText(R.string.delete).toString();
            AlertUtils.alert(context, action);
        }

        public void deleteMe(){
            Log.wtf("ekhdemni.net","delete item "+model.getId());
            Action action = new Action(context) {
                public void doFunction(String s) throws JSONException {
                    JSONObject jsonObject = new JSONObject(s);
                    //int code = jsonObject.getInt("code");
                    String message = getResponseMessage(jsonObject);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            };
            action.method = action.POST;
            action.url = Ekhdemni.comments_delete;
            action.params.put("id", model.getId());
            action.run();
        }
        public void setAsSolution(){
            Log.e("ekhdemni.net","setAsSolution item "+model.getId());
            Action action = new Action(context) {
                public void doFunction(String s) throws JSONException {
                    JSONObject jsonObject = new JSONObject(s);
                    //int code = jsonObject.getInt("code");
                    String message = getResponseMessage(jsonObject);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            };
            action.url = Ekhdemni.comments_solution+"/"+model.getId();
            action.run();
        }



        public void like(final int position,final int row,final Comment model){
            new UCLike().likeComment(model.getId(), new Closure<LikeResponse>() {
                @Override
                public void onSuccess(LikeResponse response) {
                    model.setLiked(response.getLiked());
                    model.setLikesCount(response.getLikesCount());
                    items.set(position, model);
                    notifyItemChanged(row);
                }

                @Override
                public void onError(Failure failure) {

                }
            });
        }


    }

}
