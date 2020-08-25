package tn.core.presentation.base;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import tn.core.model.responses.PagingResponse;
import tn.core.presentation.base.adapters.BaseAdapter;
import tn.core.presentation.listeners.EndlessListener;
import tn.core.presentation.listeners.OnInteractListener;


/**
 * Created by X on 8/13/2018.
 */

public class BaseRecyclerFragment<Model, VM> extends BaseFragment<VM> {

    public List<Model> lista = new ArrayList<>();

    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public OnInteractListener<Model> listener;
    public EndlessListener endlessListener;
    public int page = 1;

    @Override
    public void swipeRefresh() {
        //super.swipeRefresh(); don't call super bcz it will call getData twice
        page = 1;
        lista.removeAll(lista);
        getData();
    }

    public void onDataReceived(PagingResponse<Model> data) {
        if (endlessListener!=null) endlessListener.total = data.getTotal();
        page = data.getCurrentPage()+1;
        onDataReceived(data.getData());
    }
    public void onDataReceived(List<Model> data){
        lista.addAll(data);
        BaseActivity.log(" data super count:"+lista.size());

        toggleDataVisibility(errorShow && lista.size() == 0);
    }

    public void toggleDataVisibility(boolean b){
        if(b) {
            if (super.empty_view != null) super.empty_view.setVisibility(View.VISIBLE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
        } else {
            if (super.empty_view != null) super.empty_view.setVisibility(View.GONE);
            if (recyclerView != null) recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
