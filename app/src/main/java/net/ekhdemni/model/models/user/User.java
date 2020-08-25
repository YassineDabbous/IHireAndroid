package net.ekhdemni.model.models.user;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.ekhdemni.model.models.Commun;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class User extends Commun implements Serializable {
    //{ "birthday":"not defined", "gender":"Male", "nationality":"not defined", "video":"", "cv":"http:\/\/cdn.ekhdemni.net\/resumes\/user_14.pdf", "address":"not defined", "phone":"", "military_service":"not defined", "permis":"not defined", "year_graduation":"not defined", "min_salary":0, "university":"not defined", "school":"", "description":"", "website":"not defined", "email":"mansoura_82@hotmail.com", "photo":"http:\/\/cdn.ekhdemni.net\/categories\/0.png", "online":false, "postsCount":0, "worksCount":0, "friendsCount":0, "degree_id":0, "work_system_id":0, "category_id":0, "speciality_id":1, "country_id":1, "city_id":0, "country":"", "city":"", "category":"", "speciality":"not defined", "degree":"not defined", "work_system":"not defined", "experience":0, "relation":0, "percent":100, "skills":[""], "basic_skills":[{}], "links":[{}], "languages":[{}] }

    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("nationalityName")
    @Expose
    private String nationality;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("cv")
    @Expose
    private String cv;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("military_service")
    @Expose
    private String militaryService;
    @SerializedName("permis")
    @Expose
    private String permis;
    @SerializedName("year_graduation")
    @Expose
    private String yearGraduation;
    @SerializedName("min_salary")
    @Expose
    private Integer minSalary;
    @SerializedName("university")
    @Expose
    private String university;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("online")
    @Expose
    private Boolean online;
    @SerializedName("postsCount")
    @Expose
    private Integer postsCount;
    @SerializedName("worksCount")
    @Expose
    private Integer worksCount;
    @SerializedName("friendsCount")
    @Expose
    private Integer friendsCount;
    @SerializedName("degree_id")
    @Expose
    private Integer degreeId;
    @SerializedName("work_type_id")
    @Expose
    private Integer workTypeId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("speciality_id")
    @Expose
    private Integer specialityId;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("countryName")
    @Expose
    private String country;
    @SerializedName("cityName")
    @Expose
    private String city;
    @SerializedName("categoryName")
    @Expose
    private String category;
    @SerializedName("specialityName")
    @Expose
    private String speciality;
    @SerializedName("degreeName")
    @Expose
    private String degree;
    @SerializedName("workTypeName")
    @Expose
    private String workTypeName;
    @SerializedName("experience")
    @Expose
    private Integer experience;
    @SerializedName("relation")
    @Expose
    private Integer relation;
    @SerializedName("percent")
    @Expose
    private Integer percent;
    @SerializedName("skills")
    @Expose
    private List<String> skills = null;
    @SerializedName("basic_skills")
    @Expose
    private List<UserSkill> basicSkills = null;
    @SerializedName("links")
    @Expose
    private List<UserLink> links = null;
    @SerializedName("languages")
    @Expose
    private List<UserLanguage> languages = null;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMilitaryService() {
        return militaryService;
    }

    public void setMilitaryService(String militaryService) {
        this.militaryService = militaryService;
    }

    public String getPermis() {
        return permis;
    }

    public void setPermis(String permis) {
        this.permis = permis;
    }

    public String getYearGraduation() {
        return yearGraduation;
    }

    public void setYearGraduation(String yearGraduation) {
        this.yearGraduation = yearGraduation;
    }

    public Integer getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Integer minSalary) {
        this.minSalary = minSalary;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Integer getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(Integer postsCount) {
        this.postsCount = postsCount;
    }

    public Integer getWorksCount() {
        return worksCount;
    }

    public void setWorksCount(Integer worksCount) {
        this.worksCount = worksCount;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public Integer getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Integer degreeId) {
        this.degreeId = degreeId;
    }

    public Integer getWorkTypeId() {
        return workTypeId;
    }

    public void setWorkTypeId(Integer workTypeId) {
        this.workTypeId = workTypeId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<UserSkill> getUserSkills() {
        return basicSkills;
    }

    public void setUserSkills(List<UserSkill> basicSkills) {
        this.basicSkills = basicSkills;
    }

    public List<UserLink> getLinks() {
        return links;
    }

    public void setLinks(List<UserLink> links) {
        this.links = links;
    }

    public List<UserLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(List<UserLanguage> languages) {
        this.languages = languages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("birthday", birthday).append("gender", gender).append("nationality", nationality).append("video", video).append("cv", cv).append("address", address).append("phone", phone).append("militaryService", militaryService).append("permis", permis).append("yearGraduation", yearGraduation).append("minSalary", minSalary).append("university", university).append("school", school).append("description", description).append("website", website).append("email", email).append("photo", photo).append("online", online).append("postsCount", postsCount).append("worksCount", worksCount).append("friendsCount", friendsCount).append("degreeId", degreeId).append("workTypeId", workTypeId).append("categoryId", categoryId).append("specialityId", specialityId).append("countryId", countryId).append("cityId", cityId).append("country", country).append("city", city).append("category", category).append("speciality", speciality).append("degree", degree).append("workTypeName", workTypeName).append("experience", experience).append("relation", relation).append("percent", percent).append("skills", skills).append("basicSkills", basicSkills).append("links", links).append("languages", languages).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(skills).append(birthday).append(friendsCount).append(phone).append(percent).append(cityId).append(permis).append(workTypeId).append(city).append(online).append(militaryService).append(languages).append(worksCount).append(description).append(specialityId).append(gender).append(experience).append(degreeId).append(video).append(countryId).append(minSalary).append(university).append(postsCount).append(speciality).append(website).append(categoryId).append(relation).append(basicSkills).append(links).append(yearGraduation).append(workTypeName).append(cv).append(photo).append(country).append(category).append(degree).append(school).append(nationality).append(email).append(address).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof User) == false) {
            return false;
        }
        User rhs = ((User) other);
        return new EqualsBuilder().append(skills, rhs.skills).append(birthday, rhs.birthday).append(friendsCount, rhs.friendsCount).append(phone, rhs.phone).append(percent, rhs.percent).append(cityId, rhs.cityId).append(permis, rhs.permis).append(workTypeId, rhs.workTypeId).append(city, rhs.city).append(online, rhs.online).append(militaryService, rhs.militaryService).append(languages, rhs.languages).append(worksCount, rhs.worksCount).append(description, rhs.description).append(specialityId, rhs.specialityId).append(gender, rhs.gender).append(experience, rhs.experience).append(degreeId, rhs.degreeId).append(video, rhs.video).append(countryId, rhs.countryId).append(minSalary, rhs.minSalary).append(university, rhs.university).append(postsCount, rhs.postsCount).append(speciality, rhs.speciality).append(website, rhs.website).append(categoryId, rhs.categoryId).append(relation, rhs.relation).append(basicSkills, rhs.basicSkills).append(links, rhs.links).append(yearGraduation, rhs.yearGraduation).append(workTypeName, rhs.workTypeName).append(cv, rhs.cv).append(photo, rhs.photo).append(country, rhs.country).append(category, rhs.category).append(degree, rhs.degree).append(school, rhs.school).append(nationality, rhs.nationality).append(email, rhs.email).append(address, rhs.address).isEquals();
    }

}