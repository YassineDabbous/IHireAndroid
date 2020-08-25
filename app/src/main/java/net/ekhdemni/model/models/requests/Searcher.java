package net.ekhdemni.model.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Searcher implements Serializable {

    public Searcher(String categoryId, String countryId, String degreeId, String workTypeId, String gender, String minSalary, String experience, String permis) {
        this.categoryId = categoryId;
        this.countryId = countryId;
        this.degreeId = degreeId;
        this.workTypeId = workTypeId;
        this.gender = gender;
        this.minSalary = minSalary;
        this.experience = experience;
        this.permis = permis;
    }

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("degree_id")
    @Expose
    private String degreeId;
    @SerializedName("work_type_id")
    @Expose
    private String workTypeId;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("min_salary")
    @Expose
    private String minSalary;
    @SerializedName("experience")
    @Expose
    private String experience;
    @SerializedName("permis")
    @Expose
    private String permis;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(String degreeId) {
        this.degreeId = degreeId;
    }

    public String getWorkTypeId() {
        return workTypeId;
    }

    public void setWorkTypeId(String workTypeId) {
        this.workTypeId = workTypeId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(String minSalary) {
        this.minSalary = minSalary;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPermis() {
        return permis;
    }

    public void setPermis(String permis) {
        this.permis = permis;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("categoryId", categoryId).append("countryId", countryId).append("degreeId", degreeId).append("workTypeId", workTypeId).append("gender", gender).append("minSalary", minSalary).append("experience", experience).append("permis", permis).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(minSalary).append(countryId).append(workTypeId).append(permis).append(categoryId).append(gender).append(experience).append(degreeId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Searcher) == false) {
            return false;
        }
        Searcher rhs = ((Searcher) other);
        return new EqualsBuilder().append(minSalary, rhs.minSalary).append(countryId, rhs.countryId).append(workTypeId, rhs.workTypeId).append(permis, rhs.permis).append(categoryId, rhs.categoryId).append(gender, rhs.gender).append(experience, rhs.experience).append(degreeId, rhs.degreeId).isEquals();
    }

}