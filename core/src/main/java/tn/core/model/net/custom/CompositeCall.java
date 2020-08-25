package tn.core.model.net.custom;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeCall implements RetrofitCall.CallListener {

    private static final String TAG = "CompositeCall";

    private List<RetrofitCall> list = new ArrayList<>();

    public void cancel(String requestTag) {
        Iterator<RetrofitCall> iterator = list.iterator();
        while (iterator.hasNext()) {
            RetrofitCall call = iterator.next();
            if (call.getRequestTag().equals(requestTag)) {
                call.cancel();
                Log.d(TAG, "cancel: The call with tag " + requestTag + " was cancelled.");
                iterator.remove();
            }
        }
    }

    public void cancelAll() {
        for (RetrofitCall call : list) {
            call.cancel();
        }

        list.clear();
    }

    public boolean isRunning(String requestTag) {
        if (TextUtils.isEmpty(requestTag)) {
            return false;
        }

        boolean isRunning = false;
        for (RetrofitCall call: list) {
            if (requestTag.equals(call.getRequestTag())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    @Override
    public void onFinished(RetrofitCall call) {
        call.detach();
        list.remove(call);
    }

    @Override
    public void onAdded(RetrofitCall call) {
        list.add(call);
    }
}