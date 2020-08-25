package net.ekhdemni.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Service extends Model {


    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("css")
    @Expose
    private String css;
    @SerializedName("js")
    @Expose
    private String js;


    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("countryId", countryId).append("title", title).append("url", url).append("logo", logo).append("css", css).append("js", js).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(countryId).append(id).append(logo).append(title).append(js).append(css).append(url).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Service) == false) {
            return false;
        }
        Service rhs = ((Service) other);
        return new EqualsBuilder().append(countryId, rhs.countryId).append(id, rhs.id).append(logo, rhs.logo).append(title, rhs.title).append(js, rhs.js).append(css, rhs.css).append(url, rhs.url).isEquals();
    }

}