package net.ekhdemni.presentation.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ekhdemni.R;
import tn.core.model.responses.PagingResponse;

import net.ekhdemni.model.ModelType;
import net.ekhdemni.presentation.mchUI.adapters.CommentsAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.presentation.ui.fragments.TopicFragment;
import tn.core.presentation.listeners.EndlessListener;
import net.ekhdemni.model.models.Comment;
import net.ekhdemni.presentation.mchUI.vms.VMComments;
import net.ekhdemni.utils.ProgressUtils;
import android.widget.TextView;

/**
 * Created by X on 5/16/2018.
 */

public class CommentsActivity extends MyActivity {
    Context context;
    public static Integer item_id = 1, type = ModelType.POST;
    public static String link = "ekhdemni.net";
    public static String title;
    //public static String url = Ekhdemni.comments+"/"+type+"/"+item_id;

    EndlessListener endlessListener;
    List<Comment> lista = new ArrayList<Comment>();
    CommentsAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog pd;
    EditText commentEdit;
    ImageView send;

    @Override
    public void clean() {
        super.clean();
        adapter = null;
        recyclerView = null;
        pd = null;
        commentEdit = null;
        if(send != null)
            send.setOnClickListener(null);
        send = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_layout);
        context = getApplicationContext();
        TextView titleView = findViewById(R.id.title);
        if(title!=null){
            titleView.setText(title);
        }
        recyclerView = findViewById(R.id.comments_recycler_view);
        commentEdit = findViewById(R.id.commentEdit);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CommentsAdapter(lista, mListener);
        recyclerView.setAdapter(adapter);
        endlessListener = new EndlessListener(0, CommentsAdapter.ADS_AFTER, new EndlessListener.Action() {
            @Override
            public void getOnScroll() {
                getData();
            }
        }, EndlessListener.EndlessDirection.TOP);
        recyclerView.addOnScrollListener(endlessListener);
        pd = ProgressUtils.getProgressDialog(this);

        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.wtf("click click click click ","send send send send send send ");
                String c = commentEdit.getText().toString();
                if(!c.isEmpty()){
                    pushComment(c);
                }
            }
        });


        mViewModel = ViewModelProviders.of(this).get(VMComments.class);
        mViewModel.getLiveData().observe(this, this::bind);
        mViewModel.comments.observe(this, this::receiveComments);
        getData();
    }
    VMComments mViewModel;

    public void bind(PagingResponse<Comment> response){
        if (response!=null && response.getTotal()!=null){
            endlessListener.total = response.getTotal();
            if(endlessListener.total>lista.size())
                endlessListener.isloading = false;
            page = response.getCurrentPage()+1;
            List<Comment> ls = response.getData();
            Collections.reverse(ls);
            lista.addAll(0,ls);
            adapter.notifyDataSetChanged();
        }
        recyclerView.getLayoutManager().scrollToPosition(lista.size() - 1);
        if (lista.size()==0){
            //empty_view.setVisibility(View.VISIBLE);
            //recyclerView.setVisibility(View.INVISIBLE);
            return;
        }

    }
    public void receiveComments(List<Comment> list){
        lista.addAll(list);
        Log.wtf("ekhdemni.net","forums count:"+lista.size());
        adapter = new CommentsAdapter(lista, mListener);
        recyclerView.setAdapter(adapter);
        recyclerView.getLayoutManager().scrollToPosition(lista.size() - 1);
        commentEdit.setText("");
    }
    public void pushComment(String commentTxt){
        mViewModel.pushComment(item_id, type, link, commentTxt);
    }
    int page=1;
    public void getData(){
        mViewModel.getComments(item_id, type, link, page);
    }


    private TopicFragment.OnListFragmentInteractionListener mListener;
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Comment item);
    }
}
