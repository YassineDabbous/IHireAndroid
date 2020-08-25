package net.ekhdemni.presentation.ui.activities;


import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.florent37.tutoshowcase.TutoShowcase;
import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.OnMenuItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import net.ekhdemni.R;
import net.ekhdemni.model.ModelType;
import net.ekhdemni.model.configs.Showcases;
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.presentation.ui.fragments.ForumsFragment;
import net.ekhdemni.presentation.ui.fragments.ForumsTypesFragment;
import net.ekhdemni.presentation.ui.fragments.TopicFragment;
import net.ekhdemni.presentation.ui.fragments.TopicsFragment;
import net.ekhdemni.model.models.Comment;
import net.ekhdemni.model.models.Forum;
import net.ekhdemni.utils.AlertUtils;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;

import net.ekhdemni.utils.powerMenu.IconPowerMenuItem;
import net.ekhdemni.utils.powerMenu.MenuUtils;
import android.widget.TextView;

public class ForumsActivity extends MyActivity implements
        TopicFragment.OnListFragmentInteractionListener {

    //public static String ForumId = "";
    public boolean showPosts = false;
    public Forum forum;
    public ImageView right;
    CustomPowerMenu powerMenu;

    @Override
    public void clean() {
        super.clean();
        //powerMenu = null;
        //if(right != null) right.setOnClickListener(null);
        //right = null;
    }

    @Override
    public void init() {
        super.init();
        setupMenu();
        setupToolbar();
    }

    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        showPosts = getIntent().getBooleanExtra("showPosts", showPosts);

        setContentView(R.layout.activity_with_toolbar);
        if(showPosts){
            showPosts = false;
            setFistFragment(TopicsFragment.newInstance(null));
        }else
            setFistFragment(new ForumsTypesFragment());
    }

    @Override
    public void onItemSelected(Forum item) {
        super.onItemSelected(item);
        forum = item;
    }


    @Override
    public void onListFragmentInteraction(Comment item) {

    }


    public void onForumItemSelected(Forum item) {
        //super.onItemSelected(item);
        ForumsFragment f = new ForumsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("forumType", item.getId()+"");
        f.setArguments(bundle);
        setFragment(f);
    }

    @Override
    public void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Hide the title
        getSupportActionBar().setTitle(null);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(getText(R.string.coffee));
        right = findViewById(R.id.toolbar_right);
        ImageView left = findViewById(R.id.toolbar_left);
        findViewById(R.id.toolbar_right_2).setVisibility(View.INVISIBLE);
        right.setVisibility(View.VISIBLE);
        if(currentFragment instanceof TopicFragment) {
            right.setImageResource(R.drawable.ic_dots);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerMenu.showAsDropDown(right);
                }
            });
        }else{
            right.setVisibility(View.INVISIBLE);
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        super.setupToolbar();


        if(YDUserManager.showcases().coffee == 0){
            Showcases sc = YDUserManager.showcases();
            sc.coffee = 1;
            YDUserManager.save(sc);
            TutoShowcase.from(this)
                    .setContentView(R.layout.showcase_topics)
                    .on(title)
                    .addRoundRect()
                    .show();
        }
    }

    Integer me;
    public void setupMenu() {
        OnMenuItemClickListener<IconPowerMenuItem> onIconMenuItemClickListener = new OnMenuItemClickListener<IconPowerMenuItem>() {
            @Override
            public void onItemClick(int position, IconPowerMenuItem item) {
                switch (position) {
                    case 0: currentFragment.getData(); break;
                    case 1: reportAlert(); break;
                    case 2: if(currentFragment instanceof TopicFragment ) ((TopicFragment) currentFragment).deleteAlert(); break;
                }
                //Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                powerMenu.setSelectedPosition(position); // change selected item
                powerMenu.dismiss();
            }
        };
        CustomPowerMenu.Builder bldr = MenuUtils.builder(context, onIconMenuItemClickListener)
                .addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.refresh), context.getText(R.string.refresh).toString()))
                .addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.flag), context.getText(R.string.report).toString()));

        if(me==null) me = YDUserManager.auth().getId();
        if(currentFragment instanceof TopicFragment
                &&((TopicFragment) currentFragment).post.getUid().equals(me) ){

            MyActivity.log("model.uid "+((TopicFragment) currentFragment).post.getUid() + "&& me is "+me);
            bldr.addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.ic_delete_white_24dp), context.getText(R.string.delete).toString()));

        }
        powerMenu = bldr.build();
    }

    public void reportAlert(){
        AlertUtils.Action action = new AlertUtils.Action() {
            public void doFunction(Object o) {
                if(currentFragment instanceof TopicFragment) reportMe(((TopicFragment) currentFragment).post.getId()+"", ModelType.POST+"", o.toString());
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
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.reports;
        action.params.put("id", id);
        action.params.put("type", type);
        action.params.put("description",text);
        action.run();
    }
}
