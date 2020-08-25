package net.ekhdemni.presentation.adapters.vh

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import net.ekhdemni.model.models.LeftMenuItem
import net.ekhdemni.R
import tn.core.presentation.base.adapters.BaseViewHolder

class VHLeftDrawerItem(val view:View): BaseViewHolder<LeftMenuItem>(view) {
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

    override fun bind(model: LeftMenuItem?) {
        if (model?.count!=null && model?.count>0){
            count?.text = model?.count.toString()
            count?.visibility = View.VISIBLE
        }
        if (model?.icon!=null){
            icon?.setImageResource(model.icon)
            icon?.visibility = View.VISIBLE
        }
        title?.text = title!!.context.getText(model!!.name)
        view.setOnClickListener { listener.onClick(model) }
    }
}