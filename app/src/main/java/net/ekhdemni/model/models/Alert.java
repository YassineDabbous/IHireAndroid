package net.ekhdemni.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Alert {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("admin_id")
    @Expose
    private Integer adminId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("os")
    @Expose
    private Integer os;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOs() {
        return os;
    }

    public void setOs(Integer os) {
        this.os = os;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("adminId", adminId).append("userId", userId).append("title", title).append("type", type).append("os", os).append("createdAt", createdAt).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(os).append(title).append(adminId).append(createdAt).append(userId).append(type).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Alert) == false) {
            return false;
        }
        Alert rhs = ((Alert) other);
        return new EqualsBuilder().append(id, rhs.id).append(os, rhs.os).append(title, rhs.title).append(adminId, rhs.adminId).append(createdAt, rhs.createdAt).append(userId, rhs.userId).append(type, rhs.type).isEquals();
    }

}