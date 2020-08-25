package net.ekhdemni.model.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.ekhdemni.model.models.Fork;
import net.ekhdemni.model.models.Forum;
import net.ekhdemni.model.models.Idea;
import net.ekhdemni.model.models.Job;
import net.ekhdemni.model.models.Post;
import net.ekhdemni.model.models.user.User;
import net.ekhdemni.model.models.Work;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

import tn.core.model.responses.PagingResponse;

public class HomeResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("forums")
    @Expose
    private PagingResponse<Forum> forums;
    @SerializedName("works")
    @Expose
    private PagingResponse<Work> works;
    @SerializedName("posts")
    @Expose
    private PagingResponse<Post> posts;
    @SerializedName("jobs")
    @Expose
    private PagingResponse<Job> jobs;
    @SerializedName("ideas")
    @Expose
    private PagingResponse<Idea> ideas;
    @SerializedName("users")
    @Expose
    private PagingResponse<User> users;
    @SerializedName("forks")
    @Expose
    private List<Fork> forks;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<Fork> getForks() {
        return forks;
    }

    public void setForks(List<Fork> forks) {
        this.forks = forks;
    }

    public PagingResponse<Forum> getForums() {
        return forums;
    }

    public void setForums(PagingResponse<Forum> forums) {
        this.forums = forums;
    }

    public PagingResponse<Work> getWorks() {
        return works;
    }

    public void setWorks(PagingResponse<Work> works) {
        this.works = works;
    }

    public PagingResponse<Post> getPosts() {
        return posts;
    }

    public void setPosts(PagingResponse<Post> posts) {
        this.posts = posts;
    }

    public PagingResponse<Job> getJobs() {
        return jobs;
    }

    public void setJobs(PagingResponse<Job> jobs) {
        this.jobs = jobs;
    }

    public PagingResponse<Idea> getIdeas() {
        return ideas;
    }

    public void setIdeas(PagingResponse<Idea> ideas) {
        this.ideas = ideas;
    }

    public PagingResponse<User> getUsers() {
        return users;
    }

    public void setUsers(PagingResponse<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("code", code).append("forums", forums).append("works", works).append("posts", posts).append("jobs", jobs).append("ideas", ideas).append("users", users).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ideas).append(users).append(jobs).append(forums).append(posts).append(code).append(works).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HomeResponse) == false) {
            return false;
        }
        HomeResponse rhs = ((HomeResponse) other);
        return new EqualsBuilder().append(ideas, rhs.ideas).append(users, rhs.users).append(jobs, rhs.jobs).append(forums, rhs.forums).append(posts, rhs.posts).append(code, rhs.code).append(works, rhs.works).isEquals();
    }

}