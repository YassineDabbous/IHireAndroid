package tn.core.model.net.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class RetrofitCallAdapterFactory extends CallAdapter.Factory {

    private static RetrofitCallAdapterFactory instance = null;
    private final Executor executor = new MainThreadExecutor();

    private RetrofitCallAdapterFactory() {

    }

    public static synchronized RetrofitCallAdapterFactory getInstance() {
        if (instance == null) {
            instance = new RetrofitCallAdapterFactory();
        }
        return instance;
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        if (rawType == RetrofitCall.class && returnType instanceof ParameterizedType) {
            Type callReturnType = getParameterUpperBound(0, (ParameterizedType) returnType);
            return new RetrofitCallAdapter(callReturnType, executor);
        }
        return null;
    }
}