package net.ekhdemni.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Message {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("uid")
    @Expose
    private Integer uid;
    @SerializedName("conversation_id")
    @Expose
    private Integer conversationId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("seenBy")
    @Expose
    private Object seenBy;
    @SerializedName("readBy")
    @Expose
    private Object readBy;
    @SerializedName("isFile")
    @Expose
    private Integer isFile;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("upicture")
    @Expose
    private String upicture;
    @SerializedName("time_ago")
    @Expose
    private String timeAgo;

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

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getSeenBy() {
        return seenBy;
    }

    public void setSeenBy(Object seenBy) {
        this.seenBy = seenBy;
    }

    public Object getReadBy() {
        return readBy;
    }

    public void setReadBy(Object readBy) {
        this.readBy = readBy;
    }

    public Integer getIsFile() {
        return isFile;
    }

    public void setIsFile(Integer isFile) {
        this.isFile = isFile;
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

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("uid", uid).append("conversationId", conversationId).append("message", message).append("seenBy", seenBy).append("readBy", readBy).append("isFile", isFile).append("createdAt", createdAt).append("updatedAt", updatedAt).append("deletedAt", deletedAt).append("uname", uname).append("upicture", upicture).append("timeAgo", timeAgo).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uid).append(timeAgo).append(readBy).append(uname).append(conversationId).append(updatedAt).append(message).append(id).append(createdAt).append(isFile).append(seenBy).append(deletedAt).append(upicture).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Message) == false) {
            return false;
        }
        Message rhs = ((Message) other);
        return new EqualsBuilder().append(uid, rhs.uid).append(timeAgo, rhs.timeAgo).append(readBy, rhs.readBy).append(uname, rhs.uname).append(conversationId, rhs.conversationId).append(updatedAt, rhs.updatedAt).append(message, rhs.message).append(id, rhs.id).append(createdAt, rhs.createdAt).append(isFile, rhs.isFile).append(seenBy, rhs.seenBy).append(deletedAt, rhs.deletedAt).append(upicture, rhs.upicture).isEquals();
    }

}