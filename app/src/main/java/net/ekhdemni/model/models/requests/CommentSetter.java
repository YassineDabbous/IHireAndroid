package net.ekhdemni.model.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CommentSetter {

    public CommentSetter(Integer type, Integer pid, String link, String comment) {
        this.type = type;
        this.pid = pid;
        this.comment = comment;
        this.link = link;
    }

    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("pid")
    @Expose
    private Integer pid;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("link")
    @Expose
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("type", type).append("pid", pid).append("comment", comment).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pid).append(comment).append(type).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CommentSetter) == false) {
            return false;
        }
        CommentSetter rhs = ((CommentSetter) other);
        return new EqualsBuilder().append(pid, rhs.pid).append(comment, rhs.comment).append(type, rhs.type).isEquals();
    }

}