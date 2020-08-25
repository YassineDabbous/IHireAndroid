package net.ekhdemni.presentation.ui.fragments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.mchUI.adapters.FeedsAdapter;
import net.ekhdemni.model.feeds.db.MyDataBase;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyRecyclerFragment;
import net.ekhdemni.model.feeds.Parser;

import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.EndlessListener;
import tn.core.presentation.listeners.OnInteractListener;

import net.ekhdemni.model.models.Article;
import net.ekhdemni.model.models.Category;
import net.ekhdemni.model.models.Resource;
import net.ekhdemni.presentation.mchUI.services.MyService;
import net.ekhdemni.utils.RecyclerItemTouchHelper;

import android.widget.TextView;

import org.jetbrains.annotations.NotNull;


public class FeedsFragment extends MyRecyclerFragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //public ShimmerFrameLayout mShimmerViewContainer;
    FeedsAdapter adapter;
    View headerCardView;
    TextView headerTitle, allPosts, unseenPosts, unreadPosts, markedPosts;
    public List<Article> lista = new ArrayList<Article>();
    public boolean showBookmarks = false;
    public boolean showUnread = false;
    public boolean my_activities = false;
    public Category category = null;
    public Resource resource = null;
    boolean expanded = true;
    int expand =  0;
    int unseen = 0;
    int unread = 0;
    int marked = 0;
    boolean delete = false;
    String msg = "";
    long after = 0;
    public FeedsFragment() {}

    public static FeedsFragment newInstance(Filter type, Object model) {

        Bundle args = new Bundle();

        FeedsFragment fragment = new FeedsFragment();
        fragment.filter(type, model);
        fragment.setArguments(args);
        return fragment;
    }
    public enum Filter implements Serializable {
        all, bookmarks, historic, unread, resource, category;
    }

    public void filter(Filter type, Object model){
        showBookmarks = (type==Filter.bookmarks) ? true : false;
        my_activities = (type==Filter.historic) ? true : false;
        showUnread = (type==Filter.unread) ? true : false;
        resource = (type==Filter.resource) ? (Resource) model : null;
        category = (type==Filter.category) ? (Category) model : null;
        lista = new ArrayList<>();
    }

    @Override
    public void clean() {
        super.clean();
        if(headerCardView!=null) headerCardView.setOnClickListener(null);
        listener = null;
        adapter = null;
        headerCardView = null;
        headerTitle = allPosts = unseenPosts = unreadPosts = markedPosts = null;
    }

    @Override
    public void init(){
        final View view = getView();

        headerCardView = view.findViewById(R.id.headerCardView);
        //View tgl = view.findViewById(R.id.toggle);
        //expand = headerCardView.getHeight();
        if (expand<=0) expand = 600;
        headerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expanded)
                    view.findViewById(R.id.headerDetails).setVisibility(View.GONE);
                    //collapse(headerCardView, 800, 170);
                else
                    view.findViewById(R.id.headerDetails).setVisibility(View.VISIBLE);
                    //expand(headerCardView, 800, expand);
                expanded = !expanded;
            }
        });
        headerTitle = view.findViewById(R.id.headerTitle);
        allPosts = view.findViewById(R.id.allPosts);
        unseenPosts = view.findViewById(R.id.unseenPosts);
        unreadPosts = view.findViewById(R.id.unreadPosts);
        markedPosts = view.findViewById(R.id.markedPosts);

        //mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        view.setBackgroundColor(Color.WHITE);
        //mShimmerViewContainer.startShimmerAnimation();




        //new ItemTouchHelper().SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        recyclerView= (RecyclerView) view.findViewById(R.id.itemsRecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());


        lista = new ArrayList<>();
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new FeedsAdapter(lista, new OnInteractListener<Article>() {
            @Override
            public void onInteract(Article item, @NotNull Action action) {
                onClick(item);
            }

            @Override
            public void onClick(Article item) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }
        });
        recyclerView.setAdapter(adapter);
        endlessListener = new EndlessListener(0, 0, new EndlessListener.Action() {
            @Override
            public void getOnScroll() {
                getData();
            }
        });
        recyclerView.addOnScrollListener(endlessListener);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ((MainActivity) getActivity()).toggleSwipe(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0); // 0 is for first item position
            }
        });
        overviewOneTime = true;
        if(lista.size()!=0){
            MyActivity.isUpdating = false;
        }else{
            //after = 0;
            getData();
        }
