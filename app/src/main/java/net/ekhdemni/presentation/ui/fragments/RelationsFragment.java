package net.ekhdemni.presentation.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.vms.VMGeneral;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.mchUI.adapters.RelationsAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyRecyclerFragment;
import net.ekhdemni.model.models.Relation;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.RelationVH;

import org.jetbrains.annotations.NotNull;

import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;
import tn.core.util.Const;


public class RelationsFragment extends MyRecyclerFragment<Relation, VMGeneral> {

    RecyclerView recyclerView;
    public BaseAdapter<Relation, RelationVH> adapter;
    public RelationsFragment() {
        // Required empty public constructor
    }

    public static RelationsFragment newInstance() {
        return newInstance(0);
    }
    public static RelationsFragment newInstance(int uid) {
        Bundle args = new Bundle();
        args.putInt(Const.ID, uid);
        RelationsFragment fragment = new RelationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMGeneral.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.relations.observe(this, this::onDataReceived);
        mViewModel.relation.observe(this, this::onDataReceived);
        mViewModel.relations(getArgs().getInt(Const.ID ,0));
    }


    public void onDataReceived(Relation data) {
        adapter.refresh(data);
    }
    @Override
    public void onDataReceived(List<Relation> data) {
        super.onDataReceived(data);
        adapter = new RelationsAdapter(lista, new OnInteractListener<Relation>() {

            @Override
            public void onInteract(Relation item, @NotNull Action action) {
                switch (action){
                    case ACCEPT: {
                        MyActivity.log("like post from fragment");
                        mViewModel.acceptRelation(item.getId());
                    } break;
                    case REFUSE: {
                        MyActivity.log("like post from fragment");
                        mViewModel.refuseRelation(item.getId());
                    } break;
                }
            }

            @Override
            public void onClick(Relation item) {
                ((MainActivity) getActivity()).onItemSelected(item);
            }
        });
        recyclerView.setAdapter(adapter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView =  view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RelationsAdapter(lista , item -> {((MyActivity) getActivity()).onItemSelected(item);});
        recyclerView.setAdapter(adapter);
        empty_view = view.findViewById(R.id.empty_view);
        return view;
    }




}
