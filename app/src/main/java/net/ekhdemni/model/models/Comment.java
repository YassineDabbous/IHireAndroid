package net.ekhdemni.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import tn.core.model.net.net.NetworkUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class Comment extends Commun{

    public Comment(JSONObject c) throws JSONException {
        this.setUid(c.optInt("uid"));
        this.setUname(c.optString("uname"));
        this.setUpicture(NetworkUtils.correctUrl(c.optString("upicture")));
        this.setId(c.optInt("id"));
        this.comment = c.optString("comment");
        this.likesCount = c.optInt("likesCount");
        this.liked = c.optBoolean("liked");
    }

    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("liked")
    @Expose
    private Boolean liked;
    @SerializedName("likesCount")
    @Expose
    private Integer likesCount;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("comment", comment).append("liked", liked).append("likesCount", likesCount).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(likesCount).append(liked).append(comment).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Comment) == false) {
            return false;
        }
        Comment rhs = ((Comment) other);
        return new EqualsBuilder().append(likesCount, rhs.likesCount).append(liked, rhs.liked).append(comment, rhs.comment).isEquals();
    }

}