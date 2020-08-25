package net.ekhdemni.model.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by X on 6/22/2018.
 */

public class Experience implements Serializable {
    public String id = "", uid = "", type="0", typeName = "";
    public String title, description, company, position, address, country, city, url, started_at, ended_at, date;
    public Experience(){}
    public Experience(JSONObject c) throws JSONException {
        this.id = c.optString("id", "0");
        this.uid = c.optString("uid", "0");
        this.type = c.optString("type", "0");
        this.typeName = c.optString("type_name", "");
        this.title = c.optString("title", "");
        this.description = c.optString("description", "");
        this.company = c.optString("company", "");
        this.position = c.optString("position", "");
        this.address = c.optString("address", "");
        this.country = c.optString("country", "");
        this.city = c.optString("city", "");
        this.url = c.optString("url", "");
        this.started_at = c.optString("started_at", "");
        this.ended_at = c.optString("ended_at", "");
        this.date = c.optString("date", "");
    }

    @Override
    public String toString() {
        return "Experience{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", type='" + type + '\'' +
                ", typeName='" + typeName + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", company='" + company + '\'' +
                ", position='" + position + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", url='" + url + '\'' +
                ", started_at='" + started_at + '\'' +
                ", ended_at='" + ended_at + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
