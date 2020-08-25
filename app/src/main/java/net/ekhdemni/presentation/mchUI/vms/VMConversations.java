package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Conversation;
import net.ekhdemni.domain.paging.DataSourceFactory;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class VMConversations extends MyViewModel {

    public DataSourceFactory<Conversation> dataSource;
    public LiveData<PagedList<Conversation>> listLiveData;

    public void init() {
        dataSource = new DataSourceFactory<>(getApi());
        initializePaging();
    }


    private void initializePaging() {
        MyActivity.log("☺ initializePaging");
//        progressLoadStatus = dataSource.getMutableLiveData().getValue().getProgressLiveStatus();

        // Configure paging
        PagedList.Config config = new PagedList.Config.Builder()
                // Number of items to fetch at once. [Required]
                .setPageSize(10)
                // Number of items to fetch on initial load. Should be greater than Page size. [Optional]
                .setInitialLoadSizeHint(10 * 2)
                .setEnablePlaceholders(true) // Show empty views until data is available
                .build();

        // Build PagedList
        listLiveData = new LivePagedListBuilder<>(dataSource, config).build();


    }

    /*
    public MutableLiveData<PagingResponse<Conversation>> items = new MutableLiveData<>();
    public void loadConversations(){
        api.getConversations(1).enqueue(new Callback<PagingResponse<Conversation>>() {
            @Override
            public void onResponse(Call<PagingResponse<Conversation>> call, Response<PagingResponse<Conversation>> response) {
                if(response.isSuccessful()) {
                    Log.e("DATA", "total "+response.body().getData().size());
                    //List<Conversation> prdcts = response.body().getData();
                    items.postValue(response.body());
                } else {
                    Log.e("DATA", "isUnSuccessful ☻");
                }
            }

            @Override
            public void onFailure(Call<PagingResponse<Conversation>> call, Throwable t) {
                Log.e("DATA", "onFailure ☻"+t.getMessage());
            }
        });
    }
*/

}