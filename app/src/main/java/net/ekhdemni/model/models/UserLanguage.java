package net.ekhdemni.model.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UserLanguage implements Serializable {
    public String id = "", level_id = "", name="", level_name="";

    public UserLanguage(JSONObject c) throws JSONException {
        this.id = c.optString("id");
        this.level_id = c.optString("level_id");
        this.name = c.optString("name");
        this.level_name = c.optString("level_name");
    }


    @Override
    public String toString() {
        return "UserLanguage{" +
                "id='" + id + '\'' +
                ", level_id='" + level_id + '\'' +
                ", name='" + name + '\'' +
                ", level_name='" + level_name + '\'' +
                '}';
    }
}

