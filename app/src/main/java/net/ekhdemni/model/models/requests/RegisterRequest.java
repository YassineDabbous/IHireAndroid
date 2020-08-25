package net.ekhdemni.model.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RegisterRequest {

    public RegisterRequest(String name, String email, String password, Integer accounttype, Integer country, Integer category, Integer speciality) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accounttype = accounttype;
        this.country = country;
        this.category = category;
        this.speciality = speciality;
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("accounttype")
    @Expose
    private Integer accounttype;
    @SerializedName("country")
    @Expose
    private Integer country;
    @SerializedName("category")
    @Expose
    private Integer category;
    @SerializedName("speciality")
    @Expose
    private Integer speciality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Integer getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(Integer accounttype) {
        this.accounttype = accounttype;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Integer speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("email", email).append("password", password).append("accounttype", accounttype).append("country", country).append("category", category).append("speciality", speciality).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(category).append(speciality).append(email).append(name).append(accounttype).append(password).append(country).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RegisterRequest) == false) {
            return false;
        }
        RegisterRequest rhs = ((RegisterRequest) other);
        return new EqualsBuilder().append(category, rhs.category).append(speciality, rhs.speciality).append(email, rhs.email).append(name, rhs.name).append(accounttype, rhs.accounttype).append(password, rhs.password).append(country, rhs.country).isEquals();
    }

}