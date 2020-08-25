package tn.core.presentation.base;

import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;


/**
 * Created by X on 8/13/2018.
 */

public class BaseFragment<VM> extends Fragment implements BaseFragmentInterface {


    public boolean isInForegroundMode;
    public VM mViewModel;
    //public ProgressDialog pd;
    public View pd;
    public View empty_view;
    public boolean errorShow = true;

    public void dismissPD(){
        ((BaseActivity2) getActivity()).dismissPD();
        /*if (pd!=null){
            //pd.dismiss();
            //pd.cancel();
            //pd.hide();
            //pd.setVisibility(View.GONE);
            //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }*/
    }
    public void showPD(){
        ((BaseActivity2) getActivity()).showPD();
        /*if (pd!=null){
            //pd.show();
            //pd.setVisibility(View.VISIBLE);
            //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }*/
    }

    @Override
    public void swipeRefresh() {
        getData();
    }

    @Override
    public void getData() {

    }


    @Override
    public void showOptions() {

    }


    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
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
        ((BaseActivity2)getActivity()).currentFragment = this;

    }

    @Override
    public void onError(List<String> errors){

    }

    @Override
    public void onStatusChanged(Boolean b){
        BaseActivity.log("Status changed to ("+b+")");
        if(b) showPD();
        else dismissPD();
    }


    @Override
    public void onTrimMemory(int i) {

    }
}
