package net.ekhdemni.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Post extends Commun{

    @SerializedName("forum_id")
    @Expose
    private Integer forumId;
    @SerializedName("can_comment")
    @Expose
    private Integer canComment;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("short_description")
    @Expose
    private String short_description;

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("solution")
    @Expose
    private Integer solution;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("viewsCount")
    @Expose
    private Integer viewsCount;
    @SerializedName("likesCount")
    @Expose
    private Integer likesCount;
    @SerializedName("liked")
    @Expose
    private Boolean liked;

    public Integer getForumId() {
        return forumId;
    }

    public void setForumId(Integer forumId) {
        this.forumId = forumId;
    }

    public Integer getCanComment() {
        return canComment;
    }

    public void setCanComment(Integer canComment) {
        this.canComment = canComment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description==null? short_description : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getSolution() {
        return solution;
    }

    public void setSolution(Integer solution) {
        this.solution = solution;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("forumId", forumId).append("canComment", canComment).append("title", title).append("description", description).append("photo", photo).append("solution", solution).append("deletedAt", deletedAt).append("viewsCount", viewsCount).append("likesCount", likesCount).append("liked", liked).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(canComment).append(forumId).append(description).append(liked).append(likesCount).append(deletedAt).append(viewsCount).append(solution).append(photo).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Post) == false) {
            return false;
        }
        Post rhs = ((Post) other);
        return new EqualsBuilder().append(title, rhs.title).append(canComment, rhs.canComment).append(forumId, rhs.forumId).append(description, rhs.description).append(liked, rhs.liked).append(likesCount, rhs.likesCount).append(deletedAt, rhs.deletedAt).append(viewsCount, rhs.viewsCount).append(solution, rhs.solution).append(photo, rhs.photo).isEquals();
    }

}