package net.ekhdemni.domain.usecase

import tn.core.domain.Failure
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import net.ekhdemni.model.models.Work

class UCWorks : UseCase() {

    fun one(id:Int, closure: Closure<Work>) {
        api.getWork(id).enqueue(object :MyCallBack<BaseResponse<Work>>(){
            override fun onSuccess(response: BaseResponse<Work>) {
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

    fun getWorks(category: Int, uid: Int, page:Int, closure: Closure<PagingResponse<Work>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<Work>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Work>>) {
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
        if (uid==0)api.getWorks(page).enqueue(c)
        else api.getCategoryWorks(category, page).enqueue(c)
    }

}
