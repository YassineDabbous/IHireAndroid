package net.ekhdemni.presentation.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.OnMenuItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import net.ekhdemni.R;
import net.ekhdemni.model.ModelType;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.presentation.ui.fragments.UsersFragment;
import net.ekhdemni.presentation.ui.fragments.UsersShowFragment;
import net.ekhdemni.model.models.user.User;
import net.ekhdemni.utils.AlertUtils;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;
import net.ekhdemni.utils.powerMenu.IconPowerMenuItem;
import net.ekhdemni.utils.powerMenu.MenuUtils;
import android.widget.TextView;

public class UsersActivity extends MyActivity {


    Context context;
    UsersShowFragment fu;
    CustomPowerMenu powerMenu;
    public static ImageView right;

    @Override
    public void clean() {
        super.clean();
        fu = null;
        powerMenu = null;
        if(right != null) right.setOnClickListener(null);
        right = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_toolbar);
        context = this;
        setupMenu();
        setupToolbar();
        setFistFragment( new UsersFragment());
    }

    @Override
    public void onItemSelected(User item) {
        fu = UsersShowFragment.newInstance(item.getId());
        setFragment(fu);
    }


    @Override
    public void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Hide the title
        getSupportActionBar().setTitle(null);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(getText(R.string.profiles));
        right = findViewById(R.id.toolbar_right);
        ImageView left = findViewById(R.id.toolbar_left);
        findViewById(R.id.toolbar_right_2).setVisibility(View.INVISIBLE);
        right.setVisibility(View.VISIBLE);
        if(currentFragment instanceof UsersShowFragment) {
            right.setImageResource(R.drawable.ic_dots);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerMenu.showAsDropDown(right);
                }
            });
        }else{
            right.setImageResource(R.drawable.filter);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JobsCreatorActivity.isSearch = true;
                    JobsCreatorActivity.searchFor = 1;
                    startActivity(new Intent(getApplicationContext(), JobsCreatorActivity.class));
                }
            });
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        super.setupToolbar();
    }



    private void setupMenu() {
        OnMenuItemClickListener<IconPowerMenuItem> onIconMenuItemClickListener = new OnMenuItemClickListener<IconPowerMenuItem>() {
            @Override
            public void onItemClick(int position, IconPowerMenuItem item) {
                switch (position) {
                    case 0: currentFragment.getData(); break;
                    case 1: reportAlert(); break;
                }
                //Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                powerMenu.setSelectedPosition(position); // change selected item
                powerMenu.dismiss();
            }
        };
        powerMenu = MenuUtils.builder(context, onIconMenuItemClickListener)
                .addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.refresh), context.getText(R.string.refresh).toString()))
                .addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.flag), context.getText(R.string.report).toString()))
                .build();
    }

    public void reportAlert(){
        AlertUtils.Action action = new AlertUtils.Action() {
            public void doFunction(Object o) {
                reportMe(fu.user.getId()+"", ModelType.USER+"", o.toString());
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
}
