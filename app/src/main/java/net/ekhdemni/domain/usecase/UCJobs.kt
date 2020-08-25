package net.ekhdemni.domain.usecase

import tn.core.domain.Failure
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import net.ekhdemni.model.models.Job
import net.ekhdemni.model.models.requests.Searcher

class UCJobs : UseCase() {

    fun one(id:Int, closure: Closure<Job>) {
        api.getJob(id).enqueue(object :MyCallBack<BaseResponse<Job>>(){
            override fun onSuccess(response: BaseResponse<Job>) {
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

    fun getJobs(category: Int, uid: Int, page:Int, searcher:Searcher?, closure: Closure<PagingResponse<Job>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<Job>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Job>>) {
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
        if (searcher!=null) api.searchJobs(searcher, page).enqueue(c)
        else if (uid==0)api.getJobs(page).enqueue(c)
        else api.getCategoryJobs(category, page).enqueue(c)
    }

}
