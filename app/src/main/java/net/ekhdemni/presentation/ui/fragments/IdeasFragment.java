package net.ekhdemni.presentation.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ekhdemni.R;
import net.ekhdemni.model.models.Idea;
import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.presentation.mchUI.adapters.IdeasAdapter;
import net.ekhdemni.presentation.mchUI.vms.VMGeneral;
import net.ekhdemni.presentation.mchUI.vms.VMIdeas;
import net.ekhdemni.presentation.ui.activities.MainActivity;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import tn.core.model.responses.PagingResponse;
import tn.core.presentation.base.MyRecyclerFragment;
import tn.core.presentation.listeners.EndlessListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class IdeasFragment extends MyRecyclerFragment<Idea, VMIdeas> {


    public IdeasFragment() {
        // Required empty public constructor
    }



    public static IdeasFragment newInstance() {

        Bundle args = new Bundle();

        IdeasFragment fragment = new IdeasFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMIdeas.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.getIdeas().observe(this, this::onDataReceived);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        mViewModel.list(0, page);
    }

    @Override
    public void onDataReceived(List<Idea> data) {
        super.onDataReceived(data);
        adapter = new IdeasAdapter(lista, item -> {((MainActivity) getActivity()).onItemSelected(item);});
        recyclerView.setAdapter(adapter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        endlessListener = new EndlessListener(0, 0, new EndlessListener.Action() {
            @Override
            public void getOnScroll() {
                MyActivity.log("get in scroll");
                getData();
            }
        });
        recyclerView.addOnScrollListener(endlessListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new IdeasAdapter(lista, item -> {((MainActivity) getActivity()).onItemSelected(item);});
        recyclerView.setAdapter(adapter);

        empty_view = view.findViewById(R.id.empty_view);
        return view;
    }

}
