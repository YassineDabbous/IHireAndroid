package net.ekhdemni.model.models.responses;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LikeResponse {

    @SerializedName("liked")
    @Expose
    private Boolean liked;
    @SerializedName("likesCount")
    @Expose
    private Integer likesCount;
    @SerializedName("id")
    @Expose
    private Integer id = 0;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("liked", liked).append("likesCount", likesCount).append("id", id).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(likesCount).append(liked).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LikeResponse) == false) {
            return false;
        }
        LikeResponse rhs = ((LikeResponse) other);
        return new EqualsBuilder().append(id, rhs.id).append(likesCount, rhs.likesCount).append(liked, rhs.liked).isEquals();
    }

}