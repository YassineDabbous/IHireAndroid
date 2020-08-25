package net.ekhdemni.presentation.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.mchUI.vms.VMGeneral;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.mchUI.adapters.ServicesAdapter;
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyRecyclerFragment;
import net.ekhdemni.model.models.Service;
import net.ekhdemni.utils.ProgressUtils;


public class ServicesFragment extends MyRecyclerFragment<Service, VMGeneral> {


    public ServicesFragment() {
    }

    public static ServicesFragment newInstance() {

        Bundle args = new Bundle();

        ServicesFragment fragment = new ServicesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMGeneral.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.services.observe(this, this::onDataReceived);

        String country_id = YDUserManager.get(getContext(), YDUserManager.COUNTRY_KEY);
        if(country_id!=null) country_id = "1";
        mViewModel.services(Integer.parseInt(country_id));
    }

    @Override
    public void onDataReceived(List<Service> data) {
        super.onDataReceived(data);
        MyActivity.log("Services count:"+lista.size()+", setting list...");
        adapter = new ServicesAdapter(super.lista, item -> {((MainActivity) getActivity()).onItemSelected(item);});
        recyclerView.setAdapter(adapter);
    }





    



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        pd = ProgressUtils.getProgressDialog(getContext());
        empty_view = view.findViewById(R.id.empty_view);
        recyclerView = view.findViewById((R.id.recycler_view));
        /*recyclerView.setOnTouchListener(new View.OnTouchListener() {
            float height;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                float height = event.getY();
                if(action == MotionEvent.ACTION_DOWN){
                    this.height = height;
                }else if(action == MotionEvent.ACTION_UP){
                    if(this.height < height){
                        Log.v("scrollView", "Scrolled up");
                        ((MainActivity) getActivity()).slideUp();
                    }else if(this.height > height){
                        Log.v("scrollView", "Scrolled down");
                        ((MainActivity) getActivity()).slideBottom();
                    }
                }
                return false;
            }
        });*/


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ServicesAdapter(lista, item -> {((MainActivity) getActivity()).onItemSelected(item);});
        recyclerView.setAdapter(adapter);

        MyActivity.log("services count : " +lista.size());
        if(lista.size()==0)
            getData();
        return view;
    }
}
