package net.ekhdemni.presentation.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ekhdemni.R;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.presentation.mchUI.adapters.MessagesAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyRecyclerFragment;
import tn.core.presentation.listeners.EndlessListener;
import net.ekhdemni.model.models.Conversation;
import net.ekhdemni.model.models.Message;
import net.ekhdemni.presentation.mchUI.vms.VMMessages;
import tn.core.util.Const;

public class MessagesFragment extends MyRecyclerFragment<Message, VMMessages> {

    public EditText input;
    View send;
    public List<Message> lista = new ArrayList<>();
    LinearLayoutManager linear;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMMessages.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.getLiveData().observe(this, this::onDataReceived);
        mViewModel.messages.observe(this, this::onDataReceived);
        if(lista.size()==0){
            getData();
        }
    }


    int page = 1;
    public void onDataReceived(PagingResponse<Message> data) {
            if(endlessListener!=null && data.getTotal()!=null){
                endlessListener.total = data.getTotal();
                if(endlessListener.total>lista.size())
                    endlessListener.isloading = false;
                page = data.getCurrentPage()+1;

                List<Message> ls = data.getData();
                Collections.reverse(lista);
                lista.addAll(ls); //lista.addAll(0,ls);
                Collections.reverse(lista);
                MyActivity.log("messages count:"+lista.size());
                if(lista.size() == 0){
                    empty_view.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    return;
                }
                //Collections.reverse(lista);
                init();

            }else{
                empty_view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                return;
            }
    }
    @Override
    public void onDataReceived(List<Message> data) {
        super.onDataReceived(data);
        if (isInForegroundMode && recyclerView!=null){
            lista.addAll(data);
            MyActivity.log("messages count:"+lista.size());
            empty_view.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new MessagesAdapter(getContext(), lista);
            recyclerView.setAdapter(adapter);
            linear.scrollToPosition(lista.size() - 1);
            input.setText("");
            input.setEnabled(true);
        }
    }

    public static MessagesFragment newInstance(Conversation c, Boolean newConv) {
        Bundle args = new Bundle();
        args.putBoolean(Const.NEW, newConv);
        args.putSerializable(Const.ITEM, c);
        conversation = c;

        MessagesFragment fragment = new MessagesFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public MessagesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.reyclerview_message_list);
        adapter = new MessagesAdapter(getContext(), lista);
        linear = new LinearLayoutManager(getContext());
        //linear.setReverseLayout(true);
        //linear.setStackFromEnd(false);
        recyclerView.setLayoutManager(linear);
        recyclerView.setAdapter(adapter);
        endlessListener = new EndlessListener(0, 0, new EndlessListener.Action() {
            @Override
            public void getOnScroll() {
                getData();
            }
        }, EndlessListener.EndlessDirection.TOP);
        recyclerView.addOnScrollListener(endlessListener);
        input = view.findViewById(R.id.edittext_chatbox);
        send = view.findViewById(R.id.button_chatbox_send);
        send.isClickable();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c = input.getText().toString();
                Log.wtf("click click click click ","sending msg: "+c);
                if(!c.isEmpty()){
                    push(c);
                }
            }
        });

        empty_view = view.findViewById(R.id.empty_view);
        return view;
    }


    public static String phone;
    @Override
    public void getData(){
        //int cid = getArgs().getInt(Const.ID);
        if (conversation==null){
            Object item = getArgs().getSerializable(Const.ITEM);
            if (item!=null) conversation = (Conversation) item;
        }
        MyActivity.log("ddddddddddddddddddd"+ conversation.toString());
        mViewModel.getMessages(
                conversation.getId(),
                conversation.getUid(),
                page);
    }

    @Override
    public void onError(List<String> errors) {
        super.onError(errors);
        if(input!=null) input.setEnabled(true);
    }

    @Override
    public void init() {
        super.init();
        adapter = new MessagesAdapter(getContext(), lista);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        linear.scrollToPosition(lista.size() - 1);
    }
    public static Conversation conversation;
    public void push(String txt){
        input.setEnabled(false);
        mViewModel.pushMessage(conversation.getId(), conversation.getUid(), txt);
    }
/*
    @Override
    public void onRefresh(Object o) {
        url = Ekhdemni.conversations+"/"+conversation.id;
        getData();
    }*/

}
