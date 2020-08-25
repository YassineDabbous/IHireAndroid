package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.ekhdemni.R;
import tn.core.domain.Failure;

import net.ekhdemni.model.ModelType;
import net.ekhdemni.presentation.base.BaseAdapter;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Commun;
import net.ekhdemni.model.models.Forum;
import net.ekhdemni.model.models.Idea;
import net.ekhdemni.model.models.Job;
import net.ekhdemni.model.models.Post;
import net.ekhdemni.model.models.Work;
import net.ekhdemni.model.models.user.User;
import net.ekhdemni.domain.usecase.UCGeneric;
import tn.core.domain.base.Closure;
import tn.core.util.Const;

import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class ListVH extends BaseViewHolder<Commun> {
    public final View mView;
    public final TextView name;
    public Button more;
    public RecyclerView recyclerView;
    public List<Commun> lista = new ArrayList<>();
    public Commun mItem;
    BaseAdapter adapter;
    ContentLoadingProgressBar pd;

    public ListVH(View view) {
        super(view);
        mView = view;
        name = view.findViewById(R.id.name);
        pd = view.findViewById(R.id.looking);
        more = view.findViewById(R.id.more);
        recyclerView = view.findViewById(R.id.horizontal);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_forum_type;
    }

    @Override
    public void bind(Commun model) {
        more.setVisibility(View.GONE);
        mItem = model;
        if (model.getName()!=null && !model.getName().isEmpty())
            name.setText(model.getName());
        else
            name.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setOnClickListener(new View.OnClickListener() {
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
        getData();
    }

    public void setList() {
        recyclerView.setAdapter(adapter);
        Timber.d("special forums count: "+lista.size());
        pd.hide();
    }


     <YModel extends Commun> void doIt(){
        new UCGeneric().<YModel>generic(mItem.modelType, mItem.modelPath, 1, new Closure<List<YModel>>() {
            @Override
            public void onSuccess(List<YModel> response) {
                lista.addAll(response);
                adapter.items = response;
                setList();
            }

            @Override
            public void onError(Failure failure) {
                handleError(failure);
            }
        });
    }
    public void handleError(Failure failure){
        /*for (String error:errors) {
            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
        }*/
    }

    public void getData(){
        pd.show();
        if(mItem.modelType == ModelType.FORUM){
            adapter = new BaseAdapter<Forum, CircleVH>(new ArrayList<>(), CircleVH.class, item -> listener.onClick(item));
            this.<Forum>doIt();
        }
        else if(mItem.modelType == ModelType.IDEA){
            adapter = new BaseAdapter<Idea, IdeaVH>(new ArrayList<>(), IdeaVH.class, item -> listener.onClick(item));
            this.<Idea>doIt();
        }
        else if(mItem.modelType == ModelType.POST){
            adapter = new BaseAdapter<Post, PostsVH>(new ArrayList<>(), PostsVH.class, item -> listener.onClick(item));
            this.<Post>doIt();
        }
        else if(mItem.modelType == ModelType.WORK){
            adapter = new BaseAdapter<Work, WorkVH>(new ArrayList<>(), WorkVH.class, item -> listener.onClick(item));
            this.<Work>doIt();
        }
        else if(mItem.modelType == ModelType.JOB){
            adapter = new BaseAdapter<Job, JobVH>(new ArrayList<>(), JobVH.class, item -> listener.onClick(item));
            this.<Job>doIt();
        }
        else if(mItem.modelType == ModelType.USER){
            adapter = new BaseAdapter<User, UsersVH>(new ArrayList<>(), UsersVH.class, item -> listener.onClick(item));
            this.<User>doIt();
        }

        /*
        if(mItem.modelType == 0)
            new UCGeneric().<Forum>generic(mItem.modelPath, 1, new Closure<List<Forum>>() {
                @Override
                public void onSuccess(List<Forum> response) {
                    lista.addAll(response);
                    adapter = new ForumsHorizontalAdapter(response, item -> listener.onClick(item));
                    setList();
                }

                @Override
                public void onError(Failure failure) {
                    handleError(failure);
                }
            });
        */

    }
}