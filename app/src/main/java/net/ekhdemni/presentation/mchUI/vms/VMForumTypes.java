package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.domain.usecase.UCForums;
import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.Forum;

import java.util.List;

public class VMForumTypes extends MyViewModel<List<Forum>> {


    public void init() {
        new UCForums().getForumsTypes(getClosure());
    }


}