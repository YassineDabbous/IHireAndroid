package net.ekhdemni.presentation.mchUI.vms;


import net.ekhdemni.domain.usecase.UCLike;
import net.ekhdemni.domain.usecase.UCPosts;
import net.ekhdemni.model.models.responses.LikeResponse;
import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.Post;
import tn.core.model.responses.PagingResponse;

import androidx.lifecycle.MutableLiveData;

public class VMPosts extends MyViewModel<PagingResponse<Post>> {
    public MutableLiveData<LikeResponse> like = new MutableLiveData<>();
    public void like(int id) {
        new UCLike().likePost(id, getGenericClosure(like));
    }

    public void init(int id, int page) {
        new UCPosts().getPosts(id, 0, page, getClosure());
    }

    public void getUserPosts(int id, int page) {
        new UCPosts().getPosts(0, id, page, getClosure());
    }

}