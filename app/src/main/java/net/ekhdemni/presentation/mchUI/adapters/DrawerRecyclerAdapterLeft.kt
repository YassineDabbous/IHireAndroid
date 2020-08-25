package net.ekhdemni.presentation.adapters

import net.ekhdemni.model.models.LeftMenuItem
import net.ekhdemni.presentation.adapters.vh.VHLeftDrawerItem
import net.ekhdemni.presentation.base.BaseAdapter
import tn.core.presentation.listeners.OnClickItemListener

class DrawerRecyclerAdapterLeft(val items:List<LeftMenuItem>, val listener:OnClickItemListener<LeftMenuItem>):
        BaseAdapter<LeftMenuItem, VHLeftDrawerItem>(items, VHLeftDrawerItem::class.java, listener, false) {
}