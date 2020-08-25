package net.ekhdemni.presentation.ui.fragments;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ekhdemni.R;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.presentation.ui.activities.ForumsActivity;
import net.ekhdemni.presentation.mchUI.adapters.ForumsAdapter;
import tn.core.presentation.base.MyRecyclerFragment;
import tn.core.presentation.listeners.EndlessListener;
import net.ekhdemni.model.models.Forum;
import net.ekhdemni.presentation.mchUI.vms.VMForums;
import net.ekhdemni.utils.ProgressUtils;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class ForumsFragment extends MyRecyclerFragment<Forum, VMForums> {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMForums.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.getLiveData().observe(this, this::onDataReceived);
        if(lista.size()==0){
            String forumType = getArgs().getString("forumType", "1");
            mViewModel.init(Integer.parseInt(forumType));
        }
    }


    public void onDataReceived(PagingResponse<Forum> data) {
        super.onDataReceived(data.getData());
        if(lista.size() == 0){
            empty_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forums, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ForumsAdapter(lista, item -> {((ForumsActivity) getActivity()).onItemSelected(item);});
        recyclerView.setAdapter(adapter);
        endlessListener = new EndlessListener(0, 0, new EndlessListener.Action() {
            @Override
            public void getOnScroll() {
                getData();
            }
        });
        recyclerView.addOnScrollListener(endlessListener);


        FloatingActionButton newTopic = view.findViewById(R.id.newTopic);
        newTopic.setVisibility(View.GONE);

        pd = ProgressUtils.getProgressDialog(getActivity());

        empty_view = view.findViewById(R.id.empty_view);

        return view;
    }

}
