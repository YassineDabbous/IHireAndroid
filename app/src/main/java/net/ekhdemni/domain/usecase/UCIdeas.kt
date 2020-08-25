package net.ekhdemni.domain.usecase

import tn.core.domain.Failure
import net.ekhdemni.model.models.Idea
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCIdeas : UseCase() {


    fun one(id:Int, closure: Closure<Idea>) {
        api.getIdea(id).enqueue(object :MyCallBack<BaseResponse<Idea>>(){
            override fun onSuccess(response: BaseResponse<Idea>) {
                super.onSuccess(response)
                //MyActivity.log("Ideas count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }


    fun getIdeas(uid: Int, page:Int, closure: Closure<PagingResponse<Idea>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<Idea>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Idea>>) {
                super.onSuccess(response)
                //MyActivity.log("Ideas count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        if (uid==0)api.getIdeas(page).enqueue(c)
        else api.getUserIdeas(uid, page).enqueue(c)
    }

}
