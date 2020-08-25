package net.ekhdemni.presentation.mchUI.vms

import androidx.lifecycle.MutableLiveData
import net.ekhdemni.domain.usecase.UCGeneral
import net.ekhdemni.model.configs.ConfigsResponse
import net.ekhdemni.model.models.responses.NewDataResponse
import net.ekhdemni.presentation.base.MyViewModel

class VMActivity : MyViewModel<ConfigsResponse>() {

    fun configs() {
        UCGeneral().configs(closure)
    }


    var newData = MutableLiveData<NewDataResponse>()
    fun newData() {
        UCGeneral().newData(getGenericClosure(newData))
    }

}
