package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.Broadcast;
import net.ekhdemni.model.models.requests.RegisterRequest;
import net.ekhdemni.model.models.responses.AuthResponse;
import tn.core.model.responses.BaseResponse;
import net.ekhdemni.domain.usecase.UCAuth;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class VMAuth extends MyViewModel<BaseResponse<AuthResponse>> {

    UCAuth uc = new UCAuth();
    public MutableLiveData<List<Broadcast>> broadcasts = new MutableLiveData();
    public MutableLiveData<BaseResponse<Object>> recover = new MutableLiveData();

    public void login(String email, String password) {
        uc.login(email, password, getClosure());
    }


    public void register(String name, String email, String password, Integer accounttype, Integer country, Integer category, Integer specality) {
        uc.register(new RegisterRequest(name, email, password, accounttype, country, category, specality), getClosure());
    }

    public void recover(String email) {
        uc.recover(email, getGenericClosure(recover));
    }

    public void broadcasts() {
        uc.broadcasts(getGenericClosure(broadcasts));
    }


}