/*
        ViewTreeObserver.OnScrollChangedListener listener;
        listener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = recyclerView.getScrollY();

                if (scrollY < 50) { //
                    ((MainActivity) getActivity()).toggleSwipe(true);
                } else {
                    ((MainActivity) getActivity()).toggleSwipe(false);
                }
            }
        };
        recyclerView.getViewTreeObserver().addOnScrollChangedListener(listener);

* */



/*recyclerView.setOnTouchListener(new View.OnTouchListener() {
            float height;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                float height = event.getY();
                if(action == MotionEvent.ACTION_DOWN){
                    this.height = height;
                }else if(action == MotionEvent.ACTION_UP){
                    if(this.height < height){
                        Log.v("scrollView", "Scrolled up");
                        ((MainActivity) getActivity()).slideUp();
                    }else if(this.height > height){
                        Log.v("scrollView", "Scrolled down");
                        ((MainActivity) getActivity()).slideBottom();


                    }
                }
                return false;
            }
        });*/





        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    void waitForUpdate(){
        MyActivity.log("waitForUpdate "+MyActivity.isUpdating);
        ((MainActivity) getActivity()).showSwipe();
        if(MyActivity.isUpdating){
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    waitForUpdate();
                }
            }, 1000);
        }else{
            ((MainActivity) getActivity()).hideSwipe();
//            ((MainActivity) getActivity()).navigation.setSelectedItemId(R.id.news);
            getData();

        }
    }



    @Override
    public void getData(){
        List<Article> l = new ArrayList<>();
        if(MyActivity.isUpdating){
            waitForUpdate();
            //return;
        }
        MyDataBase dba = MyDataBase.getInstance(getActivity());
        dba.openToRead();
        if(resource!=null){
            msg = resource.title;
            l = dba.getResourcePosts(resource.dbId, after);
        }
        else if(showBookmarks){
            msg = getActivity().getString(R.string.bookmarks);
            l = dba.getMarkedPosts(after);
        }
        else if(showUnread){
            msg = getActivity().getString(R.string.not_readed);
            l = dba.getUnreadPosts(after);
        }
        else if(my_activities){
            msg = getActivity().getString(R.string.my_activities);
            l = dba.getLastReadedPosts(after);
        }
        else if(category != null){
            msg = category.getTitle();
            l = dba.getPostsByCategory(category.getTitle(), (int) after);
        }
        else{
            msg = getActivity().getString(R.string.all);
            l = dba.take("10",after);
            /*if(overviewOneTime){
                updateOverview(dba.take("100",0));
                overviewOneTime=false;
            }*/

        }
        dba.close();
        lista.addAll(l);
        updateOverview(lista);
        /*if(overviewOneTime){
            updateOverview(l);
            overviewOneTime=false;
        }*/
        int s = lista.size();
        endlessListener.isloading = false;
        if(l.size()<10){
            Log.w("data", "no more data in db");
            endlessListener.total = s;
        }else {
            endlessListener.total = s+20;
        };
        int last = lista.size()-1;
        MyActivity.log( "last one "+last);
        if(last >= 0)
            after = lista.get(last).getDbId(); //Long.parseLong(lista.get(lista.size()-1).getPublished());
        else after = 0;
        MyActivity.log( "Next time will get posts after "+after);
        adapter.notifyDataSetChanged();
        MyActivity.isUpdating = false;
        if(pd!=null) pd.dismiss();
        setEmptyView(lista.size() == 0);
    }


    void setEmptyView(boolean isEmpty){
        if(isInForegroundMode && getView()!=null){
            getView().setBackgroundColor(Color.TRANSPARENT);
            if (isEmpty){
                final Button refreshBtn = getView().findViewById(R.id.refreshBtn);
                refreshBtn.setEnabled(true);
                refreshBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refreshBtn.setEnabled(false);
                        refresh(new ArrayList<Article>());
                    }
                });
                getView().findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
            }else{
                getView().findViewById(R.id.emptyView).setVisibility(View.GONE);
            }
        }
    }


    boolean overviewOneTime = false;


    public void updateOverview(List<Article> listAll){
        for (int j = 0; j < listAll.size(); j++) {
            Article item = listAll.get(j);
            if(!item.isSeen()) unseen++;
            if(!item.isRead()) unread++;
            if(!item.isMarked()) marked++;
        }
        headerTitle.setText(msg);
        allPosts.setText(getActivity().getString(R.string.sum)+" "+(listAll.size()>= 100 ? "+99" : listAll.size()));
        unseenPosts.setText(getActivity().getString(R.string.new_opportunity)+" "+(unseen>= 100 ? "+99" : unseen));
        unreadPosts.setText(getActivity().getString(R.string.not_readed)+" "+(unread>= 100 ? "+99" : unread));
        markedPosts.setText(getActivity().getString(R.string.bookmarks)+" "+(marked>= 100 ? "+99" : marked));
        //reset
        unseen = 0;
        unread = 0;
        marked = 0;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_feeds, container, false);
    }



    public void refresh(List<Article> list) {
        MyActivity.isUpdating = false;
        if(list.size()>0){
            lista = list;
            adapter.notifyDataSetChanged();
            return;
        }
        else if(resource!=null){
            MyActivity.log( "resource: "+resource.url);
            Parser parser = new Parser(getContext());
            parser.execute(resource);
            parser.onFinish(new Parser.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(ArrayList<Article> list) {
                    MyDataBase dba = MyDataBase.getInstance(getContext());
                    dba.openToWrite();
                    List<Article> dbArticles;
                    List<String> cats;
                    for (Article a : list) {
                        dbArticles = dba.getPosts(a.getTitle());
                        if (dbArticles == null || dbArticles.size()==0) {
                            a.setDbId(dba.insertPost(a));
                            cats = a.getCategories();
                            for (String cn : cats) {
                                dba.insertCategory(cn);
                                dba.addPostForCategory(cn, a.getDbId());
                            }
                        }
                    }
                    dba.close();
                    MyActivity.isUpdating = false;
                    getData();
                }
                @Override
                public void onError() {
                    MyActivity.isUpdating = false;

                }
            });
        }
        else if(showBookmarks || showUnread || my_activities || my_activities || category != null){
        }else{
            MyService.force = true;
            MyActivity.isUpdating = true;
            getActivity().startService(new Intent(getActivity(), MyService.class));
            waitForUpdate();
        }
        //after = 0;
    }





    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof FeedsAdapter.ViewHolder) {
            final Article deletedItem = lista.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.RIGHT) {
                    // get the removed item name to display it in snack bar
                    String name = lista.get(viewHolder.getAdapterPosition()).getTitle();
                    // backup of removed item for undo purpose

                    // remove the item from recycler view
                    delete= true;
                    deleteAfter(lista.get(viewHolder.getAdapterPosition()));
                    adapter.removeItem(viewHolder.getAdapterPosition());


                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar
                            .make(getView(), name + " إزالة من القائمة", Snackbar.LENGTH_LONG);
                    snackbar.setAction("تراجع", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // undo is selected, restore the deleted item
                            delete = false;
                            adapter.restoreItem(deletedItem, deletedIndex);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
            }
        }
    }


    void deleteAfter(final Article article){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(delete){
                    MyDataBase dba = MyDataBase.getInstance(getContext());
                    dba.openToWrite();
                    dba.deleteArticle(article.getDbId());
                    dba.close();
                    delete = false;
                }
            }
        }, 1000);
    }













    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }




    public static void expand(final View v, int duration, int targetHeight) {

        int prevHeight  = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void collapse(final View v, int duration, int targetHeight) {
        int prevHeight  = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }



}
