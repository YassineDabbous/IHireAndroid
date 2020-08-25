package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.domain.usecase.UCLike;
import net.ekhdemni.domain.usecase.UCWorks;
import net.ekhdemni.model.models.responses.LikeResponse;
import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.Work;
import tn.core.model.responses.PagingResponse;

import androidx.lifecycle.MutableLiveData;

public class VMWorks extends MyViewModel<PagingResponse<Work>> {
    public MutableLiveData<LikeResponse> like = new MutableLiveData<>();
    public void like(int id) {
        new UCLike().likeWork(id, getGenericClosure(like));
    }

    public void init(int page) {
        new UCWorks().getWorks(0,0,page, getClosure());
    }


    public void search(int category, int page) {
        new UCWorks().getWorks(category,0, page, getClosure());
    }


}