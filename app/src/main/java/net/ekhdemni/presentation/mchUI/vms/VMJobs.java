package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.domain.usecase.UCJobs;
import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.Job;
import net.ekhdemni.model.models.requests.Searcher;
import tn.core.model.responses.PagingResponse;

public class VMJobs extends MyViewModel<PagingResponse<Job>> {


    public void init(int page) {
        new UCJobs().getJobs(0,0 , page, null, getClosure());
    }

    public void getCategoryJobs(int category, int page) {
        new UCJobs().getJobs(category,0 , page, null, getClosure());
    }

    public void search(Searcher searcher, int page) {
        new UCJobs().getJobs(0,0 , page, searcher, getClosure());
    }

}