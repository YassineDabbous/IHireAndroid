package net.ekhdemni.presentation.ui.views.tagview;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;


public class Tag implements Comparable<Tag> {

    public int id;
    public String text;
    public int number = 99;
    public int tagTextColor;
    public float tagTextSize;
    public int layoutColor;
    public int layoutColorPress;
    public boolean isDeletable;
    public int deleteIndicatorColor;
    public float deleteIndicatorSize;
    public float radius;
    public String deleteIcon;
    public float layoutBorderSize;
    public int layoutBorderColor;
    public Drawable background;


    public Tag(String text, int number) {
        init(0, text, number, Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_LAYOUT_COLOR, Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
                Constants.DEFAULT_TAG_IS_DELETABLE, Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR);
    }

    private void init(int id, String text, int number, int tagTextColor, float tagTextSize,
                      int layoutColor, int layoutColorPress, boolean isDeletable,
                      int deleteIndicatorColor,float deleteIndicatorSize, float radius,
                      String deleteIcon, float layoutBorderSize, int layoutBorderColor) {
        this.number = number;
        this.id = id;
        this.text = text;
        this.tagTextColor = tagTextColor;
        this.tagTextSize = tagTextSize;
        this.layoutColor = layoutColor;
        this.layoutColorPress = layoutColorPress;
        this.isDeletable = isDeletable;
        this.deleteIndicatorColor = deleteIndicatorColor;
        this.deleteIndicatorSize = deleteIndicatorSize;
        this.radius = radius;
        this.deleteIcon = deleteIcon;
        this.layoutBorderSize = layoutBorderSize;
        this.layoutBorderColor = layoutBorderColor;
    }

    @Override
    public int compareTo(@NonNull Tag tag) {
        //ascending order
        //return (int)(this.number - tag.number);

        //descending order
        return (int)(tag.number - this.number);
    }
}