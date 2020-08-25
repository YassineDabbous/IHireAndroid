package net.ekhdemni.model.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class UserLanguage implements Serializable {

    public UserLanguage(String id, String levelId, String name, String levelName) {
        this.id = id;
        this.levelId = levelId;
        this.name = name;
        this.levelName = levelName;
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("level_id")
    @Expose
    private String levelId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("level_name")
    @Expose
    private String levelName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("levelId", levelId).append("name", name).append("levelName", levelName).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(levelId).append(name).append(levelName).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UserLanguage) == false) {
            return false;
        }
        UserLanguage rhs = ((UserLanguage) other);
        return new EqualsBuilder().append(id, rhs.id).append(levelId, rhs.levelId).append(name, rhs.name).append(levelName, rhs.levelName).isEquals();
    }

}