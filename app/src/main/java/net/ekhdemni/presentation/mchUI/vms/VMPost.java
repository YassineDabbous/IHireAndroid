package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.domain.usecase.UCPosts;
import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.Comment;
import net.ekhdemni.model.models.Post;
import net.ekhdemni.model.models.responses.LikeResponse;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.domain.usecase.UCComments;
import net.ekhdemni.domain.usecase.UCLike;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class VMPost extends MyViewModel<Post> {

    public MutableLiveData<List<Comment>> comments = new MutableLiveData<>();
    public MutableLiveData<PagingResponse<Comment>> pagingComments = new MutableLiveData<>();
    public MutableLiveData<LikeResponse> like = new MutableLiveData<>();

    public void init(int id) {
        new UCPosts().one(id, getClosure());
    }

    public void like(int id) {
        new UCLike().likePost(id, getGenericClosure(like));
    }
    public void getComments(int id) {
        new UCComments().getCommentsPost(id,1, getGenericClosure(pagingComments));
    }


    public void pushComment(int id, String cmt) {
        new UCComments().pushPostComment(id, cmt, getGenericClosure(comments));
    }

}