package net.ekhdemni.presentation.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.mchUI.adapters.ResourcesAdapter;
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager;
import net.ekhdemni.model.feeds.db.MyDataBase;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyRecyclerFragment;
import net.ekhdemni.model.models.Resource;
import net.ekhdemni.utils.ProgressUtils;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;

import org.jetbrains.annotations.NotNull;

import tn.core.model.net.net.NetworkUtils;
import tn.core.presentation.listeners.OnInteractListener;


public class ResourcesFragment extends MyRecyclerFragment {

    MyDataBase dba;
    Context context;
    public ResourcesAdapter adapter;
    public List<Resource> lista = new ArrayList<>();
    //private ItemTouchHelper mItemTouchHelper;

    public static ResourcesFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ResourcesFragment fragment = new ResourcesFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void clean() {
        super.clean();
        dba = null;
        context = null;
        //mItemTouchHelper = null;
    }



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResourcesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    boolean oneTime = true;
    @Override
    public void getData(){
        dba.openToRead();
        lista = dba.takeResources(false);
        dba.close();
        MyActivity.log("db resources count: "+lista.size());
        if(oneTime && lista.size()==0){
            oneTime=false;
            if(NetworkUtils.isOnline(getActivity()))
                updateResourceFromServer();
            else
                getData();
        }else if(lista.size()==0){
            empty_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }else{
            MyActivity.log("db resources count: "+lista.size()+", setting adapter...");
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new ResourcesAdapter(getActivity(), lista, new OnInteractListener<Resource>() {
                @Override
                public void onInteract(Resource item, tn.core.presentation.listeners.@NotNull Action action) {
                    onClick(item);
                }

                @Override
                public void onClick(Resource item) {
                    ((MyActivity) getActivity()).setFragment(FeedsFragment.newInstance(FeedsFragment.Filter.resource, item));
                }
            });
            recyclerView.setAdapter(adapter);
        }

    }
    //View view;
    @Override
    public void init() {
        View view = getView();
        context = view.getContext();
        dba = MyDataBase.getInstance(context);

        pd = ProgressUtils.getProgressDialog(context);

        empty_view = view.findViewById(R.id.empty_view);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ResourcesAdapter(getActivity(), lista, listener);
        recyclerView.setAdapter(adapter);
        Activity activity = getActivity();
        if(activity !=null)
            ((MainActivity) activity).hideSwipe();

        view.findViewById(R.id.allFeed).setOnClickListener(view1 ->  {
            ((MyActivity) activity).setFragment(FeedsFragment.newInstance(FeedsFragment.Filter.all, null));
        });
        view.findViewById(R.id.newSource).setOnClickListener(view1 ->  {
            ((MyActivity) activity).setFragment(NewResourceFragment.newInstance());
        });

        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return  inflater.inflate(R.layout.fragment_resources, container, false);
    }





    public void updateResourceFromServer(){
        pd.show();
        Action action = new Action(getContext()) {
            public void doFunction(String s) {
                List<Resource> list = parseResources(s);
                Log.wtf("ekhdemni.net","Resources count:"+list.size());
                for (Resource r: list) {
                    dba.openToRead();
                    boolean exit = dba.resourceExist(r.title, r.url);
                    dba.close();
                    if(!exit){
                        dba.openToWrite();
                        dba.insertResource(r.title, r.url, r.logo, r.verified, r.enabled);
                        dba.close();
                    }
                }
                pd.dismiss();
                getData();
            }
        };
        String country_id = YDUserManager.get(getActivity(), YDUserManager.COUNTRY_KEY);
        String url;
        if(country_id!=null)
            url = Ekhdemni.countries+"/"+country_id+"/resources";
        else
            url = Ekhdemni.countries+"/1/resources";
        MyActivity.log("resources url: "+url);
        action.url = url;
        action.needAuth = false;
        action.enableCache = true;
        action.run();
    }

}
