package net.ekhdemni.model.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LoginRequest {

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("email", email).append("password", password).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(email).append(password).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LoginRequest) == false) {
            return false;
        }
        LoginRequest rhs = ((LoginRequest) other);
        return new EqualsBuilder().append(email, rhs.email).append(password, rhs.password).isEquals();
    }

}