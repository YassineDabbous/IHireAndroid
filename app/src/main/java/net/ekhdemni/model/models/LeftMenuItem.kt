package net.ekhdemni.model.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import net.ekhdemni.R
import java.io.Serializable

class LeftMenuItem : Serializable {
    var id:MenuID

    @StringRes
    var name:Int = R.string.update_profile
    @DrawableRes
    var icon:Int = R.drawable.menu_open

    var count:Int = 0

    constructor(id: MenuID, @StringRes name: Int, @DrawableRes icon: Int, count: Int) {
        this.id = id
        this.name = name
        this.icon = icon
        this.count = count
    }
}

enum class MenuID {
    profile,
    jobs,
    works,
    users,
    forums,
    posts,
    ideas
}