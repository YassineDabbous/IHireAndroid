package net.ekhdemni.domain.usecase

import net.ekhdemni.model.ModelType
import tn.core.domain.Failure
import net.ekhdemni.presentation.base.MyActivity
import tn.core.model.responses.BaseResponse
import net.ekhdemni.model.models.responses.LikeResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCLike : UseCase() {

    fun likePost(id: Int, closure: Closure<LikeResponse>) = like(id, ModelType.POST, closure)
    fun likeWork(id: Int, closure: Closure<LikeResponse>) = like(id, ModelType.WORK, closure)
    fun likeIdea(id: Int, closure: Closure<LikeResponse>) = like(id, ModelType.IDEA, closure)
    fun likeComment(id: Int, closure: Closure<LikeResponse>) = like(id, ModelType.COMMENT, closure)

    fun like(id: Int, type: Int, closure: Closure<LikeResponse>) {
        api.like(id, type).enqueue(object : MyCallBack<BaseResponse<LikeResponse>>() {
            override fun onSuccess(response: BaseResponse<LikeResponse>) {
                super.onSuccess(response)
                MyActivity.log("likes count " + response.data.likesCount!!)
                closure.onSuccess(response.data)
            }


            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }
}
