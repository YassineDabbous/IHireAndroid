package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.domain.usecase.UCUsers;
import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.requests.Searcher;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.model.models.user.User;

public class VMUsers extends MyViewModel<PagingResponse<User>> {

    public void init(int page) {
        new UCUsers().list(null, page, getClosure());
    }


    public void search(Searcher searcher, int page) {
        new UCUsers().list(searcher, page, getClosure());
    }


}