package net.ekhdemni.model.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResetRequest {

    public ResetRequest(String email) {
        this.email = email;
    }

    @SerializedName("email")
    @Expose
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("email", email).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(email).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ResetRequest) == false) {
            return false;
        }
        ResetRequest rhs = ((ResetRequest) other);
        return new EqualsBuilder().append(email, rhs.email).isEquals();
    }

}