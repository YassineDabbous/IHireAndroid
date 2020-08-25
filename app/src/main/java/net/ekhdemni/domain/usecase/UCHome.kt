package net.ekhdemni.domain.usecase

import tn.core.domain.Failure
import tn.core.model.responses.BaseResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import net.ekhdemni.model.models.Commun
import net.ekhdemni.model.models.responses.HomeResponse

class UCHome : UseCase() {

    fun home(closure: Closure<List<Commun>>) {
        api.paginateHome().enqueue(object :MyCallBack<BaseResponse<HomeResponse>>() {
            override fun onSuccess(response: BaseResponse<HomeResponse>) {
                super.onSuccess(response)

                var list: MutableList<Commun> = ArrayList()
                if (response.data.forks != null && response.data.forks.size > 0)
                    list.addAll(response.data.forks);
                if (response.data.ideas != null && response.data.ideas?.data?.size != 0)
                    list.addAll(response.data.ideas.data)
                if (response.data.forums != null && response.data.forums.data.size > 0)
                    list.addAll(response.data.forums.data)
                if (response.data.jobs != null && response.data.jobs.data.size > 0)
                    list.addAll(response.data.jobs.data);
                if (response.data.posts != null && response.data.posts.data.size > 0)
                    list.addAll(response.data.posts.data)
                if (response.data.works != null && response.data.works.data.size > 0)
                    list.addAll(response.data.works.data);
                if (response.data.users != null && response.data.users.data.size > 0)
                    list.addAll(response.data.users.data);

                /**
                if (response.users.data.size > 0)
                list.addAll(response.users.data);
                 **/
                val sortedList = list.sortedWith(compareBy({ it.modelPosition}, {it.createdAt }))

                closure.onSuccess(sortedList)


            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }

        })
    }
}
