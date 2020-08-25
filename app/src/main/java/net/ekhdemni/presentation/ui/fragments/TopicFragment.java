package net.ekhdemni.presentation.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import net.ekhdemni.R;
import net.ekhdemni.model.ModelType;
import net.ekhdemni.model.models.responses.LikeResponse;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.presentation.ui.activities.CommentsActivity;
import net.ekhdemni.presentation.ui.activities.ForumsActivity;
import net.ekhdemni.presentation.mchUI.adapters.CommentsAdapter;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyRecyclerFragment;
import net.ekhdemni.model.models.Comment;
import net.ekhdemni.model.models.Post;
import net.ekhdemni.presentation.mchUI.vms.VMPost;
import net.ekhdemni.utils.AlertUtils;
import net.ekhdemni.utils.ImageHelper;
import net.ekhdemni.utils.ProgressUtils;
import tn.core.util.TextDrawable;
import net.ekhdemni.utils.TextUtils;
import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TopicFragment extends MyRecyclerFragment<Comment, VMPost> {

    EditText commentEdit;
    ImageView like, send;
    TextView title,description, username, date, likesNbr;
    ImageView photo, user_photo;
    View loadMore, userPart, senderView, closedComments;

    CommentsAdapter adapter;
    LinearLayoutManager linear;
    private OnListFragmentInteractionListener listener;

    public Post post;
    public static String POST_ID = "POST_ID";


    public TopicFragment(){}
    public static TopicFragment newInstance(int pid) {
        Bundle args = new Bundle();
        args.putInt(POST_ID, pid);
        TopicFragment fragment = new TopicFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public void onDataReceived(PagingResponse<Comment> data) {
        onDataReceived(data.getData());
    }

    @Override
    public void onDataReceived(List<Comment> data) {
        super.onDataReceived(data);
        MyActivity.log("Comments count: data"+data.size());
        MyActivity.log("Comments count:"+lista.size());
        if(lista.size() > 0){
            if (total>lista.size()) loadMore.setVisibility(View.VISIBLE);
            Collections.reverse(lista);
            adapter.notifyDataSetChanged();
            adapter.areSolutions = true;
            adapter.solutionId = post.getSolution().toString();
            recyclerView.getLayoutManager().scrollToPosition(lista.size() - 1);
            commentEdit.setText("");
        }
    }

    public void onDataReceived(Post data){
        post = data;
        Log.wtf("ekhdemni.net","post:"+post.getTitle());
        mViewModel.getComments(post.getId());
        //getComments();
        bind();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMPost.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.getLiveData().observe(this, this::onDataReceived);
        mViewModel.like.observe(this, this::handleLike);
        mViewModel.comments.observe(this, this::onDataReceived);
        mViewModel.pagingComments.observe(this, this::onDataReceived);
        if(lista.size()==0){
            int id = getArgs().getInt(POST_ID);
            mViewModel.init(id);
        }
    }





/*
    @Override
    public void clean() {
        super.clean();
        title = description = username = date = likesNbr = null;
        loadMore = userPart = senderView = closedComments  = null;
        like = send = null;
        commentEdit = null;
        photo = user_photo = null;
        userPart = senderView = closedComments = null;
        linear = null;
        adapter =null;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);

        userPart = view.findViewById(R.id.userPart);
        loadMore = view.findViewById(R.id.loadMore);
        senderView = view.findViewById(R.id.senderView);
        closedComments = view.findViewById(R.id.closedComments);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        photo = view.findViewById(R.id.photo);
        likesNbr = view.findViewById(R.id.likesNbr);
        username  = view.findViewById(R.id.uid);
        user_photo = view.findViewById(R.id.poster_photo);
        date = view.findViewById(R.id.date);
        like = view.findViewById(R.id.like);
        send = view.findViewById(R.id.send);
        commentEdit = view.findViewById(R.id.comment);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentsActivity.title = post.getTitle();
                CommentsActivity.type = ModelType.POST;
                CommentsActivity.item_id = post.getId();
                getContext().startActivity(new Intent(getContext(), CommentsActivity.class));
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.wtf("click click click click ","send send send send send send ");
                if(commentEdit!=null){
                    String c = commentEdit.getText().toString();
                    if(!c.isEmpty()){
                        pushComment(c);
                    }
                }
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.wtf("click click click click ","like like like like like like like ");

                mViewModel.like(post.getId());
            }
        });
        userPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyActivity) getActivity()).setFragment(UsersShowFragment.newInstance(post.getUid()));
            }
        });

        recyclerView = view.findViewById(R.id.list);
        linear = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linear);
        adapter = new CommentsAdapter(lista, listener);
        recyclerView.setAdapter(adapter);

        pd = ProgressUtils.getProgressDialog(getContext());

        return view;
    }

    public void bind(){
        setLiker();
        if (post.getPhoto()!=null) {
            Log.e("EkhdemniEkhdemniEkh",post.getPhoto());
            ImageHelper.load(photo, post.getPhoto());
        }
        title.setText(post.getTitle());
        TextUtils.htmlToView(description, post.getDescription());
        likesNbr.setText(post.getLikesCount()+"");
        username.setText(post.getUname());
        date.setText(post.getTimeAgo());
        //String me = YDUserManager.get(getContext(), YDUserManager.ID_KEY);
        if(post.getCanComment()==0){ //((getActivity()instanceof ForumsActivity) && ((ForumsActivity) getActivity()).forum!=null && !((ForumsActivity) getActivity()).forum.canComment )
            senderView.setVisibility(View.GONE);
            closedComments.setVisibility(View.VISIBLE);
        }
        if(post.getUpicture().equals("")){
            TextDrawable drawable = TextDrawable.builder().buildRound(post.getUname().charAt(0)+"");
            user_photo.setImageDrawable(drawable);
        }else{
            ImageHelper.load(user_photo, post.getUpicture(), 40,40);
        }


        if(getActivity() instanceof ForumsActivity)
            ((ForumsActivity) getActivity()).setupMenu();
    }


    public void handleLike(LikeResponse response){
        post.setLikesCount(response.getLikesCount());
        post.setLiked(response.getLiked());
        pd.dismiss();
        setLiker();
    }

    public void setLiker(){
        likesNbr.setText(post.getLikesCount()+"");
        if (post.getLiked()) {
            like.setImageResource(R.drawable.liked);
        } else {
            like.setImageResource(R.drawable.like);
        }
    }
    public void pushComment(String commentTxt){
        mViewModel.pushComment(post.getId(), commentTxt);
    }


    int total = 0;


    public void deleteAlert(){
        AlertUtils.Action action = new AlertUtils.Action() {
            public void doFunction(Object o) {
                deleteMe();
            }
        };
        action.message = getContext().getText(R.string.delete).toString();
        AlertUtils.alert(getContext(), action);
    }

    public void deleteMe(){
        Log.wtf("ekhdemni.net","delete item "+post.getId());
        Action action = new Action(getContext()) {
            public void doFunction(String s) throws JSONException {
                JSONObject jsonObject = new JSONObject(s);
                String message = getResponseMessage(jsonObject);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                int code = jsonObject.getInt("code");
                if (code==0)
                getActivity().onBackPressed();
            }
        };
        action.method = action.DELETE;
        action.url = Ekhdemni.posts+"/"+post.getId();
        action.run();
    }



    ///////////////////////////////////////////////////

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            Log.e("ekhdemni.net", " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        if(getActivity() instanceof ForumsActivity)
            ((ForumsActivity) getActivity()).setupToolbar();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Comment item);
    }
}
