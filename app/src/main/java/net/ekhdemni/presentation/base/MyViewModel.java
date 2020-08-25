package net.ekhdemni.presentation.base;


import android.widget.Toast;

import net.ekhdemni.MyApplication;
import net.ekhdemni.R;
import tn.core.domain.Failure;

import net.ekhdemni.model.RestAPI;
import net.ekhdemni.presentation.ui.activities.auth.LoginActivity;

import tn.core.presentation.base.BaseViewModel;

public class MyViewModel<T> extends BaseViewModel<RestAPI, T> {

    public RestAPI getApi() {
        return MyApplication.getInstance().getRestAPI();
    }
    public void handleError(Failure failure) {
        loadStatus.postValue(false);
        if (failure instanceof Failure.NeedAuthFailure){
            MyApplication.goTo(LoginActivity.class);
            Toast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getText(R.string.need_auth).toString(), Toast.LENGTH_SHORT).show();
        }else if (failure instanceof Failure.NetworkFailure){
            Toast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getText(R.string.check_network), Toast.LENGTH_SHORT).show();
        }
        else if (failure instanceof Failure.GeneralFailure)
            callErrors.postValue(((Failure.GeneralFailure) failure).getErrors());
    }

    @Override
    public void cancel() {

    }
}