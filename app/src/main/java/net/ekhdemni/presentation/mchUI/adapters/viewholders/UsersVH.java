package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import net.ekhdemni.R;
import net.ekhdemni.presentation.ui.activities.ConversationsActivity;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.user.User;
import net.ekhdemni.utils.ImageHelper;
import android.widget.TextView;

public class UsersVH extends BaseViewHolder<User> {
    public final View mView, dot;
    public final TextView title, description;
    public ImageView thumbnail;
    public ImageView overflow;
    User model;

    public UsersVH(View view) {
        super(view);
        mView = view;
        title = view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);
        thumbnail = view.findViewById(R.id.thumbnail);
        overflow = (ImageView) view.findViewById(R.id.overflow);
        dot = view.findViewById(R.id.online);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_service;
    }

    @Override
    public void bind(final User model) {
        description.setVisibility(View.VISIBLE);
        this.model = model;
        if (model.getOnline()) dot.setVisibility(View.VISIBLE);
        title.setText(model.getName());
        description.setText(model.getSpeciality());
        ImageHelper.load(thumbnail, model.getPhoto(), 200, 200);
        overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(overflow);
            }
        });
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(model);
                }
            }
        });
    }




    private void showPopupMenu(View view) {
        // inflate menu
         getLayoutPosition();
         getAdapterPosition();
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(getRealPosition()));
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        int position = 0;
        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action1:
                    Intent i = new Intent(itemView.getContext(), ConversationsActivity.class);
                    i.putExtra("to", model.getId());
                    i.putExtra("newConv", true);
                    itemView.getContext().startActivity(i);
                    return true;
                //case R.id.action2:
                //Toast.makeText(context, "follow", Toast.LENGTH_SHORT).show();
                //return true;
                default:
            }
            return false;
        }
    }
}