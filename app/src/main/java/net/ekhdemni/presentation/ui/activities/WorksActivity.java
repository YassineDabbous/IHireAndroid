package net.ekhdemni.presentation.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import net.ekhdemni.presentation.ui.fragments.WorksCreatorFragment;
import net.ekhdemni.presentation.ui.fragments.WorksFragment;
import net.ekhdemni.presentation.ui.fragments.WorksShowFragment;
import net.ekhdemni.model.models.Work;
import net.ekhdemni.utils.AlertUtils;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;
import tn.core.util.Const;
import net.ekhdemni.utils.powerMenu.IconPowerMenuItem;
import net.ekhdemni.utils.powerMenu.MenuUtils;
import android.widget.TextView;

public class WorksActivity extends MyActivity {

    boolean mine = false;
    public Integer me = null;
    Context context;
    public ImageView right;
    public static boolean isChildFragment = false;
    CustomPowerMenu powerMenu;
    CustomPowerMenu.Builder b;

    @Override
    public void clean() {
        super.clean();
        if(right != null)
           right.setOnClickListener(null);
        right = null;
        powerMenu = null;
        b = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_toolbar);
    }

    @Override
    public void init() {
        super.init();
        context = this;
        if (YDUserManager.auth() != null)
            me = YDUserManager.auth().getId();
        setupMenu();
        setupToolbar();
        if (currentFragment==null)
            setFistFragment(WorksFragment.newInstance());
    }

    int selected=0;
    public void onItemSelected(Work item) {
        if(item.getUid().equals(me))
            mine = true;
        else
            mine = false;
        isChildFragment = true;
        selected = Integer.valueOf(item.getId());
        setFragment(WorksShowFragment.newInstance(item.getId()));
        setupMenu();
        //setupToolbar();
    }

    public void setToolbarTitle(int title) {
        toolbarTitle.setText(getText(title));
    }
    TextView toolbarTitle;

    @Override
    public void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Hide the title
        getSupportActionBar().setTitle(null);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getText(R.string.works));
        Intent i = getIntent();
        if(i!=null){
            String title = i.getStringExtra(Const.TITLE);
            if (title!=null) toolbarTitle.setText(title);
        }
        right = findViewById(R.id.toolbar_right);
        findViewById(R.id.toolbar_right_2).setVisibility(View.GONE);
        View left = findViewById(R.id.toolbar_left);
        right.setVisibility(View.VISIBLE);
        if(isChildFragment) {
            right.setImageResource(R.drawable.ic_dots);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerMenu.showAsDropDown(right);
                }
            });
        }else{
            right.setImageResource(R.drawable.plus);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFragment(new WorksCreatorFragment());
                }
            });
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        View left2 = findViewById(R.id.toolbar_left_2);
        left2.setVisibility(View.VISIBLE);
        left2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CategoriesActivity.class);
                i.putExtra("go", 1);
                startActivity(i);
            }
        });
        super.setupToolbar();



        if(YDUserManager.showcases().works == 0){
            Showcases sc = YDUserManager.showcases();
            sc.works = 1;
            YDUserManager.save(sc);
            TutoShowcase.from(this)
                    .setContentView(R.layout.showcase_works)
                    .on(right)
                    .addRoundRect()
                    .show();
        }
    }


    private void setupMenu() {
        OnMenuItemClickListener<IconPowerMenuItem> onIconMenuItemClickListener = new OnMenuItemClickListener<IconPowerMenuItem>() {
            @Override
            public void onItemClick(int position, IconPowerMenuItem item) {
                if(!mine) position++;
                switch (position) {
                    case 0: deleteAlert();   break;
                    case 1: currentFragment.getData(); break;
                    case 2: reportAlert(); break;
                }
                //Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                powerMenu.setSelectedPosition(position); // change selected item
                powerMenu.dismiss();
            }
        };
        b = MenuUtils.builder(context, onIconMenuItemClickListener);
        if(mine) b = b.addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.ic_delete_white_24dp), context.getText(R.string.delete).toString()));
        b =  b.addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.refresh), context.getText(R.string.refresh).toString()))
              .addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.flag), context.getText(R.string.report).toString()));
        powerMenu = b.build();
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
        Log.wtf("ekhdemni.net","delete item "+selected);
        Action action = new Action(context) {
            public void doFunction(String s) throws JSONException {
                JSONObject jsonObject = new JSONObject(s);
                //int code = jsonObject.getInt("code");
                String message = getResponseMessage(jsonObject);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        };
        action.method = action.POST;
        action.url = Ekhdemni.works_delete;
        action.params.put("id", selected);
        action.run();
    }

    public void reportAlert(){
        AlertUtils.Action action = new AlertUtils.Action() {
            public void doFunction(Object o) {
                reportMe(String.valueOf(selected), ModelType.WORK+"", o.toString());
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
