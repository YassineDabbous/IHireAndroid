package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.domain.usecase.UCGeneral;
import net.ekhdemni.domain.usecase.UCUsers;
import net.ekhdemni.model.models.Experience;
import net.ekhdemni.presentation.base.MyViewModel;
import net.ekhdemni.model.models.user.User;

import java.util.List;

import androidx.lifecycle.MutableLiveData;


public class VMUser extends MyViewModel<User> {

    //users+"/update/"+user.getId();
    public void init(int id) {
        new UCUsers().one(id, getClosure());
    }


    public MutableLiveData<List<Experience>> experiences = new MutableLiveData<>();
    public void experiences(int id) {
        new UCGeneral().experiences(id, this.getGenericClosure(experiences));
    }

}