package net.ekhdemni.domain.usecase

import net.ekhdemni.model.ModelType
import tn.core.domain.Failure
import net.ekhdemni.model.models.*
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import tn.core.util.Const
import retrofit2.Call

class UCGeneric : UseCase() {

    /*fun customIdeas(path: String, page:Int, closure: Closure<List<Idea>>) = generic2<Idea>("ideas", path, page, closure)
    fun customWorks(path: String, page:Int, closure: Closure<List<Work>>) = generic2<Work>("works", path, page, closure)
    fun customJobs(path: String, page:Int, closure: Closure<List<Job>>) = generic2<Job>("jobs", path, page, closure)
    fun customForums(path: String, page:Int, closure: Closure<List<Forum>>) = generic2<Forum>("forums", path, page, closure)
    fun customPosts(path: String, page:Int, closure: Closure<List<Post>>) = generic2<Post>("posts", path, page, closure)
    fun customUsers(path: String, page:Int, closure: Closure<List<User>>) = generic2<User>("users", path, page, closure)
*/

    fun <T:Commun> generic(type: Int, path: String, page:Int, closure: Closure<List<T>>){
        val c:MyCallBack<BaseResponse<PagingResponse<T>>>  = object: MyCallBack<BaseResponse<PagingResponse<T>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<T>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        getCall<T>(type, path, page).enqueue((c) );
    }

    fun <T:Commun> getCall(type: Int, path: String, page:Int): Call<BaseResponse<PagingResponse<T>>>{
        when (type){
            ModelType.FORUM -> return getApi().customForums(path, page) as Call<BaseResponse<PagingResponse<T>>>
            ModelType.POST -> return getApi().customPosts(path, page) as Call<BaseResponse<PagingResponse<T>>>
            ModelType.WORK -> return getApi().customWorks(path, page) as Call<BaseResponse<PagingResponse<T>>>
            ModelType.JOB -> return getApi().customJobs(path, page) as Call<BaseResponse<PagingResponse<T>>>
            ModelType.USER -> return getApi().customUsers(path, page) as Call<BaseResponse<PagingResponse<T>>>
            ModelType.IDEA -> return getApi().customIdeas(path, page) as Call<BaseResponse<PagingResponse<T>>>
            else -> return getApi().customIdeas(path, page) as Call<BaseResponse<PagingResponse<T>>>
        }
    }



    fun <T> genericX(path: String, page:Int, closure: Closure<List<T>>){
        getApi().customation<T>(path, page).enqueue(object: MyCallBack<BaseResponse<PagingResponse<T>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<T>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }

}
