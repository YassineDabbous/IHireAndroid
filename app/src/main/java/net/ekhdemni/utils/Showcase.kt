package net.ekhdemni.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.Color.parseColor
import android.view.View
import net.ekhdemni.presentation.base.MyActivity
import tn.core.presentation.listeners.OnClickItemListener
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import android.graphics.Typeface
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener


class Showcase {
    companion object {
        var random:Int = 40;
        fun show(activity:Activity, view:View, title:String, content:String, listener:OnClickItemListener<View>){
            random++
            MyActivity.log("$random showcase for: $title")
             GuideView.Builder(activity)
                    .setTitle(title)
                     //.setContentSpan((Spannable) Html.fromHtml("<font color='red'>testing spannable</p>"))
                    .setContentText(content)
                    .setTargetView(view)
                     .setContentTextSize(12)//optional
                     .setTitleTextSize(14)//optional
                     .setGravity(Gravity.center)//optional
                     //.setContentTypeFace(Typeface)//optional
                    //.setTitleTypeFace(Typeface)//optional
                    .setDismissType(DismissType.outside) //optional - default dismissible by TargetView
                     .setGuideListener {
                         listener.onClick(it)
                     }
                    .build()
                    .show()
        }
    }
}