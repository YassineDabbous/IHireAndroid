package net.ekhdemni.model.models.responses;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewDataResponse implements Serializable
{

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("notifications")
    @Expose
    private Integer notifications;
    @SerializedName("conversations")
    @Expose
    private Integer conversations;
    @SerializedName("requests")
    @Expose
    private Integer requests;
    @SerializedName("jobs")
    @Expose
    private Integer jobs;
    @SerializedName("works")
    @Expose
    private Integer works;
    @SerializedName("posts")
    @Expose
    private Integer posts;
    @SerializedName("users")
    @Expose
    private Integer users;
    @SerializedName("percent")
    @Expose
    private Integer percent;
    private final static long serialVersionUID = 4282884139817412471L;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getNotifications() {
        return notifications;
    }

    public void setNotifications(Integer notifications) {
        this.notifications = notifications;
    }

    public Integer getConversations() {
        return conversations;
    }

    public void setConversations(Integer conversations) {
        this.conversations = conversations;
    }

    public Integer getRequests() {
        return requests;
    }

    public void setRequests(Integer requests) {
        this.requests = requests;
    }

    public Integer getJobs() {
        return jobs;
    }

    public void setJobs(Integer jobs) {
        this.jobs = jobs;
    }

    public Integer getWorks() {
        return works;
    }

    public void setWorks(Integer works) {
        this.works = works;
    }

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

}