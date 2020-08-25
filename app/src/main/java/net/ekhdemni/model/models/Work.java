package net.ekhdemni.model.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Work extends Commun{

    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("speciality_id")
    @Expose
    private Integer specialityId;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("images")
    @Expose
    private List<String> images = null;
    @SerializedName("liked")
    @Expose
    private Boolean liked;
    @SerializedName("likesCount")
    @Expose
    private Integer likesCount;
    @SerializedName("viewsCount")
    @Expose
    private Integer viewsCount;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        if (images==null) return new ArrayList<>();
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("categoryId", categoryId).append("specialityId", specialityId).append("photo", photo).append("title", title).append("description", description).append("images", images).append("liked", liked).append("likesCount", likesCount).append("viewsCount", viewsCount).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(description).append(likesCount).append(liked).append(categoryId).append(images).append(specialityId).append(viewsCount).append(photo).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Work) == false) {
            return false;
        }
        Work rhs = ((Work) other);
        return new EqualsBuilder().append(title, rhs.title).append(description, rhs.description).append(likesCount, rhs.likesCount).append(liked, rhs.liked).append(categoryId, rhs.categoryId).append(images, rhs.images).append(specialityId, rhs.specialityId).append(viewsCount, rhs.viewsCount).append(photo, rhs.photo).isEquals();
    }

}