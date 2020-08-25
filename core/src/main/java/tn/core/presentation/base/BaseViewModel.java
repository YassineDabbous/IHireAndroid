package tn.core.presentation.base;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import tn.core.domain.Failure;
import tn.core.domain.base.Closure;

public abstract class BaseViewModel<Rest, T> extends ViewModel {

    public MutableLiveData<Boolean> loadStatus = new MutableLiveData<>();
    public MutableLiveData<List<String>> callErrors = new MutableLiveData<>();
    public MutableLiveData<T> liveData = new MutableLiveData<>();

    public LiveData<T> getLiveData() {
        return liveData;
    }
    public abstract Rest getApi();
    public abstract void cancel();


    public Closure<T> getClosure(){
        return getGenericClosure(liveData);
    }
    public void handleResponse(T t){
        handleGenericResponse(liveData, t);
    }
    public abstract void handleError(Failure failure);


    public <G> Closure<G> getGenericClosure(final MutableLiveData<G> mutable){
        loadStatus.postValue(true);
        return new Closure<G>() {
            @Override
            public void onSuccess(G response) {
                handleGenericResponse(mutable, response);
            }

            @Override
            public void onError(Failure failure) {
                handleError(failure);
            }
        };
    }


    public  <G> void handleGenericResponse(MutableLiveData<G> mutable, G t){
        loadStatus.postValue(false);
        mutable.postValue(t);
    }

    public abstract class ResponseClosure<T> implements Closure<T>{
        @Override
        public void onSuccess(T response) {
            loadStatus.postValue(false);
            onReceived(response);

        }
        public abstract void onReceived(T response);
        @Override
        public void onError(Failure failure) {
            handleError(failure);
        }
    }

}