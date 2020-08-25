package net.ekhdemni.presentation.ui.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.model.models.responses.LikeResponse;
import tn.core.model.responses.PagingResponse;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

import net.ekhdemni.presentation.mchUI.adapters.WorksAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyRecyclerFragment;
import tn.core.presentation.listeners.EndlessListener;
import net.ekhdemni.model.models.Work;
import net.ekhdemni.presentation.mchUI.vms.VMWorks;
import tn.core.util.Const;
import net.ekhdemni.utils.ProgressUtils;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@paginator Fragment} subclass.
 */

public class WorksFragment extends MyRecyclerFragment<Work, VMWorks> {

    //GridRecyclerView recyclerView;
    public int page = 1;
    public WorksAdapter adapter;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    List<Work> lista = new ArrayList<>();


    public static WorksFragment newInstance() {

        Bundle args = new Bundle();

        WorksFragment fragment = new WorksFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public WorksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMWorks.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.like.observe(this, this::onDataReceived);
        mViewModel.getLiveData().observe(this, this::onDataReceived);
        if(lista.size()==0){
            getData();
        }
    }
    public void onDataReceived(LikeResponse data) {
        MyActivity.log("search for liked item...");
        for (int i = 0; i <lista.size(); i++) {
            if (lista.get(i).getId().equals(data.getId())){
                MyActivity.log("Liked item found!");
                lista.get(i).setLiked(data.getLiked());
                lista.get(i).setLikesCount(data.getLikesCount());
                MyActivity.log("Refresh adapter at "+i+" position with "+data.getLikesCount()+" likes");
                //adapter.notifyItemChanged(i);
                adapter.refresh(i, lista.get(i));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_grid, container, false);

        recyclerView = v.findViewById(R.id.recycler_view);

        pd = ProgressUtils.getProgressDialog(getContext());

        category = getActivity().getIntent().getIntExtra(Const.CATEGORY, 0);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        adapter = new WorksAdapter(lista, new OnInteractListener<Work>() {
            @Override
            public void onInteract(Work item, @NotNull Action action) {
                switch (action){
                    case LIKE: {
                        MyActivity.log("like post from fragment");
                        mViewModel.like(item.getId());
                    } break;
                }
            }
            @Override
            public void onClick(Work item) {
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
        empty_view = v.findViewById(R.id.empty_view);


        return v;
    }



    public int category;
    @Override
    public void getData(){
        if (endlessListener!=null) endlessListener.isloading = true;
        if (category==0)
            mViewModel.init(page);
        else
            mViewModel.search(category, page);
    }


    public void onDataReceived(PagingResponse<Work> data) {
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

}
