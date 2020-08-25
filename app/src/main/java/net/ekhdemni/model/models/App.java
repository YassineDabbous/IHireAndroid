package net.ekhdemni.model.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class App {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("os")
    @Expose
    private Integer os;
    @SerializedName("packageName")
    @Expose
    private String packageName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo")
    @Expose
    private String logo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOs() {
        return os;
    }

    public void setOs(Integer os) {
        this.os = os;
    }

    public String getPackage() {
        return packageName;
    }

    public void setPackage(String _package) {
        this.packageName = _package;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("os", os).append("packageName", packageName).append("name", name).append("logo", logo).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(logo).append(os).append(packageName).append(name).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof App) == false) {
            return false;
        }
        App rhs = ((App) other);
        return new EqualsBuilder().append(id, rhs.id).append(logo, rhs.logo).append(os, rhs.os).append(packageName, rhs.packageName).append(name, rhs.name).isEquals();
    }

}