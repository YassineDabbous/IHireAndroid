package tn.core.presentation.base;

import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.os.Bundle;

import android.widget.TextView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Created by X on 8/13/2018.
 */

public class MyFragment<VM> extends Fragment implements ComponentCallbacks2 {

    public boolean isInForegroundMode;
    public VM mViewModel;
    public ProgressDialog pd;
    public TextView empty_view;

    @Override
    public void onTrimMemory(int i) {
        clean();
    }

    public void getData(){

    }


    public Bundle getArgs() {
        Bundle bundle = getArguments();
        if (bundle==null)
            return new Bundle();
        return bundle;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BaseActivity.log("onDestroyView ===============> "+ getClass().getName());
        //clean();
    }

    @Override
    public void onPause() {
        super.onPause();
        isInForegroundMode = false;
        BaseActivity.log("onPause ===============> "+ getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseActivity.log("onDestroy ===============> "+ getClass().getName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseActivity.log("onCreate ===============> "+ getClass().getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        isInForegroundMode = true;
        BaseActivity.log("onResume ===============> "+ getClass().getName());
        ((BaseActivity)getActivity()).currentFragment = this;
        if(getView()!=null)
            init();
    }

    public void init(){
        BaseActivity.log("☺☺☺ INIT views for "+ getClass().getName());
        //if(getActivity()!=null && ((BaseActivity) getActivity()).currentFragment != null)
         //   BaseActivity.log("i'm: "+getClass().getName()+" and current fragment is:" +((BaseActivity)getActivity()).currentFragment.getClass().getName());
    }
    public void clean(){
        BaseActivity.log("setUserVisibleHint cleanupResources for "+ getClass().getName());
        System.gc();
    }




    public void onError(List<String> errors){

    }
    public void onStatusChanged(Boolean b){
        BaseActivity.log("Status changed to ("+b+"), is pd ready? "+ (pd!=null));
        if(b && pd!=null) pd.show();
        else if(pd!=null) pd.dismiss();
    }

}
