package net.ekhdemni.presentation.ui.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import net.ekhdemni.R

//import kotlinx.android.synthetic.main.fragment_home.*
import net.ekhdemni.presentation.base.MyActivity
import tn.core.presentation.base.MyRecyclerFragment
import net.ekhdemni.presentation.MultiItemsListener
import net.ekhdemni.model.models.*
import net.ekhdemni.model.models.responses.LikeResponse
import net.ekhdemni.model.models.user.User
import net.ekhdemni.presentation.ui.activities.JobDetailsActivity
import net.ekhdemni.presentation.mchUI.adapters.HomeAdapter
import net.ekhdemni.presentation.mchUI.vms.HomeViewModel
import tn.core.presentation.listeners.Action
import tn.core.util.Const

class HomeFragment : MyRecyclerFragment<Commun, HomeViewModel>() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    var retryBtn:Button? = null
    var emptyView:View? = null
    var cpd:ContentLoadingProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v:View = inflater.inflate(R.layout.fragment_home, container, false)
        cpd = v.findViewById(R.id.cpd)
        retryBtn = v.findViewById(R.id.retryBtn)
        emptyView = v.findViewById(R.id.emptyView)
        recyclerView = v.findViewById(R.id.recycler_view)
        val manager = LinearLayoutManager(context)
        adapter = HomeAdapter(lista, object : MultiItemsListener {
            override fun onInteract(item: Model?, action: Action) {
                when(action){
                    Action.LIKE -> mViewModel.like(item!!)
                }
            }

            override fun onClick(item: Job?) {
                println("selected Job ${item?.title}")
                val intent = Intent(getContext(), JobDetailsActivity::class.java)
                intent.putExtra(Const.ID, item?.id)
                startActivity(intent)
            }

            override fun onClick(item: Idea?) {
                println("selected Idea ${item?.name}")
                (activity as MyActivity).onItemSelected(item)
            }
            override fun onClick(item: Work?) {
                println("selected Work ${item?.title}")
                (activity as MyActivity).onItemSelected(item)
            }

            override fun onClick(item: Post?) {
                println("selected Post ${item?.title}")
                (activity as MyActivity).onItemSelected(item)
            }

            override fun onClick(item: Forum?) {
                println("selected Forum ${item?.name}")
                (activity as MyActivity).onItemSelected(item)
            }

            override fun onClick(item: User?) {
                println("selected User ${item?.name}")
                (activity as MyActivity).onItemSelected(item)
            }

            override fun onClick(item: Article?) {
                println("selected Article ${item?.title}")
                (activity as MyActivity).onItemSelected(item)
            }

            override fun onClick(item: Service?) {
                println("selected Service ${item?.title}")
                (activity as MyActivity).onItemSelected(item)
            }

            override fun onClick(item: Model?) {
                println("selected as Any! => ${item}")
                if(item is Idea)
                    onClick(item)
                else if(item is Job)
                    onClick(item)
                else if(item is Work)
                    onClick(item)
                else if(item is Post)
                    onClick(item)
                else if(item is Forum)
                    onClick(item)
                else if(item is User)
                    onClick(item)
                else if(item is Article)
                    onClick(item)
                else if(item is Service)
                    onClick(item)
                else
                    println("No type found for this item => ${item}")
            }
        })
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        retryBtn?.setOnClickListener {
            getData()
        }
        return v
    }


    override fun onResume() {
        super.onResume()
        MyActivity.logHome("onResume onResume "+lista.size);
        //doit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        mViewModel!!.callErrors.observe(this, Observer<List<String>> { response -> onError(response) })
        mViewModel!!.loadStatus.observe(this, Observer<Boolean> { response -> onStatusChanged(response) })
        mViewModel.like.observe(this, Observer { this.onDataReceived(it) })
        mViewModel!!.getLiveData().observe(this, Observer<List<Commun>> { response -> onDataReceived(response) })
        getData()
    }

    fun onDataReceived(data: LikeResponse) {
        MyActivity.log("search for liked item...")
        for (i in lista.indices) {
            if (lista[i].getId() == data.id) {
                MyActivity.log("Liked item found!")
                if (lista[i] is Post){
                    (lista[i] as Post).setLiked(data.liked)
                    (lista[i] as Post).setLikesCount(data.likesCount)
                }else if (lista[i] is Work){
                    (lista[i] as Work).setLiked(data.liked)
                    (lista[i] as Work).setLikesCount(data.likesCount)
                }
                MyActivity.log("Refresh adapter at " + i + " position with " + data.likesCount + " likes")
                //adapter.notifyItemChanged(i);
                //adapter.refresh(i, lista[i])
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun getData() {
        super.getData()
        if(lista.size==0){
            mViewModel!!.init()
        }else{
            MyActivity.logHome("data are: "+ lista.size);
        }
    }

    override fun onError(errors: List<String>) {

    }
    override fun onStatusChanged(b: Boolean) {
        MyActivity.logHome("Status changed to (" + b + ")")
        if (b)
            cpd?.show()
        else
            cpd?.hide()
    }

    override fun onDataReceived(data:List<Commun>) {
        //super.onDataReceived(data)
        lista.addAll(data)
        MyActivity.logHome("ellllllldata "+lista.size);
        if(lista.size == 0){
            emptyView?.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            //return
        }else{
            recyclerView.setVisibility(View.VISIBLE)
            adapter?.notifyDataSetChanged()
        }
    }




}
