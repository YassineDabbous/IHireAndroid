package net.ekhdemni.presentation.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.presentation.ui.activities.ForumsActivity;
import net.ekhdemni.presentation.mchUI.adapters.ForumsTypesAdapter;
import tn.core.presentation.base.MyRecyclerFragment;
import net.ekhdemni.model.models.Forum;
import net.ekhdemni.presentation.mchUI.vms.VMForumTypes;
import net.ekhdemni.utils.ProgressUtils;
import net.ekhdemni.utils.ProgressUtils;

public class ForumsTypesFragment extends MyRecyclerFragment<Forum, VMForumTypes> { //ForumsTypesAdapter

    public ForumsTypesAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMForumTypes.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.getLiveData().observe(this, this::onDataReceived);
        if(lista.size()==0){
            mViewModel.init();
        }
    }

    @Override
    public void onDataReceived(List<Forum> data) {
        super.onDataReceived(data);
        if(data.size() == 0){
            empty_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        MyActivity.log("ellllllldata "+lista.size());
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ForumsTypesAdapter(lista, item -> ((ForumsActivity) getActivity()).onForumItemSelected(item));
        recyclerView.setAdapter(adapter);

        pd = ProgressUtils.getProgressDialog(getActivity());

        empty_view = view.findViewById(R.id.empty_view);
        return view;
    }

}
