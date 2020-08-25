package net.ekhdemni.domain.paging;

import android.annotation.SuppressLint;

import tn.core.domain.Failure;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Conversation;
import tn.core.model.responses.BaseResponse;
import tn.core.model.responses.PagingResponse;
import tn.core.model.net.Constant;
import net.ekhdemni.model.RestAPI;
import tn.core.model.net.custom.MyCallBack;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

public class DataSourceClass<T> extends PageKeyedDataSource<Integer, T> {

    private RestAPI repository;
    private MutableLiveData<String> progressLiveStatus;
    Class<T> type;
    public DataSourceClass(RestAPI repository, Class<T> o) { type = o;
        this.repository = repository;
        progressLiveStatus = new MutableLiveData<>();
    }


    public MutableLiveData<String> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, T> callback) {
        // Initial page
        MyActivity.log("☺ loadInitial");
        final int page = 1;
        progressLiveStatus.postValue(Constant.LOADING);

        // TODO  do call switch the generic type <------------------

        MyActivity.log("get data for "+type.getName());
        if ( type.isInstance(new Conversation(0)) ) { //Instance of conversation
            MyActivity.log("get Conversations Call ");
            //....
        }

        repository.getConversations(page).enqueue(new MyCallBack<BaseResponse<PagingResponse<Conversation>>>() {
            @Override
            public void onSuccess(BaseResponse<PagingResponse<Conversation>> response) {
                super.onSuccess(response);
                progressLiveStatus.postValue(Constant.LOADED);
                List<T> items = (List<T>) response.getData().getData();
                // Result can be passed asynchronously
                callback.onResult(
                        items, // List of data items
                        0, // Position of first item
                        response.getData().getTotal(), // Total number of items that can be fetched from api
                        null, // Previous page. `null` if there's no previous page
                        page + 1 // Next Page (Used at the next request). Return `null` if this is the last page.
                );
            }


            @Override
            public void onError(Failure failure) {
                super.onError(failure);
                progressLiveStatus.postValue(Constant.LOADED);
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, T> callback) {

    }

    @SuppressLint("CheckResult")
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, T> callback) {
        MyActivity.log("☺ loadAfter "+params.key);
        progressLiveStatus.postValue(Constant.LOADING);
        // Next page.
        final int page = params.key;
        repository.getConversations(page).enqueue(new MyCallBack<BaseResponse<PagingResponse<Conversation>>>() {
            @Override
            public void onSuccess(BaseResponse<PagingResponse<Conversation>> response) {
                super.onSuccess(response);
                progressLiveStatus.postValue(Constant.LOADED);
                // Result can be passed asynchronously
                callback.onResult(
                        (List<T>) response.getData().getData(), // List of data items
                        // Next Page key (Used at the next request). Return `null` if this is the last page.
                        page + 1
                );
            }

            @Override
            public void onError(Failure failure) {
                super.onError(failure);
                progressLiveStatus.postValue(Constant.LOADED);
            }

        });
    }
}