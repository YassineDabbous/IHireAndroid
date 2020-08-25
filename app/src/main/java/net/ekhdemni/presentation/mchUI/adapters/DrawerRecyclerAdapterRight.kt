package net.ekhdemni.presentation.adapters

import android.view.MenuItem
import net.ekhdemni.model.models.LeftMenuItem
import net.ekhdemni.presentation.adapters.vh.VHRightDrawerItem
import net.ekhdemni.presentation.base.BaseAdapter
import tn.core.presentation.listeners.OnClickItemListener

class DrawerRecyclerAdapterRight(val items:List<MenuItem>, val listener:OnClickItemListener<MenuItem>):
        BaseAdapter<MenuItem, VHRightDrawerItem>(items, VHRightDrawerItem::class.java, listener, false) {
}