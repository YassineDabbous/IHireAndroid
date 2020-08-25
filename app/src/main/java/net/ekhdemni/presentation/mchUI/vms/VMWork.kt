package net.ekhdemni.presentation.mchUI.vms

import net.ekhdemni.presentation.base.MyViewModel
import net.ekhdemni.model.models.Work
import net.ekhdemni.model.models.responses.LikeResponse

import androidx.lifecycle.MutableLiveData
import net.ekhdemni.domain.usecase.*


class VMWork : MyViewModel<Work>() {

    var like = MutableLiveData<LikeResponse>()

    fun init(id: Int) {
        UCWorks().one(id, closure)
    }


    fun like(id: Int) {
        UCLike().likeWork(id, getGenericClosure(like))
    }



}