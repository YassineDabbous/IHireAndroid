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
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.requests.Searcher;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.presentation.mchUI.adapters.JobsAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Job;
import net.ekhdemni.presentation.mchUI.vms.VMJobs;
import tn.core.util.Const;
import net.ekhdemni.utils.ProgressUtils;
import android.widget.TextView;

public class JobsActivity extends MyActivity {

    List<Job> lista = new ArrayList<Job>();
    JobsAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog pd;
    LinearLayoutManager linearLayoutManager;

    VMJobs mViewModel;
    OnClickItemListener<Job> listener = item -> {
        onItemSelected(item);
    };



    @Override
    public void clean() {
        super.clean();
        if(recyclerView != null)
            recyclerView.setAdapter(null);
        pd=null;
        adapter = null;
        linearLayoutManager = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);


        setupToolbar();

        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new JobsAdapter(lista, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new endlessListener());
        pd = ProgressUtils.getProgressDialog(this);

        Intent i = getIntent();
        searcher = (Searcher) i.getSerializableExtra(Const.SEARCH);
        id = i.getIntExtra(Const.CATEGORY, 0);

        mViewModel = ViewModelProviders.of(this).get(VMJobs.class);
        mViewModel.getLiveData().observe(this, this::bind);
        getData();
    }

    void bind(PagingResponse<Job> response){
        pd.dismiss();
        lista.addAll(response.getData());
        MyActivity.log("Users count:"+lista.size()+" and total is "+total);

        total = response.getTotal();
        if(response.getNextPageUrl()!=null);
        page = response.getCurrentPage()+1;
        adapter = new JobsAdapter(lista, listener);
        recyclerView.setAdapter(adapter);
        loading = true;
    }

    int id ;//= i.getStringExtra(Const.CATEGORY);

    Searcher searcher;
    int page = 1;
    void getData(){
        if(searcher!=null){
            mViewModel.search(searcher, page);
        }
        else if (id!=0)
            mViewModel.getCategoryJobs(id, page);
        else
            mViewModel.init(page);
    }


    int total = 0;
    public boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public class endlessListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy)
        {
            if(dy > 0 && loading && total>lista.size()) //or total>totalItemCount //check for scroll down
            {
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                if ( (visibleItemCount + pastVisiblesItems + 4) >= totalItemCount)
                {
                    loading = false;
                    Log.e("...", "Last Item Wow !");
                    getData();
                }
            }
        }
    }




    @Override
    public void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        TextView titleView = findViewById(R.id.toolbar_title);
        String title = getIntent().getStringExtra(Const.TITLE);
        if(title==null)
            titleView.setText(getText(R.string.jobs));
        else
            titleView.setText(title);
        ImageView right = findViewById(R.id.toolbar_right);
        ImageView right2 = findViewById(R.id.toolbar_right_2);
        ImageView left = findViewById(R.id.toolbar_left);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobsCreatorActivity.isSearch = true;
                JobsCreatorActivity.searchFor = 0;
                startActivity(new Intent(getApplicationContext(), JobsCreatorActivity.class));
            }
        });
        right2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobsCreatorActivity.isSearch = false;
            startActivity(new Intent(getApplicationContext(), JobsCreatorActivity.class));
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        View left2 = findViewById(R.id.toolbar_left_2);
        left2.setVisibility(View.VISIBLE);
        left2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CategoriesActivity.class);
                i.putExtra("go", 0);
                startActivity(i);
            }
        });

    }
}
