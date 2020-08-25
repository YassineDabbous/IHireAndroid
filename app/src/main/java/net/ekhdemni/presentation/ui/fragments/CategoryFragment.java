package net.ekhdemni.presentation.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.presentation.mchUI.adapters.CategoryAdapter;
import net.ekhdemni.model.feeds.db.MyDataBase;
import tn.core.presentation.base.MyRecyclerFragment;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

import net.ekhdemni.model.models.Category;
import net.ekhdemni.presentation.mchUI.vms.VMComments;

import org.jetbrains.annotations.NotNull;

public class CategoryFragment extends MyRecyclerFragment<Category, VMComments> {


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void clean() {
        super.clean();
    }

    @Override
    public void init() {
        super.init();

        if(lista.size()==0){
            getData();
        }

        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryAdapter(lista, new OnInteractListener<Category>() {
            @Override
            public void onInteract(Category item, @NotNull Action action) {
                onClick(item);
            }

            @Override
            public void onClick(Category item) {
                ((MyActivity) getActivity()).setFragment(FeedsFragment.newInstance(FeedsFragment.Filter.category, item));
            }
        });
        recyclerView.setAdapter(adapter);

        if (lista.size() == 0)
            Toast.makeText(getContext(), getResources().getString(R.string.empty), Toast.LENGTH_LONG).show();
    }

    public static String category = null;
    public static boolean showCategories = false;
    @Override
    public void getData(){
        MyDataBase dba = MyDataBase.getInstance(getContext());
        dba.openToRead();
        if(showCategories){
            lista = dba.takeCategories("100000");
        }
        else{
            lista = dba.takeCities("300");
        }
        dba.close();
    }


    List<Category> lista = new ArrayList<Category>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }


}
