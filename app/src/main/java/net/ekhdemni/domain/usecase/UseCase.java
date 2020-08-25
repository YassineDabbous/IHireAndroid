package net.ekhdemni.domain.usecase;

import net.ekhdemni.MyApplication;
import net.ekhdemni.model.RestAPI;

public class UseCase {

    public RestAPI getApi() {
        return MyApplication.getInstance().getRestAPI();
    }

}
