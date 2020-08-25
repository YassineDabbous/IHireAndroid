package tn.core.presentation.base;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import tn.core.model.responses.PagingResponse;
import tn.core.presentation.base.adapters.BaseAdapter;
import tn.core.presentation.listeners.EndlessListener;
import tn.core.presentation.listeners.OnInteractListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by X on 8/13/2018.
 */

public class MyRecyclerFragment<Model, VM> extends MyFragment<VM> {

    public List<Model> lista = new ArrayList<>();

    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public OnInteractListener<Model> listener;
    public EndlessListener endlessListener;
    public int page = 1;

    @Override
    public void clean() {
        super.clean();
        if(recyclerView!=null){
            BaseActivity.log("onPause cleaning recyclerView for "+ recyclerView.getClass().getName());
            recyclerView.clearOnScrollListeners();
            if(adapter!=null)   BaseActivity.log("adapter = null "+ adapter.getClass().getName());
            recyclerView.setAdapter(null);
        }else {
            BaseActivity.log("onPause recyclerView is nulled");
        }
        listener = null;
        adapter = null;
        endlessListener=null;
        recyclerView=null;
    }



    public void onDataReceived(PagingResponse<Model> data) {
        endlessListener.total = data.getTotal();
        page = data.getCurrentPage()+1;
        onDataReceived(data.getData());
    }
    public void onDataReceived(List<Model> data){
        lista.addAll(data);
        BaseActivity.log(" data super count:"+lista.size());
        if(lista.size() == 0) {
            if (empty_view != null) empty_view.setVisibility(View.VISIBLE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
            return;
        }
    }
/*
    @Override
    public void onStart() {
        super.onStart();
        BaseActivity.log("onPause ===============> "+ getClass().getName());
        if(recyclerView!=null && adapter!=null){
            recyclerView.setAdapter(adapter);
            BaseActivity.log("onStart recyclerView.setAdapter(adapter); "+ adapter.getClass().getName());
        }
        else
            BaseActivity.log("onStart recyclerView and adapter are nulled");
    }*/

}
