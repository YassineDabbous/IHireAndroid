package net.ekhdemni.domain.usecase

import tn.core.domain.Failure
import net.ekhdemni.model.models.*
import net.ekhdemni.model.configs.ConfigsResponse
import net.ekhdemni.model.models.responses.NewDataResponse
import tn.core.model.responses.BaseResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import net.ekhdemni.presentation.base.MyActivity
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager

class UCGeneral : UseCase() {


    fun configs(closure: Closure<ConfigsResponse>){
        var lastTime = 0
        if (YDUserManager.configs()!=null) lastTime = YDUserManager.configs().time
        getApi().configs(lastTime).enqueue(object: MyCallBack<BaseResponse<ConfigsResponse>>(){
            override fun onSuccess(response: BaseResponse<ConfigsResponse>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }
    fun newData(closure: Closure<NewDataResponse>){
        getApi().newData().enqueue(object: MyCallBack<BaseResponse<NewDataResponse>>(){
            override fun onSuccess(response: BaseResponse<NewDataResponse>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }

    fun experiences(id:Int, closure: Closure<List<Experience>>){
        getApi().experiences(id).enqueue(object: MyCallBack<BaseResponse<List<Experience>>>(){
            override fun onSuccess(response: BaseResponse<List<Experience>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }

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
        })
    }


    fun resources(closure: Closure<List<Resource>>){
        getApi().resources().enqueue(object: MyCallBack<BaseResponse<List<Resource>>>(){
            override fun onSuccess(response: BaseResponse<List<Resource>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }

    fun services(id:Int, closure: Closure<List<Service>>){
        getApi().services(id).enqueue(object: MyCallBack<BaseResponse<List<Service>>>(){
            override fun onSuccess(response: BaseResponse<List<Service>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }


    fun apps(closure: Closure<List<App>>){
        getApi().apps().enqueue(object: MyCallBack<BaseResponse<List<App>>>(){
            override fun onSuccess(response: BaseResponse<List<App>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }

    fun categories(id:Int, closure: Closure<List<Category>>){
        val c:MyCallBack<BaseResponse<List<Category>>> = object: MyCallBack<BaseResponse<List<Category>>>(){
            override fun onSuccess(response: BaseResponse<List<Category>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        if (id == 0)
            getApi().categories().enqueue(c)
        else
            getApi().getCategories(id).enqueue(c)
    }

    fun countries(closure: Closure<List<Country>>){
        getApi().countries().enqueue(object: MyCallBack<BaseResponse<List<Country>>>(){
            override fun onSuccess(response: BaseResponse<List<Country>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }


    fun notifications(closure: Closure<List<Notification>>){
        getApi().notifications().enqueue(object: MyCallBack<BaseResponse<List<Notification>>>(){
            override fun onSuccess(response: BaseResponse<List<Notification>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }


    fun relations(uid:Int, closure: Closure<List<Relation>>){
        val c:MyCallBack<BaseResponse<List<Relation>>> = object: MyCallBack<BaseResponse<List<Relation>>>(){
            override fun onSuccess(response: BaseResponse<List<Relation>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        if (uid == 0)
            getApi().requests().enqueue(c)
        else
            getApi().relationsFor(uid).enqueue(c)
    }
    fun relationsAccept(uid:Int, closure: Closure<Relation>){
        getApi().relationsChange(2, uid).enqueue(object : MyCallBack<BaseResponse<Relation>>(){
            override fun onSuccess(response: BaseResponse<Relation>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }
    fun relationsRefuse(uid:Int, closure: Closure<Relation>){
        getApi().relationsChange(0, uid).enqueue(object : MyCallBack<BaseResponse<Relation>>(){
            override fun onSuccess(response: BaseResponse<Relation>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }




    fun alerts(closure: Closure<List<Alert>>){
        getApi().alerts().enqueue(object: MyCallBack<BaseResponse<List<Alert>>>(){
            override fun onSuccess(response: BaseResponse<List<Alert>>?) {
                super.onSuccess(response)
                MyActivity.log("uc received alerts" + response?.data?.size)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }
}
