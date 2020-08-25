package net.ekhdemni.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Relation {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_one")
    @Expose
    private String userOne;
    @SerializedName("user_two")
    @Expose
    private String userTwo;
    @SerializedName("relation")
    @Expose
    private Integer relation;
    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("upicture")
    @Expose
    private String upicture;
    @SerializedName("uid")
    @Expose
    private Integer uid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserOne() {
        return userOne;
    }

    public void setUserOne(String userOne) {
        this.userOne = userOne;
    }

    public String getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(String userTwo) {
        this.userTwo = userTwo;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userOne", userOne).append("userTwo", userTwo).append("relation", relation).append("uname", uname).append("upicture", upicture).append("uid", uid).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uid).append(id).append(uname).append(relation).append(upicture).append(userOne).append(userTwo).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Relation) == false) {
            return false;
        }
        Relation rhs = ((Relation) other);
        return new EqualsBuilder().append(uid, rhs.uid).append(id, rhs.id).append(uname, rhs.uname).append(relation, rhs.relation).append(upicture, rhs.upicture).append(userOne, rhs.userOne).append(userTwo, rhs.userTwo).isEquals();
    }

}