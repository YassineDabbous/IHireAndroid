package net.ekhdemni.domain.usecase

import tn.core.domain.Failure
import net.ekhdemni.presentation.base.MyActivity
import net.ekhdemni.model.models.Broadcast
import net.ekhdemni.model.models.requests.LoginRequest
import net.ekhdemni.model.models.requests.RegisterRequest
import net.ekhdemni.model.models.responses.AuthResponse
import tn.core.model.responses.BaseResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCAuth : UseCase() {

    fun broadcasts(closure: Closure<List<Broadcast>>){
        getApi().broadcasts().enqueue(object: MyCallBack<BaseResponse<List<Broadcast>>>(){
            override fun onSuccess(response: BaseResponse<List<Broadcast>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }

    fun recover(email:String, closure: Closure<BaseResponse<Any>>){
        MyActivity.log("recover: $email")
        getApi().recover(email).enqueue(object: MyCallBack<BaseResponse<Any>>(){
            override fun onSuccess(response: BaseResponse<Any>?) {
                super.onSuccess(response)
                closure.onSuccess(response)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }


    fun login(email:String, password:String, closure: Closure<BaseResponse<AuthResponse>>){
        MyActivity.log("login: $email")
        getApi().login(LoginRequest(email, password)).enqueue(object: MyCallBack<BaseResponse<AuthResponse>>(){
            override fun onSuccess(response: BaseResponse<AuthResponse>?) {
                super.onSuccess(response)
                closure.onSuccess(response)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }

    fun register(register:RegisterRequest, closure: Closure<BaseResponse<AuthResponse>>){
        MyActivity.log("login: {$register.email}")
        getApi().register(register).enqueue(object: MyCallBack<BaseResponse<AuthResponse>>(){
            override fun onSuccess(response: BaseResponse<AuthResponse>?) {
                super.onSuccess(response)
                closure.onSuccess(response)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }
}
