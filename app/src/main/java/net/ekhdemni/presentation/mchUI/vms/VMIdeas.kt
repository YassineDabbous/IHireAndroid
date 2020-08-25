package net.ekhdemni.presentation.mchUI.vms


import androidx.lifecycle.MutableLiveData
import net.ekhdemni.domain.usecase.UCIdeas
import net.ekhdemni.domain.usecase.UCLike
import net.ekhdemni.domain.usecase.UCReport
import net.ekhdemni.presentation.base.MyViewModel
import net.ekhdemni.model.models.Idea
import net.ekhdemni.model.models.responses.LikeResponse
import tn.core.model.responses.PagingResponse

class VMIdeas : MyViewModel<Idea>() {


    var ideas : MutableLiveData<PagingResponse<Idea>> = MutableLiveData();
    var report : MutableLiveData<Any> = MutableLiveData();
    var like = MutableLiveData<LikeResponse>()

    fun one(id:Int) {
        UCIdeas().one(id, closure)
    }
    fun list(uid:Int, page:Int) {
        UCIdeas().getIdeas(uid, page, getGenericClosure(ideas))
    }


    fun like(id: Int) {
        UCLike().likeIdea(id, getGenericClosure(like))
    }
    fun report(id:Int, txt:String){
        UCReport().reportIdea(id, txt, getGenericClosure(report))
    }
}
