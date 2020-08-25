package tn.core.model.net.custom;

import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.CallAdapter;

public class RetrofitCallAdapter<T> implements CallAdapter<T, RetrofitCall<T>> {

    private final Type responseType;
    private final Executor executor;

    public RetrofitCallAdapter(Type responseType, Executor executor) {
        this.responseType = responseType;
        this.executor = executor;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public RetrofitCall<T> adapt(Call<T> call) {
        return new RetrofitCall<>(call, executor);
    }
}