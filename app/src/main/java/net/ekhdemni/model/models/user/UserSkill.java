package net.ekhdemni.model.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class UserSkill implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("level")
    @Expose
    private Integer level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("level", level).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(level).append(name).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UserSkill) == false) {
            return false;
        }
        UserSkill rhs = ((UserSkill) other);
        return new EqualsBuilder().append(level, rhs.level).append(name, rhs.name).isEquals();
    }

}