package net.ekhdemni.domain.usecase

import tn.core.domain.Failure
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import net.ekhdemni.model.models.requests.Searcher
import net.ekhdemni.model.models.user.User

class UCUsers : UseCase() {


    fun one(id:Int, closure: Closure<User>) {
        api.getUser(id).enqueue(object :MyCallBack<BaseResponse<User>>(){
            override fun onSuccess(response: BaseResponse<User>) {
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

    fun list(searcher: Searcher?, page:Int, closure: Closure<PagingResponse<User>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<User>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<User>>) {
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
        if (searcher==null)api.getUsers(page).enqueue(c)
        else api.searchUsers(searcher, page).enqueue(c)
    }


}
