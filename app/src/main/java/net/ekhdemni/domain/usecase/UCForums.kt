package net.ekhdemni.domain.usecase

import tn.core.domain.Failure
import net.ekhdemni.model.models.Forum
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCForums : UseCase() {

    fun getForums(id: Int, page:Int, closure: Closure<PagingResponse<Forum>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<Forum>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Forum>>) {
                super.onSuccess(response)
                //MyActivity.log("Forums count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        api.getForums(id, page).enqueue(c)
    }

    fun getForumsTypes(closure: Closure<List<Forum>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<Forum>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Forum>>) {
                super.onSuccess(response)
                //MyActivity.log("Forums count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(response.data.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        api.forumsTypes().enqueue(c)
    }

}
