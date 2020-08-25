package net.ekhdemni.presentation.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.mchUI.vms.VMGeneral;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.mchUI.adapters.NotificationsAdapter;
import tn.core.presentation.base.MyRecyclerFragment;
import net.ekhdemni.model.models.Notification;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class NotificationsFragment extends MyRecyclerFragment<Notification, VMGeneral> {


    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMGeneral.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.notifications.observe(this, this::onDataReceived);
        mViewModel.notifications();
    }

    @Override
    public void onDataReceived(List<Notification> data) {
        super.onDataReceived(data);
        adapter = new NotificationsAdapter(lista, item -> {((MainActivity) getActivity()).onItemSelected(item);});
        recyclerView.setAdapter(adapter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationsAdapter(lista, item -> {((MainActivity) getActivity()).onItemSelected(item);});
        recyclerView.setAdapter(adapter);
        empty_view = view.findViewById(R.id.empty_view);
        return view;
    }

}
