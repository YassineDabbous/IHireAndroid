package net.ekhdemni.presentation.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import android.widget.ImageView;
import com.onesignal.OneSignal;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import net.ekhdemni.R;
import net.ekhdemni.model.ModelType;
import net.ekhdemni.model.models.responses.LikeResponse;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.presentation.ui.activities.CommentsActivity;
import net.ekhdemni.presentation.ui.activities.ForumsActivity;
import net.ekhdemni.presentation.mchUI.adapters.PostsAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyRecyclerFragment;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.EndlessListener;
import net.ekhdemni.model.models.Forum;
import net.ekhdemni.model.models.Post;
import net.ekhdemni.presentation.mchUI.vms.VMPosts;
import net.ekhdemni.utils.BroadUtils;

import tn.core.presentation.listeners.OnInteractListener;
import tn.core.util.Completion;
import tn.core.util.Const;
import net.ekhdemni.utils.ImageHelper;
import net.ekhdemni.utils.ProgressUtils;
import tn.core.util.Utilities;
import android.widget.TextView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class TopicsFragment extends MyRecyclerFragment<Post, VMPosts> {


    public Forum forum;
    boolean subscribed = false;
    public PostsAdapter adapter;
    Button follow;

    public TopicsFragment() {}

    public static TopicsFragment newInstance(Forum forum) {
        Bundle args = new Bundle();
        if (forum!=null) args.putSerializable(Const.ITEM, forum);
        TopicsFragment fragment = new TopicsFragment();
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMPosts.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.like.observe(this, this::onDataReceived);
        mViewModel.getLiveData().observe(this, this::onDataReceived);
        if(lista.size()==0){
            getData();
        }
    }

    @Override
    public void getData() {
        super.getData();
        int uid = getActivity().getIntent().getIntExtra(Const.UID, 0);
        if(uid>0)
            mViewModel.getUserPosts(uid, page);
        else{
            if(forum==null)
                forum = (Forum) getArgs().getSerializable(Const.ITEM);
            if(uid==0 && forum!=null)
                mViewModel.init(forum.getId(), page);
            else
                mViewModel.init(1, page);
        }
    }

    @Override
    public void onDataReceived(PagingResponse<Post> data) {
        super.onDataReceived(data);
        if(lista.size() == 0){
            empty_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        adapter.notifyDataSetChanged();
    }
    public void onDataReceived(LikeResponse data) {
        MyActivity.log("search for liked item...");
        for (int i = 0; i <lista.size(); i++) {
            if (lista.get(i).getId().equals(data.getId())){
                MyActivity.log("Liked item found!");
                lista.get(i).setLiked(data.getLiked());
                lista.get(i).setLikesCount(data.getLikesCount());
                MyActivity.log("Refresh adapter at "+i+" position with "+data.getLikesCount()+" likes");
                //adapter.notifyItemChanged(i);
                adapter.refresh(i, lista.get(i));
            }
        }
    }

    void subscribe(){
        final String broad = "forum"+forum.getId();
        Completion completion = new Completion() {
            @Override
            public void finish(Object s) {
                if (isInForegroundMode){
                    super.finish(s);
                    if(!subscribed){
                        OneSignal.sendTag(broad,broad);
                        follow.setText(getText(R.string.unfollow));
                    }else {
                        OneSignal.deleteTag(broad);
                        follow.setText(getText(R.string.follow));
                    }
                    subscribed = !subscribed;
                }
            }
        };
        BroadUtils.follow(getContext(), !subscribed, broad,broad, completion);
    }
    void getSubscriptionTags(){
        OneSignal.getTags(new OneSignal.GetTagsHandler() {
            @Override
            public void tagsAvailable(final JSONObject tags) {
                if(isInForegroundMode && tags != null ){//&& getActivity()!=null
                    MyActivity.log( "tagsAvailable: "+tags.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Utilities.keys(tags).contains("forum"+forum.getId())){
                                subscribed = true;
                                if(follow!=null) follow.setText(getText(R.string.unfollow));
                            }else {
                                subscribed = false;
                                if(follow!=null) follow.setText(getText(R.string.follow));
                            }
                        }
                    });
                }
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics, container, false);
        pd = ProgressUtils.getProgressDialog(getContext());
        recyclerView = view.findViewById(R.id.list);
        follow = view.findViewById(R.id.follow);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribe();
            }
        });
        FloatingActionButton newTopic = view.findViewById(R.id.newTopic);
        if (forum!=null && forum.getFollowersCount()>0) {
            //TextView flws = view.findViewById(R.id.followersCount);
            ((TextView) view.findViewById(R.id.followersCount)).setText(forum.getFollowersCount()+" "+getString(R.string.followers));
        }
        newTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(forum!=null){
                    NewTopicFragment f = new NewTopicFragment();
                    f.forum = forum;
                    ((MyActivity) getActivity()).setFragment(f);
                }else{
                    Toast.makeText(getActivity(), getString(R.string.choose_forum_first), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), ForumsActivity.class));
                }
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        endlessListener = new EndlessListener(0, PostsAdapter.ADS_AFTER, new EndlessListener.Action() {
            @Override
            public void getOnScroll() {
                MyActivity.log("get in scroll");
                getData();
            }
        });
        recyclerView.addOnScrollListener(endlessListener);


        adapter = new PostsAdapter(lista, new OnInteractListener<Post>() {

            @Override
            public void onInteract(Post item, @NotNull Action action) {
                switch (action){
                    case LIKE: {
                        MyActivity.log("like post from fragment");
                        mViewModel.like(item.getId());
                    } break;
                    case COMMENT: {
                        MyActivity.log("comment post from fragment");
                        CommentsActivity.title = item.getTitle();
                        CommentsActivity.type = ModelType.POST;
                        CommentsActivity.item_id = item.getId();
                        startActivity(new Intent(getActivity(), CommentsActivity.class));
                        //mViewModel.comment(item)
                    }break;
                    case SHARE: {
                        MyActivity.log("Share post from fragment");
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "ekhdemni.net");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, item.getTitle());
                        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_via)));
                        //mViewModel.share(item)
                    }break;
                }
            }

            @Override
            public void onClick(Post item) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }

        });
        recyclerView.setAdapter(adapter);

        if(forum==null)
            forum = (Forum) getArgs().getSerializable(Const.ITEM);
        if(forum!=null){
            getSubscriptionTags();
            if(forum.getCanPost()==0){
                newTopic.setVisibility(View.GONE);
            }
            ImageView logo = view.findViewById(R.id.logo);
            TextView name = view.findViewById(R.id.name);
            TextView description = view.findViewById(R.id.description);
            ImageHelper.load(logo, forum.getImage(), 100,100);
            name.setText(forum.getName());
            description.setText(forum.getDescription());
            view.findViewById(R.id.creator).setVisibility(View.VISIBLE);
        }else{
            view.findViewById(R.id.creator).setVisibility(View.GONE);
        }

        empty_view = view.findViewById(R.id.empty_view);

        return view;
    }
}
