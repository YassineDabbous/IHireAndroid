package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.Forum;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.domain.usecase.UCForums;

public class VMForums extends MyViewModel<PagingResponse<Forum>> {


    public void init(int id) {
        new UCForums().getForums(id, 1, getClosure());
    }


}