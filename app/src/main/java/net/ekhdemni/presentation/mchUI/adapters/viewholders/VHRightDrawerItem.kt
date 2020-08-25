package net.ekhdemni.presentation.adapters.vh

import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import net.ekhdemni.model.models.LeftMenuItem
import net.ekhdemni.R
import tn.core.presentation.base.adapters.BaseViewHolder

class VHRightDrawerItem(val view:View): BaseViewHolder<MenuItem>(view) {
    var title:TextView? = null
    var count:TextView? = null
    var icon:ImageView? = null

    override fun getLayoutId(): Int {
        return R.layout.list_item_drawer_category_left
    }

    init {
        title = view.findViewById(R.id.drawer_list_item_text)
        count = view.findViewById(R.id.drawer_list_item_count)
        icon = view.findViewById(R.id.drawer_list_item_icon)
    }

    override fun bind(model: MenuItem?) {
        if (model?.icon!=null){
            icon?.setImageDrawable(model.icon)
            icon?.visibility = View.VISIBLE
        }
        title?.text = model?.title
        view.setOnClickListener { listener.onClick(model) }
    }
}