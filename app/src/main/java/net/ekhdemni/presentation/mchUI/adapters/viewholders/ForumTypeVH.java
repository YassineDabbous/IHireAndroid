package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.ekhdemni.R;
import tn.core.domain.Failure;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Forum;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.domain.usecase.UCForums;
import tn.core.domain.base.Closure;
import net.ekhdemni.presentation.mchUI.adapters.ForumsHorizontalAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ForumTypeVH extends BaseViewHolder<Forum> {
    public final View mView;
    public final TextView name;
    public Button more;
    public RecyclerView horizontalRecyclerView;
    public List<Forum> horizontalLista = new ArrayList<>();
    public Forum mItem;
    ForumsHorizontalAdapter horizontalAdapter;
    ContentLoadingProgressBar pd;

    public ForumTypeVH(View view) {
        super(view);
        mView = view;
        name = view.findViewById(R.id.name);
        pd = view.findViewById(R.id.looking);
        more = view.findViewById(R.id.more);
        horizontalRecyclerView = view.findViewById(R.id.horizontal);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_forum_type;
    }

    @Override
    public void bind(Forum model) {
        mItem = model;
        name.setText(model.getName());
        getData();
        horizontalRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(mItem);
                }
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(mItem);
                }
            }
        });
    }

    public void setList() {
        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        horizontalRecyclerView.setNestedScrollingEnabled(false);
        horizontalAdapter = new ForumsHorizontalAdapter(horizontalLista, listener);
        horizontalRecyclerView.setAdapter(horizontalAdapter);
    }


    public void getData(){
        pd.show();
        new UCForums().getForums(mItem.getId(), 1, new Closure<PagingResponse<Forum>>() {
            @Override
            public void onSuccess(PagingResponse<Forum> response) {
                horizontalLista.addAll(response.getData());
                Log.wtf("ekhdemni.net","special forums count:"+horizontalLista.size());
                pd.hide();
                setList();
            }

            @Override
            public void onError(Failure failure) {

            }
        });
    }
}