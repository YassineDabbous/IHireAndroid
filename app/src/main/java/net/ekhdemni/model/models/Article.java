package net.ekhdemni.model.models;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.ekhdemni.model.feeds.db.MyDataBase;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.oldNet.Ekhdemni;

/**
 * Created by YaSsIn on 24/09/2016.
 */


public class Article extends Model implements Serializable {

    private String id="";
    public long resource = 0;
    private String title = "";
    private String content = "";
    private String author = "";
    private String description = "";
    private String readed_at = "";
    private String published = "";
    private String updated = "";
    private String url = "";
    private List<String> links = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private String img = "";

    private long dbId = 0;
    public boolean read = false;
    public boolean seen = false;

    public int position = 0;

    public String getReaded_at() {
        return readed_at;
    }

    public void setReaded_at(String readed_at) {
        this.readed_at = readed_at;
    }

    public boolean marked = false;
    public boolean isMarked() {
        return marked;
    }
    public void setAsMarked(Context context, boolean marked) {
        this.marked = marked;
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToWrite();
        if(marked)
            db.marked(dbId);
        else
            db.unmarked(dbId);
        db.close();
    }

    public Resource getResource(Context context){
        MyDataBase db = MyDataBase.getInstance(context);
        db.openToRead();
        Resource r = db.getResource(resource);
        db.close();
        return  r;
    }

    public void addLink(String link) {
        links.add(link);
    }
    public void addCategory(String category) {
        categories.add(category);
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getImg() {
        if(img.isEmpty())
            img = Ekhdemni.randomPlaceHolder();
        MyActivity.log("img will be this => "+img);
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(Context context, boolean read) {
        if(read != this.read){
            MyDataBase dba = MyDataBase.getInstance(context);
            dba.openToWrite();
            if(!seen)
                dba.markAsRead(dbId);
            else
                dba.markAsUnread(dbId);
            dba.updateReadDate(dbId);
            dba.close();
            setSeen(context, read);
            this.read = read;
        }
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(Context context, boolean seen) {
        if(seen != this.seen){
            MyDataBase dba = MyDataBase.getInstance(context);
            dba.openToWrite();
            if(!seen)
                dba.markAsSeen(dbId);
            else
                dba.markAsUnSeen(dbId);
            dba.close();
            this.seen = seen;
        }
    }
}
