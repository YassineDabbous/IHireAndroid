package net.ekhdemni.domain.paging;


import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Conversation;
import net.ekhdemni.model.RestAPI;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class DataSourceFactory<T> extends DataSource.Factory<Integer, T> {

    private MutableLiveData<DataSourceClass> liveData;
    private RestAPI repository;

    public DataSourceFactory(RestAPI repository) {
        MyActivity.log("☺ DataSourceFactory");
        this.repository = repository;
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataSourceClass> getMutableLiveData() {
        return liveData;
    }

    @Override
    public DataSource<Integer, T> create() {
        MyActivity.log("☺ DataSource create");
        DataSourceClass dataSourceClass = new DataSourceClass(repository, new Conversation(0).getClass());
        liveData.postValue(dataSourceClass);
        return dataSourceClass;
    }
}