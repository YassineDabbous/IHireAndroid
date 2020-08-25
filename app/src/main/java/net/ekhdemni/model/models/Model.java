package net.ekhdemni.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by X on 11/30/2018.
 */

public class Model {
    @SerializedName("id")
    @Expose
    public Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
