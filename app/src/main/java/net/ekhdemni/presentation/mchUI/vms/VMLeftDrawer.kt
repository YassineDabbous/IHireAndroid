package net.ekhdemni.presentation.vms


import android.content.Context
import net.ekhdemni.R
import net.ekhdemni.model.models.MenuID
import net.ekhdemni.model.models.LeftMenuItem
import net.ekhdemni.presentation.base.MyViewModel
import android.view.MenuInflater
import androidx.appcompat.view.menu.MenuBuilder



class VMLeftDrawer : MyViewModel<List<LeftMenuItem>>() {

    fun menu(){
        val lista = listOf<LeftMenuItem>(
                LeftMenuItem(MenuID.profile, R.string.menu_item_3, R.drawable.account_settings_variant, 3),
                LeftMenuItem(MenuID.forums, R.string.coffee, R.drawable.coffee, 45),
                LeftMenuItem(MenuID.works, R.string.works, R.drawable.android_studio, 4),
                LeftMenuItem(MenuID.jobs, R.string.jobs, R.drawable.briefcase_search, 65),
                LeftMenuItem(MenuID.ideas, R.string.projects, R.drawable.lightbulb_on_outline, 65),
                LeftMenuItem(MenuID.users, R.string.profiles, R.drawable.worker, 102)
        )
        liveData.postValue(lista)
    }

}
