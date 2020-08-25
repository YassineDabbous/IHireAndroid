package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.domain.usecase.UCJobs;
import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.Job;

public class VMJob extends MyViewModel<Job> {


    public void init(int id) {
        new UCJobs().one(id, getClosure());
    }
}