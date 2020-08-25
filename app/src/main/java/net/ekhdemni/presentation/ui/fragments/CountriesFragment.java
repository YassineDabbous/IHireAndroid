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
import net.ekhdemni.presentation.mchUI.adapters.CountriesAdapter;
import tn.core.presentation.base.MyRecyclerFragment;
import net.ekhdemni.model.models.Country;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class CountriesFragment extends MyRecyclerFragment<Country, VMGeneral> {

    public CountriesAdapter adapter;
    public static int goTo = 0;

    public CountriesFragment() {}

    public static CountriesFragment newInstance(int goTo) {

        Bundle args = new Bundle();

        CountriesFragment fragment = new CountriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMGeneral.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.countries.observe(this, this::onDataReceived);
        mViewModel.countries();
    }

    @Override
    public void onDataReceived(List<Country> data) {
        super.onDataReceived(data);
        adapter = new CountriesAdapter(lista, item -> {((MainActivity) getActivity()).onCountrySelected(item);});
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        empty_view = view.findViewById(R.id.empty_view);
        return view;
    }

}
