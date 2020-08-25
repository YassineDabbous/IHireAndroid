package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.Comment;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.domain.usecase.UCComments;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class VMComments extends MyViewModel<PagingResponse<Comment>> {

    public MutableLiveData<List<Comment>> comments = new MutableLiveData<>();


    public void getComments(int id, int type, String link, int page) {
        new UCComments().getComments(id,type, link, page, getClosure());
    }


    public void pushComment(int id, int type, String link, String cmt) {
        new UCComments().pushComment(id, type, link, cmt, getGenericClosure(comments));
    }

}