package net.ekhdemni.presentation.ui.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer

import net.ekhdemni.R
import tn.core.presentation.base.MyFragment
import net.ekhdemni.model.models.Idea
import net.ekhdemni.presentation.mchUI.vms.VMIdeas
import tn.core.util.Const

//import net.ekhdemni.binding
import kotlinx.android.synthetic.main.fragment_idea.*
import net.ekhdemni.model.models.responses.LikeResponse
import net.ekhdemni.model.oldNet.Action
import net.ekhdemni.model.oldNet.Ekhdemni
import net.ekhdemni.utils.AlertUtils
import net.ekhdemni.utils.TextUtils
import org.json.JSONException
import org.json.JSONObject
import tn.core.model.responses.BaseResponse

class IdeaFragment : MyFragment<VMIdeas>() {

    companion object {
        fun newInstance(id: Int) : IdeaFragment {
            val f = IdeaFragment()
            val b = Bundle()
            b.putInt(Const.ID, id)
            f.arguments = b
            return f
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_idea, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(VMIdeas::class.java)
        mViewModel.callErrors.observe(this, Observer<List<String>> { response -> onError(response) })
        mViewModel.loadStatus.observe(this, Observer<Boolean> { response -> onStatusChanged(response) })
        mViewModel.getLiveData().observe(this, Observer<Idea> { response -> onDataReceived(response) })
        mViewModel.report.observe(this, Observer<Any> { response -> onReportSuccess(response) })
        mViewModel.like.observe(this, Observer<LikeResponse> { this.handleLike(it) })

        val id:Int? = arguments?.getInt(Const.ID, 0)
        if (id != 0)
            mViewModel.one(id!!)
    }

    fun handleLike(response: LikeResponse) {
        item?.setLikesCount(response.likesCount)
        item?.setLiked(response.liked)
        //pd.dismiss()
        setLiker()
    }

    fun onReportSuccess(o:Any){
        Toast.makeText(context, o.toString(), Toast.LENGTH_SHORT).show()
    }

    var item:Idea? = null
    fun onDataReceived(item:Idea){
        this.item = item;
        shareBtn.setOnClickListener {
            shareUrl(item.name, item.webLink)
        }
        like.setOnClickListener {
            mViewModel.like(item.id)
        }
        likeBtn.setOnClickListener {
            mViewModel.like(item.id)
        }
        reportBtn.setOnClickListener {
            reportAlert()
        }

        uname.setText(item.uname)
        net.ekhdemni.utils.ImageHelper.load(upicture, item.upicture)


        title.setText(item.name)
        TextUtils.htmlToView(description, item.description)
        date.setText(item.timeAgo)

        if (item.likesCount!=null && item.likesCount>0)
            likesNbr.setText("${item.likesCount}")
        if (item.viewsCount!=null && item.viewsCount>0)
            viewsCount.setText("${item.viewsCount}")


        if (item.categoryName != null && item.specialityName!=null) {
            speciality.setText("${item.categoryName} (${item.specialityName}) ")
        }else if (item.categoryName != null) {
            speciality.setText(item.categoryName)
        }else if (item.specialityName != null) {
            speciality.setText(item.specialityName)
        }
        budget.setText(item.budget.toString())
        if (item.tools != null) {
            tools.setText(item.tools)
        }
    }


    fun setLiker() {
        likesNbr.setText(item?.getLikesCount().toString())
        likesCount.setText(item?.getLikesCount().toString())
        if (item?.getLiked()!!) {
            like.setImageResource(R.drawable.liked)
            likeBtn.setImageResource(R.drawable.liked)
        } else {
            like.setImageResource(R.drawable.like)
            likeBtn.setImageResource(R.drawable.liked)
        }
    }
    fun reportAlert() {
        val action = object : AlertUtils.Action() {
            override fun doFunction(o: Any) {
                mViewModel.report(item?.id!!, o.toString() )
            }
        }
        action.message = context?.getText(R.string.report).toString()
        AlertUtils.report(context, action)
    }

    fun shareUrl(title:String, url:String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url + " \n " + resources.getString(R.string.shared_via) + " " + resources.getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" + context!!.packageName)
        startActivity(Intent.createChooser(sharingIntent, resources.getString(R.string.share_via)))
    }


}
