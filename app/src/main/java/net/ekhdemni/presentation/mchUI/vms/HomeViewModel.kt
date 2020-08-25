package net.ekhdemni.presentation.mchUI.vms

import androidx.lifecycle.MutableLiveData
import net.ekhdemni.domain.usecase.UCHome
import net.ekhdemni.domain.usecase.UCLike
import net.ekhdemni.presentation.base.MyViewModel
import net.ekhdemni.presentation.base.MyActivity
import net.ekhdemni.model.models.Commun
import net.ekhdemni.model.models.Model
import net.ekhdemni.model.models.Post
import net.ekhdemni.model.models.Work
import net.ekhdemni.model.models.responses.LikeResponse

class HomeViewModel : MyViewModel<List<Commun>>() {

    fun init() {
        MyActivity.logHome("---------------------- REQUEST DATA FOR HOME -----------------------------")
        UCHome().home(closure)
    }


    var like = MutableLiveData<LikeResponse>()
    fun like(model: Model) {
        if (model is Post)
            UCLike().likePost(model.id, getGenericClosure(like))
        if (model is Work)
            UCLike().likeWork(model.id, getGenericClosure(like))
    }


}
