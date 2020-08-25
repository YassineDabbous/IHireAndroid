package net.ekhdemni.presentation.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.onesignal.OneSignal;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import net.ekhdemni.BuildConfig;
import net.ekhdemni.MyApplication;
import net.ekhdemni.R;
import tn.core.domain.Failure;

import net.ekhdemni.model.ModelType;
import net.ekhdemni.model.models.Article;
import net.ekhdemni.model.models.Comment;
import net.ekhdemni.model.models.Conversation;
import net.ekhdemni.model.models.Forum;
import net.ekhdemni.model.models.Idea;
import net.ekhdemni.model.models.Job;
import net.ekhdemni.model.models.Message;
import net.ekhdemni.model.models.Notification;
import net.ekhdemni.model.models.Post;
import net.ekhdemni.model.models.Service;
import net.ekhdemni.model.models.Work;
import net.ekhdemni.domain.usecase.UCAuth;
import tn.core.domain.base.Closure;
import net.ekhdemni.presentation.ui.activities.BrowserActivity;
import net.ekhdemni.presentation.ui.activities.JobDetailsActivity;
import net.ekhdemni.presentation.ui.activities.ViewerActivity;
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager;
import net.ekhdemni.presentation.ui.fragments.IdeaFragment;
import net.ekhdemni.presentation.ui.fragments.TopicFragment;
import net.ekhdemni.presentation.ui.fragments.TopicsFragment;
import net.ekhdemni.presentation.ui.fragments.UsersShowFragment;
import net.ekhdemni.model.models.Broadcast;
import net.ekhdemni.model.models.Relation;
import net.ekhdemni.model.models.user.User;
import net.ekhdemni.presentation.ui.fragments.WorksShowFragment;
import net.ekhdemni.utils.Themes;

