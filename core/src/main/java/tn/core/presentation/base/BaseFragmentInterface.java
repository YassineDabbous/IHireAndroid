package tn.core.presentation.base;

import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;


/**
 * Created by X on 8/13/2018.
 */

public interface BaseFragmentInterface extends ComponentCallbacks2 {

    boolean isInForegroundMode = false;
    ProgressDialog pd= null;
    View empty_view = null;
    //boolean isLoading = false;

    void dismissPD();
    void showPD();

    void swipeRefresh();

    void showOptions();

    void getData();

    enum Destination{
        AUTH
    }
    /*void goTo(Destination destination){
        switch (destination){
            case AUTH: ((BaseActivity2)getActivity()).setFragment();
        }
    }*/

    /**
     * Could handle back press.
     * @return true if back press was handled
     */
    boolean onBackPressed();


    Bundle getArgs();



    void onError(List<String> errors);


    void onStatusChanged(Boolean b);



}
