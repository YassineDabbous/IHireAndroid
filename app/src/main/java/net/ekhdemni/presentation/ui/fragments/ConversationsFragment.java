package net.ekhdemni.presentation.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import net.ekhdemni.R;
import net.ekhdemni.presentation.mchUI.adapters.ConversationsAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyRecyclerFragment;
import net.ekhdemni.model.models.Conversation;
import net.ekhdemni.utils.ProgressUtils;
import net.ekhdemni.presentation.mchUI.vms.VMConversations;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class ConversationsFragment extends MyRecyclerFragment {

    private VMConversations mViewModel;
    ConversationsAdapter adapter;

    public ConversationsFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMConversations.class);
        mViewModel.init();

        adapter = new ConversationsAdapter(null,  item -> {
            ((MyActivity) getActivity()).setFragment(MessagesFragment.newInstance(item, false));
        });
        mViewModel.listLiveData.observe(this, adapter::submitList);//
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list, container, false);
        pd = ProgressUtils.getProgressDialog(getContext());
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        empty_view = view.findViewById(R.id.empty_view);
        return view;
    }



    //List<Conversation> list = new ArrayList<>();
    public void setData(PagedList<Conversation> data){
        //list.addAll(data);
        MyActivity.log("☺☺☺• set data: tolal is "+data.size()+" new are "+data.size());
        adapter.submitList(data);
        if(data.size() == 0){
            empty_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void networkStatus(boolean status){
        endlessListener.isloading = status;
        pd.show();
    }

    public void onFailure(Call call, IOException e) {
        if(pd!=null) pd.dismiss();
    }


}
