package net.ekhdemni.model.models;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

import androidx.recyclerview.widget.DiffUtil;

/**
 * Created by YaSsIn on 24/09/2016.
 */


public class Conversation implements Serializable {


    public Conversation(int uid){
        this.uid = uid;
    }

    @SerializedName("id")
    @Expose
    private Integer id = 0;

    @SerializedName("id_user")
    @Expose
    private Integer idUser;

    @SerializedName("id_to")
    @Expose
    private Integer idTo;

    @SerializedName("last_message")
    @Expose
    private String lastMessage;

    @SerializedName("last_message_time")
    @Expose
    private String lastMessageTime;

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("seen")
    @Expose
    private String seen;
    @SerializedName("readBy")
    @Expose
    private Object readBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("uid")
    @Expose
    private Integer uid;
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

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdTo() {
        return idTo;
    }

    public void setIdTo(Integer idTo) {
        this.idTo = idTo;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public Object getReadBy() {
        return readBy;
    }

    public void setReadBy(Object readBy) {
        this.readBy = readBy;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("idUser", idUser).append("idTo", idTo).append("lastMessage", lastMessage).append("lastMessageTime", lastMessageTime).append("type", type).append("seen", seen).append("readBy", readBy).append("createdAt", createdAt).append("updatedAt", updatedAt).append("deletedAt", deletedAt).append("uid", uid).append("uname", uname).append("upicture", upicture).append("timeAgo", timeAgo).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uid).append(timeAgo).append(readBy).append(lastMessageTime).append(uname).append(idTo).append(seen).append(type).append(updatedAt).append(id).append(createdAt).append(deletedAt).append(lastMessage).append(idUser).append(upicture).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Conversation) == false) {
            return false;
        }
        Conversation rhs = ((Conversation) other);
        return new EqualsBuilder().append(uid, rhs.uid).append(timeAgo, rhs.timeAgo).append(readBy, rhs.readBy).append(lastMessageTime, rhs.lastMessageTime).append(uname, rhs.uname).append(idTo, rhs.idTo).append(seen, rhs.seen).append(type, rhs.type).append(updatedAt, rhs.updatedAt).append(id, rhs.id).append(createdAt, rhs.createdAt).append(deletedAt, rhs.deletedAt).append(lastMessage, rhs.lastMessage).append(idUser, rhs.idUser).append(upicture, rhs.upicture).isEquals();
    }



    // Allows the adapter to calculate the difference between the old list and new list. This also simplifies animations.
    public static final DiffUtil.ItemCallback<Conversation> DIFF_CALLBACK = new DiffUtil.ItemCallback<Conversation>() {

        // Check if items represent the same thing.
        @Override
        public boolean areItemsTheSame(Conversation oldItem, Conversation newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        // Checks if the item contents have changed.
        @Override
        public boolean areContentsTheSame(Conversation oldItem, Conversation newItem) {
            return true; // Assume Repository details don't change
        }
    };
}