import tn.core.presentation.base.BaseActivity;
import tn.core.presentation.base.MyFragment;
import tn.core.util.Const;
import tn.core.util.LocaleManager;
import tn.core.util.PrefManager;
import net.ekhdemni.model.oldNet.Ekhdemni;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends BaseActivity  {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        MyActivity.log("attachBaseContext");
    }

    public static void log(String... logs){
        if(BuildConfig.DEBUG)
            for (String log: logs) {
                Log.wtf(Ekhdemni.TAG, log);
            }
    }
    public static void logHome(String... logs){
        if(BuildConfig.DEBUG)
            for (String log: logs) {
                Log.wtf(Ekhdemni.TAG, log);
            }
    }



    public boolean isInForeground() {
        return isInForegroundMode;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isInForegroundMode = true;
    }

    @Override
    protected void onPause() {
        isInForegroundMode = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        isInForegroundMode = true;
        super.onResume();
        setupToolbar();
        init();
    }

    public void setBackground(){
        ImageView bg = findViewById(R.id.backgroundImage);
        String bgLink = null;
        if (YDUserManager.auth() != null)
            bgLink = YDUserManager.configs().getBackground();
        if (bg!=null && bgLink!=null && !bgLink.isEmpty())
            Picasso.get().load(bgLink).into(bg, new Callback() {
                @Override
                public void onSuccess() {
                    bg.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    bg.setVisibility(View.GONE);
                }
            });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(Themes.Companion.getSavedTheme(this));
        prefManager = new PrefManager(getApplicationContext());
        super.onCreate(savedInstanceState);
    }



    public void setFistFragment(MyFragment fragment){
        currentFragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment, fragment.getClass().getSimpleName()+tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        tag++;
        setupToolbar();
    }
    public void setFragment(MyFragment fragment){;
        currentFragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        String eltag = fragment.getTag();
        if(eltag==null)eltag = fragment.getClass().getSimpleName()+tag;
        else MyActivity.log(fragment.getClass().getSimpleName()+" has a tag ₧ƒ◄↕v╞");
        ft.replace(R.id.content_main, fragment, eltag);
        ft.addToBackStack(fragment.getClass().getSimpleName()+tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        tag++;
        setupToolbar();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
            setupToolbar();
            String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            currentFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if(currentFragment!=null) currentFragment.init();
            else{
                MyActivity.log("trrrrrrrrrryyyyyyyyyyy seeconddd method");
                currentFragment = (MyFragment) getSupportFragmentManager().findFragmentById(R.id.content_main);
                if(currentFragment!=null) currentFragment.init();
                else MyActivity.log("currentFragment is nuuuuuuuuuuuuuuuuuuuuuuuulll");
            }
        }
        else{
            super.onBackPressed();
            finish();
        }
    }

    public void setupToolbar() {
        log("");
    }



    public void onItemSelected(Relation r) {
        setFragment(UsersShowFragment.newInstance(r.getUid()));
    }
    public void onItemSelected(User item) {
        setFragment(UsersShowFragment.newInstance(item.getId()));
    }
    public void onItemSelected(Post item) {
        setFragment(TopicFragment.newInstance(item.getId()));
    }
    public void onItemSelected(Forum item) {
        setFragment(TopicsFragment.newInstance(item));
    }
    public void onItemSelected(Work item) {
        setFragment(WorksShowFragment.newInstance(item.getId()));
    }
    public void onItemSelected(Job item) {
        Intent intent = new Intent(getApplicationContext(), JobDetailsActivity.class);
        intent.putExtra(Const.ID, item.getId());
        startActivity(intent);
    }
    public void onItemSelected(Article item) {
        item.setRead(getApplicationContext(), true);
        //ViewerActivity.article = item;
        Intent i = new Intent(getApplicationContext(), ViewerActivity.class);
        //ArrayList<Article> ls = new ArrayList<>();
        //ls.addAll(feedsFragment.lista);
        //i.putExtra("articles", ls);
        i.putExtra("ARTICLE", item);
        startActivity(i);
    }
    public void onItemSelected(Service item) {
        BrowserActivity.service = item;
        Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
        startActivity(i);
    }


    void goTo(int type, int id){
        MyActivity.log("Handle Notification: go to "+type+" with id "+id);
        switch (type){
            case ModelType.WORK: {
                setFragment(WorksShowFragment.newInstance(id));
            } break;
            case ModelType.POST: {
                setFragment(TopicFragment.newInstance(id));
            } break;
            case ModelType.IDEA: {
                setFragment(IdeaFragment.Companion.newInstance(id));
            } break;
            case ModelType.JOB: {
                Intent intent = new Intent(this, JobDetailsActivity.class);
                intent.putExtra(Const.ID, id);
                startActivity(intent);
            } break;
            default: MyActivity.log("Can't Handle Notification Type :/ "+getClass().getName());
        }
    }
    public void onItemSelected(Notification notification) {
        switch (notification.getItemType()){
            case ModelType.COMMENT: {
                if(notification.getParentType()!=null && notification.getParentType()!=0)
                    goTo(notification.getParentType(), notification.getParentId());
            }
            default: goTo(notification.getItemType(), notification.getItemId());
        }
    }
    public void onItemSelected(Idea item) {
        setFragment(IdeaFragment.Companion.newInstance(item.getId()));
    }
    public void onItemSelected(Conversation item) {}
    public void onItemSelected(Message item) {}
    public void onItemSelected(Comment item) {}





    public static List<Broadcast> broadcasts = new ArrayList<>();
    public void followBroadcasts(){
        Integer id = null;
        if (YDUserManager.auth() != null)
            id = YDUserManager.auth().getId();
        if(id!=null){
            OneSignal.sendTag("user", "user"+id);
            new UCAuth().broadcasts(new Closure<List<Broadcast>>() {
                @Override
                public void onSuccess(List<Broadcast> brs) {
                    if (isInForeground()){
                        if (brs.size()>0){
                            broadcasts.removeAll(broadcasts);
                            broadcasts.addAll(brs);
                        }
                        for (Broadcast b: broadcasts) {
                            MyActivity.log( "Follow Broadcast: "+b.getBroadvalue());
                            OneSignal.sendTag(b.getBroadkey(), b.getBroadvalue());
                        }
                    }
                }

                @Override
                public void onError(Failure failure) {

                }
            });
        }
    }




}