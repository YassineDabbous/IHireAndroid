package net.ekhdemni.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Forum extends Commun implements Serializable {

    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("can_post")
    @Expose
    private Integer canPost;
    @SerializedName("can_comment")
    @Expose
    private Integer canComment;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("posts_count")
    @Expose
    private Integer postsCount;
    @SerializedName("last_update")
    @Expose
    private Integer lastUpdate;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("followersCount")
    @Expose
    private Integer followersCount;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCanPost() {
        return canPost;
    }

    public void setCanPost(Integer canPost) {
        this.canPost = canPost;
    }

    public Integer getCanComment() {
        return canComment;
    }

    public void setCanComment(Integer canComment) {
        this.canComment = canComment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(Integer postsCount) {
        this.postsCount = postsCount;
    }

    public Integer getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Integer lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("type", type).append("canPost", canPost).append("canComment", canComment).append("description", description).append("image", image).append("postsCount", postsCount).append("lastUpdate", lastUpdate).append("active", active).append("followersCount", followersCount).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(canPost).append(postsCount).append(canComment).append(lastUpdate).append(description).append(followersCount).append(active).append(image).append(type).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Forum) == false) {
            return false;
        }
        Forum rhs = ((Forum) other);
        return new EqualsBuilder().append(canPost, rhs.canPost).append(postsCount, rhs.postsCount).append(canComment, rhs.canComment).append(lastUpdate, rhs.lastUpdate).append(description, rhs.description).append(followersCount, rhs.followersCount).append(active, rhs.active).append(image, rhs.image).append(type, rhs.type).isEquals();
    }

}