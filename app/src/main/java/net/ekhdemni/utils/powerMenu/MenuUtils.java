package net.ekhdemni.utils.powerMenu;

import android.content.Context;

import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;

import net.ekhdemni.R;

/**
 * Created by X on 7/27/2018.
 */

public class MenuUtils {


    public static CustomPowerMenu.Builder builder(Context context, OnMenuItemClickListener listener) {
        return new CustomPowerMenu.Builder<>(context, new IconMenuAdapter())
                .setOnMenuItemClickListener(listener)
                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setBackgroundColor(context.getResources().getColor(R.color.transparent_dark))
                .setShowBackground(true)
                .setHeight(600)
                .setWidth(600);
    }
}
