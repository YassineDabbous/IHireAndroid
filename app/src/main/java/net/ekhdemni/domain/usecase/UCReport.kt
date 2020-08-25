package net.ekhdemni.domain.usecase

import net.ekhdemni.model.ModelType
import tn.core.domain.Failure
import tn.core.model.responses.BaseResponse
import tn.core.model.net.custom.MyCallBack
import net.ekhdemni.model.models.requests.ReportRequest
import tn.core.domain.base.Closure

class UCReport : UseCase() {

    fun reportPost(id: Int, description:String, closure: Closure<Any>) = report(ReportRequest(ModelType.POST, id, description), closure)
    fun reportWork(id: Int, description:String, closure: Closure<Any>) = report(ReportRequest(ModelType.WORK, id, description), closure)
    fun reportIdea(id: Int, description:String, closure: Closure<Any>) = report(ReportRequest(ModelType.IDEA, id, description), closure)
    fun reportComment(id: Int, description:String, closure: Closure<Any>) = report(ReportRequest(ModelType.COMMENT, id, description), closure)

    fun report(r:ReportRequest, closure: Closure<Any>) {
        api.report(r).enqueue(object : MyCallBack<BaseResponse<Any>>() {
            override fun onSuccess(response: BaseResponse<Any>) {
                super.onSuccess(response)
                if(response.data!=null)
                closure.onSuccess(response.data)
            }


            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }
}
