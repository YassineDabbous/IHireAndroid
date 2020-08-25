package net.ekhdemni.presentation.ui.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.mchUI.adapters.TabsPagerAdapter;
import net.ekhdemni.model.feeds.db.MyDataBase;
import tn.core.presentation.extensions.CustomViewPager;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.extensions.ParentRequestInterface;
import net.ekhdemni.presentation.ui.fragments.ArticleFragment;
import net.ekhdemni.model.models.Article;

public class ViewerActivity extends MyActivity implements ParentRequestInterface {

    //public static boolean show = true;

    Context context;
    public Article article;
    TabsPagerAdapter mPagerAdapter;
    int current = 0;
    private CustomViewPager mViewPager;
    //public WebView browser;
    Toolbar toolbar;

    @Override
    public void clean() {
        super.clean();
        if(mViewPager != null)
            mViewPager.setAdapter(null);
        mViewPager = null;
        mPagerAdapter = null;
        toolbar = null;
        context = null;
    }

    @Override
    public void init() {
        super.init();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        Article a = (Article) getIntent().getSerializableExtra("ARTICLE");
        if(a!=null){
            article = a;
            MyActivity.log("i get article: "+a.getTitle());
        }
        List<ArticleFragment> fragments = buildFragments();
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(current);//current
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        //initAds();
    }


    /*
    * boolean isUp = false;
    public void slideUp() {
        if(!isUp){
            toolbar.clearAnimation();
            toolbar.animate().translationY(0).setDuration(200);
            isUp = true;
        }
    }
    public void slideBottom() {
        if(isUp){
            toolbar.clearAnimation();
            toolbar.animate().translationY(200).setDuration(0);
            isUp = false;
        }
    }
    * */
    public void setToolbarTitle(int s) {
        toolbar.setTitle(s);
    }


    public void moveNext() {
        //it doesn't matter if you're already in the last item
        int next = mViewPager.getCurrentItem() + 1;
        if(fragments.size()<= next)
            next=0;
        ArticleFragment articleFragment = fragments.get(next);
        articleFragment.scrollMe = true;
        articleFragment.startAutoScroll();
        mViewPager.setCurrentItem(next);
    }

    List<ArticleFragment> fragments;
    private List<ArticleFragment> buildFragments() {
        List<Article> lista = new ArrayList<>();// (ArrayList<Article>) getIntent().getSerializableExtra("articles");
        //List<Article> lista = FeedsFragment.lista;
        fragments  = new ArrayList<>();
        if(article!=null){
            MyDataBase dba = MyDataBase.getInstance(context);
            dba.openToRead();
            List<Article> lista2 = dba.takeNewer("30", article.getDbId());
            lista = dba.take("30", article.getDbId());
            dba.close();
            MyActivity.log("Newest are: "+lista2.size());
            MyActivity.log("Oldest are: "+lista.size());
            /*
    4 3 2 1 ==> need reversing
    6 7 8 9
    */
            Collections.reverse(lista);
            lista.add(article);
            current = lista.size()-1;//lista.indexOf(article);
            lista.addAll(lista2);
            /*for(int i = 0; i<lista2.size(); i++) {
                lista.add(lista2.get(i));
            }*/
            //Toast.makeText(context, "pos "+current, Toast.LENGTH_SHORT).show();
        }
        for(int i = 0; i<lista.size(); i++) {
            //Bundle b = new Bundle();
            //b.putInt("position", i);
            ArticleFragment articleFragment = new ArticleFragment();
            articleFragment.article = lista.get(i);
            fragments.add(articleFragment);
        }
        //current = lista.indexOf(article);
        if (current >= TabsPagerAdapter.ADS_AFTER && TabsPagerAdapter.ADS_AFTER>0 && (current % TabsPagerAdapter.ADS_AFTER) == 0) {
            MyActivity.log("increase TabsPagerAdapter.ADS_AFTER");
            TabsPagerAdapter.ADS_AFTER += 1;
        }
        return fragments;
    }





    @Override
    public void setViewPagerStatus(Boolean b) {
        mViewPager.setPagingEnabled(b);
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.fullscreen:
                //setNormalFullScreen(true);
                return true;

            default: //pass it to fragment menu
                return false;
        }
    }



}