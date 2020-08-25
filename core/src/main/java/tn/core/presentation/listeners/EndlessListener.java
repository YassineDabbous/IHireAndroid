package tn.core.presentation.listeners;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import tn.core.presentation.base.BaseActivity;

/**
 * Created by X on 10/30/2018.
 */

public class EndlessListener extends RecyclerView.OnScrollListener{
    public boolean isloading;
    public int total;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int adsAfter;
    Action action;
    EndlessDirection direction = EndlessDirection.BOTTOM;

    private EndlessListener(){}
    public EndlessListener(int total, int adsAfter, Action action) {
        this.adsAfter = adsAfter;
        this.total = total;
        this.action = action;
        isloading = false;
    }
    public EndlessListener(int total, int adsAfter, Action action,EndlessDirection direction) {
        this.adsAfter = adsAfter;
        this.total = total;
        this.direction = direction;
        this.action = action;
        isloading = false;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy){
        int ads = 0;
        if(adsAfter!=0){
            ads = total/adsAfter;
        }
        //BaseActivity.log( "Wow scrolled! dx "+dx+", dy: "+dy);
        totalItemCount = recyclerView.getLayoutManager().getItemCount();
        //BaseActivity.log( total + "total>(totalItemCount-ads) "+(totalItemCount-ads)+" => "+(total>(totalItemCount-ads)));
        if(!isloading && total>(totalItemCount-ads)){
            if(dy > 0 && (EndlessDirection.BOTTOM == direction)) //check for scroll down
            {
                //BaseActivity.log( "Wow scrolled BOTTOM ! dx "+dx+", dy: "+dy);
                visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                totalItemCount = recyclerView.getLayoutManager().getItemCount();
                //pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPositions(null);
                    // get maximum element within the list
                    pastVisiblesItems = getLastVisibleItem(lastVisibleItemPositions);
                } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    pastVisiblesItems = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                }

                if ((visibleItemCount + pastVisiblesItems + 2) >= totalItemCount){
                    //BaseActivity.log( "Last Item Wow ! visibleItemCount + pastVisiblesItems + 2 ("+(visibleItemCount + pastVisiblesItems + 4) +") >= totalItemCount ("+ totalItemCount+")");
                    //BaseActivity.log( "Last Item Wow ! total is "+total+", loaded are "+totalItemCount);
                    isloading = true;
                    action.getOnScroll();
                }
            }
            else if(dy < 0 && (EndlessDirection.TOP == direction)) //check for scroll TOP
            {
                //BaseActivity.log( "Wow scrolled TOP ! dx "+dx+", dy: "+dy);
                visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                //pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    int[] firstVisibleItemPositions = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(null);
                    // get maximum element within the list
                    pastVisiblesItems = getFirstVisibleItem(firstVisibleItemPositions);
                } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    pastVisiblesItems = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                }

                //BaseActivity.log( "check if (pastVisiblesItems - 2) <= 0 ("+(pastVisiblesItems - 2) +") <= "+ 0);
                if ((pastVisiblesItems - 2) <= 0){
                    //BaseActivity.log( "Last Item Wow ! total is "+total+", loaded are "+totalItemCount);
                    isloading = true;
                    action.getOnScroll();
                }
            }
        }
    }

    private int getFirstVisibleItem(int[] firstVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < firstVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = firstVisibleItemPositions[i];
            } else if (firstVisibleItemPositions[i] > maxSize) {
                maxSize = firstVisibleItemPositions[i];
            }
        }
        return maxSize;
    }


    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    public static abstract class Action{
        public abstract void getOnScroll();
    }
    public enum EndlessDirection {
        TOP, BOTTOM
    }
}