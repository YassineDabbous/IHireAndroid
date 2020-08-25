package net.ekhdemni.domain.usecase

import net.ekhdemni.model.ModelType
import tn.core.domain.Failure
import net.ekhdemni.presentation.base.MyActivity
import net.ekhdemni.model.models.Comment
import net.ekhdemni.model.models.requests.CommentGetter
import net.ekhdemni.model.models.requests.CommentSetter
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCComments : UseCase() {

    fun getCommentsPost(id: Int, page:Int, closure: Closure<PagingResponse<Comment>>) = getComments(id, ModelType.POST,"",page, closure)
    //fun getCommentsWork(id: Int, closure: Closure<PagingResponse<Comment>>) = getComments(id, 4, closure)
    //fun getCommentsIdea(id: Int, closure: Closure<PagingResponse<Comment>>) = getComments(id, 5, closure)

    fun getComments(id: Int, type: Int, link:String, page:Int, closure: Closure<PagingResponse<Comment>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<Comment>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Comment>>) {
                super.onSuccess(response)
                //MyActivity.log("comments count " + response.data?.data?.size)
                closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        if (type!=3)api.getComments(type.toString(), id.toString(), page).enqueue(c)
        else api.getLinkComments(CommentGetter(link), page).enqueue(c)
    }

    fun pushPostComment(id:Int, commentTxt:String, closure: Closure<List<Comment>>) = pushComment(id, ModelType.POST, "", commentTxt, closure)
    fun pushComment(id:Int, type:Int, link:String, commentTxt:String, closure: Closure<List<Comment>>){
        MyActivity.log("pushComment: $commentTxt")
        getApi().commentsPush(CommentSetter(type, id, link, commentTxt)).enqueue(object: MyCallBack<BaseResponse<List<Comment>>>(){
            override fun onSuccess(response: BaseResponse<List<Comment>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }
}
