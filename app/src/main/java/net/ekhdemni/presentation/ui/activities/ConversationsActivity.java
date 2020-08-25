package net.ekhdemni.presentation.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
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
import net.ekhdemni.presentation.ui.fragments.ConversationsFragment;
import net.ekhdemni.presentation.ui.fragments.MessagesFragment;
import net.ekhdemni.model.models.Conversation;
import net.ekhdemni.model.models.Message;
import net.ekhdemni.utils.AlertUtils;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;
import net.ekhdemni.utils.powerMenu.IconPowerMenuItem;
import net.ekhdemni.utils.powerMenu.MenuUtils;
import android.widget.TextView;

public class ConversationsActivity extends MyActivity {
    Context context;
    public boolean newConv = false;
    public String to = "0";
    CustomPowerMenu powerMenu;

    @Override
    public void clean() {
        super.clean();
        powerMenu = null;
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
        setupMenu();
        setupToolbar();
        Intent intent = getIntent();
        newConv = intent.getBooleanExtra("newConv", false);
        to = String.valueOf(intent.getIntExtra("to",0));
        if(newConv && to!=null){
            setFistFragment(MessagesFragment.newInstance(new Conversation(Integer.parseInt(to)), newConv));
        }
        else
            setFistFragment(new ConversationsFragment());
    }

    @Override
    public void onBackPressed() {
        newConv = false;
        super.onBackPressed();
    }



    private void setupMenu() {
        OnMenuItemClickListener<IconPowerMenuItem> onIconMenuItemClickListener = new OnMenuItemClickListener<IconPowerMenuItem>() {
        @Override
        public void onItemClick(int position, IconPowerMenuItem item) {
            switch (position) {
                case 0: currentFragment.getData(); break;
                case 1: blockUser(); break;
                case 2: reportAlert(); break;
            }
            Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
            powerMenu.setSelectedPosition(position); // change selected item
            powerMenu.dismiss();
        }
    };
        powerMenu = MenuUtils.builder(context, onIconMenuItemClickListener)
                .addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.refresh), context.getText(R.string.refresh).toString()))
                .addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.block_helper), context.getText(R.string.block).toString()))
                .addItem(new IconPowerMenuItem(context.getResources().getDrawable(R.drawable.flag), context.getText(R.string.report).toString()))
                .build();
    }

    public ImageView right,right2;
    @Override
    public void setupToolbar() {
        log("setupToolbar");
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(getText(R.string.conversations));
        right = findViewById(R.id.toolbar_right);
        right2 = findViewById(R.id.toolbar_right_2);
        ImageView left = findViewById(R.id.toolbar_left);
        if(currentFragment instanceof MessagesFragment) {
            right.setVisibility(View.VISIBLE);
            log("is instance of MessagesFragment");
            right.setImageResource(R.drawable.ic_dots);
            right2.setImageResource(R.drawable.phone);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (powerMenu!=null) powerMenu.showAsDropDown(right);
                    else init();
                }
            });
            right2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                    {
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+MessagesFragment.phone)));
                    }
                }
            });
        }else{
            right.setVisibility(View.INVISIBLE);
            right2.setVisibility(View.INVISIBLE);
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        super.setupToolbar();
    }


    public void blockUser(){
        Action action = new Action(context) {
            public void doFunction(String s) {
                Log.wtf("ekhdemni.net","get user profile");
                try {
                    JSONObject o = new JSONObject(s);
                    int code = o.optInt("code");
                    if(code==200){
                        onBackPressed();
                        Toast.makeText(context, "blocked", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        action.url = Ekhdemni.relations+"/block/"+MessagesFragment.conversation.getUid();
        action.run();
    }
    public void reportAlert(){
        AlertUtils.Action action = new AlertUtils.Action() {
            public void doFunction(Object o) {
                reportMe(MessagesFragment.conversation.getId()+"", ModelType.CONVERSATION+"", o.toString());
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
