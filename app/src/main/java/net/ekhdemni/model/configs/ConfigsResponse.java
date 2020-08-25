package net.ekhdemni.model.configs;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigsResponse implements Serializable
{

    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("strict")
    @Expose
    private Boolean strict;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("ads_after")
    @Expose
    private Integer adsAfter;
    @SerializedName("alert")
    @Expose
    private String alert;
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("cache_time")
    @Expose
    private Integer cacheTime;
    @SerializedName("cache_size")
    @Expose
    private Integer cacheSize;

    public ConfigsResponse() {
        this.time = 0;
        this.active = false;
        this.strict = false;
        this.image = "";
        this.url = "";
        this.adsAfter = 0;
        this.alert = "";
        this.background = "";
        this.cacheTime = 0;
        this.cacheSize = 0;
    }

    private final static long serialVersionUID = 1896722109865285798L;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getStrict() {
        return strict;
    }

    public void setStrict(Boolean strict) {
        this.strict = strict;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getAdsAfter() {
        return adsAfter;
    }

    public void setAdsAfter(Integer adsAfter) {
        this.adsAfter = adsAfter;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }


    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Integer getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(Integer cacheTime) {
        this.cacheTime = cacheTime;
    }

    public Integer getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(Integer cacheSize) {
        this.cacheSize = cacheSize;
    }
}