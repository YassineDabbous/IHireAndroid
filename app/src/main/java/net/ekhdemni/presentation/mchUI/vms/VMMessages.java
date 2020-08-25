package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.Message;
import tn.core.model.responses.PagingResponse;
import net.ekhdemni.domain.usecase.UCMessages;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class VMMessages extends MyViewModel<PagingResponse<Message>> {

    public MutableLiveData<List<Message>> messages = new MutableLiveData<>();


    public void getMessages(int id, int uid, int page) {
        new UCMessages().getMessages(id,uid, page, getClosure());
    }


    public void pushMessage(int cid, int to, String msg) {
        new UCMessages().pushMessage(cid, to, msg, getGenericClosure(messages));
    }

}