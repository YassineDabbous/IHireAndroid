package net.ekhdemni.presentation.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.model.models.requests.Searcher;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.presentation.mchUI.adapters.UsersAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyRecyclerFragment;
import tn.core.presentation.listeners.EndlessListener;
import net.ekhdemni.model.models.user.User;
import net.ekhdemni.presentation.mchUI.vms.VMUsers;
import tn.core.util.Const;
import net.ekhdemni.utils.ProgressUtils;

public class UsersFragment extends MyRecyclerFragment<User, VMUsers> {

    public int page = 1;
    List<User> lista = new ArrayList<User>();
    GridLayoutManager mLayoutManager;
    public UsersAdapter adapter;


    public void onDataReceived(PagingResponse<User> data) {
        lista.addAll(data.getData());
        if(lista.size() == 0){
            empty_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        endlessListener.total = data.getTotal();
        if(endlessListener.total>lista.size())
            endlessListener.isloading = false;
        page = data.getCurrentPage()+1;
        MyActivity.log("next next next: "+ page);
        Log.wtf("ekhdemni.net","Users count:"+lista.size()+" and total is "+endlessListener.total);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMUsers.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.getLiveData().observe(this, this::onDataReceived);
        if(lista.size()==0){
            getData();
        }
    }


    @Override
    public void clean() {
        super.clean();
        mLayoutManager=null;
    }

    public UsersFragment() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        pd = ProgressUtils.getProgressDialog(getContext());

        recyclerView = view.findViewById((R.id.recycler_view));
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new UsersAdapter(lista, item -> ((MyActivity) getActivity()).onItemSelected(item));
        endlessListener = new EndlessListener(0, adapter.ADS_AFTER, new EndlessListener.Action() {
            @Override
            public void getOnScroll() {
                getData();
            }
        });
        recyclerView.addOnScrollListener(endlessListener);
        recyclerView.setAdapter(adapter);
        empty_view = view.findViewById(R.id.empty_view);
        return view;
    }


    boolean onetime = true;
    Searcher searcher;
    public void getData(){
        if (searcher==null && onetime){ onetime = false;
            Intent i = getActivity().getIntent();
            searcher = (Searcher) i.getSerializableExtra(Const.SEARCH);
        }
        if (endlessListener!=null) endlessListener.isloading = true;
        if(searcher!=null){
            mViewModel.search(searcher, page);
        }
        else
            mViewModel.init(page);
    }


}
