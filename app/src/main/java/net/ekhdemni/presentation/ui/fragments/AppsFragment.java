package net.ekhdemni.presentation.ui.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.mchUI.adapters.AppsAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyRecyclerFragment;
import net.ekhdemni.model.models.App;
import net.ekhdemni.presentation.mchUI.vms.VMGeneral;
import net.ekhdemni.utils.ProgressUtils;

import net.ekhdemni.model.oldNet.Ekhdemni;

public class AppsFragment extends MyRecyclerFragment<App, VMGeneral> {

    public AppsFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMGeneral.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.apps.observe(this, this::onDataReceived);
        mViewModel.apps();
    }

    @Override
    public void onDataReceived(List<App> data) {
        super.onDataReceived(data);
        adapter.notifyDataSetChanged();
    }

    List<App> lista = new ArrayList<>();
    GridLayoutManager mLayoutManager;
    RecyclerView.OnScrollListener endless;
    AppsAdapter adapter;
    //int total = 0;
    //public static String url = qarya.apps;
    //public String link;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        pd = ProgressUtils.getProgressDialog(getContext());
        recyclerView = view.findViewById((R.id.recycler_view));
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new AppsAdapter(super.lista, item -> {
            MyActivity.log("package name =>=>=> "+item.getPackage());
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + item.getPackage())));
            } catch (ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + item.getPackage())));
            }
        });
        recyclerView.setAdapter(adapter);

        empty_view = view.findViewById(R.id.empty_view);
        if (lista.size() == 0) {
            getData();
            return view;
        }
        return view;

    }

}