package net.ekhdemni.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Notification {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("uid")
    @Expose
    private Integer uid;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("item_type")
    @Expose
    private Integer itemType;
    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("item_uid")
    @Expose
    private Integer itemUid;
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("u_id")
    @Expose
    private Integer uId;
    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("upicture")
    @Expose
    private String upicture;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("parent_type")
    @Expose
    private Integer parentType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getItemUid() {
        return itemUid;
    }

    public void setItemUid(Integer itemUid) {
        this.itemUid = itemUid;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
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

    public Integer getUId() {
        return uId;
    }

    public void setUId(Integer uId) {
        this.uId = uId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getParentType() {
        return parentType;
    }

    public void setParentType(Integer parentType) {
        this.parentType = parentType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("uid", uid).append("type", type).append("itemType", itemType).append("itemId", itemId).append("itemUid", itemUid).append("time", time).append("createdAt", createdAt).append("updatedAt", updatedAt).append("uId", uId).append("uname", uname).append("upicture", upicture).append("message", message).append("parentId", parentId).append("parentType", parentType).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uid).append(uname).append(itemType).append(uId).append(itemUid).append(itemId).append(type).append(parentType).append(message).append(updatedAt).append(id).append(parentId).append(time).append(createdAt).append(upicture).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Notification) == false) {
            return false;
        }
        Notification rhs = ((Notification) other);
        return new EqualsBuilder().append(uid, rhs.uid).append(uname, rhs.uname).append(itemType, rhs.itemType).append(uId, rhs.uId).append(itemUid, rhs.itemUid).append(itemId, rhs.itemId).append(type, rhs.type).append(parentType, rhs.parentType).append(message, rhs.message).append(updatedAt, rhs.updatedAt).append(id, rhs.id).append(parentId, rhs.parentId).append(time, rhs.time).append(createdAt, rhs.createdAt).append(upicture, rhs.upicture).isEquals();
    }

}