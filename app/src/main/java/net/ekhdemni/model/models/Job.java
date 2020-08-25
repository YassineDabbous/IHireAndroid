package net.ekhdemni.model.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Job extends Commun{
    /*
    {
   "title":"",
   "description":"",

   "gender":"Male",

   "expire_date":"",
   "joining_date":"",
   "nationality":"",
   "skills":[""],
   "requirements":[""],
   "advantages":[""],
   "category":"",
   "country":"tunisia",
   "category_name":"",
   "speciality":"",
   "work_type":"Full-time",
   "degree":"Master's degree",
   "salary":"0 - 100",
   "currency":"",

   "permis":1,
   "experience_years":0,
   "viewsCount":28


    * */

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("expire_date")
    @Expose
    private String expireDate;
    @SerializedName("joining_date")
    @Expose
    private String joiningDate;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("skills")
    @Expose
    private List<String> skills = null;
    @SerializedName("requirements")
    @Expose
    private List<String> requirements = null;
    @SerializedName("advantages")
    @Expose
    private List<String> advantages = null;
    @SerializedName("countryName")
    @Expose
    private String country;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("specialityName")
    @Expose
    private String speciality;
    @SerializedName("workTypeName")
    @Expose
    private String workType;
    @SerializedName("degreeName")
    @Expose
    private String degree;
    @SerializedName("salary")
    @Expose
    private String salary;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("permis")
    @Expose
    private Integer permis;
    @SerializedName("experience_years")
    @Expose
    private Integer experienceYears;
    @SerializedName("viewsCount")
    @Expose
    private Integer viewsCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    public List<String> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(List<String> advantages) {
        this.advantages = advantages;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getPermis() {
        return permis;
    }

    public void setPermis(Integer permis) {
        this.permis = permis;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("title", title).append("description", description).append("gender", gender).append("expireDate", expireDate).append("joiningDate", joiningDate).append("nationality", nationality).append("skills", skills).append("requirements", requirements).append("advantages", advantages).append("country", country).append("categoryName", categoryName).append("speciality", speciality).append("workType", workType).append("degree", degree).append("salary", salary).append("currency", currency).append("permis", permis).append("experienceYears", experienceYears).append("viewsCount", viewsCount).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(skills).append(advantages).append(speciality).append(permis).append(workType).append(experienceYears).append(requirements).append(currency).append(country).append(joiningDate).append(categoryName).append(title).append(degree).append(nationality).append(viewsCount).append(description).append(expireDate).append(gender).append(salary).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Job) == false) {
            return false;
        }
        Job rhs = ((Job) other);
        return new EqualsBuilder().append(skills, rhs.skills).append(advantages, rhs.advantages).append(speciality, rhs.speciality).append(permis, rhs.permis).append(workType, rhs.workType).append(experienceYears, rhs.experienceYears).append(requirements, rhs.requirements).append(currency, rhs.currency).append(country, rhs.country).append(joiningDate, rhs.joiningDate).append(categoryName, rhs.categoryName).append(title, rhs.title).append(degree, rhs.degree).append(nationality, rhs.nationality).append(viewsCount, rhs.viewsCount).append(description, rhs.description).append(expireDate, rhs.expireDate).append(gender, rhs.gender).append(salary, rhs.salary).isEquals();
    }

}