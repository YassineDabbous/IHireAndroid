package net.ekhdemni.domain.usecase

import tn.core.domain.Failure
import net.ekhdemni.presentation.base.MyActivity
import net.ekhdemni.model.models.Message
import net.ekhdemni.model.models.requests.MessageSetter
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCMessages : UseCase() {
    fun getMessages(id: Int, uid: Int, page:Int, closure: Closure<PagingResponse<Message>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<Message>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Message>>) {
                super.onSuccess(response)
                //MyActivity.log("Messages count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        if (id==0)api.getMessagesWithUser(uid, page).enqueue(c)
        else api.getMessages(id, page).enqueue(c)
    }

    fun pushMessage(cid:Int, to:Int, msg:String, closure: Closure<List<Message>>){
        MyActivity.log("pushMessage: $msg")
        getApi().pushMessage(MessageSetter(cid, to, msg)).enqueue(object: MyCallBack<BaseResponse<List<Message>>>(){
            override fun onSuccess(response: BaseResponse<List<Message>>?) {
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
