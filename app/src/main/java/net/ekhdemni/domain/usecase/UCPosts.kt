package net.ekhdemni.domain.usecase

import tn.core.domain.Failure
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import net.ekhdemni.model.models.Post

class UCPosts : UseCase() {

    fun one(id:Int, closure: Closure<Post>) {
        api.getPost(id).enqueue(object :MyCallBack<BaseResponse<Post>>(){
            override fun onSuccess(response: BaseResponse<Post>) {
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

    fun getPosts(category: Int, uid: Int, page:Int, closure: Closure<PagingResponse<Post>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<Post>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Post>>) {
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
        if (uid==0)api.getPosts(category, page).enqueue(c)
        else api.getUserPosts(uid, page).enqueue(c)
    }

}
