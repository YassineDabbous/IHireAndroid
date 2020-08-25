package net.ekhdemni.model.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import net.ekhdemni.model.feeds.db.MyDataBase;

/**
 * Created by YaSsIn on 24/09/2016.
 */


public class Category {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String title;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("jobsCount")
    @Expose
    private Integer jobsCount;
    @SerializedName("worksCount")
    @Expose
    private Integer worksCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getJobsCount() {
        return jobsCount;
    }

    public void setJobsCount(Integer jobsCount) {
        this.jobsCount = jobsCount;
    }

    public Integer getWorksCount() {
        return worksCount;
    }

    public void setWorksCount(Integer worksCount) {
        this.worksCount = worksCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("title", title).append("parentId", parentId).append("jobsCount", jobsCount).append("worksCount", worksCount).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(parentId).append(worksCount).append(title).append(jobsCount).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Category) == false) {
            return false;
        }
        Category rhs = ((Category) other);
        return new EqualsBuilder().append(id, rhs.id).append(parentId, rhs.parentId).append(worksCount, rhs.worksCount).append(title, rhs.title).append(jobsCount, rhs.jobsCount).isEquals();
    }



    private int postsNumber = 1;
    private boolean marked = false;
    private boolean is_city = false;
    private long dbId;

    public Category() {
    }


    public void destroy(Context context, boolean withPosts){
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToWrite();
        if(withPosts)
            db.deleteCategoryWithPosts(dbId, title);
        else
            db.deleteCategory(dbId);
        db.close();
    }

    public boolean isCity() {
        return is_city;
    }

    public void setIsCity(boolean is_city) {
        this.is_city = is_city;
    }


    public Category(String title, int postsNumber, boolean marked) {
        this.title = title;
        this.postsNumber = postsNumber;
        this.marked = marked;
    }



    public int getPostsNumber(Context context) {
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToRead();
        postsNumber = db.getCategoryPostsNumber(title);
        db.close();
        return postsNumber;
    }

    public void setPostsNumber(int postsNumber) {
        this.postsNumber = postsNumber;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }
}
