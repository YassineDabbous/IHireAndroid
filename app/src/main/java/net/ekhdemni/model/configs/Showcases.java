package net.ekhdemni.model.configs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Showcases implements Serializable
{

    @SerializedName("works")
    @Expose
    public Integer works = 0;

    @SerializedName("resources")
    @Expose
    public Integer resources = 0;

    @SerializedName("coffee")
    @Expose
    public Integer coffee = 0;

    @SerializedName("leftDrawer")
    @Expose
    public Integer leftDrawer = 0;

    @SerializedName("rightDrawer")
    @Expose
    public Integer rightDrawer = 0;

    @SerializedName("conversations")
    @Expose
    public Integer conversations = 0;

    @SerializedName("notifications")
    @Expose
    public Integer notifications = 0;

    @SerializedName("requests")
    @Expose
    public Integer requests = 0;
    

    public Showcases() {
    }
}