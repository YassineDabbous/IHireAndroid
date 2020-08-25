package net.ekhdemni.model.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ReportRequest {
    public ReportRequest(Integer type, Integer id, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("description")
    @Expose
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("type", type).append("description", description).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(description).append(type).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ReportRequest) == false) {
            return false;
        }
        ReportRequest rhs = ((ReportRequest) other);
        return new EqualsBuilder().append(id, rhs.id).append(description, rhs.description).append(type, rhs.type).isEquals();
    }

}