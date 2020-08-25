package net.ekhdemni.presentation.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ekhdemni.R;

import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.VHAlert;
import tn.core.presentation.base.MyRecyclerFragment;
import net.ekhdemni.model.models.Alert;
import net.ekhdemni.presentation.mchUI.vms.VMGeneral;

import java.util.List;


public class AlertsFragment extends MyRecyclerFragment<Alert, VMGeneral> {

    public BaseAdapter<Alert, VHAlert> adapter;
    public AlertsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMGeneral.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.alerts.observe(this, this::onDataReceived);
        mViewModel.alerts();
    }

    @Override
    public void onDataReceived(List<Alert> data) {
        super.onDataReceived(data);
        adapter = new BaseAdapter(super.lista, VHAlert.class);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseAdapter(lista, VHAlert.class);
        recyclerView.setAdapter(adapter);
        empty_view = view.findViewById(R.id.empty_view);
        if(lista.size()==0){
            getData();
        }
        return view;
    }

}
