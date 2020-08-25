package tn.core.model.net.custom;

import android.widget.Toast;

import com.google.gson.Gson;

import tn.core.domain.Failure;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;
import tn.core.model.responses.BaseResponse;
import tn.core.presentation.base.BaseActivity2;


public class MyCallBack<T extends BaseResponse> implements Callback<T> {

    public static BaseResponse responseToPojo(ResponseBody body){
        try {
            return (new Gson()).fromJson(body.charStream(),BaseResponse.class);
        }catch (Exception e){
            Timber.e("Error parsing to BaseResponse with gson :/ => %s", e.getMessage());
        }
        return new BaseResponse();
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()){
            onSuccess(call, response);
            T b = response.body();
            onSuccess(b);
            if (b.getMessages()!=null && b.getMessages().size()>0)
                onError(new Failure.JustMessages(b.getMessages()));
            if(b.getCode()!=null && b.getCode()==333 && b.getValidation()!=null){
                onError(new Failure.ValidationFailure(b.getValidation()));
            }
        } else if (response.code() == 401) {
            onError(new Failure.NeedAuthFailure());
        } else if (response.code() == 403) {
            onError(new Failure.PermissionDeniedFailure());
        }else{
            BaseResponse message = responseToPojo(response.errorBody());
            if (message!=null){
                if (message.getMessages()!=null && message.getMessages().size()>0)
                    onError(new Failure.JustMessages(message.getMessages()));
                if (message.getErrors()!=null && message.getErrors().size()>0){
                    logErrors(false, message.getErrors());
                    onError(new Failure.GeneralFailure(message.getErrors(),0));
                }
                if(message.getCode()!=null && message.getCode()==333 && message.getValidation()!=null){
                    onError(new Failure.ValidationFailure(message.getValidation()));
                }
            }else
                onError(new Failure.UnknownFailure());

            onError(call, new Throwable());
        }
    }


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        BaseActivity2.log(t.getMessage());
        if (t instanceof IOException) {
            onError(new Failure.NetworkFailure());
            //networkError();
        }
        else {
            // todo log to some central bug tracking service
            String requestTag = "";
            if (call instanceof RetrofitCall){
                RetrofitCall<T> retrofitCall = (RetrofitCall) call;
                requestTag = retrofitCall.getRequestTag();
                Timber.e(retrofitCall.getLogTag());
                Timber.e(retrofitCall.getRequestTag());
            }

            List<String> errs = new ArrayList<String>(Arrays.asList("conversion issue! big problems :(", t.getMessage()));
            //String[] errs = {, };//"Request '"+requestTag+"' Failed: %s"+t.getMessage(),  t.getMessage()
            logErrors(true, errs);
            onError(call, t);
            onError(new Failure.GsonParsingFailure());
        }
    }


    public void onSuccess(Call<T> call, Response<T> response){

    }
    public void onError(Call<T> call, Throwable t) {

    }


    public void onSuccess(T response){

    }


    public void logErrors(Boolean isFailed, List<String> errors) {
        if (errors!=null)
            for (String err: errors) {
                Timber.e("is request Failed? : "+isFailed+" |=> error: "+ err);
            }
    }

    /*public void onAlert(List<String> messages) {
        if (messages!=null)
            for (String msg: messages) {
                Timber.e("onAlert: "+ msg);
                //Toast.makeText(MyApplication.getInstance(),  msg, Toast.LENGTH_SHORT).show();
            }
    }*/


    public void onError(Failure failure) {
        if (failure instanceof Failure.GeneralFailure && ((Failure.GeneralFailure)failure).getErrors()!=null)
            for (String err: ((Failure.GeneralFailure)failure).getErrors()) {
                Timber.e(" |=> error: "+ err);
                //if(BuildConfig.DEBUG)
                //    Toast.makeText(MyApp.getInstance(),  err, Toast.LENGTH_SHORT).show();
            }
    }


}
