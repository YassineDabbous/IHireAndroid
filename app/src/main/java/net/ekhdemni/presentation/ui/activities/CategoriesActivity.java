package net.ekhdemni.presentation.ui.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.R;
import tn.core.domain.Failure;
import net.ekhdemni.domain.usecase.UCAuth;
import tn.core.domain.base.Closure;
import net.ekhdemni.presentation.mchUI.adapters.CategoryAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Broadcast;
import net.ekhdemni.model.models.Category;
import net.ekhdemni.presentation.mchUI.vms.VMGeneral;
import tn.core.util.Const;
import net.ekhdemni.utils.ProgressUtils;

import android.widget.TextView;

public class CategoriesActivity extends MyActivity {

    VMGeneral mViewModel;
    List<Category> lista = new ArrayList<Category>();
    CategoryAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog pd;
    CategoriesListener categoriesListener;

    @Override
    public void clean() {
        super.clean();
        categoriesListener = null;
        pd = null;
        adapter=null;
        if(recyclerView != null)
            recyclerView.setAdapter(null);
        recyclerView=null;
    }


    public void onDataReceived(List<Category> data) {
        lista.addAll(data);
        setList();
    }
    public void onError(List<String> errors){

    }
    public void onStatusChanged(Boolean b){
        MyActivity.logHome("Status changed to ("+b+"), is pd ready? "+ (pd!=null));
        if(b && pd!=null) pd.show();
        else if(pd!=null) pd.dismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        categoriesListener = new CategoriesListener() {
            @Override
            public void onClick(Category item) {
                if(item.getParentId().equals(0)){
                    Intent i = new Intent(CategoriesActivity.this, CategoriesActivity.class);
                    i.putExtra(Const.ID , item.getId());
                    i.putExtra("go" , getIntent().getIntExtra("go", 0));
                    startActivity(i);
                }else{
                    Intent intent;
                    if(getIntent().getIntExtra("go", 0)==0){
                        intent = new Intent(getApplicationContext(), JobsActivity.class);
                    }else{
                        intent = new Intent(CategoriesActivity.this, WorksActivity.class);
                    }
                    intent.putExtra(Const.ID , item.getId());
                    intent.putExtra(Const.CATEGORY,item.getId());
                    intent.putExtra(Const.TITLE,item.getTitle());
                    startActivity(intent);
                }
            }
        };


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        int t = getIntent().getIntExtra("go", 0);
        if (t==0)
            adapter = new CategoryAdapter(lista, categoriesListener, CategoryAdapter.Type.JOBS);
        else
            adapter = new CategoryAdapter(lista, categoriesListener, CategoryAdapter.Type.WORKS);
        recyclerView.setAdapter(adapter);
        pd = ProgressUtils.getProgressDialog(this);

        if(broadcasts.size()==0) followBroadcasts();

        mViewModel = ViewModelProviders.of(this).get(VMGeneral.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.categories.observe(this, this::onDataReceived);
        int id = getIntent().getIntExtra(Const.ID, 0);
        if (lista.size()==0) mViewModel.categories(id);
    }




    void setList(){
        Log.wtf("ekhdemni.net","Categories count:"+lista.size());
        int t = getIntent().getIntExtra("go", 0);
        if (t==0)
            adapter = new CategoryAdapter(lista, categoriesListener, CategoryAdapter.Type.JOBS);
        else
            adapter = new CategoryAdapter(lista, categoriesListener, CategoryAdapter.Type.WORKS);
        adapter.setBroadcasts(broadcasts);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void followBroadcasts(){

        new UCAuth().broadcasts(new Closure<List<Broadcast>>() {
            @Override
            public void onSuccess(List<Broadcast> brs) {
                if (isInForeground()){
                    if (brs.size()>0){
                        broadcasts.removeAll(broadcasts);
                        broadcasts.addAll(brs);
                    }
                    setList();
                }
            }

            @Override
            public void onError(Failure failure) {

            }
        });

    }


    public interface CategoriesListener {
        void onClick(Category item);
    }




    @Override
    public void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(getText(R.string.choose_category));
        ImageView right = findViewById(R.id.toolbar_right);
        ImageView right2 = findViewById(R.id.toolbar_right_2);
        ImageView left = findViewById(R.id.toolbar_left);
        right.setVisibility(View.INVISIBLE);
        right2.setVisibility(View.INVISIBLE);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
