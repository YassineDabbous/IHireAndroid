package tn.core.model.net.custom;



import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.Executor;

import androidx.annotation.WorkerThread;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCall<T> implements Call<T> {
    private Call<T> call;
    private Executor executor;
    private String logTag = RetrofitCall.class.getSimpleName();

    public String getLogTag() {
        return logTag;
    }

    public RetrofitCall setLogTag(String logTag) {
        this.logTag = logTag;
        return this;
    }

    public RetrofitCall(Call<T> call, Executor executor) {
        this.call = call;
        this.executor = executor;
    }

    @Override
    public Response<T> execute() throws IOException {
        return call.execute();
    }

    private int retryLimit = 2;
    private int retryCount = 0;

    @Override
    public void enqueue(final Callback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(final Call<T> call, final Response<T> response) {
                if (isCallSuccess(response) || retryCount >= retryLimit) {
                    handleSuccessResponse(call, response, callback);
                } else {
                    handleErrorResponse(call, new IOException("Call failed!"), callback);
                }
            }

            @Override
            public void onFailure(final Call<T> call, final Throwable t) {
                if (isCanceled()) {
                    // Do nothing.
                } else if (retryCount >= retryLimit) {
                    handleErrorResponse(call, t, callback);
                } else {
                    retryCount++;
                    retry(callback);
                }
            }
        });
    }

    private void retry(final Callback<T> callback) {
        this.call = call.clone();
        enqueue(callback);
    }

    @WorkerThread
    private void handleErrorResponse(final Call<T> call, final Throwable t, final Callback<T> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (null == call || call.isCanceled()) {
                    return;
                }
                callback.onFailure(call, t);
                notifyCallFinished();
            }
        });
    }

    @WorkerThread
    private void handleSuccessResponse(final Call<T> call, final Response<T> response, final Callback<T> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (null == call || call.isCanceled()) {
                    return;
                }
                callback.onResponse(call, response);
                notifyCallFinished();
            }
        });
    }

    @Override
    public boolean isExecuted() {
        return call.isExecuted();
    }

    @Override
    public void cancel() {
        call.cancel();
    }

    @Override
    public boolean isCanceled() {
        return call.isCanceled();
    }

    @Override
    public RetrofitCall<T> clone() {
        RetrofitCall<T> clone = new RetrofitCall<>(call.clone(), executor);
        clone.setRequestTag(requestTag);
        clone.setLogTag(logTag);
        return clone;
    }

    @Override
    public Request request() {
        return call.request();
    }

    private boolean isCallSuccess(Response<T> response) {
        int httpStatusCode = response.code();
        return (httpStatusCode >= HttpURLConnection.HTTP_OK && httpStatusCode < HttpURLConnection.HTTP_INTERNAL_ERROR);
    }











    private String requestTag = "";
    private CallListener callListener;

    public interface CallListener {
        void onFinished(RetrofitCall call);

        void onAdded(RetrofitCall call);
    }

    public String getRequestTag() {
        return requestTag;
    }

    public RetrofitCall<T> setRequestTag(String requestTag) {
        this.requestTag = requestTag;
        return this;
    }

    public RetrofitCall<T> attachTo(CallListener listener) {
        callListener = listener;
        if (callListener == null) {
        } else {
            callListener.onAdded(this);
        }
        return this;
    }

    public void detach() {
        callListener = null;
    }

    private void notifyCallFinished() {
        if (callListener != null) {
            callListener.onFinished(this);
        }
    }
}