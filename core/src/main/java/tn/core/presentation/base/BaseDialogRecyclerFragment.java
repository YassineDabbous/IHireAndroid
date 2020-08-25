package tn.core.presentation.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tn.core.R;
import tn.core.model.responses.PagingResponse;
import tn.core.presentation.listeners.EndlessListener;
import tn.core.presentation.listeners.OnInteractListener;


/**
 * Created by X on 8/13/2018.
 */

public class BaseDialogRecyclerFragment<Model, VM> extends BaseDialogFragment<VM> {

    public List<Model> lista = new ArrayList<>();

    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public OnInteractListener<Model> listener;
    public EndlessListener endlessListener;
    public int page = 1;


    public void onDataReceived(PagingResponse<Model> data) {
        endlessListener.total = data.getTotal();
        page = data.getCurrentPage()+1;
        onDataReceived(data.getData());
    }
    public void onDataReceived(List<Model> data){
        lista.addAll(data);
        BaseActivity.log(" data super count:"+lista.size());
        if(lista.size() == 0) {
            if (errorShow && empty_view != null) super.empty_view.setVisibility(View.VISIBLE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
            return;
        }
    }

}
