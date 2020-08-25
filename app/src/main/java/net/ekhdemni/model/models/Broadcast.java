package net.ekhdemni.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Broadcast {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("uid")
    @Expose
    private Integer uid;
    @SerializedName("broadkey")
    @Expose
    private String broadkey;
    @SerializedName("broadvalue")
    @Expose
    private String broadvalue;

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

    public String getBroadkey() {
        return broadkey;
    }

    public void setBroadkey(String broadkey) {
        this.broadkey = broadkey;
    }

    public String getBroadvalue() {
        return broadvalue;
    }

    public void setBroadvalue(String broadvalue) {
        this.broadvalue = broadvalue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("uid", uid).append("broadkey", broadkey).append("broadvalue", broadvalue).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uid).append(id).append(broadvalue).append(broadkey).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Broadcast) == false) {
            return false;
        }
        Broadcast rhs = ((Broadcast) other);
        return new EqualsBuilder().append(uid, rhs.uid).append(id, rhs.id).append(broadvalue, rhs.broadvalue).append(broadkey, rhs.broadkey).isEquals();
    }

}