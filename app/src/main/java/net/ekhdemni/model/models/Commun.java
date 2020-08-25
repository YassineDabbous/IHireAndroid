package net.ekhdemni.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Commun extends Model implements Serializable {

    public Integer modelType = 0;
    public Integer modelPosition = 0;
    public String modelPath = "";

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("uid")
    @Expose
    public Integer uid;
    @SerializedName("uname")
    @Expose
    public String uname;

    @SerializedName("upicture")
    @Expose
    public String upicture;
    @SerializedName("time_ago")
    @Expose
    public String timeAgo;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpicture() {
        return upicture;
    }

    public void setUpicture(String upicture) {
        this.upicture = upicture;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("uid", uid).append("uname", uname).append("upicture", upicture).append("timeAgo", timeAgo).append("createdAt", createdAt).append("updatedAt", updatedAt).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(updatedAt).append(uid).append(id).append(timeAgo).append(createdAt).append(uname).append(name).append(upicture).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Commun) == false) {
            return false;
        }
        Commun rhs = ((Commun) other);
        return new EqualsBuilder().append(updatedAt, rhs.updatedAt).append(uid, rhs.uid).append(id, rhs.id).append(timeAgo, rhs.timeAgo).append(createdAt, rhs.createdAt).append(uname, rhs.uname).append(name, rhs.name).append(upicture, rhs.upicture).isEquals();
    }

}
