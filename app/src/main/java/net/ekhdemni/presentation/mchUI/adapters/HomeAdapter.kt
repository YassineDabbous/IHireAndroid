package net.ekhdemni.presentation.mchUI.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.ekhdemni.model.ModelType
import net.ekhdemni.presentation.base.MyActivity
import net.ekhdemni.presentation.MultiItemsListener
import tn.core.presentation.listeners.OnClickItemListener
import net.ekhdemni.model.models.*
import net.ekhdemni.model.models.user.User
import net.ekhdemni.presentation.mchUI.adapters.viewholders.*

class HomeAdapter(private val items : List<Commun>, private val listener: MultiItemsListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        MyActivity.log("viewType is :: " + viewType)
        when(viewType){
            ModelType.USER -> {
                val v = LayoutInflater.from(parent.context).inflate(UsersVH(View(parent.context)).layoutId,parent,false)
                return UsersVH(v)
            }
            ModelType.POST -> {
                val v = LayoutInflater.from(parent.context).inflate(PostsVH(View(parent.context)).layoutId,parent,false)
                return PostsVH(v)
            }
            ModelType.JOB -> {
                val v = LayoutInflater.from(parent.context).inflate(JobVH(View(parent.context)).layoutId,parent,false)
                return JobVH(v)
            }
            ModelType.WORK -> {
                val v = LayoutInflater.from(parent.context).inflate(WorkVH(View(parent.context)).layoutId,parent,false)
                return WorkVH(v)
            }
            ModelType.IDEA -> {
                val v = LayoutInflater.from(parent.context).inflate(IdeaVH(View(parent.context)).layoutId,parent,false)
                return IdeaVH(v)
            }
            ModelType.FORUM -> {
                val v = LayoutInflater.from(parent.context).inflate(ForumVH(View(parent.context)).layoutId ,parent,false)
                return ForumVH(v)
            }
            ModelType.FORK-> {
                MyActivity.log("it's a Fork ! " + viewType)
                val v = LayoutInflater.from(parent.context).inflate(ListVH(View(parent.context)).layoutId,parent,false)
                return ListVH(v) //Fork
            }
            else -> {
                MyActivity.log("else it's a Fork ! " + viewType)
                val v = LayoutInflater.from(parent.context).inflate(ListVH(View(parent.context)).layoutId,parent,false)
                return ListVH(v) //Fork
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is ListVH && item is Fork){holder.bind(item, (listener as OnClickItemListener<Commun>))}
        else if (holder is IdeaVH && item is Idea) {holder.bind(item, (listener as OnClickItemListener<Idea>))}
        else if (holder is ForumVH && item is Forum) {holder.bind(item, (listener as OnClickItemListener<Forum>))}
        else if (holder is PostsVH && item is Post)  holder.bind(item, (listener as OnClickItemListener<Post>))
        else if (holder is WorkVH && item is Work)  holder.bind(item, (listener as OnClickItemListener<Work>))
        else if (holder is UsersVH && item is User)  holder.bind(item, (listener as OnClickItemListener<User>))
        else if (holder is JobVH && item is Job)  holder.bind(item, (listener as OnClickItemListener<Job>))
    }


    override fun getItemViewType(position: Int): Int {
        when(items.get(position)) {
            is Fork -> return ModelType.FORK
            is User -> return ModelType.USER
            is Post -> return ModelType.POST
            is Job -> return ModelType.JOB
            is Work -> return ModelType.WORK
            is Idea -> return ModelType.IDEA
            is Forum -> return ModelType.FORUM
            else     -> return super.getItemViewType(position) //is Fork
        }
    }
}